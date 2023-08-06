package com.example.wealthFund.service;

import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.UserCashTransactionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CalculateWalletService {

    public float calculatePercentageOfPortfolio(PositionEntity positionEntity, WalletEntity walletEntity) {
        return ((positionEntity.getValueOfPosition() * 100) / calculateWalletActualValue(walletEntity));
    }

    float calculateWalletActualResult(WalletEntity walletEntity) {
        return calculateWalletActualValue(walletEntity) - calculateWalletBasicValue(walletEntity);
    }

    float calculateWalletActualValue(WalletEntity walletEntity) {
        float valueOfCash = walletEntity.getCashEntity().getValue();
        Set<PositionEntity> positionEntities = walletEntity.getPositions();
        return (float) positionEntities.stream().mapToDouble(PositionEntity::getValueOfPosition).sum() + valueOfCash;
    }

    float calculateWalletBasicValue(WalletEntity walletEntity) {
        List<UserCashTransactionEntity> userTransactions = walletEntity.getUserTransactions();
        return (float) userTransactions.stream().mapToDouble(UserCashTransactionEntity::getValue).sum();
    }
}
