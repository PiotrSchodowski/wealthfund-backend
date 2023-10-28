package com.example.wealthFund.repository;

import com.example.wealthFund.model.ERole;
import com.example.wealthFund.repository.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(ERole name);
}
