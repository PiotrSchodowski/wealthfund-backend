package com.example.wealthFund.service;

import com.example.wealthFund.model.Cryptocurrency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CryptocurrencyService {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public CryptocurrencyService(RestTemplate restTemplate, @Value("${apiUrl.coinGecko}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public List<Cryptocurrency> getCryptocurrenciesFromApi() {
        ResponseEntity<Cryptocurrency[]> response = restTemplate.exchange(apiUrl, HttpMethod.GET, null, Cryptocurrency[].class);
        return Arrays.asList(response.getBody());
    }

}
