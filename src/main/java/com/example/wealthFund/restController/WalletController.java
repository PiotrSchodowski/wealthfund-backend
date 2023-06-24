package com.example.wealthFund.restController;

import com.example.wealthFund.dto.WalletDto;
import com.example.wealthFund.service.WalletService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/user/{userName}/wallets/{walletName}/{currency}")
    public WalletDto addNewWallet(@PathVariable String userName, @PathVariable String walletName, @PathVariable String currency) {
        return walletService.addNewWallet(userName, walletName, currency);
    }

    @DeleteMapping("/user/{userName}/wallets/{walletName}")
    public boolean deleteWallet(@PathVariable String userName, @PathVariable String walletName) {
        return walletService.deleteWallet(userName, walletName);
    }

}
