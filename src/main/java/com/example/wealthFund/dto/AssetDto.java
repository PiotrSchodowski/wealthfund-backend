package com.example.wealthFund.dto;

import lombok.*;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AssetDto {

    private String name;
    private String symbol;
    private String currency;
    private float price;
    private String exchange;
    private String assetType;
    private float dailyPriceChange;
}
