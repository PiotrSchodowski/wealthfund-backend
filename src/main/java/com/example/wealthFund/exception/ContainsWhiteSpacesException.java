package com.example.wealthFund.exception;

public class ContainsWhiteSpacesException extends RuntimeException{
    public static final String MESSAGE = "Text cannot contain whitespace, please enter text again";

    public ContainsWhiteSpacesException(){
        super(MESSAGE);
    }
}
