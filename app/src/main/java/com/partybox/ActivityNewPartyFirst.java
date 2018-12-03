package com.partybox;

import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;
import com.partybox.exceptions.InvalidUserInputException;
import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SerializationException;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
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
    public static final int DIALOG_END_TIME = 0;
    public static final int DIALOG_START_TIME = 1;
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
        startTimeField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int inputType = startTimeField.getInputType();
                        startTimeField.setInputType(InputType.TYPE_NULL);
                        PartyFactory.getNewOrCurrentParty().setStartTime(null);
                        showTimePicker(v);
                        startTimeField.setInputType(inputType);
                        break;
                }
                return true;
            }
        });

        endTimeField = (EditText) findViewById(R.id.p_end_t);
        endTimeField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int inputType = startTimeField.getInputType();
                        startTimeField.setInputType(InputType.TYPE_NULL);
                        PartyFactory.getNewOrCurrentParty().setEndTime(null);
                        showTimePicker(v);
                        startTimeField.setInputType(inputType);
                        break;
                }
                return true;
            }
        });
        guestCountField = (EditText) findViewById(R.id.p_guests);
        locationField = (EditText) findViewById(R.id.p_location);
    }

    @Override
    public void updateOnDialogClose(int mode, String data) {
        switch (mode) {
            case DIALOG_START_TIME:
                startTimeField.setText(data);
                break;
            case DIALOG_END_TIME:
                endTimeField.setText(data);
                break;
        }
    }

    private void readFieldsAndPopulateParty(Party party) throws InvalidUserInputException {
        party.setName(nameField.getText().toString());
        String partyDate = dateField.getText().toString();
        partyDate = partyDate.replace('/', '-');
        party.setDate(partyDate);
//        party.setStartTime(startTimeField.getText().toString());
//        party.setEndTime(endTimeField.getText().toString());

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
