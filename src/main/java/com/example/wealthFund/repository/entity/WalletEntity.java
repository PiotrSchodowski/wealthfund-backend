package com.example.wealthFund.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    private String currency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private UserEntity userEntity;

    @OneToOne(cascade = CascadeType.ALL)
    private CashEntity cashEntity;

    @ElementCollection
    @OneToMany
    private Set<PositionEntity> positions;

    @ElementCollection
    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "wallet_id")
    private List<UserCashTransactionEntity> userTransactions;

}
