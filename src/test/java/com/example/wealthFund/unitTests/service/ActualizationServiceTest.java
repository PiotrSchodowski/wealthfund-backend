package com.example.wealthFund.unitTests.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.mapper.AssetMapper;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.WalletRepository;
import com.example.wealthFund.repository.entity.PositionEntity;
import com.example.wealthFund.repository.entity.WalletEntity;
import com.example.wealthFund.service.ActualizationService;
import com.example.wealthFund.service.AssetService;
import com.example.wealthFund.service.CalculatePositionService;
import com.example.wealthFund.service.CalculateWalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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
    void shouldActualizeThreeParametersOfWallet() {

        WalletEntity walletEntity = new WalletEntity();
        Set<PositionEntity> positions = new HashSet<>();
        PositionEntity positionEntity = new PositionEntity();

        positions.add(positionEntity);
        walletEntity.setPositions(positions);


        when(calculateWalletService.calculatePercentageOfPortfolio(any(), any())).thenReturn(20.0F);
        when(assetMapper.assetEntityToAssetDto(any())).thenReturn(new AssetDto());

        actualizationService.actualizeWalletData(walletEntity);
        Set<PositionEntity> positionsAfterActualization = walletEntity.getPositions();

        Assertions.assertEquals(20, positionsAfterActualization.stream().findFirst().get().getPercentageOfThePortfolio());
        Assertions.assertEquals(100, walletEntity.getBasicValue());
        Assertions.assertEquals(150, walletEntity.getActualValue());

    }

    @Test
    void shouldActualizePositionAssetPrice() {

        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setSymbol("PKN");
        positionEntity.setExchange("GPW");
        positionEntity.setBasicCurrency("PLN");
        positionEntity.setWalletCurrency("PLN");
        positionEntity.setActualPrice(60);

        AssetDto assetDto = new AssetDto();
        assetDto.setPrice(65);

//        when(assetMapper.assetEntityToAssetDto(any())).thenReturn(assetDto);
//        when(calculatePositionService.convertToCurrency(assetDto.getPrice(), "PLN", "PLN")).thenReturn(65.0f);
//
//        actualizationService.actualizePositionData(positionEntity);
//        Assertions.assertEquals(65.0f, positionEntity.getActualPrice());
    }
}

