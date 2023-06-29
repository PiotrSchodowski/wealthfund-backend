package com.example.wealthFund.repository;

import com.example.wealthFund.repository.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity,Long> {
}
