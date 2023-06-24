package com.example.wealthFund.mapper;

import com.example.wealthFund.dto.UserDto;
import com.example.wealthFund.repository.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

   UserDto userToUserDto(UserEntity user);
   UserEntity userDtoToUser(UserDto userDto);

   List<UserDto> userListToUserDtoList(List<UserEntity> userList);
   List<UserEntity> userDtoListToUserList(List<UserDto> userDtoList);
}

