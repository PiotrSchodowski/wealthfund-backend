package com.example.wealthFund.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String symbol;
    private float quantity;
    private float price;
    private String positionCurrency;
    private String walletCurrency;
    private String walletName;
    private LocalDateTime date;
    private String exchange;


    private float valueOperation;
}
