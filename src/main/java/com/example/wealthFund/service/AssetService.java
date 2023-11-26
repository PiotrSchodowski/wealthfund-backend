package com.example.wealthFund.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.mapper.AssetMapper;
import com.example.wealthFund.mapper.FileCsvToAssetDirectoryMapper;
import com.example.wealthFund.model.AssetDirectory;
import com.example.wealthFund.model.Cryptocurrency;
import com.example.wealthFund.model.GlobalQuote;
import com.example.wealthFund.model.GlobalQuoteData;
import com.example.wealthFund.repository.AssetRepository;
import com.example.wealthFund.repository.entity.AssetEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final CryptocurrencyService cryptocurrencyService;
    private final AssetMapper assetMapper;
    private final AssetDirectoryService assetDirectoryService;
    private final FileCsvToAssetDirectoryMapper fileCsvToAssetDirectoryMapper;
    private final ScrapperService scrapperService;
    private final CurrencyService currencyService;


    public AssetDto createAssetFromManualEntry(AssetDto assetDto) {
        Optional<AssetEntity> assetEntityOptional = assetRepository.findBySymbol(assetDto.getSymbol());
        if (assetEntityOptional.isPresent()) {
            throw new WealthFundSingleException("Asset with symbol " + assetDto.getSymbol() + " already exists");
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

    public AssetEntity getAssetEntityBySymbolAndExchange(String symbol, String exchange) {

        return assetRepository.findBySymbolAndExchange(symbol, exchange)
                .orElseThrow(() -> new WealthFundSingleException("Asset with symbol " + symbol + " and exchange " + exchange + " not found"));
    }

    public List<AssetDto> createAssetsFromCryptocurrencies() {
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyService.getCryptocurrenciesFromApi();
        List<AssetEntity> assetEntities = new ArrayList<>();
        for (Cryptocurrency cryptocurrency : cryptocurrencies) {
            AssetEntity assetEntity;
            assetEntity = assetMapper.cryptocurrencyToAssetEntity(cryptocurrency);
            assetEntity.setSymbol(cryptocurrency.getSymbol().toUpperCase());
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

    public AssetEntity setAssetPrice(AssetEntity assetEntity) {
        AssetDto assetDto = new AssetDto();
        assetDto.setSymbol(assetEntity.getSymbol());
        assetDto.setName(assetEntity.getName());

        assetDto = switch (assetEntity.getExchange()) {
            case "GPW" -> scrapperService.actualizeGpwAsset(assetDto);
            case "none" -> scrapperService.actualizeCryptoAsset(assetDto);
            case "NASDAQ", "BATS", "NYSE", "NYSE ARCA", "NYSE MKT" -> scrapperService.actualizeUsaAsset(assetDto);
            default -> assetDto;
        };

        assetEntity.setPrice(assetDto.getPrice());

        return assetEntity;
    }

    public AssetDto actualizeAsset(AssetDto assetDto) {

        switch (assetDto.getExchange()) {
            case "GPW":
                assetDto = scrapperService.actualizeGpwAsset(assetDto);
                break;
            case "none":
                assetDto = scrapperService.actualizeCryptoAsset(assetDto);
                break;
            case "NASDAQ":
            case "BATS":
            case "NYSE":
            case "NYSE ARCA":
            case "NYSE MKT":
                assetDto = scrapperService.actualizeUsaAsset(assetDto);
                break;
        }
        return assetDto;
    }


    public List<AssetDto> getAllAssets() {
        List<AssetEntity> assetEntities = assetRepository.findAll();
        return assetMapper.assetListToAssetDtoList(assetEntities);

    }
}



