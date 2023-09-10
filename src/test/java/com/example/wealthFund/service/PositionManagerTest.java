package com.example.wealthFund.service;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PositionManagerTest {

    @InjectMocks
    PositionManager positionManager;

    @Mock
    WalletService walletService;

    @Mock
    TextValidator textValidator;

    @Mock
    CalculatePositionService calculatePositionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAddPositionDtoWhenAddingPosition() {  //todo mam problem z tym testem chodzi o : findPositionBySymbolAndCurrency and isMatchingSymbolAndCurrency , nie bardzo wiem jak to testowac

        AddPositionDto addPositionDto = new AddPositionDto();
        addPositionDto.setSymbol("BTC");
        addPositionDto.setExchange("none");
        addPositionDto.setQuantity(1);
        addPositionDto.setPrice(10);
        addPositionDto.setCommission(0);
        addPositionDto.setPercentageCommission(true);
        addPositionDto.setCurrency("PLN");
        addPositionDto.setTimeOfOpening(LocalDateTime.now());
        addPositionDto.setOpeningCurrencyRate(1);

        String userName = "userName";
        String walletName = "walletName";

        WalletEntity walletEntity = new WalletEntity();
        PositionEntity positionEntity = new PositionEntity();

        Set<PositionEntity> positions = new HashSet<>();
        positions.add(positionEntity);
        positionEntity.setSymbol("BTC");
        walletEntity.setPositions(positions);

        when(textValidator.validateAddPosition(userName, walletName, addPositionDto)).thenReturn(addPositionDto);
        when(calculatePositionService.calculateTotalValueEntered(addPositionDto)).thenReturn(10.0f);
        when(walletService.getWalletEntity(userName, walletName)).thenReturn(walletEntity).thenReturn(walletEntity);
        when(positionManager.returnPositionEntity(walletEntity,addPositionDto)).thenReturn((positionEntity));
        when(walletEntity.getPositions()).thenReturn(positions);
        when(calculatePositionService.increasePositionData(positionEntity, addPositionDto)).thenReturn(positionEntity);



        AddPositionDto result = positionManager.addPosition(userName, walletName, addPositionDto);

        assertEquals(addPositionDto, result);

    }
}
