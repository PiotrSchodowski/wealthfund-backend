package com.example.wealthFund.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.mapper.AssetMapper;
import com.example.wealthFund.mapper.CryptoMapper;
import com.example.wealthFund.model.Cryptocurrency;
import com.example.wealthFund.repository.AssetRepository;
import com.example.wealthFund.repository.entity.AssetEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final CryptocurrencyService cryptocurrencyService;
    private final AssetMapper assetMapper;

    public AssetService(AssetRepository assetRepository, CryptocurrencyService cryptocurrencyService, AssetMapper assetMapper) {
        this.assetRepository = assetRepository;
        this.cryptocurrencyService = cryptocurrencyService;
        this.assetMapper = assetMapper;
    }

    public List<AssetDto> createAssetsFromCryptocurrencies() {
        List<Cryptocurrency> cryptocurrencies = cryptocurrencyService.getCryptocurrenciesFromApi();
        List<AssetEntity> assetEntities = CryptoMapper.mapCryptocurrenciesToAssetEntities(cryptocurrencies);
        for (AssetEntity assetEntity : assetEntities) {
            assetRepository.save(assetEntity);
        }
        return assetMapper.assetListToAssetDtoList(assetEntities);
    }
}

