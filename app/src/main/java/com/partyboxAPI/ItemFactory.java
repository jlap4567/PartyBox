package com.partyboxAPI;

import com.partyboxAPI.exceptions.PartyBoxException;

import java.util.Set;
/**
 * Package private factory for instantiating items, used internally by API when the Store is initialized
 */
abstract class ItemFactory extends BaseBO {
    static Item makeItem(Set<Item> masterSet, JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException {
        Item item = new Item(jsonObjectWrapper);
        if (masterSet.contains(item)) {
            return null;
        }

        return item;
    }

    static Item makeItem(String name, String price, int img) {
        return new Item(name, price, img);
    }
}
