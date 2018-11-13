package com.partyboxAPI.exceptions;

/**
 * Thrown when JSON builder fails to produce JSON encoding for object
 */
public class SerializationException extends PartyBoxException {
    String failedClass;
    String JSONObjectMesg;

    public SerializationException(String objClass, String jsonObjMesg) {
        this.failedClass = objClass;
        this.JSONObjectMesg = jsonObjMesg;
    }

    public String getFailedClass() {
        return failedClass;
    }

    @Override
    public String getMessage() {
        return String.format("%s for object of class: %s", JSONObjectMesg, failedClass);
    }
}