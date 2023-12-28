package com.example.wealthFund.it.controllers;

import com.example.wealthFund.it.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestController
public class UserControllerMock {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TestHelper testHelper;


    public void deleteUser(String userName) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + userName)
                .header("Authorization", "Bearer " + testHelper.getToken()))
                .andExpect(status().isOk());
    }

    public void getAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .header("Authorization", "Bearer " + testHelper.getToken()))
                .andExpect(status().isOk());
    }

}
