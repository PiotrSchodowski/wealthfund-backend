package com.example.wealthFund.exception;

public class ShouldByOnlyPositiveException extends RuntimeException{
    public static final String MESSAGE = "Please enter only positive values in the fields.";

    public ShouldByOnlyPositiveException(){
        super(MESSAGE);
    }
}

