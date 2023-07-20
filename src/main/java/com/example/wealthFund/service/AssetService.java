package com.example.wealthFund.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.mapper.AssetMapper;
import com.example.wealthFund.model.AssetDirectory;
import com.example.wealthFund.model.Cryptocurrency;
import com.example.wealthFund.model.GlobalQuote;
import com.example.wealthFund.model.GlobalQuoteData;
import com.example.wealthFund.repository.AssetRepository;
import com.example.wealthFund.repository.entity.AssetEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final CryptocurrencyService cryptocurrencyService;
    private final AssetMapper assetMapper;
    private final AssetDirectoryService assetDirectoryService;
    private final TextValidator textValidator; //bedzie zaraz wykorzystywany ;)

    public AssetService(AssetRepository assetRepository, CryptocurrencyService cryptocurrencyService, AssetMapper assetMapper, AssetDirectoryService assetDirectoryService, TextValidator textValidator) {
        this.assetRepository = assetRepository;
        this.cryptocurrencyService = cryptocurrencyService;
        this.assetMapper = assetMapper;
        this.assetDirectoryService = assetDirectoryService;
        this.textValidator = textValidator;
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

    public List<AssetDto> createAssetsFromAssetDirectory() {
        List<AssetDirectory> assetDirectories = assetDirectoryService.getAssetDirectoryFromAlphaVantageApi();
        List<AssetEntity> assetEntities = assetMapper.assetDirectoryListToAssetEntityList(assetDirectories);
        assetRepository.saveAll(assetEntities);
        return assetMapper.assetListToAssetDtoList(assetEntities);
    }

    public GlobalQuote savePriceToUsaAsset(String symbol) {
        Optional<AssetEntity> assetEntityOptional = assetRepository.findBySymbol(symbol);
        if (assetEntityOptional.isPresent()) {
            AssetEntity assetEntity = assetEntityOptional.get();
            GlobalQuote globalQuote = assetDirectoryService.getGlobalQuoteFromUsaAsset(assetEntity.getSymbol());
            GlobalQuoteData globalQuoteData = globalQuote.getGlobalQuoteData();    //todo tutaj nastąpią zmiany w nazewnictwie, zobacze najpierw co zwracaja inne Api
            assetEntity.setPrice(globalQuoteData.getPrice());
            assetRepository.save(assetEntity);
            return globalQuote;
        } else {
            throw new WealthFundSingleException("Asset with symbol " + symbol + " not found");
        }
    }

}



