package com.example.wealthFund.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.mapper.AssetMapper;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ActualizationService {

    private final WalletRepository walletRepository;
    private final CalculateWalletService calculateWalletService;
    private final AssetService assetService;
    private final AssetMapper assetMapper;
    private final CalculatePositionService calculatePositionService;
    private final PositionRepository positionRepository;

    public ActualizationService(WalletRepository walletRepository, CalculateWalletService calculateWalletService,
                                CalculatePositionService calculatePositionService, AssetService assetService, AssetMapper assetMapper,
                                PositionRepository positionRepository) {
        this.walletRepository = walletRepository;
        this.calculateWalletService = calculateWalletService;
        this.assetService = assetService;
        this.assetMapper = assetMapper;
        this.calculatePositionService = calculatePositionService;
        this.positionRepository = positionRepository;
    }

    public void actualizeWalletData(WalletEntity walletEntity) {
        walletEntity.setBasicValue(calculateWalletService.calculateWalletBasicValue(walletEntity));
        walletEntity.setActualValue(calculateWalletService.calculateWalletActualValue(walletEntity));

        Set<PositionEntity> positions = walletEntity.getPositions();
        for (PositionEntity eachPosition : positions) {
            eachPosition.setPercentageOfThePortfolio(calculateWalletService.calculatePercentageOfPortfolio(eachPosition, walletEntity));
            actualizePositionAssetPrice(eachPosition);
        }
        walletEntity.setPositions(positions);
        walletRepository.save(walletEntity);
    }

    private void actualizePositionAssetPrice(PositionEntity positionEntity) {
        AssetDto assetDto = new AssetDto();
        assetDto.setSymbol(positionEntity.getSymbol());
        assetDto.setExchange(positionEntity.getExchange());
        assetDto.setPrice(0);

        assetDto = assetMapper.assetEntityToAssetDto(assetService.setPriceIfThereIsNone(assetMapper.assetDtoToAssetEntity(assetDto)));
        assetDto.setPrice(calculatePositionService.convertToCurrency(assetDto.getPrice(), positionEntity.getBasicCurrency(), positionEntity.getWalletCurrency()));
        positionEntity.setActualPrice(assetDto.getPrice());
        positionRepository.save(positionEntity);
    }
}
