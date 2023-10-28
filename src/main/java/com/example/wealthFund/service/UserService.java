package com.example.wealthFund.service;

import com.example.wealthFund.dto.UserDto;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.UserExistException;
import com.example.wealthFund.mapper.UserMapper;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TextValidator textValidator;

    public UserService(UserMapper userMapper, UserRepository userRepository, TextValidator textValidator) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.textValidator = textValidator;
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

    public UserEntity getUserByName(String userName) {
        textValidator.checkTextValidity(userName);
        validateUserExistenceThrowExceptionDoesNotExist(userName);

        Optional<UserEntity> optionalUserEntity = userRepository.findByName(userName);
        if (optionalUserEntity.isPresent()) {
            return optionalUserEntity.get();
        }else{
            throw new NotExistException(userName);
        }

    }

    protected void validateUserExistenceThrowExceptionDoesNotExist(String userName) {
        if (!userRepository.existsByUserName(userName)) {
            throw new NotExistException(userName);
        }
    }
}
