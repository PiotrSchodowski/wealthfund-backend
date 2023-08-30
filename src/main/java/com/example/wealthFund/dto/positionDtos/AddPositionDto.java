package com.example.wealthFund.dto.positionDtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddPositionDto {

    private String symbol;
    private String exchange;
    private float quantity;
    private float price;
    private String currency;
    private float openingCurrencyRate;
    private float commission;
    private boolean isPercentageCommission;
    private LocalDateTime timeOpening;
    private float totalValueEntered;
}

