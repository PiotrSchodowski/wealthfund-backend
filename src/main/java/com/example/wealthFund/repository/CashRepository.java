package com.example.wealthFund.repository;

import com.example.wealthFund.repository.entity.CashEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashRepository extends JpaRepository<CashEntity, Long>{

    Optional<CashEntity> findById(Long id);
}
