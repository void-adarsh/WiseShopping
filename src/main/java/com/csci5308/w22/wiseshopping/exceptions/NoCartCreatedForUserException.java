package com.csci5308.w22.wiseshopping.exceptions;

/**
 * @author Adarsh Kannan
 */
public class NoCartCreatedForUserException extends RuntimeException {
    private String message;

    public NoCartCreatedForUserException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}