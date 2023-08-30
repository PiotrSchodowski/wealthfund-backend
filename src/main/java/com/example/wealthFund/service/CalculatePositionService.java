package com.example.wealthFund.service;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import com.example.wealthFund.repository.entity.AssetEntity;
import com.example.wealthFund.repository.entity.PositionEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CalculatePositionService {

    private final CurrencyService currencyService;
    private final AssetService assetService;

    public CalculatePositionService(CurrencyService currencyService, AssetService assetService) {
        this.currencyService = currencyService;
        this.assetService = assetService;
    }

    public float calculateTotalValueEntered(AddPositionDto addPositionDto) {
        float valueWithoutCommission = addPositionDto.getPrice() * addPositionDto.getQuantity();
        float nominalCommissionValue = adjustNominalCommission(addPositionDto.getCommission(), addPositionDto.isPercentageCommission(), valueWithoutCommission);
        float valueBeforeConvert = valueWithoutCommission + nominalCommissionValue;
        return calculateValueAfterCurrencyConvert(addPositionDto, valueBeforeConvert);
    }

    public float calculateTotalValueEntered(SubtractPositionDto subtractPositionDto) {
        return (subtractPositionDto.getQuantity() * subtractPositionDto.getPrice()) * subtractPositionDto.getEndingCurrencyRate();
    }

    PositionEntity decreasePositionData(PositionEntity positionEntity, SubtractPositionDto subtractPositionDto) {
        positionEntity.setQuantity(positionEntity.getQuantity() - subtractPositionDto.getQuantity());
        positionEntity.setValueBasedOnPurchasePrice(positionEntity.getQuantity() * positionEntity.getAveragePurchasePrice());
        positionEntity.setValueOfPosition(positionEntity.getQuantity() * positionEntity.getActualPrice());
        positionEntity.setResult(positionEntity.getValueOfPosition() - positionEntity.getValueBasedOnPurchasePrice());
        positionEntity.setRateOfReturn(calculateRateOfReturn(positionEntity.getActualPrice(), positionEntity));
        return positionEntity;
    }

    PositionEntity increasePositionData(PositionEntity positionEntity, AddPositionDto addPositionDto) {
        float averageAssetPrice = calculateAveragePrice(positionEntity, addPositionDto);
        AssetEntity assetEntity = checkIsAssetPresent(addPositionDto);
        assetEntity = assetService.setPriceIfThereIsNone(assetEntity);
        float actualPrice = convertToCurrency(assetEntity.getPrice(), assetEntity.getCurrency(), positionEntity.getWalletCurrency());

        if (positionEntity.getName() == null) {
            positionEntity.setName(assetEntity.getName());
        }
        positionEntity.setQuantity(positionEntity.getQuantity() + addPositionDto.getQuantity());
        positionEntity.setBasicCurrency(assetEntity.getCurrency());
        positionEntity.setExchange(assetEntity.getExchange());
        positionEntity.setAveragePurchasePrice(averageAssetPrice);
        positionEntity.setValueBasedOnPurchasePrice(positionEntity.getValueBasedOnPurchasePrice() + addPositionDto.getTotalValueEntered());
        positionEntity.setActualPrice(actualPrice);
        positionEntity.setValueOfPosition(actualPrice * positionEntity.getQuantity());
        positionEntity.setActualPriceDate(LocalDateTime.now());
        positionEntity.setResult(positionEntity.getValueOfPosition() - positionEntity.getValueBasedOnPurchasePrice());
        positionEntity.setRateOfReturn(calculateRateOfReturn(actualPrice, positionEntity));
        return positionEntity;
    }

    void copyPositionFields(PositionEntity source, PositionEntity destination) {
        destination.setQuantity(source.getQuantity());
        destination.setAveragePurchasePrice(source.getAveragePurchasePrice());
        destination.setValueBasedOnPurchasePrice(source.getValueBasedOnPurchasePrice());
        destination.setActualPrice(source.getActualPrice());
        destination.setValueOfPosition(source.getValueOfPosition());
        destination.setActualPriceDate(source.getActualPriceDate());
        destination.setResult(source.getResult());
        destination.setRateOfReturn(source.getRateOfReturn());
    }

    float convertToCurrency(float price, String baseCurrency, String targetCurrency) {
        if (targetCurrency.equals(baseCurrency)) {
            return price;
        } else {
            return currencyService.convertCurrency(baseCurrency, targetCurrency, price);
        }
    }

    private float calculateValueAfterCurrencyConvert(AddPositionDto addPositionDto, float valueBeforeConvert) {
        return addPositionDto.getOpeningCurrencyRate() * valueBeforeConvert;
    }

    private float adjustNominalCommission(float valueOfCommission, boolean isPercentageCommission, float valueWithoutCommission) {
        if (isPercentageCommission) {
            return (valueOfCommission * valueWithoutCommission) / 100;
        } else {
            return valueOfCommission;
        }
    }

    private AssetEntity checkIsAssetPresent(AddPositionDto addPositionDto) {
        return assetService.getAssetEntityBySymbolAndExchange(addPositionDto.getSymbol(), addPositionDto.getExchange());
    }

    private float calculateAveragePrice(PositionEntity positionEntity, AddPositionDto addPositionDto) {
        float totalQuantity = positionEntity.getQuantity() + addPositionDto.getQuantity();
        float totalValue = positionEntity.getValueBasedOnPurchasePrice() + addPositionDto.getTotalValueEntered();
        return totalValue / totalQuantity;
    }

    private float calculateRateOfReturn(float actualPrice, PositionEntity positionEntity) {
        return ((actualPrice - positionEntity.getAveragePurchasePrice()) / positionEntity.getAveragePurchasePrice()) * 100;
    }

}
