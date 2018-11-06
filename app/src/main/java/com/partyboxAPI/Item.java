package com.partyboxAPI;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.collect.Sets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * Represents physical item available for purchase.
 * Is immutable. A collection of these can be sorted by title.
 * Is initialized by ItemFactory when the Store is initialized.
 */
public class Item extends BaseBO implements Comparable<Item> {
    private static final String JSON_TITLE = "title";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_TYPE = "type";
    private static final String JSON_PRICE = "price";
    private static final String JSON_PRICE_UNIT = "priceUnit";
    private static final String JSON_TAGS = "tags";

    private String title;
    private String description;
    private Type type;
    private Double price;
    private PriceUnit priceUnit;
    /* tags may be some subtype of item, such as 'finger food' or 'beverage' or 'green' */
    private Set<String> tags;

    public static enum Type {
        FOOD,
        DECORATIVE,
        CUTLERY
    }

    public static enum PriceUnit {
        USDperItem,
        USDperOunce,
        USDperLiter,
        USDperPack
    }

    /**
     * Protected constructor for ItemFactory
     */
    Item(JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException {
        try {
            this.fromJSON(jsonObjectWrapper);
        } catch (PartyBoxException e) {
            Log.e("JSON", "Unable to deserialize item");
            throw e;
        }
    }

    @Override
    public JSONObjectWrapper toJSON() throws PartyBoxException {
        JSONObject itemJSONObject = new JSONObject();

        try {
            itemJSONObject.put(JSON_TITLE, title);
            itemJSONObject.put(JSON_DESCRIPTION, description);
            itemJSONObject.put(JSON_PRICE, price);
            itemJSONObject.put(JSON_PRICE_UNIT, priceUnit.name());
            itemJSONObject.put(JSON_TYPE, type.name());

            JSONArray tagSet = new JSONArray();
            for (String tag: tags) {
                tagSet.put(tag);
            }
            itemJSONObject.put(JSON_TAGS, tagSet);
        } catch (JSONException e) {
            Log.e("JSON", "Unable to serialize " + getClass().getName() + " - " + e.getMessage());
            throw new SerializationException(getClass().getName(), e.getMessage());
        }

        return new JSONObjectWrapper(itemJSONObject);
    }

    @Override
    public void fromJSON(JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException {
        JSONObject jsonObject = jsonObjectWrapper.getObject();
        if (jsonObject == null) {
            throw new SerializationException(getClass().getName(), "Expected JSONObject from wrapper");
        }

        try {
            title = jsonObject.getString(JSON_TITLE);
            description = jsonObject.getString(JSON_DESCRIPTION);
            price = jsonObject.getDouble(JSON_PRICE);
            priceUnit = PriceUnit.valueOf(jsonObject.getString(JSON_PRICE_UNIT));
            type = Type.valueOf(jsonObject.getString(JSON_TYPE));
            tags = Sets.newHashSet();

            JSONArray tagArray = jsonObject.getJSONArray(JSON_TAGS);
            for (int i = 0; i < tagArray.length(); i++) {
                tags.add(tagArray.getString(i));
            }
        } catch (JSONException e) {
            Log.e("JSON", "Unable to deserialize " + getClass().getName() + " - " + e.getMessage());
            throw new SerializationException(getClass().getName(), e.getMessage());
        }
    }

    @Override
    public int compareTo(@NonNull Item o) {
        return this.title.compareTo(o.title);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public PriceUnit getPriceUnit() {
        return priceUnit;
    }

    public Set<String> getTags() {
        return tags;
    }
}
