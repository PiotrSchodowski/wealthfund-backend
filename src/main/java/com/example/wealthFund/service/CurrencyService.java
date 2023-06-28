package com.example.wealthFund.service;

import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.model.Currency;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Data
@Service
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public CurrencyService(RestTemplate restTemplate, @Value("${apiKeyOpenExchangeRates}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiUrl = "https://openexchangerates.org/api/latest.json?app_id=" + apiKey + "&base=USD&symbols=PLN,EUR";
    }

    public Currency getCurrencyFromApi() {

        ResponseEntity<Currency> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Currency.class);
        return response.getBody();
    }

    public float convertCurrency(String baseCurrency, String targetCurrency, float valueToChange) {

        Currency currency = getCurrencyFromApi();
        float baseToTargetRate;

        if (baseCurrency.equals("USD")) {
            baseToTargetRate = getRateForCurrency(targetCurrency, currency);
        } else if (targetCurrency.equals("USD")) {
            baseToTargetRate = 1 / getRateForCurrency(baseCurrency, currency);
        } else {
            float baseToUSDRate = 1 / getRateForCurrency(baseCurrency, currency);
            float usdToTargetRate = getRateForCurrency(targetCurrency, currency);
            baseToTargetRate = baseToUSDRate * usdToTargetRate;
        }

        return valueToChange * baseToTargetRate;
    }

    private float getRateForCurrency(String currencyCode, Currency currency) {

        switch (currencyCode) {
            case "PLN":
                return currency.getRates().getPLN();
            case "EUR":
                return currency.getRates().getEUR();

            default:
                throw new WealthFundSingleException("Unknown currency");
        }
    }

}


