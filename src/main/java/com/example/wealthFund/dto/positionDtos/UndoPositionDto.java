package com.example.wealthFund.dto.positionDtos;

import lombok.Data;

@Data
public class UndoPositionDto {

    private String symbol;
    private float quantity;
    private float price;
    private String positionCurrency;
    private String walletName;
    private String exchange;
    private float valueOperation;
    private float averagePrice;
}
