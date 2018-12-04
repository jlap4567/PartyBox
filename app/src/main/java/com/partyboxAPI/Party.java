package com.partyboxAPI;

import android.util.Log;

import com.google.common.collect.ImmutableList;
import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SerializationException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Party Class, Party is a Singleton (only one instance exists in memory at a time).
 * Reason: User can only actively edit one party at a time, hence only one instance of a party
 * needs to exist in memory
 */
public class Party extends BaseBO {
    private static final String JSON_NAME = "name";
    private static final String JSON_GUESTS = "guests";
    private static final String JSON_LOCATION = "location";
    private static final String JSON_DATE = "data";
    private static final String JSON_START_TIME = "startTime";
    private static final String JSON_END_TIME = "endTime";

    private String name;
    private Integer guestCount;         // TODO: replace later with guest list
    private String location;
    private String date;                // TODO: replace later with Date
    private String startTime, endTime;  // TODO: replace later with Time
    private OrderInfo orderInfo;
    //private ShoppingCart shoppingCart; TODO: implement

    Party() {
        // protected constructor for PartyFactory
    }

    /**
     * The summary is the date:name of a party, this shall be the file name of each saved party + .party
     * @return
     */
    public String getSummary() {
        return date + ":" + name;
    }

    @Override
    public JSONObjectWrapper toJSON() throws PartyBoxException {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(JSON_NAME, name);
            jsonObject.put(JSON_DATE, date);
            jsonObject.put(JSON_START_TIME, startTime);
            jsonObject.put(JSON_END_TIME, endTime);
            jsonObject.put(JSON_LOCATION, location);
            jsonObject.put(JSON_GUESTS, guestCount);
            return new JSONObjectWrapper(jsonObject);
        } catch (JSONException e) {
            Log.e("JSON", e.getMessage());
            throw new SerializationException(getClass().getName(), e.getMessage());
        }

    }

    @Override
    public void fromJSON(JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException {
        try {
            JSONObject jsonObject = jsonObjectWrapper.getObject();

            if (jsonObject == null) {
                throw new SerializationException(getClass().getName(), "Expected JSONObject from JSONObjectWrapper");
            }

            name = jsonObject.getString(JSON_NAME);
            date = jsonObject.getString(JSON_DATE);
            startTime = jsonObject.getString(JSON_START_TIME);
            endTime = jsonObject.getString(JSON_END_TIME);
            location = jsonObject.getString(JSON_LOCATION);
            guestCount = jsonObject.getInt(JSON_GUESTS);

        } catch (JSONException e) {
            Log.e("JSON", e.getMessage());
            //throw new SerializationException(getClass().getName(), e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Name: " + name.toString() +
                "\nDate: " + date.toString() +
                "\nStart Time: " + startTime.toString() +
                "\nEnd Time: " + endTime.toString() +
                "\nLocation: " + location +
                "\nGuests: " + guestCount.toString();
    }

    @Override
    public int hashCode() {
        return hashObjects(
                ImmutableList.of(
                        name, date, startTime, endTime, location, guestCount
                )
        );
    }

    // generated automatically
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }
}
