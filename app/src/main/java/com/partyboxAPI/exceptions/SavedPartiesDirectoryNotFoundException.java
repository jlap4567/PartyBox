package com.partyboxAPI.exceptions;

public class SavedPartiesDirectoryNotFoundException extends PartyBoxException {
    private String message;

    public SavedPartiesDirectoryNotFoundException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return "Could not find dir: \"" + message + "\"";
    }
}
