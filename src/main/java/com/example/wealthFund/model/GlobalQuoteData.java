package com.example.wealthFund.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalQuoteData {

    @JsonProperty("01. symbol")
    private String symbol;

    @JsonProperty("02. open")
    private float open;

    @JsonProperty("03. high")
    private float high;

    @JsonProperty("04. low")
    private float low;

    @JsonProperty("05. price")
    private float price;

    @JsonProperty("06. volume")
    private float volume;

    @JsonProperty("07. latest trading day")
    private String latestTradingDay;

    @JsonProperty("08. previous close")
    private float previousClose;

    @JsonProperty("09. change")
    private float change;

    @JsonProperty("10. change percent")
    private String changePercent;

}
