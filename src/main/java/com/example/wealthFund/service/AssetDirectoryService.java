package com.example.wealthFund.service;

import com.example.wealthFund.mapper.CsvToObjectMapper;
import com.example.wealthFund.model.AssetDirectory;
import com.example.wealthFund.model.GlobalQuote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AssetDirectoryService {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String apiKey;

    public AssetDirectoryService(RestTemplate restTemplate, @Value("${apiKeyAlphaVantage}") String apiKey) {
        this.restTemplate = restTemplate;
        this.baseUrl = "https://www.alphavantage.co/query";
        this.apiKey = apiKey;
    }

    public List<AssetDirectory> getAssetDirectoryFromAlphaVantageApi() {
        String url = baseUrl + "?function=LISTING_STATUS&apikey=" + apiKey;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String csvData = response.getBody();
        return CsvToObjectMapper.mapCsvAssetDirectoryToObjectAssetDirectory(csvData);
    }

    public GlobalQuote getGlobalQuoteFromUsaAsset(String symbol) {
        String url = baseUrl + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
        ResponseEntity<GlobalQuote> response = restTemplate.exchange(url, HttpMethod.GET, null, GlobalQuote.class);
        return response.getBody();
    }
}

//todo wykorzystac SpringBuilder