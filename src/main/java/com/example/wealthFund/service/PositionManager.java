package com.example.wealthFund.service;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Predicate;

@Service
public class PositionManager {

    private final PositionRepository positionRepository;
    private final WalletRepository walletRepository;
    private final TextValidator textValidator;
    private final CashService cashService;
    private final CalculatePositionService calculatePositionService;
    private final WalletService walletService;
    private final ActualizationService actualizationService;

    public PositionManager(PositionRepository positionRepository, WalletRepository walletRepository, TextValidator textValidator,
                           CashService cashService, CalculatePositionService calculatePositionService, WalletService walletService,
                           ActualizationService actualizationService) {
        this.positionRepository = positionRepository;
        this.walletRepository = walletRepository;
        this.textValidator = textValidator;
        this.cashService = cashService;
        this.calculatePositionService = calculatePositionService;
        this.walletService = walletService;
        this.actualizationService = actualizationService;
    }

    public AddPositionDto addPosition(String userName, String walletName, AddPositionDto addPositionDto) {
        addPositionDto = textValidator.validateAddPosition(userName, walletName, addPositionDto);
        addPositionDto.setTotalValueEntered(calculatePositionService.calculateTotalValueEntered(addPositionDto));

        WalletEntity walletEntity = walletService.getWalletEntity(userName, walletName);
        PositionEntity positionEntity = returnPositionEntity(walletEntity, addPositionDto);
        positionEntity = calculatePositionService.increasePositionData(positionEntity, addPositionDto);

        cashService.withdrawCash(walletEntity, addPositionDto.getTotalValueEntered());
        savePositionEntity(positionEntity, walletEntity);
        actualizationService.actualizeWalletData(walletEntity);
        return addPositionDto;
    }

    public SubtractPositionDto subtractPosition(String userName, String walletName, SubtractPositionDto subtractPositionDto) {
        subtractPositionDto = textValidator.validateSubtractPosition(userName, walletName, subtractPositionDto);
        subtractPositionDto.setTotalValueEntered(calculatePositionService.calculateTotalValueEntered(subtractPositionDto));

        WalletEntity walletEntity = walletService.getWalletEntity(userName, walletName);
        PositionEntity positionEntity = returnPositionEntity(walletEntity, subtractPositionDto);
        positionEntity = updateSubtractingPosition(positionEntity, subtractPositionDto);

        cashService.depositCash(walletEntity.getCashEntity(), subtractPositionDto.getTotalValueEntered());
        savePositionEntity(positionEntity, walletEntity);
        actualizationService.actualizeWalletData(walletEntity);
        return subtractPositionDto;
    }

    private PositionEntity updateSubtractingPosition(PositionEntity positionEntity, SubtractPositionDto subtractPositionDto) {
        if (subtractPositionDto.getQuantityOfAsset() > positionEntity.getQuantity()) {
            throw new WealthFundSingleException("The quantity entered exceeds the value of the position");
        } else {
            return calculatePositionService.decreasePositionData(positionEntity, subtractPositionDto);
        }
    }

    private PositionEntity returnPositionEntity(WalletEntity walletEntity, AddPositionDto addPositionDto) {
        return walletEntity.getPositions().stream()
                .filter(positionEntity -> isMatchingSymbolAndCurrency(positionEntity, addPositionDto.getSymbol(), addPositionDto.getCurrency()))
                .findFirst()
                .orElseGet(() -> openNewPositionEntity(addPositionDto, walletEntity));
    }

    private PositionEntity returnPositionEntity(WalletEntity walletEntity, SubtractPositionDto subtractPositionDto) {
        return walletEntity.getPositions().stream()
                .filter(positionEntity -> isMatchingSymbolAndCurrency(positionEntity, subtractPositionDto.getSymbol(), subtractPositionDto.getCurrency()))
                .findFirst()
                .orElseThrow(() -> new NotExistException(subtractPositionDto.getSymbol()));
    }

    private PositionEntity openNewPositionEntity(AddPositionDto addPositionDto, WalletEntity walletEntity) {

        return PositionEntity.builder()
                .symbol(addPositionDto.getSymbol())
                .basicCurrency(addPositionDto.getCurrency())
                .targetCurrency(walletEntity.getCurrency())
                .positionOpeningDate(LocalDateTime.now())
                .build();
    }

    @Transactional
    private void savePositionEntity(PositionEntity positionEntity, WalletEntity walletEntity) {
        Set<PositionEntity> positionEntities = walletEntity.getPositions();

        if (positionEntity.getQuantity() > 0) {
            PositionEntity existingPosition = findPositionBySymbolAndCurrency(positionEntities, positionEntity.getSymbol(), positionEntity.getBasicCurrency());

            if (existingPosition != null) {
                calculatePositionService.copyPositionFields(positionEntity, existingPosition);
            } else {
                positionEntities.add(positionEntity);
            }
        } else {
            Predicate<PositionEntity> matchingSymbolAndCurrency = pos ->
                    isMatchingSymbolAndCurrency(pos, positionEntity.getSymbol(), positionEntity.getBasicCurrency());

            positionEntities.removeIf(matchingSymbolAndCurrency);
            positionRepository.deleteById(positionEntity.getId());
        }

        walletEntity.setPositions(positionEntities);
        walletRepository.save(walletEntity);
    }

    private PositionEntity findPositionBySymbolAndCurrency(Set<PositionEntity> positionEntities, String symbol, String currency) {
        Predicate<PositionEntity> symbolAndCurrencyMatcher = positionEntity ->
                isMatchingSymbolAndCurrency(positionEntity, symbol, currency);

        return positionEntities.stream()
                .filter(symbolAndCurrencyMatcher)
                .findFirst()
                .orElse(null);
    }

    private boolean isMatchingSymbolAndCurrency(PositionEntity positionEntity, String symbol, String currency) {
        return positionEntity.getSymbol().equals(symbol) && positionEntity.getBasicCurrency().equals(currency);
    }
}
