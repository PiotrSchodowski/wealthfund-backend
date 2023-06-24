package com.example.wealthFund.repository;

import com.example.wealthFund.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

     @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.name = :name")
     boolean existsByUserName(@Param("name") String name);

     UserEntity findByName(String name);

     @Transactional
     @Modifying
     int deleteByName(@Param("name") String name);

}
