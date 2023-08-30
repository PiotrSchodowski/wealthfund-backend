package com.example.wealthFund.repository;

import com.example.wealthFund.repository.entity.OperationHistory;
import com.example.wealthFund.repository.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationHistoryRepo extends JpaRepository<OperationHistory, Long> {

    Optional<OperationHistory> findById(Long id);
}
