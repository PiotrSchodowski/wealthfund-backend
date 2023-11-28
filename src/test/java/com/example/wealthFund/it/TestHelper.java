package com.example.wealthFund.it;

import com.example.wealthFund.dto.userDtos.JwtResponse;
import com.example.wealthFund.dto.userDtos.LoginRequest;
import com.example.wealthFund.dto.userDtos.SignupRequest;
import com.example.wealthFund.restController.AuthController;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

@Service
@Getter
public class TestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;

    String walletNameXtb = "xtb";
    String userNamePiotr = "Piotr";
    float valueOfDeposit1000 = 1000;
    float valueOfDepositNegative = -1000;

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
}
