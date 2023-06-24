package com.example.wealthFund.service;

import com.example.wealthFund.model.Currency;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Currency getCurrency(){

        String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=KWGvxbggxVwJiqLg07VUhsB6uvbKDYYIFZpesyln&currencies=USD%2CEUR&base_currency=PLN";

        ResponseEntity<Currency> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Currency.class);
        return response.getBody();
    }
}
