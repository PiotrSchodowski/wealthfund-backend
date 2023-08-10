package com.example.wealthFund.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.mapper.AssetMapper;
import com.example.wealthFund.mapper.FileCsvToAssetDirectoryMapper;
import com.example.wealthFund.model.*;
import com.example.wealthFund.repository.AssetRepository;
import com.example.wealthFund.repository.entity.AssetEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final CryptocurrencyService cryptocurrencyService;
    private final AssetMapper assetMapper;
    private final AssetDirectoryService assetDirectoryService;
    private final FileCsvToAssetDirectoryMapper fileCsvToAssetDirectoryMapper;
    private final ScrapperService scrapperService;

    public AssetService(AssetRepository assetRepository, CryptocurrencyService cryptocurrencyService,
                        AssetMapper assetMapper, AssetDirectoryService assetDirectoryService,
                        FileCsvToAssetDirectoryMapper fileCsvToAssetDirectoryMapper, ScrapperService scrapperService) {
        this.assetRepository = assetRepository;
        this.cryptocurrencyService = cryptocurrencyService;
        this.assetMapper = assetMapper;
        this.assetDirectoryService = assetDirectoryService;
        this.fileCsvToAssetDirectoryMapper = fileCsvToAssetDirectoryMapper;
        this.scrapperService = scrapperService;
    }

    public AssetDto createAssetFromManualEntry(AssetDto assetDto) {
        Optional<AssetEntity> assetEntityOptional = assetRepository.findBySymbol(assetDto.getSymbol());
        if (assetEntityOptional.isPresent()) {
            throw new NotExistException(assetDto.getSymbol());
        } else {
            assetRepository.save(assetMapper.assetDtoToAssetEntity(assetDto));
            return assetDto;
        }
    }

    public AssetDto updatePriceOfAssetFromManualEntry(String symbol, float price) {
        Optional<AssetEntity> assetEntityOptional = assetRepository.findBySymbol(symbol);
        if (assetEntityOptional.isPresent()) {
            AssetEntity assetEntity = assetEntityOptional.get();
            assetEntity.setPrice(price);
            assetRepository.save(assetEntity);
            return assetMapper.assetEntityToAssetDto(assetEntity);
        } else {
            throw new NotExistException(symbol);
        }
    }

    public boolean deleteAssetBySymbol(String symbol) {
        Optional<AssetEntity> assetEntityOptional = assetRepository.findBySymbol(symbol);
        if (assetEntityOptional.isPresent()) {
            int value = assetRepository.deleteBySymbol(symbol);
            return value != 0;

        } else {
            throw new NotExistException(symbol);
        }
    }

    public AssetDto getAssetBySymbol(String symbol) {
        Optional<AssetEntity> assetEntityOptional = assetRepository.findBySymbol(symbol);
        if (assetEntityOptional.isPresent()) {
            AssetEntity assetEntity = assetEntityOptional.get();
            return assetMapper.assetEntityToAssetDto(assetEntity);
        } else {
            throw new NotExistException(symbol);
        }
    }

    public AssetEntity getAssetEntityBySymbolAndExchange(String symbol, String exchange) {
        AssetEntity assetSymbolAndExchange = assetRepository.findBySymbolAndExchange(symbol, exchange)
                .orElseThrow(() -> new WealthFundSingleException("Asset with symbol " + symbol + " and exchange " + exchange + " not found"));

        return assetSymbolAndExchange;
    }

    public List<AssetDto> createAssetsFromCryptocurrencies() {
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyService.getCryptocurrenciesFromApi();
        List<AssetEntity> assetEntities = new ArrayList<>();
        for (Cryptocurrency cryptocurrency : cryptocurrencies) {
            AssetEntity assetEntity;
            assetEntity = assetMapper.cryptocurrencyToAssetEntity(cryptocurrency);
            assetEntities.add(assetEntity);
        }
        assetRepository.saveAll(assetEntities);
        return assetMapper.assetListToAssetDtoList(assetEntities);
    }

    public List<AssetDto> createAssetsFromUsaAssetApi() {
        List<AssetDirectory> assetDirectories = assetDirectoryService.getAssetDirectoryFromAlphaVantageApi();
        List<AssetEntity> assetEntities = assetMapper.assetDirectoryListToAssetEntityList(assetDirectories);
        assetRepository.saveAll(assetEntities);
        return assetMapper.assetListToAssetDtoList(assetEntities);
    }

    public List<AssetDto> createAssetsFromGpwAssetFile() {
        List<AssetDirectory> assetDirectories = fileCsvToAssetDirectoryMapper.processCsvFile();
        List<AssetEntity> assetEntities = assetMapper.assetDirectoryListToAssetEntityList(assetDirectories);
        assetRepository.saveAll(assetEntities);
        return assetMapper.assetListToAssetDtoList(assetEntities);
    }

    public GlobalQuote savePriceToUsaAsset(String symbol) {
        Optional<AssetEntity> assetEntityOptional = assetRepository.findBySymbol(symbol);
        if (assetEntityOptional.isPresent()) {
            AssetEntity assetEntity = assetEntityOptional.get();
            GlobalQuote globalQuote = assetDirectoryService.getGlobalQuoteFromUsaAsset(assetEntity.getSymbol());
            GlobalQuoteData globalQuoteData = globalQuote.getGlobalQuoteData();
            assetEntity.setPrice(globalQuoteData.getPrice());
            assetRepository.save(assetEntity);
            return globalQuote;
        } else {
            throw new WealthFundSingleException("Asset with symbol " + symbol + " not found");
        }
    }

    public AssetEntity setPriceIfThereIsNone(AssetEntity assetEntity) {
        float price = assetEntity.getPrice();

        if (price == 0) {
            String exchange = assetEntity.getExchange().toUpperCase();
            String symbol = assetEntity.getSymbol();

            switch (exchange) {
                case "GPW":
                    price = (float) scrapperService.getAssetPriceBySymbol(symbol).getPrice();
                    break;
                case "NONE":
                    price = cryptocurrencyService.getCryptocurrencyBySymbol(symbol).getPrice();
                    break;
                case "NASDAQ":
                case "BATS":
                case "NYSE":
                case "NYSE ARCA":
                case "NYSE MKT":
                    GlobalQuote globalQuote = assetDirectoryService.getGlobalQuoteFromUsaAsset(symbol);
                    price = globalQuote.getGlobalQuoteData().getPrice();
                    break;
            }

            assetEntity.setPrice(price);
        }

        return assetEntity;
    }

}



