package com.example.wealthFund.it;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
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

    public void addPosition(String walletName, AddPositionDto addPositionDto) throws Exception {


        ObjectMapper objectMapper = new ObjectMapper();
        String addPositionDtoJson = objectMapper.writeValueAsString(addPositionDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallet/" + walletName + "/position")
                        .header("Authorization", "Bearer " + testHelper.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addPositionDtoJson))
                .andExpect(status().isOk());
    }


}
