package com.partyboxAPI.exceptions;

/**
 * Thrown when party data cannot be read from file
 */
public class CorruptPartyFileException extends PartyBoxException {
    private String path;
    private String message;

    public CorruptPartyFileException(String path, String message) {
        this.path = path;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return path+":"+message;
    }

    public String getPath() {
        return path;
    }
}
