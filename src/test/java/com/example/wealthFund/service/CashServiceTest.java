package com.example.wealthFund.service;

import com.example.wealthFund.exception.InsufficientFundsException;
import com.example.wealthFund.exception.TextNotAcceptableLengthException;
import com.example.wealthFund.repository.entity.CashEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CashServiceTest {

    @InjectMocks
    CashService cashService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTheSameValueWhenDepositCash() {
        CashEntity cashEntity = new CashEntity();
        float valueOfDeposit = 100.55f;

        cashEntity = cashService.depositCash(cashEntity, valueOfDeposit);

        Assertions.assertEquals(100.55f, cashEntity.getValue());
    }

    @Test
    void shouldReturnCorrectlyValueOfPortfolio() {
        CashEntity cashEntity = new CashEntity();
        cashEntity.setValue(101.55f);

        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setCashEntity(cashEntity);

        float valueOfWithdraw = 100.55f;
        cashService.withdrawCash(walletEntity, valueOfWithdraw);

        cashEntity = walletEntity.getCashEntity();
        Assertions.assertEquals(1.0f, cashEntity.getValue());
    }

    @Test
    void shouldThrowExceptionWhenToLittleCashInWallet(){
        CashEntity cashEntity = new CashEntity();
        cashEntity.setValue(1.55f);

        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setCashEntity(cashEntity);

        float valueOfWithdraw = 100.55f;

        assertThrows(InsufficientFundsException.class, () -> cashService.withdrawCash(walletEntity, valueOfWithdraw));
    }

}
