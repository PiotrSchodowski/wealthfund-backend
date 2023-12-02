package com.example.wealthFund.service;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import com.example.wealthFund.dto.positionDtos.UndoPositionDto;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.mapper.UndoPositionMapper;
import com.example.wealthFund.repository.OperationHistoryRepo;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.OperationHistory;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class PositionManager {

    private final PositionRepository positionRepository;
    private final WalletRepository walletRepository;
    private final TextValidator textValidator;
    private final CashService cashService;
    private final CalculatePositionService calculatePositionService;
    private final WalletService walletService;
    private final ActualizationService actualizationService;
    private final OperationHistoryRepo operationHistoryRepo;
    private final UndoPositionMapper undoPositionMapper;


    public AddPositionDto addPosition(String userName, String walletName, AddPositionDto addPositionDto) {
        addPositionDto = textValidator.validateAddPosition(userName, walletName, addPositionDto);
        addPositionDto.setTotalValueEntered(calculatePositionService.calculateTotalValueEntered(addPositionDto));

        WalletEntity walletEntity = walletService.getWalletEntity(userName, walletName);
        PositionEntity positionEntity = returnPositionEntity(walletEntity, addPositionDto);
        positionEntity = calculatePositionService.increasePositionData(positionEntity, addPositionDto);

        cashService.withdrawCash(walletEntity, addPositionDto.getTotalValueEntered());
        OperationHistory operationHistory = saveOperationHistory(positionEntity, addPositionDto);
        addOperationToWallet(walletEntity, operationHistory);

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
        OperationHistory operationHistory = saveOperationHistory(positionEntity, subtractPositionDto);
        addOperationToWallet(walletEntity, operationHistory);

        savePositionEntity(positionEntity, walletEntity);
        actualizationService.actualizeWalletData(walletEntity);
        return subtractPositionDto;
    }


    public UndoPositionDto undoOperation(String userName, String walletName, Long id) {

        WalletEntity walletEntity = walletService.getWalletEntity(userName, walletName);
        UndoPositionDto undoPositionDto = deleteOperationHistory(walletEntity, id);
        PositionEntity positionEntity = returnPositionEntity(walletEntity, undoPositionDto);

        if (undoPositionDto.getQuantity() < 0) {
            AddPositionDto addPositionDto = undoPositionMapper.UndoPositionDtoToAddPositionDto(undoPositionDto);
            addPositionDto.setQuantity(undoPositionDto.getQuantity() * (-1));
            addPositionDto.setTotalValueEntered(undoPositionDto.getValueOperation() * (-1));

            calculatePositionService.increasePositionData(positionEntity, addPositionDto);
            cashService.withdrawCash(walletEntity, addPositionDto.getTotalValueEntered());
        } else {
            SubtractPositionDto subtractPositionDto = undoPositionMapper.UndoPositionDtoToSubtractPositionDto(undoPositionDto);
            positionEntity.setAveragePurchasePrice(undoPositionDto.getAveragePrice());
            calculatePositionService.decreasePositionData(positionEntity, subtractPositionDto);
            cashService.depositCash(walletEntity.getCashEntity(), undoPositionDto.getValueOperation());
        }
        savePositionEntity(positionEntity, walletEntity);
        actualizationService.actualizeWalletData(walletEntity);

        return undoPositionDto;
    }


    private UndoPositionDto deleteOperationHistory(WalletEntity walletEntity, Long id) {

        List<OperationHistory> operationHistories = walletEntity.getOperationHistories();
        Optional<OperationHistory> operationHistoryToDelete = operationHistories.stream()
                .filter(history -> history.getId().equals(id))
                .findFirst();
        OperationHistory historyToDelete = operationHistoryToDelete.orElseThrow(() ->
                new NotExistException(id + " id"));
        UndoPositionDto undoPositionDto = undoPositionMapper.operationHistoryToUndoPositionDto(historyToDelete);
        operationHistories.remove(historyToDelete);

        undoPositionDto.setAveragePrice((float) operationHistories.stream()
                .filter(history -> history.getValueOperation() > 0)
                .mapToDouble(history -> history.getPrice())
                .average()
                .orElse(0.0));
        walletEntity.setOperationHistories(operationHistories);

        operationHistoryRepo.deleteById(id);
        walletRepository.save(walletEntity);

        return undoPositionDto;
    }

    public PositionEntity returnPositionEntity(WalletEntity walletEntity, AddPositionDto addPositionDto) {
        return walletEntity.getPositions().stream()
                .filter(isMatchingSymbolAndCurrency(addPositionDto.getSymbol(), addPositionDto.getCurrency()))
                .findFirst()
                .orElseGet(() -> openNewPositionEntity(addPositionDto, walletEntity));
    }

    public ResponseEntity<?> getPosition(int id) {
        return ResponseEntity.ok(positionRepository.findById((long) id).orElseThrow(()-> new NotExistException("id")));
    }


    PositionEntity updateSubtractingPosition(PositionEntity positionEntity, SubtractPositionDto subtractPositionDto) {
        if (subtractPositionDto.getQuantity() > positionEntity.getQuantity()) {
            throw new WealthFundSingleException("The quantity entered exceeds the value of the position");
        } else {
            return calculatePositionService.decreasePositionData(positionEntity, subtractPositionDto);
        }
    }


    PositionEntity returnPositionEntity(WalletEntity walletEntity, UndoPositionDto undoPositionDto) {
        return walletEntity.getPositions().stream()
                .filter(isMatchingSymbolAndCurrency(undoPositionDto.getSymbol(), undoPositionDto.getPositionCurrency()))
                .findFirst()
                .orElseThrow(() -> new NotExistException(undoPositionDto.getSymbol()));
    }


    private PositionEntity returnPositionEntity(WalletEntity walletEntity, SubtractPositionDto subtractPositionDto) {
        return walletEntity.getPositions().stream()
                .filter(isMatchingSymbolAndCurrency(subtractPositionDto.getSymbol(), subtractPositionDto.getCurrency()))
                .findFirst()
                .orElseThrow(() -> new NotExistException(subtractPositionDto.getSymbol()));
    }


    private PositionEntity openNewPositionEntity(AddPositionDto addPositionDto, WalletEntity walletEntity) {

        return PositionEntity.builder()
                .symbol(addPositionDto.getSymbol())
                .userCurrency(addPositionDto.getCurrency())
                .walletCurrency(walletEntity.getCurrency())
                .positionOpeningDate(LocalDateTime.now())
                .build();
    }


    private OperationHistory saveOperationHistory(PositionEntity positionEntity, AddPositionDto addPositionDto) {

        return OperationHistory.builder()
                .symbol(positionEntity.getSymbol())
                .price(addPositionDto.getTotalValueEntered() / addPositionDto.getQuantity())
                .quantity(addPositionDto.getQuantity())
                .walletCurrency(positionEntity.getWalletCurrency())
                .positionCurrency(positionEntity.getUserCurrency())
                .valueOperation(addPositionDto.getTotalValueEntered())
                .date(LocalDateTime.now())
                .exchange(positionEntity.getExchange())
                .build();
    }


    private OperationHistory saveOperationHistory(PositionEntity positionEntity, SubtractPositionDto subtractPositionDto) {

        return OperationHistory.builder()
                .symbol(positionEntity.getSymbol())
                .price(subtractPositionDto.getPrice())
                .quantity(subtractPositionDto.getQuantity() * -1)
                .walletCurrency(positionEntity.getWalletCurrency())
                .positionCurrency(positionEntity.getUserCurrency())
                .valueOperation(subtractPositionDto.getTotalValueEntered() * -1)
                .date(LocalDateTime.now())
                .exchange(positionEntity.getExchange())
                .build();
    }


    private void addOperationToWallet(WalletEntity walletEntity, OperationHistory operationHistory) {
        List<OperationHistory> operationHistories = walletEntity.getOperationHistories();
        operationHistory.setWalletName(walletEntity.getName());
        operationHistories.add(operationHistory);
        walletEntity.setOperationHistories(operationHistories);
        walletRepository.save(walletEntity);
    }


    @Transactional
    private void savePositionEntity(PositionEntity positionEntity, WalletEntity walletEntity) {

        Set<PositionEntity> positionEntities = walletEntity.getPositions();
        if (positionEntity.getQuantity() > 0) {
            PositionEntity existingPosition = findPositionBySymbolAndCurrency(positionEntities, positionEntity.getSymbol(), positionEntity.getUserCurrency());

            if (existingPosition != null) {
                calculatePositionService.copyPositionFields(positionEntity, existingPosition);
            } else {
                positionEntities.add(positionEntity);
            }
        } else {
            Predicate<PositionEntity> matchingSymbolAndCurrency = isMatchingSymbolAndCurrency(positionEntity.getSymbol(), positionEntity.getUserCurrency());
            positionEntities.removeIf(matchingSymbolAndCurrency);
            positionRepository.deleteById(positionEntity.getId());
        }
        walletEntity.setPositions(positionEntities);
        walletRepository.save(walletEntity);
    }


    private PositionEntity findPositionBySymbolAndCurrency(Set<PositionEntity> positionEntities, String symbol, String currency) {

        return positionEntities.stream()
                .filter(isMatchingSymbolAndCurrency(symbol, currency))
                .findFirst()
                .orElse(null);
    }


    Predicate<PositionEntity> isMatchingSymbolAndCurrency(String symbol, String currency) {
        return pos -> pos.getSymbol().equals(symbol) && pos.getUserCurrency().equals(currency);
    }
}
