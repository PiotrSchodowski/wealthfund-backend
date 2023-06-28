package com.example.wealthFund.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


public class CurrencyServiceTest {

    @InjectMocks
    CurrencyService currencyService;

    @Test
    public void shouldReturnValueAfterChange() {
        // Given
        String baseCurrency = "USD";
        String targetCurrency = "PLN";
        float value = 100f;

        // When
        float convertedValue = currencyService.convertCurrency(baseCurrency, targetCurrency, value);

        // Then
        Assertions.assertEquals(100 * convertedValue, value);
    }

}
