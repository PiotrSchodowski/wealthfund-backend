package com.example.wealthFund.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetDto {

    private String name;
    private String isin;
    private float value;
}
