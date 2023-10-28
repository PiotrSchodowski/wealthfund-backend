package com.example.wealthFund.repository.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "wallets")
public class WalletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 20)
    private String currency;

    private float basicValue;
    private float actualValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    private UserEntity userEntity;

    @OneToOne(cascade = CascadeType.ALL)
    private CashEntity cashEntity;

    @ElementCollection
    @OneToMany(cascade = {CascadeType.ALL})
    private Set<PositionEntity> positions;

    @ElementCollection
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "wallet_id")
    private List<UserCashTransactionEntity> userTransactions;

    @ElementCollection
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "wallet_id")
    private List<OperationHistory> operationHistories;

}
