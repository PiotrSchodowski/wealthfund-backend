package com.example.wealthFund.it;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.it.controllers.AssetControllerMock;
import com.example.wealthFund.repository.AssetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureMockMvc
public class AssetIntegrationTests extends TestConfig {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AssetRepository assetRepository;

    @Autowired
    TestHelper testHelper;

    @Autowired
    AssetControllerMock assetControllerMock;


    @AfterEach
    public void clearDB() {
        assetRepository.deleteAll();
    }

    private AssetDto assetDto = AssetDto.builder()
            .symbol("WIG20")
            .name("WIG20 index ETF")
            .assetType("ETF")
            .currency("PLN")
            .build();


    @Test
    void scenarioAddGpwAssets() throws Exception {
        assetControllerMock.addGpwAssets();
    }

    @Test
    void scenarioAddUsaAssets() throws Exception {
        assetControllerMock.addUsaAssets();
    }

    @Test
    void scenarioAddCryptocurrencies() throws Exception {
        assetControllerMock.addCryptocurrencies();
    }

    @Test
    void scenarioGetAllAssets() throws Exception {
        assetControllerMock.addGpwAssets();
        assetControllerMock.addUsaAssets();
        assetControllerMock.addCryptocurrencies();
        assetControllerMock.getAllAssets();
        assertThat(assetRepository.count()).isGreaterThan(12000);
    }

    @Test
    void scenarioEntryManualAsset() throws Exception {
        assetControllerMock.entryManualAsset(assetDto);
        assertThat(assetRepository.count()).isEqualTo(1);
        assertThat(assetRepository.findAll().get(0).getSymbol()).isEqualTo("WIG20");
    }

    @Test
    void scenarioDeleteAssetBySymbol() throws Exception {
        assetControllerMock.entryManualAsset(assetDto);
        assetControllerMock.deleteAssetBySymbol("WIG20");
        assertThat(assetRepository.count()).isEqualTo(0);
    }

    @Test
    @WithMockUser
    void scenarioSavePriceToUsaAsset() throws Exception {
        assetControllerMock.addUsaAssets();
        assetControllerMock.savePriceToUsaAsset("AAPL");
        assertThat(assetRepository.findAll().get(0).getPrice()).isNotNull();
    }

    @Test
    void scenarioSavePriceOfAsset() throws Exception {
        assetControllerMock.addGpwAssets();
        assetControllerMock.savePriceOfAsset("PKN");
    }

    @Test
    void scenarioSavePriceOfAssetWrongSymbol() throws Exception {
        assetControllerMock.addGpwAssets();
        assetControllerMock.savePriceOfAssetNotAcceptable("PKNNN");
    }

}
