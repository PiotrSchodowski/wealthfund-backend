package com.example.wealthFund.restController;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.service.AssetService;
import com.example.wealthFund.service.CurrencyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SupportController {

    private final CurrencyService currencyService;
    private final AssetService assetService;

    public SupportController(CurrencyService currencyService, AssetService assetService) {
        this.currencyService = currencyService;
        this.assetService = assetService;
    }

    @PostMapping("/exchange")
    public float exchangeCurrency(@RequestParam String baseCurrency,
                                  @RequestParam String targetCurrency,
                                  @RequestParam float valueToChange) {
        return currencyService.convertCurrency(baseCurrency, targetCurrency, valueToChange);
    }

    @GetMapping("/crypto")
    public List<AssetDto> getAllCryptoFromApi(){
        return assetService.createAssetsFromCryptocurrencies();
    }
}


