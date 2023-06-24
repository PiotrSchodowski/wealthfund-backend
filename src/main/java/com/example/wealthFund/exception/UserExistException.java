package com.example.wealthFund.exception;

public class UserExistException extends RuntimeException{

    public static final String MESSAGE = " exist in database, try other name";

    public UserExistException(String userName){
        super(userName + MESSAGE);
    }
}
