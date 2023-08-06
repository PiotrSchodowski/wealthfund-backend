package com.example.wealthFund.controllerTests;

import com.example.wealthFund.dto.WalletDto;
import com.example.wealthFund.restController.WalletController;
import com.example.wealthFund.service.WalletService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private final String userName = "Piotr";
    private final String walletName = "Xtb";
    private final String currency = "PLN";
    private final WalletDto walletDto = new WalletDto(walletName, currency);

    @Test
    public void shouldAddNewWallet() {
        when(walletService.addNewWallet(anyString(), anyString(), anyString())).thenReturn(walletDto);

        WalletDto result = walletController.addNewWallet(userName, walletName, currency);

        assertThat(result).isEqualTo(walletDto);
    }

    @Test
    public void shouldDeleteWallet() {
        when(walletService.deleteWallet(anyString(), anyString())).thenReturn(true);

        boolean result = walletController.deleteWallet(userName, walletName);

        assertThat(result).isTrue();
    }
}
