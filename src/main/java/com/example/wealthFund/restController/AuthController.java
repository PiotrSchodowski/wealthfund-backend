package com.example.wealthFund.restController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.wealthFund.dto.userDtos.JwtResponse;
import com.example.wealthFund.dto.userDtos.LoginRequest;
import com.example.wealthFund.dto.userDtos.SignupRequest;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.model.ERole;
import com.example.wealthFund.repository.RoleRepository;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.entity.RoleEntity;
import com.example.wealthFund.repository.entity.UserEntity;
import com.example.wealthFund.security.JwtUtils;
import com.example.wealthFund.security.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new WealthFundSingleException("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new WealthFundSingleException("Error: Email is already in use!"));
        }

        UserEntity userEntity = new UserEntity(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        RoleEntity userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);

        userEntity.setRoles(roles);
        userRepository.save(userEntity);

        return ResponseEntity.ok(new WealthFundSingleException("User registered successfully!"));
    }
}
