package com.example.wealthFund.it;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.positionDtos.SubtractPositionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Service
public class PositionControllerMock {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestHelper testHelper;

    ObjectMapper objectMapper = new ObjectMapper();

    public void addPosition(AddPositionDto addPositionDto) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallet/" + testHelper.walletNameXtb + "/position")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addPositionDto)))
                .andExpect(status().isOk());
    }

    public void addPositionAndExpectNotAcceptable(AddPositionDto addPositionDto) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallet/" + testHelper.walletNameXtb + "/position")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addPositionDto)))
                .andExpect(status().isNotAcceptable());

    }

    public void subtractPosition(SubtractPositionDto subtractPositionDto) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/user/Piotr/wallet/" + testHelper.walletNameXtb + "/position/decrease")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subtractPositionDto)))
                .andExpect(status().isOk());
    }


}
