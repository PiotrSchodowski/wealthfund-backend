package com.example.wealthFund.exception;

public class ShouldByOnlyPositiveException extends RuntimeException{
    public static final String MESSAGE = "insufficient funds in the account";

    public ShouldByOnlyPositiveException(){
        super(MESSAGE);
    }
}

