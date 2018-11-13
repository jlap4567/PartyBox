package com.partyboxAPI;

import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SerializationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Wraps JSONObject and JSONArray into one class, PartyBoxAPI Classes know how to build themselves
 * from a JSONObjectWrapper. They also know how to serialize themselves into one
 */
public class JSONObjectWrapper {
    private JSONArray array = null;
    private JSONObject object = null;

    protected JSONObjectWrapper(JSONArray array) {
        this.array = array;
    }

    protected JSONObjectWrapper(JSONObject object) {
        this.object = object;
    }

    protected JSONObject getObject() {
        return object;
    }

    protected JSONArray getArray() {
        return array;
    }

    /**
     * Initialize JSONObjectWrapper from raw JSON String
     * @param jsonString
     * @throws PartyBoxException
     */
    public JSONObjectWrapper(String jsonString) throws PartyBoxException {
        try {
            array = new JSONArray(jsonString);
        } catch (JSONException e) {
            // intentionally fall through
        }

        try {
            object = new JSONObject(jsonString);
            return;
        } catch (JSONException e) {
            throw new SerializationException(getClass().getName(), "Invalid JSON Passed");
        }
    }

    /**
     * Retrieve raw, compressed JSON string from Wrapper
     * @return
     */
    @Override
    public String toString() {
        if (array != null) {
            return array.toString();
        } else {
            return object.toString();
        }
    }
}
