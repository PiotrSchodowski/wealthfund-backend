package com.example.wealthFund.it;

import com.example.wealthFund.WealthFundApplication;
import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import com.example.wealthFund.it.controllers.AssetControllerMock;
import com.example.wealthFund.it.controllers.CashControllerMock;
import com.example.wealthFund.it.controllers.PositionControllerMock;
import com.example.wealthFund.it.controllers.WalletControllerMock;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void setDB() throws Exception {
        testHelper.createUser();
        addWalletDepositCashAndDownloadAssets();
    }

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

    SubtractPositionDto subtractPositionDto = SubtractPositionDto.builder()
            .symbol("ALE")
            .quantity(10)
            .price(30)
            .currency("PLN")
            .endingCurrencyRate(1)
            .build();

    @Test
    void scenarioAddPosition() throws Exception {
        positionControllerMock.addPosition(addPositionDto);

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

    @Test
    void scenarioAddPositionWithWrongExchangeAndExpectNotAcceptable() throws Exception {
        addPositionDto.setExchange("NASDAQ");
        positionControllerMock.addPositionAndExpectNotAcceptable(addPositionDto);
    }

    @Test
    void scenarioAddPositionWhenNotEnoughCashAndExpectNotAcceptable() throws Exception {
        addPositionDto.setQuantity(1000);
        positionControllerMock.addPositionAndExpectNotAcceptable(addPositionDto);
    }

    @Test
    void scenarioAddPositionWhenQuantityIsNegativeAndExpectNotAcceptable() throws Exception {
        addPositionDto.setQuantity(-1);
        positionControllerMock.addPositionAndExpectNotAcceptable(addPositionDto);
    }

    @Test
    void scenarioAddPositionWhenPriceIsNegativeAndExpectNotAcceptable() throws Exception {
        addPositionDto.setPrice(-1);
        positionControllerMock.addPositionAndExpectNotAcceptable(addPositionDto);
    }

    @Test
    void scenarioAddPositionWhenOpeningCurrencyRateIsNegativeAndExpectNotAcceptable() throws Exception {
        addPositionDto.setOpeningCurrencyRate(-1);
        positionControllerMock.addPositionAndExpectNotAcceptable(addPositionDto);
    }

    @Test
    void scenarioAddPositionWhenCommissionIsNegativeAndExpectNotAcceptable() throws Exception {
        addPositionDto.setCommission(-1);
        positionControllerMock.addPositionAndExpectNotAcceptable(addPositionDto);
    }

    @Test
    void scenarioAddPositionWhenQuantityIsZeroAndExpectNotAcceptable() throws Exception {
        addPositionDto.setQuantity(0);
        positionControllerMock.addPositionAndExpectNotAcceptable(addPositionDto);
    }

    @Test
    void scenarioAddPositionWhenPriceIsZeroAndExpectNotAcceptable() throws Exception {
        addPositionDto.setPrice(0);
        positionControllerMock.addPositionAndExpectNotAcceptable(addPositionDto);
    }

    @Test
    void scenarioAddPositionWhenOpeningCurrencyRateIsZeroAndExpectNotAcceptable() throws Exception {
        addPositionDto.setOpeningCurrencyRate(0);
        positionControllerMock.addPositionAndExpectNotAcceptable(addPositionDto);
    }

    @Test
    void scenarioSubtractWholePosition() throws Exception {
        positionControllerMock.addPosition(addPositionDto);
        positionControllerMock.subtractPosition(subtractPositionDto);

        assertThat(positionRepository.findById(1L).isEmpty());
    }

    @Test
    void scenarioSubtractPartOfPosition() throws Exception {
        positionControllerMock.addPosition(addPositionDto);
        subtractPositionDto.setQuantity(5);
        positionControllerMock.subtractPosition(subtractPositionDto);

        assertThat(positionRepository.findById(1L).get().getQuantity()).isEqualTo(5);
        assertThat(positionRepository.findById(1L).get().getValueBasedOnPurchasePrice()).isEqualTo(152.5f);
        assertThat(positionRepository.findById(1L).get().getAveragePurchasePrice()).isEqualTo(30.5f);
    }

    @Test
    void scenarioSubtractPositionWhenQuantityIsTooBigAndExpectNotAcceptable() throws Exception {
        positionControllerMock.addPosition(addPositionDto);
        subtractPositionDto.setQuantity(11);
        positionControllerMock.subtractPositionAndExpectNotAcceptable(subtractPositionDto);
    }

    @Test
    void scenarioUndoAddPositionToZero() throws Exception {
        positionControllerMock.addPosition(addPositionDto);
        positionControllerMock.undoOperation(1);

        assertThat(positionRepository.findById(1L).isEmpty());
    }

    @Test
    void scenarioUndoAddPositionToPartOfPosition() throws Exception {
        positionControllerMock.addPosition(addPositionDto);
        positionControllerMock.addPosition(addPositionDto);
        positionControllerMock.undoOperation(2);


        assertThat(positionRepository.findById(1L).get().getQuantity()).isEqualTo(10);
        assertThat(positionRepository.findById(1L).get().getValueBasedOnPurchasePrice()).isEqualTo(305f);
        assertThat(positionRepository.findById(1L).get().getAveragePurchasePrice()).isEqualTo(30.5f);
    }

    @Test
    void scenarioGetPositionById() throws Exception {
        positionControllerMock.addPosition(addPositionDto);
        positionControllerMock.getPositionById(1);
        positionControllerMock.getPositionByIdAndExpectNotAcceptable(2);

    }


    private void addWalletDepositCashAndDownloadAssets() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.depositCashIntoTheWallet(testHelper.walletNameXtb, testHelper.valueOfDeposit1000);
        assetControllerMock.addGpwAssets();
    }

}

