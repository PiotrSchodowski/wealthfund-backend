//package com.example.wealthFund.service;
//
//import com.example.wealthFund.dto.PositionCloseDto;
//import com.example.wealthFund.dto.PositionDataDto;
//import com.example.wealthFund.dto.PositionOpenDto;
//import com.example.wealthFund.dto.PositionSubtractedDataDto;
//import com.example.wealthFund.exception.WealthFundSingleException;
//import com.example.wealthFund.mapper.PositionMapper;
//import com.example.wealthFund.repository.AssetRepository;
//import com.example.wealthFund.repository.PositionRepository;
//import com.example.wealthFund.repository.WalletRepository;
//import com.example.wealthFund.repository.entity.AssetEntity;
//import com.example.wealthFund.repository.entity.PositionEntity;
//import com.example.wealthFund.repository.entity.UserEntity;
//import com.example.wealthFund.repository.entity.WalletEntity;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Service
//public class PositionService {
//
//    private final PositionRepository positionRepository;
//    private final AssetRepository assetRepository;
//    private final WalletRepository walletRepository;
//    private final UserService userService;
//    private final PositionMapper positionMapper;
//    private final TextValidator textValidator;
//    private final CurrencyService currencyService;
//    private final CashService cashService;
//    private final CalculateService calculateService;
//    private final WalletService walletService;
//
//    public PositionService(PositionRepository positionRepository, AssetRepository assetRepository,
//                           WalletRepository walletRepository, UserService userService,
//                           PositionMapper positionMapper, TextValidator textValidator,
//                           CurrencyService currencyService, CashService cashService,
//                           WalletService walletService,CalculateService calculateService) {
//        this.positionRepository = positionRepository;
//        this.assetRepository = assetRepository;
//        this.walletRepository = walletRepository;
//        this.userService = userService;
//        this.positionMapper = positionMapper;
//        this.textValidator = textValidator;
//        this.currencyService = currencyService;
//        this.cashService = cashService;
//        this.walletService = walletService;
//        this.calculateService = calculateService;
//    }
//
//    public PositionOpenDto openPosition(String userName, String walletName, PositionOpenDto positionOpenDto) {
//        validateInputs(userName, walletName, positionOpenDto);
//
//        WalletEntity walletEntity = getWalletEntity(userName, walletName);
//        AssetEntity assetEntity = getAssetEntityBySymbol(positionOpenDto.getSymbol());
//
//        PositionDataDto positionDataDto = calculateService.calculatePositionData(positionOpenDto, walletEntity);
//        withdrawCash(walletEntity, positionDataDto.getTotalValue());
//        PositionEntity positionEntity = createPositionEntity(assetEntity, positionDataDto);
//
//        savePosition(positionEntity);
//        updateWalletPositions(walletEntity, positionEntity);
//        saveWallet(walletEntity);
//
//        return positionMapper.positionEntityToPositionOpenDto(positionEntity);
//    }
//
//    public PositionOpenDto increasePosition(String userName, String walletName, int positionId, PositionOpenDto positionOpenDto) {
//        validateInputs(userName, walletName, positionOpenDto);
//
//        WalletEntity walletEntity = getWalletEntity(userName, walletName);
//        PositionEntity positionEntity = getPositionById(positionId);
//
//        PositionDataDto positionDataDto = calculateService.calculatePositionData(positionOpenDto, walletEntity);
//        isTheSameCurrency(positionDataDto.getPositionOpenDto().getCurrency(), positionEntity.getCurrency());
//
//        withdrawCash(walletEntity, positionDataDto.getTotalValue());
//        increasePositionFeature(positionEntity, positionDataDto);
//
//        savePosition(positionEntity);
//        saveWallet(walletEntity);
//
//        return positionMapper.positionEntityToPositionOpenDto(positionEntity);
//    }
//
//    public PositionCloseDto closePosition(String userName, String walletName, PositionSubtractedDataDto positionSubtractedDataDto) {
//        textValidator.checkTextValidity(userName, walletName);
//
//        int positionId = positionSubtractedDataDto.getPositionId();
//        float endingAssetPrice = positionSubtractedDataDto.getEndingAssetPrice();
//        float quantityOfAsset = positionSubtractedDataDto.getQuantityOfAsset();
//
//        textValidator.checkNumberValidity(positionId, endingAssetPrice, quantityOfAsset);
//
//        WalletEntity walletEntity = getWalletEntity(userName, walletName);
//        PositionEntity positionEntity = getPositionById(positionId);
//
//        float assetPriceAfterConvert = calculateService.convertToCurrency(endingAssetPrice, positionEntity.getCurrency(), walletEntity.getCurrency());
//        float subtractedValue = assetPriceAfterConvert * quantityOfAsset;
//
//        if (quantityOfAsset < positionEntity.getAmount()) {
//            decreasePositionFeature(positionEntity, quantityOfAsset);
//        } else if (quantityOfAsset > positionEntity.getAmount()) {
//            throw new WealthFundSingleException("The quantity entered exceeds the value of the position");
//        } else {
//            float endingCurrencyRate = assetPriceAfterConvert / endingAssetPrice;
//            completeClosingOfThePosition(positionEntity, endingAssetPrice, endingCurrencyRate);
//        }
//
//        cashService.depositCash(walletEntity.getCashEntity(), subtractedValue);
//        savePosition(positionEntity);
//        saveWallet(walletEntity);
//
//        return positionMapper.positionEntityToPositionCloseDto(positionEntity);
//    }
//
//    private void increasePositionFeature(PositionEntity positionEntity, PositionDataDto positionDataDto) {
//        PositionOpenDto positionOpenDto = positionDataDto.getPositionOpenDto();
//        float totalValue = positionDataDto.getTotalValue();
//
//        float newTotalAmount = positionEntity.getAmount() + positionOpenDto.getAmount();
//        float newTotalValue = positionEntity.getTotalValueOfPosition() + totalValue;
//        float newAveragePrice = newTotalValue / newTotalAmount;
//
//        positionEntity.setAmount(newTotalAmount);
//        positionEntity.setTotalValueOfPosition(newTotalValue);
//        positionEntity.setOpeningAssetPrice(newAveragePrice);
//        positionEntity.setCommission(positionEntity.getCommission() + positionOpenDto.getCommission());
//    }
//
//    private void decreasePositionFeature(PositionEntity positionEntity, float quantityOfAsset) {
//        float newTotalAmount = positionEntity.getAmount() - quantityOfAsset;
//        float newTotalValue = positionEntity.getOpeningAssetPrice() * newTotalAmount;
//
//        positionEntity.setAmount(newTotalAmount);
//        positionEntity.setTotalValueOfPosition(newTotalValue);
//    }
//
//    private void completeClosingOfThePosition(PositionEntity positionEntity, float endingAssetPrice, float endingCurrencyRate) {
//        positionEntity.setOpen(false);
//        positionEntity.setAmount(0);
//        positionEntity.setEndingAssetPrice(endingAssetPrice); //todo wymyśleć sposób na obliczanie średniej sprzedaży, póki co jest ostatnia cena
//        positionEntity.setPositionEndingDate(LocalDateTime.now());
//        positionEntity.setEndingCurrencyRate(endingCurrencyRate);
//        positionEntity.setTotalValueOfPosition(0);
//    }
//
//    private PositionEntity createPositionEntity(AssetEntity assetEntity, PositionDataDto positionDataDto) {
//
//        PositionOpenDto positionOpenDto = positionDataDto.getPositionOpenDto();
//
//        return PositionEntity.builder()
//                .symbol(assetEntity.getSymbol())
//                .asset(assetEntity)
//                .openingAssetPrice(positionOpenDto.getOpeningAssetPrice() + positionOpenDto.getCommission())
//                .amount(positionOpenDto.getAmount())
//                .positionOpeningDate(LocalDateTime.now())
//                .commission(positionOpenDto.getCommission())
//                .isPercentageCommission(positionOpenDto.isPercentageCommission())
//                .currency(positionOpenDto.getCurrency())
//                .openingCurrencyRate(positionDataDto.getOpeningCurrencyRate())
//                .totalValueOfPosition(positionDataDto.getTotalValue())
//                .isOpen(true)
//                .build();
//    }
//
//    private void isTheSameCurrency(String basicCurrency, String targetCurrency) {
//        if (!basicCurrency.equals(targetCurrency)) {
//            throw new WealthFundSingleException("The entered currency is different from the required one");
//        }
//    }
//
//    private void updateWalletPositions(WalletEntity walletEntity, PositionEntity positionEntity) {
//        Set<PositionEntity> positionEntitySet = walletEntity.getPositions();
//        positionEntitySet.add(positionEntity);
//        walletEntity.setPositions(positionEntitySet);
//    }
//
//    private void validateInputs(String userName, String walletName, PositionOpenDto positionOpenDto) {
//        textValidator.checkTextValidity(userName, walletName);
//        textValidator.checkNumberValidity(positionOpenDto.getOpeningAssetPrice(), positionOpenDto.getAmount(), positionOpenDto.getCommission());
//    }
//
//    private WalletEntity getWalletEntity(String userName, String walletName) {
//        UserEntity userEntity = userService.getUserByName(userName);
//        return getWalletByName(userEntity, walletName);
//    }
//
//    private WalletEntity getWalletByName(UserEntity userEntity, String walletName) {
//        return walletService.getWalletByName(userEntity, walletName);
//    }
//
//    private AssetEntity getAssetEntityBySymbol(String symbol) {
//        return assetRepository.findBySymbol(symbol)
//                .orElseThrow(() -> new WealthFundSingleException("Asset with symbol " + symbol + " not found"));
//    }
//
//    private PositionEntity getPositionById(int id) {
//        return positionRepository.findById((long) id)
//                .orElseThrow(() -> new WealthFundSingleException("Position with id " + id + " not found"));
//    }
//
//    private void withdrawCash(WalletEntity walletEntity, float amount) {
//        walletEntity.setCashEntity(cashService.
//                tryToWithdrawAndUpdateCash(walletEntity.getCashEntity(), amount, walletEntity.getCurrency()));
//    }
//
//    private void savePosition(PositionEntity positionEntity) {
//        positionRepository.save(positionEntity);
//    }
//
//    private void saveWallet(WalletEntity walletEntity) {
//        walletRepository.save(walletEntity);
//    }
//}
