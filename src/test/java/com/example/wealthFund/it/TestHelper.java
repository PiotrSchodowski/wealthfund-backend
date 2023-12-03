package com.example.wealthFund.it;

import com.example.wealthFund.dto.userDtos.JwtResponse;
import com.example.wealthFund.dto.userDtos.LoginRequest;
import com.example.wealthFund.dto.userDtos.SignupRequest;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.model.ERole;
import com.example.wealthFund.repository.RoleRepository;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.entity.RoleEntity;
import com.example.wealthFund.repository.entity.UserEntity;
import com.example.wealthFund.restController.AuthController;
import com.example.wealthFund.service.DataLoader;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Getter
public class TestHelper extends TestConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String walletNameXtb = "xtb";
    public String userNamePiotr = "Piotr";
    float valueOfDeposit1000 = 1000;
    float valueOfDepositNegative = (-1000);

    public String getToken() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Piotr");
        loginRequest.setPassword("password");

        return ((JwtResponse) Objects.requireNonNull(authController.authenticateUser(loginRequest).getBody())).getAccessToken();
    }

    public String getTokenByAdmin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("Admin123");

        return ((JwtResponse) Objects.requireNonNull(authController.authenticateUser(loginRequest).getBody())).getAccessToken();
    }

    public void createUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("Piotr");
        signupRequest.setPassword("password");
        signupRequest.setEmail("email@gmail.com");
        authController.registerUser(signupRequest);
    }

    public void createAdmin() {
        if (!userRepository.existsByName("admin")) {
            RoleEntity adminRole = roleRepository.findByName(ERole.ADMIN)
                    .orElseThrow(() -> new WealthFundSingleException("Admin role is not found"));
            Set<RoleEntity> roles = new HashSet<>();
            roles.add(adminRole);
            UserEntity adminUser = new UserEntity("admin", "admin@gmail.com", passwordEncoder.encode("Admin123"));
            adminUser.setRoles(roles);
            userRepository.save(adminUser);
        }
    }


}
