package com.example.wealthFund.unitTests.service;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import com.example.wealthFund.repository.entity.AssetEntity;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.service.AssetService;
import com.example.wealthFund.service.CalculatePositionService;
import com.example.wealthFund.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CalculatePositionServiceTest {

    @Mock
    private CurrencyService currencyService;

    @Mock
    private AssetService assetService;

    private CalculatePositionService calculatePositionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        calculatePositionService = new CalculatePositionService(currencyService, assetService);
    }

    @Test
    void shouldCalculateTotalValueEntered() {
        AddPositionDto addPositionDto = new AddPositionDto();
        addPositionDto.setPrice(10);
        addPositionDto.setQuantity(5);
        addPositionDto.setCommission(10);
        addPositionDto.setPercentageCommission(false);
        addPositionDto.setOpeningCurrencyRate(1.0f);

        when(currencyService.convertCurrency(anyString(), anyString(), anyFloat())).thenReturn(10.0f);
        when(assetService.getAssetEntityBySymbolAndExchange(anyString(), anyString())).thenReturn(new AssetEntity());

        float result = calculatePositionService.calculateTotalValueEntered(addPositionDto);

        assertEquals(60f, result);
    }

    @Test
    public void testDecreasePositionData() {
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setQuantity(10);
        SubtractPositionDto subtractPositionDto = new SubtractPositionDto();
        subtractPositionDto.setQuantity(4);
        subtractPositionDto.setPrice(15);
        subtractPositionDto.setEndingCurrencyRate(1);

        PositionEntity result = calculatePositionService.decreasePositionData(positionEntity, subtractPositionDto);

        assertEquals(6, result.getQuantity());

    }

    @Test
    public void shouldIncreasePositionData() {

        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setQuantity(10);
        positionEntity.setWalletCurrency("currency");
        AddPositionDto addPositionDto = new AddPositionDto();
        addPositionDto.setQuantity(3);
        addPositionDto.setTotalValueEntered(50);

        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setPrice(10);

        when(assetService.getAssetEntityBySymbolAndExchange(anyString(), anyString())).thenReturn(new AssetEntity());
        when(assetService.setAssetPrice(any())).thenReturn(assetEntity);

        PositionEntity result = calculatePositionService.increasePositionData(positionEntity, addPositionDto);

        assertEquals(13, result.getQuantity());
    }

}

