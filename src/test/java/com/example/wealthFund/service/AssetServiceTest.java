package com.example.wealthFund.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.mapper.AssetMapper;
import com.example.wealthFund.mapper.FileCsvToAssetDirectoryMapper;
import com.example.wealthFund.model.*;
import com.example.wealthFund.repository.AssetRepository;
import com.example.wealthFund.repository.entity.AssetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetServiceTest {

    @InjectMocks
    AssetService assetService;

    @Mock
    ScrapperService scrapperService;

    @Mock
    CryptocurrencyService cryptocurrencyService;

    @Mock
    AssetDirectoryService assetDirectoryService;

    @Mock
    AssetRepository assetRepository;

    @Mock
    FileCsvToAssetDirectoryMapper fileCsvToAssetDirectoryMapper;

    @Mock
    AssetMapper assetMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAssetDtoWhenFoundSymbolAndExchange() { //todo nie czaje tego testu czemu nie przechodzi, program przechodzi
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setSymbol("BTC");
        assetEntity.setExchange("none");

        when(assetService.getAssetEntityBySymbolAndExchange("BTC", "none")).thenReturn(assetEntity);

        AssetEntity assetActual = assetService.getAssetEntityBySymbolAndExchange("BTC", "none");

        assertEquals("BTC", assetActual.getSymbol());
    }

    @Test
    void shouldSetPriceForZeroPriceByCrypto() {
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setSymbol("BTC");
        assetEntity.setExchange("NONE");
        assetEntity.setPrice(0);

        Cryptocurrency cryptocurrency = new Cryptocurrency();
        cryptocurrency.setSymbol("BTC");
        cryptocurrency.setPrice(50000);

        when(cryptocurrencyService.getCryptocurrencyBySymbol("BTC")).thenReturn(cryptocurrency);

        AssetEntity updatedAssetEntity = assetService.setPriceIfThereIsNone(assetEntity);

        verify(cryptocurrencyService).getCryptocurrencyBySymbol("BTC");

        assertEquals(50000, updatedAssetEntity.getPrice());
    }

    @Test
    void shouldSetPriceForZeroPriceByExchangeGPW() {
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setSymbol("PKO");
        assetEntity.setExchange("GPW");
        assetEntity.setPrice(0);

        AssetPrice stockPrice = new AssetPrice();
        stockPrice.setSymbol("PKO");
        stockPrice.setPrice(150);

        when(scrapperService.getAssetPriceBySymbol("PKO")).thenReturn(stockPrice);

        AssetEntity updatedAssetEntity = assetService.setPriceIfThereIsNone(assetEntity);

        verify(scrapperService).getAssetPriceBySymbol("PKO");

        assertEquals(150, updatedAssetEntity.getPrice());
    }

    @Test
    void shouldSetPriceForZeroPriceByExchangeNASDAQ() {
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setSymbol("MSFT");
        assetEntity.setExchange("NASDAQ");
        assetEntity.setPrice(0);

        GlobalQuote globalQuote = new GlobalQuote();
        GlobalQuoteData globalQuoteData = new GlobalQuoteData();
        globalQuoteData.setPrice(300);
        globalQuote.setGlobalQuoteData(globalQuoteData);

        when(assetDirectoryService.getGlobalQuoteFromUsaAsset("MSFT")).thenReturn(globalQuote);

        AssetEntity updatedAssetEntity = assetService.setPriceIfThereIsNone(assetEntity);

        verify(assetDirectoryService).getGlobalQuoteFromUsaAsset("MSFT");

        assertEquals(300, updatedAssetEntity.getPrice());
    }

    @Test
    void shouldSavePriceForExistingAsset() {
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setSymbol("AAPL");

        GlobalQuote globalQuote = new GlobalQuote();
        GlobalQuoteData globalQuoteData = new GlobalQuoteData();
        globalQuoteData.setPrice(150);
        globalQuote.setGlobalQuoteData(globalQuoteData);

        when(assetRepository.findBySymbol("AAPL")).thenReturn(Optional.of(assetEntity));
        when(assetDirectoryService.getGlobalQuoteFromUsaAsset("AAPL")).thenReturn(globalQuote);

        GlobalQuote savedGlobalQuote = assetService.savePriceToUsaAsset("AAPL");

        verify(assetRepository).findBySymbol("AAPL");
        verify(assetDirectoryService).getGlobalQuoteFromUsaAsset("AAPL");
        verify(assetRepository).save(assetEntity);

        assertEquals(globalQuote, savedGlobalQuote);
        assertEquals(150, assetEntity.getPrice());
    }

    @Test
    void shouldThrowExceptionForNonExistingAsset() {
        when(assetRepository.findBySymbol("AAPL")).thenReturn(Optional.empty());

        assertThrows(WealthFundSingleException.class, () -> {
            assetService.savePriceToUsaAsset("AAPL");
        });

        verify(assetRepository).findBySymbol("AAPL");
        verifyNoMoreInteractions(assetDirectoryService);
        verifyNoMoreInteractions(assetRepository);
    }

    @Test
    void shouldCreateAssetsFromUsaAssetApi() {
        List<AssetDirectory> assetDirectories = new ArrayList<>();
        AssetDirectory assetDirectory = new AssetDirectory();
        assetDirectory.setSymbol("AAPL");
        assetDirectory.setName("Apple Inc");
        assetDirectory.setExchange("NASDAQ");
        assetDirectories.add(assetDirectory);

        List<AssetEntity> assetEntities = new ArrayList<>();
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setSymbol("AAPL");
        assetEntity.setName("Apple Inc");
        assetEntity.setExchange("NASDAQ");
        assetEntities.add(assetEntity);

        when(assetDirectoryService.getAssetDirectoryFromAlphaVantageApi()).thenReturn(assetDirectories);
        when(assetMapper.assetDirectoryListToAssetEntityList(assetDirectories)).thenReturn(assetEntities);

        List<AssetDto> assetDtos = new ArrayList<>();
        AssetDto assetDto = new AssetDto();
        assetDto.setSymbol("AAPL");
        assetDto.setName("Apple Inc");
        assetDto.setExchange("NASDAQ");
        assetDtos.add(assetDto);

        when(assetMapper.assetListToAssetDtoList(assetEntities)).thenReturn(assetDtos);

        List<AssetDto> result = assetService.createAssetsFromUsaAssetApi();

        verify(assetDirectoryService).getAssetDirectoryFromAlphaVantageApi();
        verify(assetMapper).assetDirectoryListToAssetEntityList(assetDirectories);
        verify(assetRepository).saveAll(assetEntities);
        verify(assetMapper).assetListToAssetDtoList(assetEntities);

        assertEquals(assetDtos, result);
    }

    @Test
    void shouldCreateAssetsFromGpwAssetFile() {
        List<AssetDirectory> assetDirectories = new ArrayList<>();
        AssetDirectory assetDirectory = new AssetDirectory();
        assetDirectory.setSymbol("WIG20");
        assetDirectory.setName("WIG20 Index");
        assetDirectory.setExchange("GPW");
        assetDirectories.add(assetDirectory);

        List<AssetEntity> assetEntities = new ArrayList<>();
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setSymbol("WIG20");
        assetEntity.setName("WIG20 Index");
        assetEntity.setExchange("GPW");
        assetEntities.add(assetEntity);

        when(fileCsvToAssetDirectoryMapper.processCsvFile()).thenReturn(assetDirectories);
        when(assetMapper.assetDirectoryListToAssetEntityList(assetDirectories)).thenReturn(assetEntities);

        List<AssetDto> assetDtos = new ArrayList<>();
        AssetDto assetDto = new AssetDto();
        assetDto.setSymbol("WIG20");
        assetDto.setName("WIG20 Index");
        assetDto.setExchange("GPW");
        assetDtos.add(assetDto);

        when(assetMapper.assetListToAssetDtoList(assetEntities)).thenReturn(assetDtos);

        List<AssetDto> result = assetService.createAssetsFromGpwAssetFile();

        verify(fileCsvToAssetDirectoryMapper).processCsvFile();
        verify(assetMapper).assetDirectoryListToAssetEntityList(assetDirectories);
        verify(assetRepository).saveAll(assetEntities);
        verify(assetMapper).assetListToAssetDtoList(assetEntities);

        assertEquals(assetDtos, result);
    }

    @Test
    void shouldCreateAssetsFromCryptocurrencies() {
        List<Cryptocurrency> cryptocurrencies = new ArrayList<>();
        Cryptocurrency crypto1 = new Cryptocurrency();
        crypto1.setName("Bitcoin");
        crypto1.setSymbol("BTC");
        cryptocurrencies.add(crypto1);

        List<AssetEntity> assetEntities = new ArrayList<>();
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setName("Bitcoin");
        assetEntity.setSymbol("BTC");
        assetEntities.add(assetEntity);

        List<AssetDto> expectedAssetDtos = new ArrayList<>();
        AssetDto expectedAssetDto = new AssetDto();
        expectedAssetDto.setName("Bitcoin");
        expectedAssetDto.setSymbol("BTC");
        expectedAssetDtos.add(expectedAssetDto);

        when(cryptocurrencyService.getCryptocurrenciesFromApi()).thenReturn(cryptocurrencies);
        when(assetMapper.cryptocurrencyToAssetEntity(any(Cryptocurrency.class))).thenReturn(assetEntity);
        when(assetRepository.saveAll(assetEntities)).thenReturn(assetEntities);
        when(assetMapper.assetListToAssetDtoList(assetEntities)).thenReturn(expectedAssetDtos);

        List<AssetDto> result = assetService.createAssetsFromCryptocurrencies();

        verify(cryptocurrencyService).getCryptocurrenciesFromApi();
        verify(assetMapper, times(cryptocurrencies.size())).cryptocurrencyToAssetEntity(any(Cryptocurrency.class));
        verify(assetRepository).saveAll(assetEntities);
        verify(assetMapper).assetListToAssetDtoList(assetEntities);

        assertEquals(expectedAssetDtos, result);
    }

    @Test
    void shouldDeleteAssetWhenFoundSymbol() {
        String symbolToDelete = "AAPL";
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setSymbol(symbolToDelete);

        when(assetRepository.findBySymbol(symbolToDelete)).thenReturn(Optional.of(assetEntity));
        when(assetRepository.deleteBySymbol(symbolToDelete)).thenReturn(1);

        boolean result = assetService.deleteAssetBySymbol(symbolToDelete);

        verify(assetRepository).findBySymbol(symbolToDelete);
        verify(assetRepository).deleteBySymbol(symbolToDelete);

        assertTrue(result);
    }

    @Test
    void shouldThrowExceptionWhenNotExistingSymbol() {
        String symbolToDelete = "AAPL";
        when(assetRepository.findBySymbol(symbolToDelete)).thenReturn(Optional.empty());
        assertThrows(NotExistException.class, () -> assetService.deleteAssetBySymbol(symbolToDelete));
    }

    @Test
    void shouldCreateAssetWhenManualEntry() {
        AssetDto assetDto = new AssetDto();
        assetDto.setSymbol("AAPL");
        assetDto.setName("Apple Inc");
        assetDto.setExchange("NASDAQ");

        when(assetRepository.findBySymbol(assetDto.getSymbol())).thenReturn(Optional.empty());
        when(assetMapper.assetDtoToAssetEntity(assetDto)).thenReturn(new AssetEntity());

        AssetDto result = assetService.createAssetFromManualEntry(assetDto);

        verify(assetRepository).findBySymbol(assetDto.getSymbol());
        verify(assetRepository).save(any(AssetEntity.class));
        verify(assetMapper).assetDtoToAssetEntity(assetDto);

        assertEquals(assetDto, result);
    }

    @Test
    void shouldThrowExceptionWhenNotFoundAsset() {
        AssetDto assetDto = new AssetDto();
        assetDto.setSymbol("AAPL");
        assetDto.setName("Apple Inc");
        assetDto.setExchange("NASDAQ");

        when(assetRepository.findBySymbol(assetDto.getSymbol())).thenReturn(Optional.of(new AssetEntity()));

        assertThrows(NotExistException.class, () -> assetService.createAssetFromManualEntry(assetDto));
    }

    @Test
    void shouldUpdatePriceOfAssetFromManualEntry() {
        String symbol = "AAPL";
        float newPrice = 150.0f;
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setSymbol(symbol);

        when(assetRepository.findBySymbol(symbol)).thenReturn(Optional.of(assetEntity));

        AssetDto updatedAssetDto = new AssetDto();
        updatedAssetDto.setSymbol(symbol);
        updatedAssetDto.setPrice(newPrice);
        when(assetMapper.assetEntityToAssetDto(assetEntity)).thenReturn(updatedAssetDto);

        AssetDto result = assetService.updatePriceOfAssetFromManualEntry(symbol, newPrice);

        verify(assetRepository).findBySymbol(symbol);
        verify(assetRepository).save(assetEntity);
        verify(assetMapper).assetEntityToAssetDto(assetEntity);

        assertEquals(symbol, result.getSymbol());
        assertEquals(newPrice, result.getPrice(), 0.001);
    }

    @Test
    void shouldThrowExceptionWhenUpdatePriceOfAssetFromManualEntryFailed() {
        String symbol = "AAPL";
        float newPrice = 150.0f;

        when(assetRepository.findBySymbol(symbol)).thenReturn(Optional.empty());

        assertThrows(NotExistException.class, () -> assetService.updatePriceOfAssetFromManualEntry(symbol, newPrice));
    }
}
