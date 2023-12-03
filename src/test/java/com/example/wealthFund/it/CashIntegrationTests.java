package com.example.wealthFund.it;

import com.example.wealthFund.it.controllers.CashControllerMock;
import com.example.wealthFund.it.controllers.WalletControllerMock;
import com.example.wealthFund.repository.CashRepository;
import com.example.wealthFund.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@AutoConfigureMockMvc
public class CashIntegrationTests extends TestConfig{

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

    @BeforeEach
    public void setDB() {
        testHelper.createUser();
    }

    @AfterEach
    public void clearDB() {
        userRepository.deleteAll();
    }

    float valueCannotWithdraw = 1001;


    @Test
    @WithMockUser
    void scenarioCashDeposit() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.depositCashIntoTheWallet(testHelper.walletNameXtb, testHelper.valueOfDeposit1000);
        assertThat(cashRepository.findAll().get(0).getValue()).isEqualTo(testHelper.valueOfDeposit1000);
    }

    @Test
    @WithMockUser
    void scenarioCashWithdraw() throws Exception {
        walletControllerMock.addNewWallet(testHelper.walletNameXtb);
        cashControllerMock.depositCashIntoTheWallet(testHelper.walletNameXtb, testHelper.valueOfDeposit1000);
        cashControllerMock.withdrawCashFromTheWallet(testHelper.valueOfDeposit1000);
        assertThat(cashRepository.findAll().get(0).getValue()).isEqualTo(0);
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
