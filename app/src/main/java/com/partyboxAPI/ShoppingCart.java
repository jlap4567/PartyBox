package com.partyboxAPI;

import com.google.common.collect.Maps;
import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SerializationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Since all instances of items are unique, the ShoppingCart can merly be a map of each item to a quantity
 * of said item. It shall also be a single, as only one needs to be in memory at a time.
 */
public class ShoppingCart extends BaseBO {
    private static String JSON_ITEM = "item";
    private static String JSON_QUANTITY = "quantity";
    private static ShoppingCart instance;

    private Map<Item, Integer> itemToQuantityMap;

    private ShoppingCart() {
        itemToQuantityMap = Maps.newHashMap();
    }

    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }

        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

    public Map<Item, Integer> getItemToQuantityMap() {
        return itemToQuantityMap;
    }

    @Override
    public JSONObjectWrapper toJSON() throws PartyBoxException {
        JSONObjectWrapper jsonObjectWrapper = null;
        try {
            JSONArray jsonArray = new JSONArray();

            for (Item item: itemToQuantityMap.keySet()) {
                JSONObject keyValuePair = new JSONObject();
                JSONObjectWrapper itemWrapper = item.toJSON();
                keyValuePair.put(JSON_ITEM, itemWrapper.getObject());
                keyValuePair.put(JSON_QUANTITY, itemToQuantityMap.get(item));

                jsonArray.put(keyValuePair);
            }

            jsonObjectWrapper = new JSONObjectWrapper(jsonArray);
        } catch (JSONException e) {
            throw new SerializationException(getClass().getName(), e.getMessage());
        } catch (PartyBoxException e) {
            throw new SerializationException(getClass().getName(), e.getMessage());
        }

        return jsonObjectWrapper;
    }

    @Override
    public void fromJSON(JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException {
        JSONArray jsonArray = jsonObjectWrapper.getArray();
        if (jsonArray == null) {
            throw new SerializationException(getClass().getName(), "Expected array from wrapper");
        }

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject keyValuePair = jsonArray.getJSONObject(i);

                JSONObject itemObject = keyValuePair.getJSONObject(JSON_ITEM);
                Item item = ItemFactory.makeItem(Store.getMasterItemSet(), new JSONObjectWrapper(itemObject));
                if (item == null) {
                    throw new SerializationException(Item.class.getName(), "Failed to deserialize item");
                }

                Integer quantity = keyValuePair.getInt(JSON_QUANTITY);
                itemToQuantityMap.put(item, quantity);
            }
        } catch (JSONException e) {
            throw new SerializationException(getClass().getName(), e.getMessage());
        } catch (PartyBoxException e) {
            throw new SerializationException(getClass().getName(), e.getMessage());
        }
    }
}
