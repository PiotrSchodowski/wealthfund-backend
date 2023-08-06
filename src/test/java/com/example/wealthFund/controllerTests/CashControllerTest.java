package com.example.wealthFund.controllerTests;

import com.example.wealthFund.restController.CashController;
import com.example.wealthFund.service.CashService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CashControllerTest {

    @Mock
    private CashService cashService;

    @InjectMocks
    private CashController cashController;

    private final String userName = "testUser";
    private final String walletName = "testWallet";

    @Test
    public void shouldDepositCashIntoTheWallet() {
        float valueOfDeposit = 100.0f;
        when(cashService.depositCashIntoTheWallet(userName, walletName, valueOfDeposit)).thenReturn(true);

        boolean result = cashController.depositCashIntoTheWallet(userName, walletName, valueOfDeposit);

        assertThat(result).isTrue();
    }

    @Test
    public void shouldWithdrawCashFromTheWallet() {
        float valueOfWithdraw = 50.0f;
        when(cashService.withdrawCashFromTheWallet(userName, walletName, valueOfWithdraw)).thenReturn(true);

        boolean result = cashController.withdrawCashFromTheWallet(userName, walletName, valueOfWithdraw);

        assertThat(result).isTrue();
    }
}

