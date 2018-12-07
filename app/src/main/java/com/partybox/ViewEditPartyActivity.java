package com.partybox;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

import com.partybox.exceptions.InvalidUserInputException;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;

public class ViewEditPartyActivity extends BaseActivity {
    private static char OLD_DATE_DELIM = '-';
    private static char NEW_DATE_DELIM = '/';

    EditText nameField;
    EditText dateField;
    EditText startTimeField;
    EditText endTimeField;
    EditText guestCountField;
    EditText locationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.made_party_view);
        setFieldsFromParty();

        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    readFieldsAndPopulateParty(PartyFactory.getNewOrCurrentParty());
                } catch (InvalidUserInputException e) {
                    Log.i("UserInputError", e.getMessage());
                    // tell user something
                }
                switchToActivity(v, MadePartyListActivity.class, Direction.RIGHT);
            }
        });

        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File partyFile = new File(BaseActivity.currentPartyPath);
                if (!partyFile.delete()) {
                    Log.e("FILE", "Failed to delete saved party: " + BaseActivity.currentPartyPath);
                }

                BaseActivity.currentPartyPath = null;
                switchToActivity(v, MadePartyListActivity.class, Direction.RIGHT);
            }
        });
    }

    /* Copied from newPartyFirst because lazy*/
    private void readFieldsAndPopulateParty(Party party) throws InvalidUserInputException {
        party.setName(nameField.getText().toString());
        party.setDate(dateField.getText().toString());
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

    private void setFieldsFromParty() {
        Party party = PartyFactory.getNewOrCurrentParty();

        nameField = (EditText) findViewById(R.id.p_name);
        dateField = (EditText) findViewById(R.id.p_date);
        dateField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int inputType = dateField.getInputType();
                        dateField.setInputType(InputType.TYPE_NULL);
                        PartyFactory.getNewOrCurrentParty().setStartTime(null);
                        showDatePicker(v);
                        dateField.setInputType(inputType);
                        break;
                }
                return true;
            }
        });

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
                        int inputType = endTimeField.getInputType();
                        endTimeField.setInputType(InputType.TYPE_NULL);
                        PartyFactory.getNewOrCurrentParty().setEndTime(null);
                        showTimePicker(v);
                        endTimeField.setInputType(inputType);
                        break;
                }
                return true;
            }
        });
        guestCountField = (EditText) findViewById(R.id.p_guests);
        locationField = (EditText) findViewById(R.id.p_location);

        nameField.setText(party.getName());
        dateField.setText(party.getDate().replace(OLD_DATE_DELIM,NEW_DATE_DELIM));
        startTimeField.setText(party.getStartTime());
        endTimeField.setText(party.getEndTime());
        locationField.setText(party.getLocation());
        if (party.getGuestCount() != null) {
            guestCountField.setText(party.getGuestCount().toString(), TextView.BufferType.NORMAL);
        }
    }
}
