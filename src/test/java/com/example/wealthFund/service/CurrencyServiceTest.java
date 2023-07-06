package com.example.wealthFund.service;

import com.example.wealthFund.model.Currency;
import com.example.wealthFund.model.Rates;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    @Mock
    RestTemplate restTemplate;

    @Value("${apiKey.OpenExchangeRates}")
    private String apiKey;

    @Value("${apiUrl.OpenExchangeRates}")
    private String apiUrl;

    private CurrencyService currencyService;

    @BeforeEach
    public void setup() {
        apiKey = "your-api-key";
        apiUrl = "https://api.example.com";
        currencyService = new CurrencyService(restTemplate, apiKey, apiUrl);
    }

    @Test
    public void shouldConvertCurrencyCorrectly() {
        String baseCurrency = "USD";
        String targetCurrency = "PLN";
        float value = 100f;


        Rates rates = new Rates();
        rates.setPLN(4.0f);
        rates.setEUR(0.9f);
        Currency currency = new Currency(rates);

        ResponseEntity<Currency> responseEntity = new ResponseEntity<>(currency, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(null), eq(Currency.class))).thenReturn(responseEntity);

        // When
        float convertedValue = currencyService.convertCurrency(baseCurrency, targetCurrency, value);

        // Then
        float expectedValue = value * 4.0f;
        Assertions.assertEquals(expectedValue, convertedValue);
    }
}

