package com.example.wealthFund.exception;

public class PrecisionException extends RuntimeException{
    public static final String MESSAGE = "Text has more than two decimal places";

    public PrecisionException(){
        super(MESSAGE);
    }
}
