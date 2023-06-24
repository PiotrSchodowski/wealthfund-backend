package com.example.wealthFund.exception;

public class TextNotAcceptableLengthException extends RuntimeException{
    public static final String MESSAGE = "Text should contain from 3 to 16 characters, please enter text again";

    public TextNotAcceptableLengthException(){
        super(MESSAGE);
    }
}
