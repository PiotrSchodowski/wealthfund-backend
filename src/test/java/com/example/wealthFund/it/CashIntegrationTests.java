package com.example.wealthFund.it;

import com.example.wealthFund.WealthFundApplication;
import com.example.wealthFund.repository.CashRepository;
import com.example.wealthFund.repository.UserRepository;
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
public class CashIntegrationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CashRepository cashRepository;

    @Autowired
    TestHelper testHelper;

    @Autowired
    WalletControllerMock walletControllerMock;

    @Autowired
    CashControllerMock cashControllerMock;

    @AfterEach
    public void clearDB() {
        userRepository.deleteAll();
    }

    float valueCannotWithdraw = 1001;

    @Test
    void scenarioCashDeposit() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.depositCashIntoTheWallet(testHelper.walletNameXtb, testHelper.valueOfDeposit1000);
        assertThat(cashRepository.findById(1L).get().getValue()).isEqualTo(testHelper.valueOfDeposit1000);
    }

    @Test
    void scenarioCashWithdraw() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.depositCashIntoTheWallet(testHelper.walletNameXtb, testHelper.valueOfDeposit1000);
        cashControllerMock.withdrawCashFromTheWallet(testHelper.valueOfDeposit1000);
        assertThat(cashRepository.findById(1L).get().getValue()).isEqualTo(0);
    }

    @Test
    void scenarioCashCannotWithdraw() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.depositCashIntoTheWallet(testHelper.walletNameXtb, testHelper.valueOfDeposit1000);
        cashControllerMock.withdrawCashFromTheWalletAndExpectNotAcceptable(valueCannotWithdraw);
    }

    @Test
    void scenarioCashCannotWithdrawWhenWalletIsEmpty() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.withdrawCashFromTheWalletAndExpectNotAcceptable(valueCannotWithdraw);
    }

    @Test
    void scenarioCashCannotDepositWhenValueIsNegative() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.depositCashIntoTheWalletAndExpectNotAcceptable(testHelper.valueOfDepositNegative);
    }
}
