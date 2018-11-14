package com.partyboxAPI;

import android.provider.Telephony;
import android.util.Log;

import com.partyboxAPI.exceptions.CorruptPartyFileException;
import com.partyboxAPI.exceptions.PartyBoxException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Holds information on identifying parties without loading the entire party into memory.
 * Get these from <code>
 *     PartyFactory.getPartySummaries(String path)
 * </code>
 */
public class PartySummary {
    private Date date;
    private String name;

    PartySummary(String date, String name) throws PartyBoxException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH);
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e("Parse", e.getMessage());
            throw new CorruptPartyFileException(name, e.getMessage());
        }
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return date.toString() + ":" + name;
    }
}
