package com.example.wealthFund.restController;

import com.example.wealthFund.WealthFundApplication;
import com.example.wealthFund.dto.walletDtos.WalletDto;
import com.example.wealthFund.dto.walletDtos.WalletResponseDto;
import com.example.wealthFund.service.WalletService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "2 Wallet Management", description = "adding and deleting wallets")
@RestController
@RequestMapping("/api")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/user/{userName}/wallets/{walletName}/{currency}")
    public ResponseEntity<?> addNewWallet(@PathVariable String userName, @PathVariable String walletName, @PathVariable String currency) {
        return walletService.addNewWallet(userName, walletName, currency);
    }

    @DeleteMapping("/user/{userName}/wallets/{walletName}")
    public ResponseEntity<?> deleteWallet(@PathVariable String userName, @PathVariable String walletName) {
        try {
            return walletService.deleteWallet(userName, walletName);
        } catch (Exception e) {
            return new ResponseEntity<>("Delete wallet operation failed", HttpStatus.NOT_ACCEPTABLE);

        }
    }

    @GetMapping("/user/{userName}/wallets")
    public List<WalletResponseDto> getUserWallets(@PathVariable String userName) {
        return walletService.getWallets(userName);
    }

    @GetMapping("/user/{userName}/wallets/{walletName}")
    public WalletResponseDto getUserWallet(@PathVariable String userName, @PathVariable String walletName) {
        return walletService.getWalletResponseDtoByName(userName, walletName);
    }

}
