package com.example.wealthFund.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AssetDto {

    private String name;
    private String symbol;
    private String currency;
    private float price;
    private String exchange;
    private String assetType;
}
