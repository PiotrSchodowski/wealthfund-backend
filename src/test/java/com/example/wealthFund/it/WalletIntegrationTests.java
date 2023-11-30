package com.example.wealthFund.it;

import com.example.wealthFund.WealthFundApplication;
import com.example.wealthFund.it.controllers.WalletControllerMock;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.WalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(classes = {WealthFundApplication.class, TestConfig.class})
public class WalletIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TestHelper testHelper;

    @Autowired
    WalletControllerMock walletControllerMock;

    @BeforeEach
    public void setDB() {
        testHelper.createUser();
    }


    @AfterEach
    public void clearDB() {
        userRepository.deleteAll();
    }


    @Test
    void scenarioCorrectWalletBuilding() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        assertThat(walletRepository.existsByWalletNameAndUserName(testHelper.walletNameXtb, testHelper.userNamePiotr)).isTrue();
        assertThat(walletRepository.existsByWalletNameAndUserName("xtbb", testHelper.userNamePiotr)).isFalse();
        walletControllerMock.addNewWalletWhenSecondTimeTheSameData();  // check if the same wallet cannot be added twice
    }

    @ParameterizedTest
    @MethodSource("invalidWalletNames")
    void scenarioNotCorrectlyWalletBuilding(String walletName) throws Exception {
        walletControllerMock.addNewWalletAndExpectNotAcceptable(walletName);
    }

    @Test
    void scenarioCorrectWalletDelete() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        walletControllerMock.deleteWalletAndExpectOk();
        assertThat(walletRepository.existsByWalletNameAndUserName(testHelper.walletNameXtb, testHelper.userNamePiotr)).isFalse();
    }

    @Test
    void scenarioGetWallets() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        walletControllerMock.addNewWallet("ing");
        walletControllerMock.addNewWallet("ledger");
        assertThat(walletControllerMock.getWallets()).contains(testHelper.walletNameXtb, "ing", "ledger");
    }

    @Test
    void scenarioGetWallet() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        walletControllerMock.addNewWallet("ing");
        walletControllerMock.addNewWallet("ledger");
        assertThat(walletControllerMock.getWallet()).contains(testHelper.walletNameXtb);
    }

    private static Stream<String> invalidWalletNames() {
        return Stream.of("xt", "x t b", "x.,tb", "x", ".1", " xtb", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
    }
}
