package com.example.wealthFund.service;

import com.example.wealthFund.dto.walletDtos.WalletDto;
import com.example.wealthFund.dto.walletDtos.WalletResponseDto;
import com.example.wealthFund.exception.ContainsWhiteSpacesException;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.mapper.WalletMapper;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.UserEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TextValidator textValidator;
    private final UserService userService;



    public ResponseEntity<?> addNewWallet(String userName, String walletName, String currency) {

        textValidator.checkTextValidity(walletName);
        currency = textValidator.checkAndAdjustCurrencyCode(currency);
        validateUniqueWalletName(userName, walletName);
        UserEntity userEntity = userService.getUserByName(userName);
        WalletEntity walletEntity = createWallet(walletName, currency, userEntity);
        saveWalletWithUser(walletEntity, userEntity);

        return ResponseEntity.ok("Wallet added successfully");
    }


    public List<WalletResponseDto> getWallets(String userName) {

        UserEntity userEntity = userService.getUserByName(userName);
        Set<WalletEntity> walletsEntity = userEntity.getWallets();
        if (!walletsEntity.isEmpty()) {
            List<WalletResponseDto> wallets = new ArrayList<>();
            for (WalletEntity walletEntity : walletsEntity) {
                WalletResponseDto walletToResponse = getWalletResponseDto(walletEntity);

                wallets.add(walletToResponse);
            }
            return wallets;
        }
        throw new NotExistException("wallets");
    }


    private WalletResponseDto getWalletResponseDto(WalletEntity walletEntity) {

        WalletResponseDto walletToResponse = new WalletResponseDto();
        walletToResponse.setName(walletEntity.getName());
        walletToResponse.setCurrency(walletEntity.getCurrency());
        walletToResponse.setPositions(walletEntity.getPositions());
        walletToResponse.setBasicValue(walletEntity.getBasicValue());
        walletToResponse.setActualValue(walletEntity.getActualValue());
        walletToResponse.setCashEntity(walletEntity.getCashEntity());
        walletToResponse.setUserTransactions(walletEntity.getUserTransactions());
        walletToResponse.setOperationHistories(walletEntity.getOperationHistories());
        walletToResponse.setWalletValueHistories(walletEntity.getWalletValueHistories());
        return walletToResponse;
    }


    @Transactional
    public ResponseEntity<?> deleteWallet(String userName, String walletName) {

        UserEntity userEntity = userService.getUserByName(userName);
        Set<WalletEntity> wallets = userEntity.getWallets();
        WalletEntity walletToRemove = findWalletByName(wallets, walletName);
        wallets.remove(walletToRemove);
        walletRepository.delete(walletToRemove);
        userEntity.setWallets(wallets);
        userRepository.save(userEntity);
        return ResponseEntity.ok("Wallet deleted successfully");
    }


    public WalletEntity getWalletEntity(String userName, String walletName) {
        return getWalletByName(userService.getUserByName(userName), walletName);
    }


    WalletEntity getWalletByName(UserEntity userEntity, String walletName) {

        textValidator.checkTextValidity(walletName);
        throwDoesNotExistException(userEntity.getName(), walletName);
        return findWalletByName(userEntity.getWallets(), walletName);
    }


    public WalletResponseDto getWalletResponseDtoByName(String userName, String walletName) {
        WalletEntity walletEntity = getWalletEntity(userName, walletName);
        return getWalletResponseDto(walletEntity);
    }


    private WalletEntity findWalletByName(Set<WalletEntity> wallets, String walletName) {

        for (WalletEntity walletEntity : wallets) {
            if (walletEntity.getName().equalsIgnoreCase(walletName)) {
                return walletEntity;
            }
        }
        throw new NotExistException(walletName);
    }


    private WalletEntity createWallet(String walletName, String currency, UserEntity userEntity) {

        return WalletEntity.builder()
                .name(walletName)
                .currency(currency)
                .userEntity(userEntity)
                .actualValue(0)
                .basicValue(0)
                .build();
    }


    private void saveWalletWithUser(WalletEntity walletEntity, UserEntity userEntity) {

        walletRepository.save(walletEntity);
        Set<WalletEntity> wallets = userEntity.getWallets();
        wallets.add(walletEntity);
        userEntity.setWallets(wallets);
        userRepository.save(userEntity);
    }


    private void validateUniqueWalletName(String userName, String walletName) {
        if (walletRepository.existsByWalletNameAndUserName(walletName, userName)) {
            throw new WealthFundSingleException("This name of wallet already exists");
        }
    }


    private void throwDoesNotExistException(String userName, String walletName) {
        if (!walletRepository.existsByWalletNameAndUserName(walletName, userName)) {
            throw new WealthFundSingleException("This wallet does not exist");
        }
    }
}
