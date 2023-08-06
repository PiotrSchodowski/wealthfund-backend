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
    private float valueOfPosition;              //cena aktualna * ilość
    private float percentageOfThePortfolio;     //stosunek wartości portfela do wartości pozycji

    private float rateOfReturn;  //stopa zwrotu czyli stosunek aktualnej ceny waloru do średniej zakupu
    private float result;           //valueOfPosition - averagePurchasePrice * quantity

}
