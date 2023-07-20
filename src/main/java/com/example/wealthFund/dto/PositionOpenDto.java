package com.example.wealthFund.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionOpenDto {

    private String symbol;
    private float amount;
    private float commission;
    private boolean isPercentageCommission;
    private String currency;
    private float openingAssetPrice;

}

