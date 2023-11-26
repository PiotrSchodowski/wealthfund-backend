package com.example.wealthFund.unitTests.controllerTests;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.model.GlobalQuote;
import com.example.wealthFund.restController.AssetController;
import com.example.wealthFund.service.AssetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssetControllerTest {

    @Mock
    private AssetService assetService;

    @InjectMocks
    private AssetController assetController;

    private final String name = "Crowdstrike Holdings.";
    private final String symbol = "CRWD";
    private final String currency = "USD";
    private final float price = 150.0f;
    private final String exchange = "NASDAQ";
    private final String assetType = "Stock";

    private final float dailyPriceChange = 0;
    private final AssetDto assetDto = new AssetDto(name, symbol, currency, price, exchange, assetType, dailyPriceChange);

    @Test
    public void shouldEntryManualAsset() {
        when(assetService.createAssetFromManualEntry(any(AssetDto.class))).thenReturn(assetDto);

        AssetDto result = assetController.entryManualAsset(assetDto);

        assertThat(result).isEqualTo(assetDto);
    }

    @Test
    public void shouldEntryManualPriceOfAsset() {
        when(assetService.updatePriceOfAssetFromManualEntry(eq(symbol), eq(price))).thenReturn(assetDto);

        AssetDto result = assetController.entryManualPriceOfAsset(symbol, price);

        assertThat(result).isEqualTo(assetDto);
    }

    @Test
    public void shouldDeleteAssetBySymbol() {
        when(assetService.deleteAssetBySymbol(eq(symbol))).thenReturn(true);

        boolean result = assetController.deleteAssetBySymbol(symbol);

        assertThat(result).isTrue();
    }

    @Test
    public void shouldSaveCryptocurrenciesFromApi() {
        List<AssetDto> assetList = new ArrayList<>();
        when(assetService.createAssetsFromCryptocurrencies()).thenReturn(assetList);

        List<AssetDto> result = assetController.saveCryptocurrenciesFromApi();

        assertThat(result).isEqualTo(assetList);
    }

    @Test
    public void shouldSaveUsaAssetsFromApi() {
        List<AssetDto> assetList = new ArrayList<>();
        when(assetService.createAssetsFromUsaAssetApi()).thenReturn(assetList);

        List<AssetDto> result = assetController.saveUsaAssetsFromApi();

        assertThat(result).isEqualTo(assetList);
    }

    @Test
    public void shouldSavePriceToUsaAsset() {
        GlobalQuote globalQuote = new GlobalQuote();
        when(assetService.savePriceToUsaAsset(eq(symbol))).thenReturn(globalQuote);

        GlobalQuote result = assetController.savePriceToUsaAsset(symbol);

        assertThat(result).isEqualTo(globalQuote);
    }
}
