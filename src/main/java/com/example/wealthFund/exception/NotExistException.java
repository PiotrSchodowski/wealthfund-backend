package com.example.wealthFund.exception;

public class NotExistException extends RuntimeException {
    public static final String MESSAGE = " does not exist, please try again.";
    public NotExistException(String substantive){
        super(substantive + MESSAGE);
    }
}
