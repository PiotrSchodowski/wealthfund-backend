package com.example.wealthFund.service;

import com.example.wealthFund.dto.UserDto;
import com.example.wealthFund.dto.userDtos.JwtResponse;
import com.example.wealthFund.dto.userDtos.LoginRequest;
import com.example.wealthFund.dto.userDtos.SignupRequest;
import com.example.wealthFund.exception.NotExistException;
import com.example.wealthFund.exception.UserExistException;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.mapper.UserMapper;
import com.example.wealthFund.model.ERole;
import com.example.wealthFund.repository.RoleRepository;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.entity.RoleEntity;
import com.example.wealthFund.repository.entity.UserEntity;
import com.example.wealthFund.security.JwtUtils;
import com.example.wealthFund.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new UserExistException(signUpRequest.getUsername()));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new UserExistException(signUpRequest.getEmail()));
        }

        UserEntity userEntity = new UserEntity(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        RoleEntity userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new WealthFundSingleException("Role is not found"));

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);
        userEntity.setRoles(roles);
        userRepository.save(userEntity);

        return ResponseEntity.ok(new WealthFundSingleException("User registered successfully!"));
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }


    public boolean deleteUser(String userName) {
        validateUserExistenceThrowExceptionDoesNotExist(userName);
        int value = userRepository.deleteByName(userName);
        return value != 0;
    }


    public List<UserDto> getUsers() {
        return userMapper.userListToUserDtoList(userRepository.findAll());
    }


    public UserEntity getUserByName(String userName) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByName(userName);
        if (optionalUserEntity.isPresent()) {
            return optionalUserEntity.get();
        } else {
            throw new NotExistException(userName);
        }

    }

    private void validateUserExistenceThrowExceptionDoesNotExist(String userName) {
        if (!userRepository.existsByUserName(userName)) {
            throw new NotExistException(userName);
        }
    }
}
