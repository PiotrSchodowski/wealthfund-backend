package com.example.wealthFund.repository.entity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private float amount;
    private float commission;
    private float result;
    private float openingAssetPrice;
    private float endingAssetPrice;
    private Date positionOpeningDate;
    private Date positionEndingDate;
    private float openingCurrencyPrice;
    private float endingCurrencyPrice;

    private boolean isOpen;

    @ManyToOne
    private AssetEntity asset;

}
