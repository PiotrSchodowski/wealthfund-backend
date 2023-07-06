package com.example.wealthFund.repository;

import com.example.wealthFund.repository.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<PositionEntity,Long> {

    Optional<PositionEntity> findById(Long id);
}
