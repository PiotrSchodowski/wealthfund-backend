package com.example.wealthFund.service;

import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.model.AssetPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScrapperServiceTest {


    @InjectMocks
    private ScrapperService scrapperService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnNumberInApproximation() {
        String symbol = "PKN";
        float approximation = 63.5f;

        AssetPrice assetPrice = scrapperService.getAssetPriceBySymbol(symbol);

        assertEquals(symbol, assetPrice.getSymbol());
        assertEquals(approximation, assetPrice.getPrice(), 1);
    }

    @Test
    void shouldThrowExceptionWhenNotFoundSymbol() {
        String symbol = "PKNN";

        assertThrows(WealthFundSingleException.class, () -> scrapperService.getAssetPriceBySymbol(symbol));
    }
}
