package com.example.wealthFund.mapper;

import com.example.wealthFund.model.Cryptocurrency;
import com.example.wealthFund.repository.entity.AssetEntity;

import java.util.ArrayList;
import java.util.List;

public class CryptoMapper {

    public static List<AssetEntity> mapCryptocurrenciesToAssetEntities(List<Cryptocurrency> cryptocurrencies) {
        List<AssetEntity> assetEntities = new ArrayList<>();
        for (Cryptocurrency cryptocurrency : cryptocurrencies) {
            AssetEntity assetEntity = new AssetEntity();
            assetEntity.setName(cryptocurrency.getName());
            assetEntity.setIsin(cryptocurrency.getSymbol());
            assetEntity.setValue((float) cryptocurrency.getCurrentPrice());
            assetEntities.add(assetEntity);
        }
        return assetEntities;
    }
}
