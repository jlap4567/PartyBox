package com.partybox.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.partybox.ActivityNewPartyFirst;
import com.partybox.BaseActivity;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;

import java.util.Calendar;
import java.util.Locale;

public class DatePickerfragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Party currentParty = PartyFactory.getNewOrCurrentParty();
        String dateString = String.format(Locale.ENGLISH, "%d-%d-%d", year, month, day);
        currentParty.setDate(dateString);
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) {
            return;
        }

        activity.updateOnDialogClose(BaseActivity.DIALOG_DATE, dateString);
    }
}
