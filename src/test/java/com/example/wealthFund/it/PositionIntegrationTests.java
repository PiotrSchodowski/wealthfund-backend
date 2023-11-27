package com.example.wealthFund.it;

import com.example.wealthFund.WealthFundApplication;
import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(classes = {WealthFundApplication.class, TestConfig.class})
public class PositionIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    TestHelper testHelper;

    @Autowired
    WalletControllerMock walletControllerMock;

    @Autowired
    CashControllerMock cashControllerMock;

    @Autowired
    AssetControllerMock assetControllerMock;

    @Autowired
    PositionControllerMock positionControllerMock;

    @AfterEach
    public void clearDB() {
        userRepository.deleteAll();
    }

    AddPositionDto addPositionDto = AddPositionDto.builder()
            .symbol("ALE")
            .exchange("GPW")
            .quantity(10)
            .price(30)
            .currency("PLN")
            .openingCurrencyRate(1)
            .commission(5)
            .isPercentageCommission(false)
            .build();

    @Test
    void scenarioAddPosition() throws Exception {

        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.depositCashIntoTheWallet(testHelper.walletNameXtb, testHelper.valueOfDeposit1000);
        assetControllerMock.addGpwAssets();
        positionControllerMock.addPosition(testHelper.walletNameXtb, addPositionDto);

        assertThat(positionRepository.findById(1L).get().getSymbol()).isEqualTo(addPositionDto.getSymbol());
        assertThat(positionRepository.findById(1L).get().getName()).isEqualTo("Allegro");
        assertThat(positionRepository.findById(1L).get().getQuantity()).isEqualTo(addPositionDto.getQuantity());
        assertThat(positionRepository.findById(1L).get().getUserCurrency()).isEqualTo(addPositionDto.getCurrency());

        // 30.5 = (30*10+5)/10
        assertThat(positionRepository.findById(1L).get().getAveragePurchasePrice()).isEqualTo(30.5f);

        // 305 = 30*10+5
        assertThat(positionRepository.findById(1L).get().getValueBasedOnPurchasePrice())
                .isEqualTo(addPositionDto.getPrice() * addPositionDto.getQuantity() + addPositionDto.getCommission());

        // current market price ALLEGRO is greater than 25
        assertThat(positionRepository.findById(1L).get().getActualPrice()).isGreaterThan(25);
    }
}

