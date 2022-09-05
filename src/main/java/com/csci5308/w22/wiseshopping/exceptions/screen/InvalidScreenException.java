package com.csci5308.w22.wiseshopping.exceptions.screen;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * @author Elizabeth James
 */
public class InvalidScreenException extends RuntimeException {

    private String message;
    public InvalidScreenException(String message){
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
