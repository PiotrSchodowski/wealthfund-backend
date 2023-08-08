package com.example.wealthFund.repository.entity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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
    private float quantity;
    private String basicCurrency;
    private String targetCurrency;
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
