package com.example.wealthFund.it.controllers;

import com.example.wealthFund.it.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class AssetControllerMock {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestHelper testHelper;

    public void addGpwAssets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dataManagement/assets/import/gpwAssets")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
