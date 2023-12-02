package com.example.wealthFund.it.controllers;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.it.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestController
public class AssetControllerMock {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestHelper testHelper;

    ObjectMapper objectMapper = new ObjectMapper();

    public void addGpwAssets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dataManagement/assets/import/gpwAssets")
                        .header("Authorization", "Bearer " + testHelper.getTokenByAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void addUsaAssets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dataManagement/assets/import/usaAssets")
                        .header("Authorization", "Bearer " + testHelper.getTokenByAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void addCryptocurrencies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dataManagement/assets/import/cryptocurrencies")
                        .header("Authorization", "Bearer " + testHelper.getTokenByAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void getAllAssets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dataManagement/assets/getAll")
                        .header("Authorization", "Bearer " + testHelper.getTokenByAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void entryManualAsset(AssetDto assetDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dataManagement/assets/insert")
                        .header("Authorization", "Bearer " + testHelper.getTokenByAdmin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetDto)))
                .andExpect(status().isOk());
    }

    public void deleteAssetBySymbol(String symbol) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/dataManagement/assets/delete/" + symbol)
                        .header("Authorization", "Bearer " + testHelper.getTokenByAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void savePriceToUsaAsset(String symbol) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dataManagement/assets/update/usaAssets/setPrice/" + symbol)
                        .header("Authorization", "Bearer " + testHelper.getTokenByAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void savePriceOfAsset(String symbol) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dataManagement/assets/update/" + symbol)
                        .header("Authorization", "Bearer " + testHelper.getTokenByAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public void savePriceOfAssetNotAcceptable(String symbol) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dataManagement/assets/update/" + symbol)
                        .header("Authorization", "Bearer " + testHelper.getTokenByAdmin())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }


}
