package com.example.wealthFund.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CryptocurrencyService {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public CryptocurrencyService(RestTemplate restTemplate, String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=100&page=1";
    }

}
