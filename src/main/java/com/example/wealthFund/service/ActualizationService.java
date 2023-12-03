package com.example.wealthFund.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ActualizationService {

    private final WalletRepository walletRepository;
    private final CalculateWalletService calculateWalletService;
    private final AssetService assetService;
    private final CalculatePositionService calculatePositionService;
    private final PositionRepository positionRepository;


    public void actualizeWalletData(WalletEntity walletEntity) {

        walletEntity.setBasicValue(calculateWalletService.calculateWalletBasicValue(walletEntity));
        walletEntity.setActualValue(calculateWalletService.calculateWalletActualValue(walletEntity));

        Set<PositionEntity> positions = walletEntity.getPositions();
        for (PositionEntity eachPosition : positions) {
            eachPosition.setPercentageOfThePortfolio(calculateWalletService.calculatePercentageOfPortfolio(eachPosition, walletEntity));
            actualizePositionData(eachPosition);
        }
        walletEntity.setPositions(positions);
        walletRepository.save(walletEntity);
    }


    public void actualizeWalletDataInterval(WalletEntity walletEntity) {

        walletEntity.setBasicValue(calculateWalletService.calculateWalletBasicValue(walletEntity));
        walletEntity.setActualValue(calculateWalletService.calculateWalletActualValue(walletEntity));
        walletEntity.setWalletValueHistories(calculateWalletService.updateWalletValueHistory(walletEntity));

        Set<PositionEntity> positions = walletEntity.getPositions();
        for (PositionEntity eachPosition : positions) {
            eachPosition.setPercentageOfThePortfolio(calculateWalletService.calculatePercentageOfPortfolio(eachPosition, walletEntity));
            actualizePositionData(eachPosition);
        }
        walletEntity.setPositions(positions);
        walletRepository.save(walletEntity);
    }


    void actualizePositionData(PositionEntity positionEntity) {

        AssetDto assetDto = new AssetDto();
        assetDto.setSymbol(positionEntity.getSymbol());
        assetDto.setName(positionEntity.getName());
        assetDto.setExchange(positionEntity.getExchange());
        assetDto = assetService.actualizeAsset(assetDto);
        assetDto.setPrice(calculatePositionService.convertToCurrency(assetDto.getPrice(), positionEntity.getBasicCurrency(), positionEntity.getWalletCurrency()));

        positionEntity.setActualPrice(assetDto.getPrice());
        positionEntity.setDailyPriceChange(assetDto.getDailyPriceChange());
        positionEntity.setActualPriceDate(LocalDateTime.now());
        positionEntity.setValueOfPosition(positionEntity.getActualPrice() * positionEntity.getQuantity());
        positionEntity.setResult(positionEntity.getValueOfPosition() - positionEntity.getValueBasedOnPurchasePrice());
        positionEntity.setRateOfReturn(calculatePositionService.calculateRateOfReturn(assetDto.getPrice(), positionEntity));

        positionRepository.save(positionEntity);
    }


    public void actualizeEachWallet() {
        walletRepository.findAll().forEach(this::actualizeWalletDataInterval);
    }
}

