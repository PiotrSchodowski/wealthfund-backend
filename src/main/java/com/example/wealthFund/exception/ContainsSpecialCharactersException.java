package com.example.wealthFund.exception;

public class ContainsSpecialCharactersException extends RuntimeException{
    public static final String MESSAGE = "Text cannot contain special character, please enter text again without it";

    public ContainsSpecialCharactersException(){
        super(MESSAGE);
    }
}
