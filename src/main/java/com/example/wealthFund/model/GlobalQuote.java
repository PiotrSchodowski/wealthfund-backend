package com.example.wealthFund.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalQuote {

    @JsonProperty("Global Quote")
    private GlobalQuoteData globalQuoteData;
}
