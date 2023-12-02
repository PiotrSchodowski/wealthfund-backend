package com.example.wealthFund.service;

import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.model.ERole;
import com.example.wealthFund.repository.RoleRepository;
import com.example.wealthFund.repository.UserRepository;
import com.example.wealthFund.repository.entity.RoleEntity;
import com.example.wealthFund.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!userRepository.existsByName("admin")) {
            RoleEntity adminRole = roleRepository.findByName(ERole.ADMIN)
                    .orElseThrow(() -> new WealthFundSingleException("Admin role is not found"));

            Set<RoleEntity> roles = new HashSet<>();
            roles.add(adminRole);

            UserEntity adminUser = new UserEntity("admin", "admin@gmail.com", passwordEncoder.encode("Admin123"));
            adminUser.setRoles(roles);
            userRepository.save(adminUser);
        }
    }
}

