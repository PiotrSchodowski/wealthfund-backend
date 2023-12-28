package com.example.wealthFund.it.controllers;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
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
public class PositionControllerMock {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestHelper testHelper;

    ObjectMapper objectMapper = new ObjectMapper();

    public void addPosition(AddPositionDto addPositionDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/Piotr/wallet/" + testHelper.walletNameXtb + "/position")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addPositionDto)))
                .andExpect(status().isOk());
    }

    public void addPositionAndExpectNotAcceptable(AddPositionDto addPositionDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/Piotr/wallet/" + testHelper.walletNameXtb + "/position")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addPositionDto)))
                .andExpect(status().isNotAcceptable());

    }

    public void subtractPosition(SubtractPositionDto subtractPositionDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/Piotr/wallet/" + testHelper.walletNameXtb + "/position/decrease")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subtractPositionDto)))
                .andExpect(status().isOk());
    }

    public void subtractPositionAndExpectNotAcceptable(SubtractPositionDto subtractPositionDto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/Piotr/wallet/" + testHelper.walletNameXtb + "/position/decrease")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subtractPositionDto)))
                .andExpect(status().isNotAcceptable());
    }

    public void undoOperation(int operationId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/Piotr/wallet/" + testHelper.walletNameXtb + "/position/undo/" + operationId)
                        .header("Authorization", "Bearer " + testHelper.getToken()))
                .andExpect(status().isOk());
    }

    public void getPositionById(int id) throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/position/" + id)
                .header("Authorization", "Bearer " + testHelper.getToken()))
                .andExpect(status().isOk());
    }

    public void getPositionByIdAndExpectNotAcceptable(int id) throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/position/" + id)
                        .header("Authorization", "Bearer " + testHelper.getToken()))
                .andExpect(status().isNotAcceptable());
    }
}
