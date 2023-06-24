package com.example.wealthFund.service;

import com.example.wealthFund.dto.WalletDto;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.UserEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TextValidator textValidator;
    private final UserService userService;

    public WalletService(UserRepository userRepository, WalletRepository walletRepository,TextValidator textValidator,UserService userService) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.textValidator = textValidator;
        this.userService = userService;
    }
    public WalletDto addNewWallet(String userName, String walletName, String currency) {

        textValidator.checkTextValidity(userName);
        textValidator.checkTextValidity(walletName);
        userService.validateUserExistenceThrowExceptionDoesNotExist(userName);
        validateUniqueWalletName(userName, walletName);

        UserEntity userEntity = userRepository.findByName(userName);
        WalletEntity walletEntity = createWallet(walletName, currency, userEntity);
        saveWalletWithUser(walletEntity, userEntity);

        return new WalletDto(walletEntity.getName(), walletEntity.getCurrency());
    }
    @Transactional
    public boolean deleteWallet(String userName, String walletName) {

        textValidator.checkTextValidity(userName);
        textValidator.checkTextValidity(walletName);
        userService.validateUserExistenceThrowExceptionDoesNotExist(userName);

        UserEntity userEntity = userRepository.findByName(userName);
        Set<WalletEntity> wallets = userEntity.getWallets();
        WalletEntity walletToRemove = findWalletByName(wallets, walletName);

        if (walletToRemove != null) {
            wallets.remove(walletToRemove);
            userEntity.setWallets(wallets);

            userRepository.save(userEntity);
            walletRepository.delete(walletToRemove);
            return true;
        }
        throw new WealthFundSingleException("This wallet does not exist");
    }

    WalletEntity getWalletByName(UserEntity userEntity, String walletName) {

        textValidator.checkTextValidity(walletName);
        ThrowDoesNotExistException(userEntity.getName(), walletName);
        return findWalletByName(userEntity.getWallets(), walletName);
    }
    WalletEntity findWalletByName(Set<WalletEntity> wallets, String walletName) {

        for (WalletEntity walletEntity : wallets) {
            if (walletEntity.getName().equalsIgnoreCase(walletName)) {
                return walletEntity;
            }
        }
        return null;
    }
    private void validateUniqueWalletName(String userName, String walletName) {

        if (walletRepository.existsByWalletNameAndUserName(walletName, userName)) {
            throw new WealthFundSingleException("This name of wallet already exists");
        }
    }
    private WalletEntity createWallet(String walletName, String currency, UserEntity userEntity) {

        return WalletEntity.builder()
                .name(walletName)
                .currency(currency)
                .userEntity(userEntity)
                .build();
    }
    private void saveWalletWithUser(WalletEntity walletEntity, UserEntity userEntity) {

        walletRepository.save(walletEntity);
        Set<WalletEntity> wallets = userEntity.getWallets();
        wallets.add(walletEntity);
        userEntity.setWallets(wallets);
        userRepository.save(userEntity);
    }
    void ThrowDoesNotExistException(String userName, String walletName) {

        if (!walletRepository.existsByWalletNameAndUserName(walletName, userName)) {
            throw new WealthFundSingleException("This wallet does not exist");
        }
    }
}
