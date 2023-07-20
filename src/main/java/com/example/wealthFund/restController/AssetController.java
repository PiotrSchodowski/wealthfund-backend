package com.example.wealthFund.restController;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.model.GlobalQuote;
import com.example.wealthFund.service.AssetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping("/dataManagement/assets/insert")
    public AssetDto entryManualAsset(@RequestBody AssetDto assetDto) {
        return assetService.createAssetFromManualEntry(assetDto);
    }

    @PostMapping("/dataManagement/assets/update/{symbol}/{price}")
    public AssetDto entryManualPriceOfAsset(@PathVariable String symbol,
                                            @PathVariable float price) {
        return assetService.updatePriceOfAssetFromManualEntry(symbol, price);
    }

    @DeleteMapping("/dataManagement/assets/delete/{symbol}")
    public boolean deleteAssetBySymbol(@PathVariable String symbol) {
        return assetService.deleteAssetBySymbol(symbol);
    }

    @GetMapping("/dataManagement/assets/get/{symbol}")
    public AssetDto entryManualPriceOfAsset(@PathVariable String symbol) {
        return assetService.getAssetBySymbol(symbol);
    }

    @GetMapping("/dataManagement/assets/import/cryptocurrencies")
    public List<AssetDto> saveCryptocurrenciesFromApi() {
        return assetService.createAssetsFromCryptocurrencies();
    }

    @GetMapping("/dataManagement/assets/import/usaAssets")
    public List<AssetDto> saveUsaAssetsFromApi() {
        return assetService.createAssetsFromAssetDirectory();
    }

    @PostMapping("/dataManagement/assets/update/usaAssets/setPrice/{symbol}")
    public GlobalQuote savePriceToUsaAsset(@PathVariable String symbol) {
        return assetService.savePriceToUsaAsset(symbol);
    }//todo controller oraz service bedzie modyfikowany po wprowadzeniu większej ilości ApiServisów
    //todo tataj sa tylko Amerykańskie, w planach sa jeszcze europejskie(LON,FRK,BER,PAR) oraz GPW
    //todo kazda odpowiedz z Api bedzie pewnie miała swoje "GlobalQuote" więc utworze klasę mapującą te wszystkie ApiResponsy na jedną uniwersalną klasę
}
