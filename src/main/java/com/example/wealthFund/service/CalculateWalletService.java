package com.example.wealthFund.service;

import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.UserCashTransactionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import com.example.wealthFund.repository.entity.WalletValueHistory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class CalculateWalletService {

    public float calculatePercentageOfPortfolio(PositionEntity positionEntity, WalletEntity walletEntity) {
        return ((positionEntity.getValueOfPosition() * 100) / calculateWalletActualValue(walletEntity));
    }

    public float calculateWalletActualResult(WalletEntity walletEntity) {
        return calculateWalletActualValue(walletEntity) - calculateWalletBasicValue(walletEntity);
    }

    float calculateWalletActualValue(WalletEntity walletEntity) {
        float valueOfCash;
        valueOfCash = walletEntity.getCashEntity().getValue();
        Set<PositionEntity> positionEntities = walletEntity.getPositions();
        return (float) positionEntities.stream().mapToDouble(PositionEntity::getValueOfPosition).sum() + valueOfCash;
    }

    float calculateWalletBasicValue(WalletEntity walletEntity) {
        List<UserCashTransactionEntity> userTransactions = walletEntity.getUserTransactions();
        return (float) userTransactions.stream().mapToDouble(UserCashTransactionEntity::getValue).sum();
    }

    List<WalletValueHistory> updateWalletValueHistory(WalletEntity walletEntity) {
        List<WalletValueHistory> walletValueHistories = walletEntity.getWalletValueHistories();
        WalletValueHistory walletValueHistory = new WalletValueHistory();
        walletValueHistory.setActualValue(walletEntity.getActualValue());
        walletValueHistory.setBasicValue(walletEntity.getBasicValue());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime truncatedNow = LocalDateTime.of(
                now.getYear(),
                now.getMonth(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                0,
                0
        );

        walletValueHistory.setDate(truncatedNow);

        walletValueHistories.add(walletValueHistory);
        return walletValueHistories;
    }


}
