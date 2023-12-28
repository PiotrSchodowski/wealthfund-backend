package com.example.wealthFund.restController;

import com.example.wealthFund.dto.UserDto;
import com.example.wealthFund.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "1 User account management", description = "adding, deleting and getting users")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @DeleteMapping("/users/{name}")
    public boolean deleteUser(@PathVariable String name) {
        return userService.deleteUser(name);
    }


    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

}
