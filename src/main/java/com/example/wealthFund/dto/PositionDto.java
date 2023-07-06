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

    private float amount;
    private float commission;
    private float result;
    private String currency;
    private float openingAssetPrice;
    private float endingAssetPrice;
    private LocalDateTime positionOpeningDate;
    private LocalDateTime positionEndingDate;
    private float openingCurrencyRate;
    private float endingCurrencyRate;
    private boolean isOpen;
}

