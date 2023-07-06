package com.example.wealthFund.service;

import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.UserEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TextValidator textValidator;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddNewWallet() {
        String userName = "Piotr";
        String walletName = "Xtb";
        String currency = "USD";

        UserEntity userEntity = new UserEntity();
        Set<WalletEntity> wallets = new HashSet<>();
        userEntity.setWallets(wallets);

        when(userRepository.findByName(userName)).thenReturn(userEntity);
        doNothing().when(textValidator).checkTextValidity(anyString());
        when(textValidator.checkAndAdjustCurrencyCode(currency)).thenReturn(currency);
        when(walletRepository.existsByWalletNameAndUserName(walletName, userName)).thenReturn(false);

        assertDoesNotThrow(() -> walletService.addNewWallet(userName, walletName, currency));
    }

    @Test
    void shouldReturnTrueAndDeleteWallet() {
        String userName = "Piotr";
        String walletName = "Xtb";

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userName);
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setName(walletName);
        walletEntity.setUserEntity(userEntity);
        Set<WalletEntity> wallets = new HashSet<>();
        wallets.add(walletEntity);
        userEntity.setWallets(wallets);

        when(userRepository.findByName(userName)).thenReturn(userEntity);
        doNothing().when(textValidator).checkTextValidity(anyString());
        doNothing().when(userService).validateUserExistenceThrowExceptionDoesNotExist(userName);


        boolean result = walletService.deleteWallet(userName, walletName);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseAndThrowExceptionWallet() {
        String userName = "Piotr";
        String walletName = "Xtb";

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userName);
        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setName("walletName");
        walletEntity.setUserEntity(userEntity);
        Set<WalletEntity> wallets = new HashSet<>();
        wallets.add(walletEntity);
        userEntity.setWallets(wallets);

        when(userRepository.findByName(userName)).thenReturn(userEntity);
        doNothing().when(textValidator).checkTextValidity(anyString());
        doNothing().when(userService).validateUserExistenceThrowExceptionDoesNotExist(userName);

        WealthFundSingleException exception = assertThrows(WealthFundSingleException.class, () -> walletService.deleteWallet(userName, walletName));
        Assertions.assertEquals("This wallet does not exist", exception.getMessage());

    }
}

