package com.csci5308.w22.wiseshopping.exceptions.user;

/**
 * @author Elizabeth James
 */
public class NoSuchUserException extends RuntimeException{
    String message;
    public NoSuchUserException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
