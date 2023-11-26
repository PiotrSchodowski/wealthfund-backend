package com.example.wealthFund.it;

import com.example.wealthFund.WealthFundApplication;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.WalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Objects;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(classes = {WealthFundApplication.class, TestConfig.class})
public class Wallet_IT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TestHelper testHelper;

    String walletName = "xtb";

    @AfterEach
    public void clearDB() {
        userRepository.deleteAll();
    }


    @Test
    void scenarioCorrectWalletBuilding() throws Exception {

        testHelper.addNewWallet(walletName);

        assertThat(walletRepository.existsByWalletNameAndUserName(walletName, "Piotr")).isTrue();
        assertThat(walletRepository.existsByWalletNameAndUserName("xtbb", "Piotr")).isFalse();

        // should throw exception when wallet name is not unique
        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallets/" + walletName + "/PLN")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> {
                    assertThat(result.getResolvedException()).isInstanceOf(WealthFundSingleException.class);
                    assertThat(Objects.requireNonNull(result.getResolvedException()).getMessage())
                            .isEqualTo("This name of wallet already exists");
                });
    }


    @Test
    void scenarioNotCorrectlyWalletBuilding() throws Exception {

        String[] walletNames = {"xt", "x t b", "x.,tb", "x", ".1", " xtb", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"};

        for (String walletName : walletNames) {
            // should throw exception when wallet names is not correct
            mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallets/" + walletName + "/PLN")
                            .header("Authorization", "Bearer " + testHelper.getToken())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotAcceptable());
        }
    }


    @Test
    void scenarioCorrectWalletDelete() throws Exception {

        testHelper.addNewWallet(walletName);
        assertThat(walletRepository.existsByWalletNameAndUserName("xtb", "Piotr")).isTrue();

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/Piotr/wallets/" + walletName)
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(walletRepository.existsByWalletNameAndUserName("xtb", "Piotr")).isFalse();
    }


    @Test
    void scenarioGetWallets() throws Exception {

        testHelper.addNewWallet(walletName);
        testHelper.addNewWallet("ing");
        testHelper.addNewWallet("ledger");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/user/Piotr/wallets")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(resultActions.andReturn().getResponse().getContentAsString()).contains(walletName, "ing", "ledger");
    }


    @Test
    void scenarioGetWallet() throws Exception {

        testHelper.addNewWallet(walletName);
        testHelper.addNewWallet("ing");
        testHelper.addNewWallet("ledger");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/user/Piotr/wallets/" + walletName)
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(resultActions.andReturn().getResponse().getContentAsString()).contains(walletName);
    }
}
