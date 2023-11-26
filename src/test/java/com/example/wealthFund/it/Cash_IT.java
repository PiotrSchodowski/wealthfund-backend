package com.example.wealthFund.it;

import com.example.wealthFund.WealthFundApplication;
import com.example.wealthFund.exception.InsufficientFundsException;
import com.example.wealthFund.repository.CashRepository;
import com.example.wealthFund.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(classes = {WealthFundApplication.class, TestConfig.class})
public class Cash_IT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CashRepository cashRepository;

    @Autowired
    TestHelper testHelper;

    @AfterEach
    public void clearDB() {
        userRepository.deleteAll();
    }


    String walletName = "xtb";
    float valueOfDeposit = 1000;
    float valueCanWithdraw = 1000;
    float valueCannotWithdraw = 1001;

    @Test
    void scenarioCashDeposit() throws Exception {

        testHelper.addNewWallet(walletName);
        testHelper.depositCashIntoTheWallet(walletName, valueOfDeposit);

        assertThat(cashRepository.findById(1L).get().getValue()).isEqualTo(valueOfDeposit);
    }


    @Test
    void scenarioCashWithdraw() throws Exception {

        testHelper.addNewWallet(walletName);
        testHelper.depositCashIntoTheWallet(walletName, valueOfDeposit);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/Piotr/wallet/"
                                + walletName
                                + "/cashWithdraw/"
                                + valueCanWithdraw)
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(cashRepository.findById(1L).get().getValue()).isEqualTo(0);
    }


    @Test
    void scenarioCashCannotWithdraw() throws Exception {

        testHelper.addNewWallet(walletName);
        testHelper.depositCashIntoTheWallet(walletName, valueOfDeposit);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/Piotr/wallet/"
                                + walletName
                                + "/cashWithdraw/"
                                + valueCannotWithdraw)
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> {
                    assertThat(result.getResolvedException()).isInstanceOf(InsufficientFundsException.class);
                });
    }
}
