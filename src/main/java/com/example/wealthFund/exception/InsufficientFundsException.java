package com.example.wealthFund.exception;

public class InsufficientFundsException extends RuntimeException {
    private static final String MESSAGE_FORMAT = "Insufficient funds in the account! You are trying to trade for an amount: %.2f %s, " +
            "when you have in your account: %.2f %s";

    public InsufficientFundsException(float valueInTheAccount, float subtractedValue, String currency) {
        super(String.format(MESSAGE_FORMAT, subtractedValue, currency, valueInTheAccount, currency));
    }
}

