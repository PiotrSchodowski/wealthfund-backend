package com.example.wealthFund.repository;

import com.example.wealthFund.repository.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity,Long> {

    AssetEntity findBySymbol(String symbol);

    @Transactional
    @Modifying
    int deleteBySymbol(@Param("symbol") String symbol);
}
