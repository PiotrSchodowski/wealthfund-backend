package com.example.wealthFund.repository.entity;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String symbol;
    private String name;
    private String exchange;
    private float quantity;
    private String userCurrency;
    private String basicCurrency;
    private String walletCurrency;
    private LocalDateTime positionOpeningDate;
    private float valueBasedOnPurchasePrice;
    private float averagePurchasePrice;
    private float actualPrice;
    private LocalDateTime actualPriceDate;
    private float valueOfPosition;
    private float percentageOfThePortfolio;
    private float rateOfReturn;
    private float result;

}
