package com.example.wealthFund.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.exception.SymbolDoesNotExistException;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.mapper.AssetMapper;
import com.example.wealthFund.mapper.CryptoToAssetMapper;
import com.example.wealthFund.model.AssetDirectory;
import com.example.wealthFund.model.Cryptocurrency;
import com.example.wealthFund.model.GlobalQuote;
import com.example.wealthFund.model.GlobalQuoteData;
import com.example.wealthFund.repository.AssetRepository;
import com.example.wealthFund.repository.entity.AssetEntity;
import org.springframework.stereotype.Service;

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
            throw new WealthFundSingleException("The asset with that symbol already exists, please try enter other symbol");
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
            throw new SymbolDoesNotExistException();
        }
    }       //todo zrobic osobnego exceptiona

    public boolean deleteAssetBySymbol(String symbol) {
        Optional<AssetEntity> assetEntityOptional = assetRepository.findBySymbol(symbol);
        if (assetEntityOptional.isPresent()) {
            int value = assetRepository.deleteBySymbol(symbol);
            return value != 0;

        } else {
            throw new SymbolDoesNotExistException();
        }
    }

    public AssetDto getAssetBySymbol(String symbol) {
        Optional<AssetEntity> assetEntityOptional = assetRepository.findBySymbol(symbol);
        if (assetEntityOptional.isPresent()) {
            AssetEntity assetEntity = assetEntityOptional.get();
            return assetMapper.assetEntityToAssetDto(assetEntity);
        } else {
            throw new SymbolDoesNotExistException();
        }
    }

    public List<AssetDto> createAssetsFromCryptocurrencies() {
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyService.getCryptocurrenciesFromApi();
        List<AssetEntity> assetEntities = CryptoToAssetMapper.mapCryptocurrenciesToAssetEntities(cryptocurrencies);
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



