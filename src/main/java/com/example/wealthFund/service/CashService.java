package com.example.wealthFund.service;

import com.example.wealthFund.exception.InsufficientFundsException;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.CashEntity;
import com.example.wealthFund.repository.entity.UserCashTransactionEntity;
import com.example.wealthFund.repository.entity.UserEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CashService {

    private final WalletRepository walletRepository;
    private final TextValidator textValidator;
    private final UserService userService;
    private final WalletService walletService;
    private final ActualizationService actualizationService;

    public CashService(WalletRepository walletRepository,
                       TextValidator textValidator, UserService userService,
                       WalletService walletService, ActualizationService actualizationService) {
        this.walletRepository = walletRepository;
        this.textValidator = textValidator;
        this.userService = userService;
        this.walletService = walletService;
        this.actualizationService = actualizationService;
    }

    public boolean depositCashIntoTheWallet(String userName, String walletName, float valueOfDeposit) {
        textValidator.checkNumberValidity(valueOfDeposit);
        UserEntity userEntity = userService.getUserByName(userName);
        WalletEntity walletEntity = setupWalletWithDepositOperation(walletName, valueOfDeposit, userEntity);
        walletRepository.save(walletEntity);
        actualizationService.actualizeWalletData(walletEntity);
        return true;
    }

    public boolean withdrawCashFromTheWallet(String userName, String walletName, float valueOfWithdraw) {
        textValidator.checkNumberValidity(valueOfWithdraw);
        UserEntity userEntity = userService.getUserByName(userName);
        WalletEntity walletEntity = setupWalletWithWithdrawOperation(walletName, valueOfWithdraw, userEntity);
        walletRepository.save(walletEntity);
        actualizationService.actualizeWalletData(walletEntity);
        return true;
    }

    public void withdrawCash(WalletEntity walletEntity, float amount) {
        walletEntity.setCashEntity(tryToWithdrawAndUpdateCash(walletEntity.getCashEntity(), amount, walletEntity.getCurrency()));
    }

    CashEntity depositCash(CashEntity cashEntity, float valueOfDeposit) {
        cashEntity.setValue(cashEntity.getValue() + valueOfDeposit);
        return cashEntity;
    }

    private WalletEntity setupWalletWithDepositOperation(String walletName, float valueOfDeposit, UserEntity userEntity) {
        WalletEntity walletEntity = walletService.getWalletByName(userEntity, walletName);
        CashEntity actualCash = getOrCreateCash(walletEntity);
        CashEntity updatedCash = depositCash(actualCash, valueOfDeposit);
        walletEntity.setCashEntity(updatedCash);
        UserCashTransactionEntity userCashTransactionEntity = UserCashTransactionEntity.builder()
                .value(valueOfDeposit)
                .date(LocalDateTime.now())
                .build();

        walletEntity.getUserTransactions().add(userCashTransactionEntity);
        return walletEntity;
    }

    private WalletEntity setupWalletWithWithdrawOperation(String walletName, float valueOfWithdraw, UserEntity userEntity) {
        WalletEntity walletEntity = walletService.getWalletByName(userEntity, walletName);
        CashEntity actualCash = getOrCreateCash(walletEntity);
        CashEntity updatedCash = tryToWithdrawAndUpdateCash(actualCash, valueOfWithdraw, walletEntity.getCurrency());
        walletEntity.setCashEntity(updatedCash);
        UserCashTransactionEntity userCashTransactionEntity = UserCashTransactionEntity.builder()
                .value(valueOfWithdraw * -1)
                .date(LocalDateTime.now())
                .build();

        walletEntity.getUserTransactions().add(userCashTransactionEntity);
        return walletEntity;
    }

    private CashEntity tryToWithdrawAndUpdateCash(CashEntity cashEntity, float valueOfWithdraw, String currency) {
        float previousValueOfCash = 0;
        Optional<CashEntity> optionalCash = Optional.ofNullable(cashEntity);
        if (optionalCash.isPresent()) {
            previousValueOfCash = optionalCash.get().getValue();
        }
        if (previousValueOfCash < valueOfWithdraw) {
            throw new InsufficientFundsException(previousValueOfCash, valueOfWithdraw, currency);
        }
        cashEntity.setValue(previousValueOfCash - valueOfWithdraw);
        return cashEntity;
    }

    private CashEntity getOrCreateCash(WalletEntity walletEntity) {
        Optional<CashEntity> optionalCash = Optional.ofNullable(walletEntity.getCashEntity());
        return optionalCash.orElseGet(CashEntity::new);
    }
}
