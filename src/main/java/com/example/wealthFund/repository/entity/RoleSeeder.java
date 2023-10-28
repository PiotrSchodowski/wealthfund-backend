package com.example.wealthFund.repository.entity;



import com.example.wealthFund.model.ERole;
import com.example.wealthFund.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


import java.util.Optional;


@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initializeRoles();
    }

    private void initializeRoles() {
        createRoleIfNotExists(ERole.USER);
        createRoleIfNotExists(ERole.ADMIN);
    }

    private void createRoleIfNotExists(ERole roleName) {
        Optional<RoleEntity> existingRole = roleRepository.findByName(roleName);
        if (existingRole.isEmpty()) {
            RoleEntity roleEntity = new RoleEntity(roleName);
            roleRepository.save(roleEntity);
        }
    }
}
