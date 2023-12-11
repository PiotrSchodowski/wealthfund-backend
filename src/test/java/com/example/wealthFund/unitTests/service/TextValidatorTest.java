package com.example.wealthFund.unitTests.service;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import com.example.wealthFund.exception.*;
import com.example.wealthFund.service.TextValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TextValidatorTest {

    @InjectMocks
    private TextValidator textValidator;

    @Mock
    private ContainsWhiteSpacesException containsWhiteSpacesException;

    @Mock
    private TextNotAcceptableLengthException textNotAcceptableLengthException;

    @Mock
    private ContainsSpecialCharactersException containsSpecialCharactersException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldNotThrowExceptionWhileTextProper() {
        String validText = "Pioter123";
        textValidator.checkTextValidity(validText);
        verifyNoMoreInteractions(containsWhiteSpacesException, textNotAcceptableLengthException, containsSpecialCharactersException);
    }

    @Test
    void shouldThrowContainsWhiteSpacesException() {
        String textWithWhitespaces = "Piotr Schodowski";
        assertThrows(ContainsWhiteSpacesException.class, () -> textValidator.checkTextValidity(textWithWhitespaces));
    }

    @Test
    void shouldThrowTextNotAcceptableLengthException() {
        String shortText = "Oo";
        String longText = "AlaMaKotaAKotMaAleINieZalujeWcale";
        assertThrows(TextNotAcceptableLengthException.class, () -> textValidator.checkTextValidity(shortText));
        assertThrows(TextNotAcceptableLengthException.class, () -> textValidator.checkTextValidity(longText));
    }

    @Test
    void shouldThrowContainsSpecialCharactersException() {
        String textWithSpecialCharacters = "Hello@123";
        assertThrows(ContainsSpecialCharactersException.class, () -> textValidator.checkTextValidity(textWithSpecialCharacters));
    }

    @Test
    void shouldNotThrowExceptionWhileNumberIsValid() {
        float validNumber = 10.5f;
        textValidator.checkNumberValidity(validNumber);
        verifyNoMoreInteractions(containsWhiteSpacesException, textNotAcceptableLengthException, containsSpecialCharactersException);
    }

    @Test
    void shouldThrowShouldByOnlyPositiveException() {
        float negativeNumber = -10.5f;
        assertThrows(ShouldByOnlyPositiveException.class, () -> textValidator.checkNumberValidity(negativeNumber));
    }

    @Test
    void shouldThrowPrecisionException() {
        float numberWithTooManyDecimals = 10.55577f;
        assertThrows(PrecisionException.class, () -> textValidator.checkNumberValidity(numberWithTooManyDecimals));
    }

    @Test
    void shouldReturnAdjustCurrencyCode() {
        String currency = "usd";
        currency = textValidator.checkAndAdjustCurrencyCode(currency);
        assertEquals("USD", currency);
    }

    @Test
    void shouldThrowNotSupportedException() {
        String currency = "jpy";
        assertThrows(WealthFundSingleException.class, () -> textValidator.checkAndAdjustCurrencyCode(currency));
    }

}
