package com.example.wealthFund.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @ElementCollection
    @OneToMany(cascade = CascadeType.REMOVE)
    private Set<WalletEntity> wallets;

}
