package com.example.wealthFund.exception;

public class SymbolDoesNotExistException extends RuntimeException {
    public static final String MESSAGE = "The asset with that symbol does not exist, please try entering another symbol.";
    public SymbolDoesNotExistException(){
        super(MESSAGE);
    }
}
