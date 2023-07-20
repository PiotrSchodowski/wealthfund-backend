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

    private float amount;
    private float commission;
    private float result;
    private String currency;
    private boolean isPercentageCommission;
    private float openingAssetPrice;
    private float endingAssetPrice;
    private LocalDateTime positionOpeningDate;
    private LocalDateTime positionEndingDate;
    private float openingCurrencyRate;
    private float endingCurrencyRate;
    private boolean isOpen;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private AssetEntity asset;

}
