package com.partybox.exceptions;

import com.partyboxAPI.exceptions.PartyBoxException;

/**
 * Throw this on invalid user input
 * Stores useful information on which field was incorrect
 */
public class InvalidUserInputException extends PartyBoxException {
    private int id;
    private String message;

    public InvalidUserInputException(int id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "Field ID: " + Integer.toString(id) + ", Message: " + message;
    }
}
