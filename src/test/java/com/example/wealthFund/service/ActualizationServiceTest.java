package com.example.wealthFund.service;

import com.example.wealthFund.mapper.AssetMapper;
import com.example.wealthFund.repository.entity.AssetEntity;
import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ActualizationServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CalculateWalletService calculateWalletService;

    @Mock
    private AssetService assetService;

    @Mock
    private AssetMapper assetMapper;

    @Mock
    private CalculatePositionService calculatePositionService;

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private ActualizationService actualizationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldActualizeWalletData() {
        // Given
        WalletEntity walletEntity = new WalletEntity();
        Set<PositionEntity> positions = new HashSet<>();
        PositionEntity positionEntity = new PositionEntity();
        positions.add(positionEntity);
        walletEntity.setPositions(positions);

        when(calculateWalletService.calculateWalletBasicValue(walletEntity)).thenReturn(100.0F);
        when(calculateWalletService.calculateWalletActualValue(walletEntity)).thenReturn(150.0F);
        when(calculateWalletService.calculatePercentageOfPortfolio(any(), any())).thenReturn(10.0F);
        when(assetService.setPriceIfThereIsNone(any())).thenReturn(new AssetEntity());
        when(assetMapper.assetEntityToAssetDto(any())).thenReturn(new AssetDto());

        // When
        actualizationService.actualizeWalletData(walletEntity);

        // Then
        verify(calculateWalletService).calculateWalletBasicValue(walletEntity);
        verify(calculateWalletService).calculateWalletActualValue(walletEntity);
        verify(calculateWalletService, times(1)).calculatePercentageOfPortfolio(any(), any());
        verify(assetService, times(1)).setPriceIfThereIsNone(any());
        verify(assetMapper, times(1)).assetEntityToAssetDto(any());
        verify(positionRepository, times(1)).save(any());
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void shouldActualizePositionAssetPrice() {
        // Given
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setSymbol("AAPL");
        positionEntity.setExchange("NASDAQ");
        positionEntity.setBasicCurrency("USD");
        positionEntity.setWalletCurrency("EUR");

        when(assetService.setPriceIfThereIsNone(any())).thenReturn(new AssetEntity());
        when(assetMapper.assetEntityToAssetDto(any())).thenReturn(new AssetDto());

        // When
        actualizationService.actualizePositionAssetPrice(positionEntity);

        // Then
        verify(assetService, times(1)).setPriceIfThereIsNone(any());
        verify(assetMapper, times(1)).assetEntityToAssetDto(any());
        verify(positionRepository, times(1)).save(any());
    }
}

