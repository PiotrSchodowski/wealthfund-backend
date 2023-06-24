package com.example.wealthFund.exception;

public class UserNotExistException extends RuntimeException{

    public static final String MESSAGE = " does not exist in database";

    public UserNotExistException(String userName){
        super(userName + MESSAGE);
    }
}
