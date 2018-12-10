package com.partybox.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

import com.partybox.BaseActivity;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;

import java.util.Calendar;
import java.util.Locale;

/**
 * Time Picker dialog
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute, false);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Party currentParty = PartyFactory.getNewOrCurrentParty();
        String timeString = String.format(Locale.ENGLISH, "%d:%d", hourOfDay, minute);
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity == null) {
            return;
        }

        if (currentParty.getStartTime() == null) {
            activity.updateOnDialogClose(BaseActivity.DIALOG_START_TIME, timeString);
            currentParty.setStartTime(timeString);
        } else {
            activity.updateOnDialogClose(BaseActivity.DIALOG_END_TIME, timeString);
            currentParty.setEndTime(timeString);
        }

    }
}
