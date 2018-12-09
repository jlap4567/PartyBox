package com.partybox;

import com.partyboxAPI.OrderInfo;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;
import com.partybox.exceptions.InvalidUserInputException;
import com.partyboxAPI.ShoppingCart;
import com.partyboxAPI.exceptions.PartyBoxException;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class ActivityNewPartyFirst extends BaseActivity {
    private static String ALERT_MESG = "Field(s) left blank";
    private static String ALERT_TITLE = "Incomplete Form";

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
                PartyFactory.clearCurrentParty();
                ShoppingCart.clearInstance();
                switchToActivity(v, MainActivity.class, Direction.RIGHT);
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
                    showAlertDialog(ALERT_TITLE, ALERT_MESG);
                    return;
                }

                if (PartyFactory.getNewOrCurrentParty().verify()) {
                    writePartyToFile();
                    PartyFactory.getNewOrCurrentParty().setOrderInfo(new OrderInfo());
                    switchToActivity(v, FoodListActivity.class, Direction.LEFT);
                } else {
                    showAlertDialog(ALERT_TITLE, ALERT_MESG);
                }
            }
        });

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

        if (PartyFactory.hasInstance()) {
            setFieldsFromExistingParty();
        }
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
            case DIALOG_DATE:
                dateField.setText(data);
        }
    }

    private void readFieldsAndPopulateParty(Party party) throws InvalidUserInputException {
        party.setName(nameField.getText().toString());
        String partyDate = dateField.getText().toString();
        partyDate = partyDate.replace('/', '-');
        party.setDate(partyDate);

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

    private void setFieldsFromExistingParty() {
        Party party = PartyFactory.getNewOrCurrentParty();
        // Set text is null-safe
        nameField.setText(party.getName());
        dateField.setText(party.getDate());
        startTimeField.setText(party.getStartTime());
        endTimeField.setText(party.getEndTime());
        locationField.setText(party.getLocation());

        if (party.getGuestCount() != null) {
            // toString is not
            guestCountField.setText(Integer.toString(party.getGuestCount()));
        }
    }
}
