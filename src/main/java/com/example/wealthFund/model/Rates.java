package com.example.wealthFund.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rates {

    @JsonProperty("PLN")
    private float PLN;
    @JsonProperty("EUR")
    private float EUR;
}
