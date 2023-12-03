package com.example.wealthFund.it;

import com.example.wealthFund.WealthFundApplication;
import com.example.wealthFund.it.controllers.CashControllerMock;
import com.example.wealthFund.it.controllers.UserControllerMock;
import com.example.wealthFund.it.controllers.WalletControllerMock;
import com.example.wealthFund.repository.CashRepository;
import com.example.wealthFund.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@AutoConfigureMockMvc
public class UserIntegrationTests extends TestConfig{

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CashRepository cashRepository;

    @Autowired
    TestHelper testHelper;

    @Autowired
    WalletControllerMock walletControllerMock;

    @Autowired
    UserControllerMock userControllerMock;

    @BeforeEach
    public void setDB() {
        testHelper.createUser();
    }

    @AfterEach
    public void clearDB() {
        userRepository.deleteAll();
    }


    @Test
    void scenarioDeleteUser() throws Exception {
        userControllerMock.deleteUser(testHelper.userNamePiotr);
        assertThat(userRepository.findByName(testHelper.userNamePiotr).isPresent());
    }

    @Test
    void scenarioGetAllUsers() throws Exception{
        userControllerMock.getAllUsers();
    }
}
