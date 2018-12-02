package com.partybox;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;

/**
 * Put stuff here that every activity needs to use
 */
public abstract class BaseActivity extends Activity {
    public enum Direction {LEFT, RIGHT}
    protected static final String PARTIES_DIRECTORY = "savedParties";
    /* Hacky method for keeping track of open party file */
    protected static String currentPartyPath = null;

    /**
     * Switch from current activity to another
     * @param v
     * @param activity
     */

    protected void switchToActivity(View v, Class<? extends Activity> activity, Direction dir) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        switch (dir){
            case LEFT: overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            case RIGHT: overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            default: overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        }

    }

    /**
     * Get the path to the local files dir, create if doesn't exist
     * @return
     */
    protected static String getOrCreatePartiesDirectory() {
        File localFilesDir = Environment.getExternalStorageDirectory();
        File partiesDir = new File(localFilesDir, PARTIES_DIRECTORY);
        if (!partiesDir.exists()) {
            if (!partiesDir.mkdirs()) {
                throw new java.lang.RuntimeException("Failed to create: " + localFilesDir + File.separator + PARTIES_DIRECTORY);
            }
        }

        return partiesDir.getPath();
    }

    /**
     * Returns path to dir if exists, null otherwise
     * @param localFilesDir
     * @return
     */
    private static String savedPartiesDirExists(File localFilesDir) {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(PARTIES_DIRECTORY);
            }
        };

        File[] partiesDir = localFilesDir.listFiles(filter);
        if (partiesDir == null || partiesDir.length == 0) {
            return null;
        }

        return partiesDir[0].getName();
    }
}
