package com.partyboxAPI;

import android.util.Log;

import com.partyboxAPI.exceptions.CorruptPartyFileException;
import com.partyboxAPI.exceptions.PartyBoxException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Holds information on identifying parties without loading the entire party into memory.
 * Get these from <code>
 *     PartyFactory.getPartySummaries(String path)
 * </code>
 */
public class PartySummary {
    private Date date;
    private String name;

    private String filePath;

    PartySummary(String filePath, String date, String name) throws PartyBoxException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            Log.e("Parse", e.getMessage());
            throw new CorruptPartyFileException(name, e.getMessage());
        }
        this.name = name;
        this.filePath = filePath;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }
    
    @Override
    public String toString() {
        return date.toString() + ":" + name;
    }
}
