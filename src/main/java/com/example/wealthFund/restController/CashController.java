package com.example.wealthFund.restController;

import com.example.wealthFund.service.CashService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CashController {

    private final CashService cashService;

    public CashController(CashService cashService){
        this.cashService = cashService;
    }

    @PostMapping("user/{userName}/wallet/{walletName}/cashDeposit/{valueOfDeposit}")
    public boolean depositCashIntoTheWallet(@PathVariable String userName,
                                            @PathVariable String walletName,
                                            @PathVariable float valueOfDeposit){
        return cashService.depositCashIntoTheWallet(userName,walletName,valueOfDeposit);
    }

    @PostMapping("user/{userName}/wallet/{walletName}/cashWithdraw/{valueOfWithdraw}")
    public boolean withdrawCashFromTheWallet(@PathVariable String userName,
                                             @PathVariable String walletName,
                                             @PathVariable float valueOfWithdraw){
        return cashService.withdrawCashFromTheWallet(userName,walletName,valueOfWithdraw);
    }

}
