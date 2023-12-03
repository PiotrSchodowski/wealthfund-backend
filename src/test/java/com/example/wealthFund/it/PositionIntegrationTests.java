package com.example.wealthFund.it;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import com.example.wealthFund.it.controllers.CashControllerMock;
import com.example.wealthFund.it.controllers.PositionControllerMock;
import com.example.wealthFund.it.controllers.WalletControllerMock;
import com.example.wealthFund.repository.PositionRepository;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.entity.PositionEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@AutoConfigureMockMvc
public class PositionIntegrationTests extends TestConfig {

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
    PositionControllerMock positionControllerMock;

    @BeforeAll
    public static void addExampleAssets(@Autowired DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("db-init.sql"));
        }
    }

    @BeforeEach
    public void setDB() throws Exception {
        testHelper.createUser();
        testHelper.createAdmin();
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.depositCashIntoTheWallet(testHelper.walletNameXtb, testHelper.valueOfDeposit1000);
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

        PositionEntity positionAdded = positionRepository.findBySymbol("ALE").get();

        assertThat(positionAdded.getSymbol()).isEqualTo(addPositionDto.getSymbol());
        assertThat(positionAdded.getName()).isEqualTo("Allegro");
        assertThat(positionAdded.getQuantity()).isEqualTo(addPositionDto.getQuantity());
        assertThat(positionAdded.getUserCurrency()).isEqualTo(addPositionDto.getCurrency());

        // 30.5 = (30*10+5)/10
        assertThat(positionAdded.getAveragePurchasePrice()).isEqualTo(30.5f);

        // 305 = 30*10+5
        assertThat(positionAdded.getValueBasedOnPurchasePrice())
                .isEqualTo(addPositionDto.getPrice() * addPositionDto.getQuantity() + addPositionDto.getCommission());

        // current market price ALLEGRO is greater than 25
        assertThat(positionAdded.getActualPrice()).isGreaterThan(25);
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

        assertThat(positionRepository.findBySymbol("ALE")).isEmpty();

    }

    @Test
    void scenarioSubtractPartOfPosition() throws Exception {
        positionControllerMock.addPosition(addPositionDto);
        subtractPositionDto.setQuantity(5);
        positionControllerMock.subtractPosition(subtractPositionDto);

        assertThat(positionRepository.findAll().get(0).getQuantity()).isEqualTo(5);
        assertThat(positionRepository.findAll().get(0).getValueBasedOnPurchasePrice()).isEqualTo(152.5f);
        assertThat(positionRepository.findAll().get(0).getAveragePurchasePrice()).isEqualTo(30.5f);
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
        PositionEntity positionAdded = positionRepository.findBySymbol("ALE").get();
        positionControllerMock.undoOperation(positionAdded.getId().intValue());

        assertThat(positionRepository.findBySymbol("ALE")).isEmpty();
    }

    @Test
    void scenarioGetPositionById() throws Exception {
        positionControllerMock.addPosition(addPositionDto);
        PositionEntity positionAdded = positionRepository.findBySymbol("ALE").get();
        positionControllerMock.getPositionById(positionAdded.getId().intValue());
        positionControllerMock.getPositionByIdAndExpectNotAcceptable(positionAdded.getId().intValue() + 1);

    }
}

