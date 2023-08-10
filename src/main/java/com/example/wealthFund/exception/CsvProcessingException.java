package com.example.wealthFund.exception;

public class CsvProcessingException extends RuntimeException {
    public static final String MESSAGE = "Csv file execution error";

    public CsvProcessingException() {
        super(MESSAGE);
    }
}
