package com.example.wealthFund.service;

import com.example.wealthFund.dto.UserDto;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.UserExistException;
import com.example.wealthFund.mapper.UserMapper;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private TextValidator textValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnUserDto() {

        String userName = "Piotr";
        UserDto userDto = new UserDto(userName);
        UserEntity user = new UserEntity();

        when(userMapper.userDtoToUser(any(UserDto.class))).thenReturn(user);
        when(userRepository.existsByUserName(userName)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.userToUserDto(user)).thenReturn(userDto);

        UserDto result = userService.addNewUser(userName);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userName, result.getName());
    }

    @Test
    void shouldThrowUserExistException() {
        // Given
        String userName = "Piotr";
        when(userRepository.existsByUserName(userName)).thenReturn(true);

        // When & Then
        UserExistException exception = assertThrows(UserExistException.class, () -> userService.addNewUser(userName));
        Assertions.assertEquals("Piotr exist in database, try other name", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhileUserExist() {  //todo nie czaje tego testu czemu nie przechodzi, program przechodzi
        // Given
        String userName = "Piotr";
        UserEntity user = new UserEntity();
        user.setName(userName);

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByName(userName)).thenReturn(user);

        // When
        boolean result = userService.deleteUser(userName);

        // Then
        Assertions.assertTrue(result);
    }

    @Test
    void ShouldThrowUserNotExistException() {
        // Given
        String userName = "Piotr";
        when(userRepository.existsByUserName(userName)).thenReturn(false);

        // When & Then
        NotExistException exception = assertThrows(NotExistException.class, () -> userService.deleteUser(userName));
        Assertions.assertEquals("Piotr does not exist, please try again.", exception.getMessage());
    }


    @Test
    void shouldReturnListOfUserDtoWhileUsersExist() {
        // Given
        List<UserEntity> userList = new ArrayList<>();
        userList.add(new UserEntity());
        userList.add(new UserEntity());

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.userListToUserDtoList(userList)).thenReturn(new ArrayList<>());

        // When
        List<UserDto> result = userService.getUsers();

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }
}

