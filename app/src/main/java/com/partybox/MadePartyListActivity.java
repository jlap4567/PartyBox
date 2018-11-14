package com.partybox;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.partybox.adapters.PartySummaryAdapter;
import com.partyboxAPI.PartyFactory;
import com.partyboxAPI.PartySummary;
import com.partyboxAPI.exceptions.PartyBoxException;

import java.io.File;
import java.util.Set;

public class MadePartyListActivity extends ListActivity {
    // This is the Adapter being used to display the list's data
    private PartySummaryAdapter partySummaryAdapter = null;
    private ListView partyList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_list);

        // Get party summaries
        Context context = getApplicationContext();
        File localFilesDir = context.getFilesDir();
        String partiesDir = BaseActivity.getOrCreatePartiesDirectory();
        Set<PartySummary> partySummaries = null;
        try {
            partySummaries = PartyFactory.getPartySummaries(partiesDir);
        } catch (PartyBoxException e) {
            throw new java.lang.RuntimeException(e.getMessage());
        }

        partySummaryAdapter = new PartySummaryAdapter(this, R.layout.party_list_entry, partySummaries);
        //partyList = (ListView) findViewById(R.id.party_list);
        //partyList.setAdapter(partySummaryAdapter);
        setListAdapter(partySummaryAdapter);

        Log.d("XXXXXXX", partySummaries.toString());
    }
}
