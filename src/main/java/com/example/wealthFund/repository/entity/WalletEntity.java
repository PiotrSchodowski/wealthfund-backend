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

    @ElementCollection(fetch = FetchType.EAGER)
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Set<PositionEntity> positions;

    @ElementCollection(fetch = FetchType.EAGER)
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    private List<UserCashTransactionEntity> userTransactions;

    @ElementCollection(fetch = FetchType.EAGER)
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    private List<OperationHistory> operationHistories;

    @ElementCollection(fetch = FetchType.EAGER)
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    private List<WalletValueHistory> walletValueHistories;

}
