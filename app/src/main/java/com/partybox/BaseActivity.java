package com.partybox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.partybox.dialogs.DatePickerfragment;
import com.partybox.dialogs.TimePickerFragment;

import java.io.File;
import java.io.FileFilter;

/**
 * Put stuff here that every activity needs to use
 */
public abstract class BaseActivity extends FragmentActivity {
    public static final int DIALOG_END_TIME = 0;
    public static final int DIALOG_START_TIME = 1;
    public static final int DIALOG_DATE = 2;

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

    /**
     * Show the time picker dialog fragment
     * @param v
     */
    protected void showTimePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    /**
     * Show the date picker dialog
     * @param v
     */
    protected void showDatePicker(View v) {
        DialogFragment newFragment = new DatePickerfragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Override this to change object state on dialog change. Since baseActivity shows dialog fragments.
     * In fragment on close:
     * <code>
     *     BaseActivity activity = (BaseActivity) getActivity();
     *     // check if null
     *     activity.updateDialogOnClose(DerivedActivity.DATA_TYPE, data)
     * </code>
     */
    public void updateOnDialogClose(int mode, String data) {

    }


    protected void showAlertDialog(String title, String mesg) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(mesg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
