package com.example.wealthFund.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WealthFundExceptionHandler {

    @ExceptionHandler(WealthFundSingleException.class)
    public ResponseEntity<String> handleSingleException(WealthFundSingleException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<String> handleUserNotExistException(UserNotExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<String> handleUserExistException(UserExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TextNotAcceptableLengthException.class)
    public ResponseEntity<String> handleTextNotAcceptableLengthException(TextNotAcceptableLengthException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(ContainsWhiteSpacesException.class)
    public ResponseEntity<String> handleContainsWhiteSpacesException(ContainsWhiteSpacesException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(ContainsSpecialCharactersException.class)
    public ResponseEntity<String> handleContainsSpecialCharactersException(ContainsSpecialCharactersException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(ShouldByOnlyPositiveException.class)
    public ResponseEntity<String> handleShouldByOnlyPositiveException(ShouldByOnlyPositiveException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

    @ExceptionHandler(PrecisionException.class)
    public ResponseEntity<String> handlePrecisionException(PrecisionException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }
}
