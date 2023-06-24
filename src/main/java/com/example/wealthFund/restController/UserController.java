package com.example.wealthFund.restController;

import com.example.wealthFund.dto.UserDto;
import com.example.wealthFund.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/{name}")
    public UserDto addNewUser(@PathVariable String name) {
        return userService.addNewUser(name);
    }

    @DeleteMapping("/users/{name}")
    public boolean deleteUser(@PathVariable String name) {
       return userService.deleteUser(name);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

}
