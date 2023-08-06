package com.example.wealthFund.service;

import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ActualizationService {

    private final WalletRepository walletRepository;
    private final CalculateWalletService calculateWalletService;

    public ActualizationService(WalletRepository walletRepository, CalculateWalletService calculateWalletService) {
        this.walletRepository = walletRepository;
        this.calculateWalletService = calculateWalletService;
    }

    public void actualizeWalletData(WalletEntity walletEntity) {
        walletEntity.setBasicValue(calculateWalletService.calculateWalletBasicValue(walletEntity));
        walletEntity.setActualValue(calculateWalletService.calculateWalletActualValue(walletEntity));

        Set<PositionEntity> positions = walletEntity.getPositions();
        for (PositionEntity eachPosition : positions) {
            eachPosition.setPercentageOfThePortfolio(calculateWalletService.calculatePercentageOfPortfolio(eachPosition, walletEntity));
        }
        walletEntity.setPositions(positions);
        walletRepository.save(walletEntity);
    }
}
