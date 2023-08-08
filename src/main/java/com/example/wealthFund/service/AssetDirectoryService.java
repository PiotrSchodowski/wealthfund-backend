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
    private static final String GLOBAL_QUOTE_PATH = "?function=GLOBAL_QUOTE&symbol=";
    private static final String LISTING_STATUS_PATH = "?function=LISTING_STATUS&apikey=";

    public AssetDirectoryService(RestTemplate restTemplate, @Value("${apiKeyAlphaVantage}") String apiKey) {
        this.restTemplate = restTemplate;
        this.baseUrl = "https://www.alphavantage.co/query";
        this.apiKey = apiKey;
    }

    public List<AssetDirectory> getAssetDirectoryFromAlphaVantageApi() {
        String url = baseUrl + LISTING_STATUS_PATH + apiKey;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        String csvData = response.getBody();
        return CsvToObjectMapper.mapCsvAssetDirectoryToObjectAssetDirectory(csvData);
    }

    public GlobalQuote getGlobalQuoteFromUsaAsset(String symbol) {
        String url = baseUrl + GLOBAL_QUOTE_PATH + symbol + "&apikey=" + apiKey;
        ResponseEntity<GlobalQuote> response = restTemplate.exchange(url, HttpMethod.GET, null, GlobalQuote.class);
        return response.getBody();
    }
}