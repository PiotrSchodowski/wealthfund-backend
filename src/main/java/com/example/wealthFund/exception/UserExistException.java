package com.example.wealthFund.exception;

public class UserExistException extends RuntimeException{

    public static final String MESSAGE = " is already taken!";

    public UserExistException(String userName){
        super(userName + MESSAGE);
    }
}
