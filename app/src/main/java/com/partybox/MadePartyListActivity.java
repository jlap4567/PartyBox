package com.partybox;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.partyboxAPI.PartyFactory;
import com.partyboxAPI.PartySummary;
import com.partyboxAPI.exceptions.PartyBoxException;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public class MadePartyListActivity extends BaseActivity {
    public static final String PARTIES_DIRECTORY = "/savedParties/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.made_party_list);

        // Get party summaries
        Context context = getApplicationContext();
        File localFilesDir = context.getFilesDir();
        String partiesDir = savedPartiesDirExists(localFilesDir);
        if (partiesDir == null) {
            File newPartiesDir = new File(localFilesDir.getName() + PARTIES_DIRECTORY);
            if (!newPartiesDir.mkdir()) {
                throw new java.lang.RuntimeException();
            }
            partiesDir = newPartiesDir.getName();
        }

        Set<PartySummary> partySummaries = null;
        try {
            partySummaries = PartyFactory.getPartySummaries(partiesDir);
        } catch (PartyBoxException e) {
            throw new java.lang.RuntimeException(e.getMessage());
        }


    }

    /**
     * Returns path to dir if exists, null otherwise
     * @param localFilesDir
     * @return
     */
    private String savedPartiesDirExists(File localFilesDir) {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(PARTIES_DIRECTORY);
            }
        };

        File[] partiesDir = localFilesDir.listFiles(filter);
        if (partiesDir.length == 0) {
            return null;
        }

        return partiesDir[0].getName();
    }
}
