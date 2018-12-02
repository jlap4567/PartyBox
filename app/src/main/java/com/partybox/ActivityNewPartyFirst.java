package com.partybox;

import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;
import com.partybox.exceptions.InvalidUserInputException;
import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SerializationException;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ActivityNewPartyFirst extends BaseActivity {
    EditText nameField;
    EditText dateField;
    EditText startTimeField;
    EditText endTimeField;
    EditText guestCountField;
    EditText locationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_party_first);

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    readFieldsAndPopulateParty(PartyFactory.getNewOrCurrentParty());
                } catch (InvalidUserInputException e) {
                    Log.i("UserInputError", e.getMessage());
                    // tell user something
                }
                switchToActivity(v, MainActivity.class, Direction.LEFT);
            }
        });

        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    readFieldsAndPopulateParty(PartyFactory.getNewOrCurrentParty());
                } catch (InvalidUserInputException e) {
                    Log.i("UserInputError", e.getMessage());
                    // tell user something
                }
                //switchToActivity(v, BrowseStoreActivity.class); TODO: Implement
                // Right now just print party contents to log and write to file
                Log.i("Info", "Party:\n" + PartyFactory.getNewOrCurrentParty().toString());
                writePartyToFile();
            }
        });

        nameField = (EditText) findViewById(R.id.p_name);
        dateField = (EditText) findViewById(R.id.p_date);
        startTimeField  = (EditText) findViewById(R.id.p_start_t);
        endTimeField = (EditText) findViewById(R.id.p_end_t);
        guestCountField = (EditText) findViewById(R.id.p_guests);
        locationField = (EditText) findViewById(R.id.p_location);
    }

    private void readFieldsAndPopulateParty(Party party) throws InvalidUserInputException {
        party.setName(nameField.getText().toString());
        String partyDate = dateField.getText().toString();
        partyDate = partyDate.replace('/', '-');
        party.setDate(partyDate);
        party.setStartTime(startTimeField.getText().toString());
        party.setEndTime(endTimeField.getText().toString());

        int guestCount;
        try {
            guestCount = Integer.parseInt(guestCountField.getText().toString());
        } catch (Exception e) {
            throw new InvalidUserInputException(R.id.p_guests, "Guest count must be a positive integer");
        }

        if (guestCount <= 0) {
            throw new InvalidUserInputException(R.id.p_guests, "Guest count must be a positive integer");
        }
        party.setGuestCount(guestCount);

        party.setLocation(locationField.getText().toString());
    }

    private void writePartyToFile() {
        String fileName = null;
        try {
            String partiesDir = getOrCreatePartiesDirectory();
            Party party = PartyFactory.getNewOrCurrentParty();
            String json = party.toJSON().toString();
            fileName = partiesDir + File.separator + String.format("%s:%s.party", party.getDate().toString(), party.getName());
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.printf(json);
            writer.close();

            File partyDir = new File(partiesDir);
        } catch (PartyBoxException e) {
            throw new java.lang.RuntimeException(e.getMessage() + "\nPath: " + fileName);
        } catch (FileNotFoundException e) {
            throw new java.lang.RuntimeException(e.getMessage() + "\nPath: " + fileName);
        } catch (UnsupportedEncodingException e) {
            throw new java.lang.RuntimeException(e.getMessage());
        }
    }
}
