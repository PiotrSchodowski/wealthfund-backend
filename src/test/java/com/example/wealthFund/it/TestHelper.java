package com.example.wealthFund.it;

import com.example.wealthFund.dto.positionDtos.AddPositionDto;
import com.example.wealthFund.dto.userDtos.JwtResponse;
import com.example.wealthFund.dto.userDtos.LoginRequest;
import com.example.wealthFund.dto.userDtos.SignupRequest;
import com.example.wealthFund.restController.AuthController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Service
@Getter
public class TestHelper {

    @Autowired
    private  MockMvc mockMvc;

    @Autowired
    private AuthController authController;


    public TestHelper() throws JsonProcessingException {
    }


    public String getToken() {

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("Piotr");
        signupRequest.setPassword("password");
        signupRequest.setEmail("email@gmail.com");
        authController.registerUser(signupRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Piotr");
        loginRequest.setPassword("password");

        return ((JwtResponse) Objects.requireNonNull(authController.authenticateUser(loginRequest).getBody())).getAccessToken();
    }


    public void addNewWallet(String walletName) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallets/" + walletName + "/PLN")
                        .header("Authorization", "Bearer " + getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    public void depositCashIntoTheWallet(String walletName, float valueOfDeposit) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallet/" + walletName + "/cashDeposit/" + valueOfDeposit)
                        .header("Authorization", "Bearer " + getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    public void addGpwAssets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dataManagement/assets/import/gpwAssets")
                        .header("Authorization", "Bearer " + getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    public void addPosition(String walletName, float valueOfDeposit, AddPositionDto addPositionDto) throws Exception {

        addNewWallet(walletName);
        depositCashIntoTheWallet(walletName, valueOfDeposit);
        addGpwAssets();

        ObjectMapper objectMapper = new ObjectMapper();
        String addPositionDtoJson = objectMapper.writeValueAsString(addPositionDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/Piotr/wallet/" + walletName + "/position")
                        .header("Authorization", "Bearer " + getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addPositionDtoJson))
                .andExpect(status().isOk());
    }
}
