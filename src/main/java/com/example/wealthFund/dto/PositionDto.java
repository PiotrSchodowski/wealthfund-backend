package com.example.wealthFund.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionDto {

    private String symbol;
    private float amount;
    private float commission;
    private boolean isPercentageCommission;
    private String currency;
    private float openingAssetPrice;
    private float endingAssetPrice;
    private LocalDateTime positionOpeningDate;
    private LocalDateTime positionEndingDate;
    private float openingCurrencyRate;
    private float endingCurrencyRate;
    private boolean isOpen;
    private float result;
}

