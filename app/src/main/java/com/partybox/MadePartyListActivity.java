package com.partybox;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.partyboxAPI.PartyFactory;
import com.partyboxAPI.PartySummary;
import com.partyboxAPI.exceptions.PartyBoxException;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public class MadePartyListActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.made_party_list);

        // Get party summaries
        Context context = getApplicationContext();
        File localFilesDir = context.getFilesDir();
        String partiesDir = getOrCreatePartiesDirectory();
        Set<PartySummary> partySummaries = null;
        try {
            partySummaries = PartyFactory.getPartySummaries(partiesDir);
        } catch (PartyBoxException e) {
            throw new java.lang.RuntimeException(e.getMessage());
        }

        Log.d("XXXXXXX", partySummaries.toString());
    }
}
