package com.example.wealthFund.service;

import com.example.wealthFund.dto.UserDto;
import com.example.wealthFund.exception.UserExistException;
import com.example.wealthFund.exception.UserNotExistException;
import com.example.wealthFund.mapper.UserMapper;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TextValidator textValidator;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.textValidator = new TextValidator();
    }

    public UserDto addNewUser(String userName) {

        textValidator.checkTextValidity(userName);
        validateUserExistenceThrowExceptionWhenExist(userName);

        UserDto user = new UserDto(userName);
        userRepository.save(userMapper.userDtoToUser(user));
        return user;
    }

    public boolean deleteUser(String userName) {

        textValidator.checkTextValidity(userName);
        validateUserExistenceThrowExceptionDoesNotExist(userName);

        int value = userRepository.deleteByName(userName);
        return value != 0;
    }

    public List<UserDto> getUsers() {

        List<UserDto> userDtoList;
        userDtoList = userMapper.userListToUserDtoList(userRepository.findAll());
        return userDtoList;
    }

    protected UserEntity getUserByName(String userName){

        textValidator.checkTextValidity(userName);
        validateUserExistenceThrowExceptionDoesNotExist(userName);
        return userRepository.findByName(userName);
    }

    protected void validateUserExistenceThrowExceptionDoesNotExist(String userName) {

        if (!userRepository.existsByUserName(userName)) {
            throw new UserNotExistException(userName);
        }
    }

    protected void validateUserExistenceThrowExceptionWhenExist(String userName) {

        if (userRepository.existsByUserName(userName)) {
            throw new UserExistException(userName);
        }
    }
}
