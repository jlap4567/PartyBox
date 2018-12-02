package com.partybox;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.partyboxAPI.JSONObjectWrapper;
import com.partyboxAPI.Party;
import com.partybox.adapters.PartySummaryAdapter;
import com.partyboxAPI.PartyFactory;
import com.partyboxAPI.PartySummary;
import com.partyboxAPI.exceptions.PartyBoxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        setListAdapter(partySummaryAdapter);

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToActivity(v, MainActivity.class);
            }
        });
    }

    private void switchToActivity(View v, Class<? extends Activity> className) {
        Intent intent = new Intent();
        intent.setClass(this, className);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        PartySummary selectedParty = (PartySummary) this.getListAdapter().getItem(position);
        String partyFilePath = selectedParty.getFilePath();
        try {
            File partyFile = new File(partyFilePath);
            FileInputStream fis = new FileInputStream(partyFile);
            byte[] data = new byte[(int) partyFile.length()];
            fis.read(data);
            fis.close();
            String rawJSON = new String(data, "UTF-8");
            JSONObjectWrapper jsonObjectWrapper = new JSONObjectWrapper(rawJSON);
            PartyFactory.initNewPartyFromJSON(jsonObjectWrapper);
        } catch (FileNotFoundException e) {
            Log.e("FILE", "file not found: " + partyFilePath);
            return;
        } catch (IOException e) {
            Log.e("FILE", "IOException: " + e.getMessage());
            return;
        } catch (PartyBoxException e) {
            Log.e("JSON", "Failed to deserialize party: " + e.getMessage());
            return;
        }

        BaseActivity.currentPartyPath = selectedParty.getFilePath();
        Intent intent = new Intent();
        intent.setClass(this, ViewEditPartyActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
