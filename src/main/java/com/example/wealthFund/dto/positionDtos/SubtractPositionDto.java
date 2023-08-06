package com.example.wealthFund.dto.positionDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubtractPositionDto {

    private String symbol;
    private String currency;
    private float endingCurrencyRate;
    private float endingAssetPrice;
    private float quantityOfAsset;
    private float totalValueEntered;
}
