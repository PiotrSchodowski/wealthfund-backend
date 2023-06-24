package com.example.wealthFund.repository;

import com.example.wealthFund.repository.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity,Long> {

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM WalletEntity w WHERE w.name = :name AND w.userEntity.name = :userName")
    boolean existsByWalletNameAndUserName(@Param("name") String name, @Param("userName") String userName);

}
