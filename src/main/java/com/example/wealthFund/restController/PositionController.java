package com.example.wealthFund.restController;

import com.example.wealthFund.dto.PositionDto;
import com.example.wealthFund.service.PositionService;
import com.example.wealthFund.service.WalletService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PositionController {

    private final PositionService positionService;
    private final WalletService walletService;

    public PositionController(PositionService positionService, WalletService walletService) {
        this.positionService = positionService;
        this.walletService = walletService;
    }

    @PostMapping("/user/{userName}/wallet/{walletName}/{symbol}/{currency}/{openingPrice}/{amount}/{commission}")
    public PositionDto openPosition(@PathVariable String userName,
                                    @PathVariable String walletName,
                                    @PathVariable String symbol,
                                    @PathVariable String currency,
                                    @PathVariable float openingPrice,
                                    @PathVariable float amount,
                                    @PathVariable float commission) {
        return positionService.openPosition(userName, walletName, symbol, currency, openingPrice, amount, commission);

    }
}
