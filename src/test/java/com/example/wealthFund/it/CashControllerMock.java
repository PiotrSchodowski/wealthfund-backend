package com.example.wealthFund.it;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class CashControllerMock {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestHelper testHelper;

    public void depositCashIntoTheWallet(String walletName, float valueOfDeposit) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallet/" + walletName + "/cashDeposit/" + valueOfDeposit)
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    public void withdrawCashFromTheWallet(float valueCanWithdraw) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/Piotr/wallet/"
                                + testHelper.walletNameXtb
                                + "/cashWithdraw/"
                                + valueCanWithdraw)
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void withdrawCashFromTheWalletAndExpectNotAcceptable(float valueCannotWithdraw) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/user/Piotr/wallet/"
                                + testHelper.walletNameXtb
                                + "/cashWithdraw/"
                                + valueCannotWithdraw)
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }
}
