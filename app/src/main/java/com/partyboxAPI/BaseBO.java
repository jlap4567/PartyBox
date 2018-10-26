package com.partyboxAPI;

import java.util.List;

abstract class BaseBO {
    static final int BASE_PRIME = 53;
    static final int OBJECT_PRIME = 17;

    /**
     * Return JSOnObjectWrapper containing compressed, parsable JSON
     * @return
     */
    public abstract JSONObjectWrapper toJSON() throws PartyBoxException;

    /**
     * Parse object from JSONObjectWrapper
     * @param jsonObjectWrapper
     */
    public abstract void fromJSON(JSONObjectWrapper jsonObjectWrapper) throws PartyBoxException;

    /**
     * Convenience function used for overriding hashcode
     * @param objects
     * @return
     */
    protected int hashObjects(List<?> objects) {
        int hash = BASE_PRIME;
        for (Object obj: objects) {
            hash =  OBJECT_PRIME * hash + obj.hashCode();
        }

        return hash;
    }
}
