package com.example.wealthFund.restController;

import com.example.wealthFund.service.CurrencyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temporary")
public class SupportController { // support controller w przyszłości zostanie rozbity na kilka controllerów

    private final CurrencyService currencyService;

    public SupportController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/exchange")
    public float exchangeCurrency(@RequestParam String baseCurrency,
                                  @RequestParam String targetCurrency,
                                  @RequestParam float valueToChange) {
        return currencyService.convertCurrency(baseCurrency, targetCurrency, valueToChange);
    }
}


