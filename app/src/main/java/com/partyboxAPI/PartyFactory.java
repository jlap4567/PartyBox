package com.partyboxAPI;

/**
 * Abstract Party Singleton Factory
 */
public abstract class PartyFactory {
    private static Party currentParty = null;

    public static Party getNewOrCurrentParty() {
        if (currentParty == null) {
            currentParty = new Party();
        }

        return currentParty;
    }

    public static void clearCurrentParty() {
        currentParty = null;
    };

    public static Party initNewPartyFromJSON(JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException {
        currentParty = new Party();
        currentParty.fromJSON(jsonObjectWrapper);
        return currentParty;
    }
}
