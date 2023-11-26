package com.example.wealthFund.unitTests.service;

import com.example.wealthFund.repository.entity.CashEntity;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.UserCashTransactionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import com.example.wealthFund.service.CalculateWalletService;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CalculateWalletServiceTest {

    private CalculateWalletService calculateWalletService;

    @Before
    public void setUp() {
        calculateWalletService = new CalculateWalletService();
    }

    @Test
    public void shouldCalculatePercentageOfPortfolio() {
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setValueOfPosition(100.0f);

        WalletEntity walletEntity = new WalletEntity();
        CashEntity cashEntity = new CashEntity();
        cashEntity.setValue(1000.0f);
        walletEntity.setCashEntity(cashEntity);
        Set<PositionEntity> positionEntities = new HashSet<>();
        positionEntities.add(positionEntity);
        walletEntity.setPositions(positionEntities);

        float result = calculateWalletService.calculatePercentageOfPortfolio(positionEntity, walletEntity);

        float expectedResult = 9.0909f;
        assertEquals(expectedResult, result, 0.0001);
    }

    @Test
    public void shouldCalculateWalletActualResult() {
        WalletEntity walletEntity = new WalletEntity();
        CashEntity cashEntity = new CashEntity();
        cashEntity.setValue(1000.0f);
        walletEntity.setCashEntity(cashEntity);
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setValueOfPosition(1000.0f);
        Set<PositionEntity> positionEntities = new HashSet<>();
        positionEntities.add(positionEntity);
        walletEntity.setPositions(positionEntities);
        List<UserCashTransactionEntity> userTransactions = Collections.singletonList(new UserCashTransactionEntity());
        walletEntity.setUserTransactions(userTransactions);

        float result = calculateWalletService.calculateWalletActualResult(walletEntity);

        float expectedValue = 2000.0f;

        assertEquals(expectedValue, result, 0.001);
    }
}

