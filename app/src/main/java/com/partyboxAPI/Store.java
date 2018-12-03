package com.partyboxAPI;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.partyboxAPI.exceptions.PartyBoxException;
import com.partyboxAPI.exceptions.SerializationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 *  Static Store class.
 *  Client code is restricted from pulling the entire store at once. Client code should use
 *  filter function to return a subset of the item set.
 */
public abstract class Store extends BaseBO {
    static final int RETRIES = 3;
    private static Set<Item> itemMasterSet = null;
    private static Map<Item.Type, Set<Item>> typeTable = null;
    private static Map<String, Set<Item>> tagTable = null;

    /**
     * Initialize party supply store
     * @param address
     * @param port
     */
    public static void init(String address, Integer port) throws PartyBoxException {
        itemMasterSet = Sets.newHashSet();
        typeTable = Maps.newHashMap();
        tagTable = Maps.newHashMap();
        String storeData = sync(address, port);

        try {
            populateItemSets(storeData);
        } catch (JSONException e) {
            Log.e("JSON", e.getMessage());
            throw new SerializationException("Store", e.getMessage());
        }
    }

    /**
     * Free resources used by the store
     */
    public static void close() {
        itemMasterSet = null;
        typeTable = null;
        tagTable = null;
    }

    /**
     * Return all items in store of certain type
     * @param type
     * @return
     */
    public static Set<Item> returnItemsByType(Item.Type type) {
        Set<Item> itemsByType = typeTable.get(type);
        if (itemsByType == null) {
            // Makes this null safe by returning empty set
            return Sets.newHashSet();
        }

        return itemsByType;
    }

    /**
     * Return items in store which have certain tags
     * @param tags
     * @return
     */
    public static Set<Item> getItemsByTags(Set<String> tags) {
        Set<Item> filteredItems = Sets.newHashSet();
        for (String tag: tags) {
            Set<Item> matched = tagTable.get(tag);
            if (matched != null) {
                filteredItems.addAll(matched);
            }
        }

        return filteredItems;
    }

    /**
     * Since the store maintains an open connection to the remote server, orders shall be placed via the store
     * @param cart
     * @throws PartyBoxException
     */
    public static void placeOrder(ShoppingCart cart) throws PartyBoxException {
        // TODO: Implement
    }

    /**
     * Retrieves products available for sale from remote server
     * @param address
     * @param port
     * @return
     */
    @VisibleForTesting
    public static String sync(String address, Integer port) {
        // TODO: Implement
        return "";
    }

    private static void populateItemSets(String itemSetJSON) throws PartyBoxException,JSONException {
        System.out.println(itemSetJSON);
        JSONArray itemArray = new JSONArray(itemSetJSON);
        System.out.println(itemArray.toString());
        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject itemJSONObject = itemArray.getJSONObject(i);
            System.out.println(itemJSONObject.toString());
            Item resurrectedItem = ItemFactory.makeItem(itemMasterSet, new JSONObjectWrapper(itemJSONObject));
            if (resurrectedItem == null) {
                continue;
            }

            itemMasterSet.add(resurrectedItem);

            if (typeTable.get(resurrectedItem.getType()) == null) {
                typeTable.put(resurrectedItem.getType(), Sets.<Item>newHashSet(resurrectedItem));
            } else {
                typeTable.get(resurrectedItem.getType()).add(resurrectedItem);
            }

            for (String tag: resurrectedItem.getTags()) {
                if (tagTable.get(tag) == null) {
                    tagTable.put(tag, Sets.<Item>newHashSet(resurrectedItem));
                } else {
                    tagTable.get(tag).add(resurrectedItem);
                }
            }
        }
    }

    @VisibleForTesting
    public static String readFile(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        String contents = new String();
        int character;
        while ((character = fis.read()) != -1) {
            contents += (char) character;
        }

        fis.close();
        return contents;
    }

    protected static Set<Item> getMasterItemSet() {
        return itemMasterSet;
    }
}
