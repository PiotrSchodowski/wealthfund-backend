package com.example.wealthFund.controllerTests;

import com.example.wealthFund.dto.UserDto;
import com.example.wealthFund.restController.UserController;
import com.example.wealthFund.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final String userName = "Piotr";
    private UserDto userDto;

    @Before
    public void setup() {
        userDto = new UserDto(userName);
    }

    @Test
    public void shouldAddNewUser() {
        when(userService.addNewUser(userName)).thenReturn(userDto);

        UserDto result = userController.addNewUser(userName);

        assertThat(result).isEqualTo(userDto);
    }

    @Test
    public void shouldDeleteUser() {
        when(userService.deleteUser(userName)).thenReturn(true);

        boolean result = userController.deleteUser(userName);

        assertThat(result).isTrue();
    }

    @Test
    public void shouldGetUsers() {
        List<UserDto> users = Arrays.asList(userDto, new UserDto("Piotr"));
        when(userService.getUsers()).thenReturn(users);

        List<UserDto> result = userController.getUsers();

        assertThat(result).isEqualTo(users);
    }
}
