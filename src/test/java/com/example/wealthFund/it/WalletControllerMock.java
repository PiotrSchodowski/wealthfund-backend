package com.example.wealthFund.it;

import com.example.wealthFund.exception.WealthFundSingleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class WalletControllerMock {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestHelper testHelper;


    public void addNewWallet(String walletName) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallets/" + walletName + "/PLN")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void addNewWalletWhenSecondTimeTheSameData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallets/" + testHelper.walletNameXtb + "/PLN")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(result -> {
                    assertThat(result.getResolvedException()).isInstanceOf(WealthFundSingleException.class);
                    assertThat(Objects.requireNonNull(result.getResolvedException()).getMessage())
                            .isEqualTo("This name of wallet already exists");
                });
    }

    public void addNewWalletAndExpectNotAcceptable(String walletName) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallets/" + walletName + "/PLN")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    public void deleteWalletAndExpectOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/Piotr/wallets/" + testHelper.walletNameXtb)
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public ResultActions getWallets() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/user/Piotr/wallets")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public ResultActions getWallet() throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/user/Piotr/wallets/" + testHelper.walletNameXtb)
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
