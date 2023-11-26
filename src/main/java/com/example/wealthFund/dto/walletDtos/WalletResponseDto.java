package com.example.wealthFund.dto.walletDtos;

import com.example.wealthFund.repository.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponseDto {

    String name;
    String currency;
    private float basicValue;
    private float actualValue;

    private CashEntity cashEntity;
    private Set<PositionEntity> positions;
    private List<UserCashTransactionEntity> userTransactions;
    private List<OperationHistory> operationHistories;
    private List<WalletValueHistory> walletValueHistories;
}
