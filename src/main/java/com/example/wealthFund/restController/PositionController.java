package com.example.wealthFund.restController;

import com.example.wealthFund.dto.PositionDto;
import com.example.wealthFund.dto.PositionOpenDto;
import com.example.wealthFund.service.PositionService;
import com.example.wealthFund.service.WalletService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PositionController {

    private final PositionService positionService;
    private final WalletService walletService;

    public PositionController(PositionService positionService, WalletService walletService) {
        this.positionService = positionService;
        this.walletService = walletService;
    }

    @PostMapping("/user/{userName}/wallet/{walletName}/position")
    public PositionDto openPosition(@PathVariable String userName,
                                    @PathVariable String walletName,
                                    @RequestBody PositionOpenDto positionOpenDto) {
        return positionService.openPosition(userName, walletName, positionOpenDto);
    }
}
