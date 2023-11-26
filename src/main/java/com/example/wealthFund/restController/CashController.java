package com.example.wealthFund.restController;

import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.repository.entity.CashEntity;
import com.example.wealthFund.service.CashService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "3 Cash Management", description = "depositing and withdrawing cash from the wallet")
@RestController
public class CashController {

    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    @PostMapping("user/{userName}/wallet/{walletName}/cashDeposit/{valueOfDeposit}")
    public ResponseEntity<?> depositCashIntoTheWallet(@PathVariable String userName,
                                                               @PathVariable String walletName,
                                                               @PathVariable float valueOfDeposit) {
        return cashService.depositCashIntoTheWallet(userName, walletName, valueOfDeposit);
    }


    @PutMapping("user/{userName}/wallet/{walletName}/cashWithdraw/{valueOfWithdraw}")
    public ResponseEntity<?> withdrawCashFromTheWallet(@PathVariable String userName,
                                                       @PathVariable String walletName,
                                                       @PathVariable float valueOfWithdraw) {
        cashService.withdrawCashFromTheWallet(userName, walletName, valueOfWithdraw);
        return ResponseEntity.ok(new WealthFundSingleException("Cash withdraw successfully!"));
    }

}
