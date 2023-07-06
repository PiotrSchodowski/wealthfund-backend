package com.example.wealthFund.service;

import com.example.wealthFund.dto.PositionDto;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.mapper.PositionMapper;
import com.example.wealthFund.repository.AssetRepository;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.AssetEntity;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.UserEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final AssetRepository assetRepository;
    private final WalletRepository walletRepository;
    private final UserService userService;
    private final PositionMapper positionMapper;
    private final TextValidator textValidator;
    private final CurrencyService currencyService;
    private final CashService cashService;
    private final AssetService assetService;
    private final WalletService walletService;

    public PositionService(PositionRepository positionRepository, AssetRepository assetRepository,
                           WalletRepository walletRepository, UserService userService,
                           PositionMapper positionMapper, TextValidator textValidator,
                           CurrencyService currencyService, CashService cashService,
                           AssetService assetService, WalletService walletService) {
        this.positionRepository = positionRepository;
        this.assetRepository = assetRepository;
        this.walletRepository = walletRepository;
        this.userService = userService;
        this.positionMapper = positionMapper;
        this.textValidator = textValidator;
        this.currencyService = currencyService;
        this.cashService = cashService;
        this.assetService = assetService;
        this.walletService = walletService;
    }

    public PositionDto openPosition(String userName, String walletName, String symbol,
                                    String positionCurrency, float openingPrice, float amount, float commission) {

        validateInputs(userName, walletName, openingPrice, amount, commission);
        positionCurrency = textValidator.checkAndAdjustCurrencyCode(positionCurrency);

        UserEntity userEntity = userService.getUserByName(userName);
        WalletEntity walletEntity = getWalletByName(userEntity, walletName);

        AssetEntity assetEntity = getAssetBySymbol(symbol);

        float finalPrice = convertToCurrency(openingPrice, positionCurrency, walletEntity.getCurrency());
        float openingCurrencyRate = finalPrice / openingPrice;

        float totalValue = calculateTotalValue(finalPrice, amount, commission);
        withdrawCash(walletEntity, totalValue, walletEntity.getCurrency());

        PositionEntity positionEntity = createPositionEntity(assetEntity, openingPrice, amount, commission, positionCurrency, openingCurrencyRate);
        savePosition(positionEntity);
        updateWalletPositions(walletEntity, positionEntity);
        saveWallet(walletEntity);

        return positionMapper.positionEntityToPositionDto(positionEntity);
    }

//    public boolean closePosition(String userName, String walletName, int positionId, float endingAssetPrice) {
//        UserEntity userEntity = userService.getUserByName(userName);
//        WalletEntity walletEntity = getWalletByName(userEntity, walletName);
//
//        PositionEntity positionEntity = getPositionById(positionId);
//
//        positionEntity.setOpen(false);
//        positionEntity.setEndingAssetPrice(endingAssetPrice);
//        positionEntity.setPositionEndingDate(LocalDateTime.now());
//        positionEntity.getEndingCurrencyRate();
//
//        return true;
//    }

    private void validateInputs(String userName, String walletName, float openingPrice, float amount, float commission) {
        textValidator.checkTextValidity(userName, walletName);
        textValidator.checkNumberValidity(openingPrice, amount, commission);
    }

    private WalletEntity getWalletByName(UserEntity userEntity, String walletName) {
        return walletService.getWalletByName(userEntity, walletName);
    }

    private AssetEntity getAssetBySymbol(String symbol) {
        return assetRepository.findBySymbol(symbol)
                .orElseThrow(() -> new WealthFundSingleException("Asset with symbol " + symbol + " not found"));
    }

    private PositionEntity getPositionById(int id) {
        return positionRepository.findById((long) id)
                .orElseThrow(()-> new WealthFundSingleException("Position with id " + id + " not found"));
    }

    private float convertToCurrency(float amount, String baseCurrency, String targetCurrency) {
        if (targetCurrency.equals(baseCurrency)) {
            return amount;
        } else {
            return currencyService.convertCurrency(baseCurrency, targetCurrency, amount);
        }
    }

    private float calculateTotalValue(float price, float amount, float commission) {
        return price * amount * commission;
    }

    private void withdrawCash(WalletEntity walletEntity, float amount, String currency) {
        walletEntity.setCashEntity(cashService.tryToWithdrawAndUpdateCash(walletEntity.getCashEntity(), amount, currency));
    }

    private PositionEntity createPositionEntity(AssetEntity assetEntity, float price, float amount,
                                                float commission, String positionCurrency, float openingCurrencyRate) {
        return PositionEntity.builder()
                .asset(assetEntity)
                .openingAssetPrice(price)
                .amount(amount)
                .positionOpeningDate(LocalDateTime.now())
                .commission(commission)
                .currency(positionCurrency)
                .openingCurrencyRate(openingCurrencyRate)
                .isOpen(true)
                .build();
    }

    private void savePosition(PositionEntity positionEntity) {
        positionRepository.save(positionEntity);
    }

    private void updateWalletPositions(WalletEntity walletEntity, PositionEntity positionEntity) {
        Set<PositionEntity> positionEntitySet = walletEntity.getPositions();
        positionEntitySet.add(positionEntity);
        walletEntity.setPositions(positionEntitySet);
    }

    private void saveWallet(WalletEntity walletEntity) {
        walletRepository.save(walletEntity);
    }
}
