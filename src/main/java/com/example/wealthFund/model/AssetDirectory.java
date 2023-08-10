package com.example.wealthFund.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDirectory {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("name")
    private String name;

    private String currency;

    @JsonProperty("exchange")
    private String exchange;

    @JsonProperty("assetType")
    private String assetType;

}