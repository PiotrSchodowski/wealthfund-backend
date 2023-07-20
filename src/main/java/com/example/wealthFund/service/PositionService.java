package com.example.wealthFund.service;

import com.example.wealthFund.dto.PositionDto;
import com.example.wealthFund.dto.PositionOpenDto;
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

    public PositionDto openPosition(String userName, String walletName, PositionOpenDto positionOpenDto) {

        String symbol = positionOpenDto.getSymbol();
        String positionCurrency = positionOpenDto.getCurrency();
        float openingPrice = positionOpenDto.getOpeningAssetPrice();
        float amount = positionOpenDto.getAmount();
        float valueOfCommission = positionOpenDto.getCommission();
        boolean isPercentageCommission = positionOpenDto.isPercentageCommission();

        validateInputs(userName, walletName, openingPrice, amount, valueOfCommission);
        positionCurrency = textValidator.checkAndAdjustCurrencyCode(positionCurrency);

        UserEntity userEntity = userService.getUserByName(userName);
        WalletEntity walletEntity = getWalletByName(userEntity, walletName);
        AssetEntity assetEntity = getAssetBySymbol(symbol);

        float finalPrice = convertToCurrency(openingPrice, positionCurrency, walletEntity.getCurrency()); // czy takie przekazanie jest ok
        float openingCurrencyRate = finalPrice / openingPrice;
        float valueWithoutCommission = finalPrice * amount;
        float totalValue = calculateTotalValue(valueOfCommission, isPercentageCommission, valueWithoutCommission);

        withdrawCash(walletEntity, totalValue);

        PositionEntity positionEntity = createPositionEntity(assetEntity, openingPrice, amount, valueOfCommission, positionCurrency, openingCurrencyRate, totalValue);
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
//        float finalPrice = convertToCurrency()
//        float endingCurrencyRate = ;
//
//        positionEntity.setOpen(false);
//        positionEntity.setEndingAssetPrice(endingAssetPrice);
//        positionEntity.setPositionEndingDate(LocalDateTime.now());
//        positionEntity.getEndingCurrencyRate();
//
//        return true;
//    }

    private float calculateTotalValue(float valueOfCommission, boolean isPercentageCommission, float valueWithoutCommission) {
        if (isPercentageCommission) {
            return valueWithoutCommission + ((valueOfCommission * valueWithoutCommission) / 100);
        } else {
            return valueWithoutCommission + valueOfCommission;
        }
    }

    private void validateInputs(String userName, String walletName, float openingPrice, float amount, float percentageCommission) {
        textValidator.checkTextValidity(userName, walletName);
        textValidator.checkNumberValidity(openingPrice, amount, percentageCommission);
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
                .orElseThrow(() -> new WealthFundSingleException("Position with id " + id + " not found"));
    }

    private float convertToCurrency(float amount, String baseCurrency, String targetCurrency) {
        if (targetCurrency.equals(baseCurrency)) {
            return amount;
        } else {
            return currencyService.convertCurrency(baseCurrency, targetCurrency, amount);
        }
    }

    private void withdrawCash(WalletEntity walletEntity, float amount) {
        walletEntity.setCashEntity(cashService.
                tryToWithdrawAndUpdateCash(walletEntity.getCashEntity(), amount, walletEntity.getCurrency())); // czy takie przekazanie jest ok
    }

    private PositionEntity createPositionEntity(AssetEntity assetEntity, float price, float amount,
                                                float commission, String positionCurrency, float openingCurrencyRate, float totalValue) {
        return PositionEntity.builder()
                .asset(assetEntity)
                .openingAssetPrice(price)
                .amount(amount)
                .positionOpeningDate(LocalDateTime.now())
                .commission(commission)
                .currency(positionCurrency)
                .openingCurrencyRate(openingCurrencyRate)
                .result(totalValue) //tymczasowo
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
