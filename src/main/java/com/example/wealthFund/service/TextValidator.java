package com.example.wealthFund.service;

import com.example.wealthFund.exception.*;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class TextValidator {

    public void checkTextValidity(String text) {
        containsWhitespaces(text);
        isTheTextNotAcceptableLength(text);
        containsSpecialCharacters(text);
    }

    public void checkNumberValidity(float text) {
        shouldByOnlyPositive(text);
        validateFloatPrecision(text);
    }

    public String checkAndAdjustCurrencyCode(String text) {
        text = text.toUpperCase(Locale.ROOT);
        if (!(text.equals("USD") || text.equals("PLN") || text.equals("EUR"))) {
            throw new WealthFundSingleException("please enter \"USD\" , \"EUR\" , \"PLN. Other currencies are not supported.");
        } else {
            return text;
        }
    }

    private void containsWhitespaces(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i))) {
                throw new ContainsWhiteSpacesException();
            }
        }
    }

    private void isTheTextNotAcceptableLength(String text) {
        if (text.length() < 3 || text.length() > 16) {
            throw new TextNotAcceptableLengthException();
        }
    }

    private void containsSpecialCharacters(String text) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        if (pattern.matcher(text).find()) {
            throw new ContainsSpecialCharactersException();
        }
    }

    private void shouldByOnlyPositive(Float text) {
        if (text < 0) {
            throw new ShouldByOnlyPositiveException();
        }
    }

    private void validateFloatPrecision(Float text) {
        String numberString = Float.toString(text);
        int decimalIndex = numberString.indexOf('.');
        if (decimalIndex != -1) {
            int precision = numberString.length() - decimalIndex - 1;
            if (precision > 2) {
                throw new PrecisionException();
            }
        }
    }
}

