package com.example.wealthFund.restController;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.model.AssetPrice;
import com.example.wealthFund.model.GlobalQuote;
import com.example.wealthFund.service.ActualizationService;
import com.example.wealthFund.service.AssetService;
import com.example.wealthFund.service.ScrapperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "4 Asset Management", description = "uploading, updating and deleting assets")
@RestController
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final ScrapperService scrapperService;
    private final ActualizationService actualizationService;


    @GetMapping("/user/{userName}/wallets/{walletName}/updatePrices")
    public ResponseEntity<?> actualizeWalletData(@PathVariable String userName,
                                                 @PathVariable String walletName) {
        return ResponseEntity.ok(actualizationService.actualizePricesToWallet(userName, walletName));
    }

//    @GetMapping("/dataManagement/{symbol}")
//    public String getChange(@PathVariable String symbol) {
//        return scrapperService.getAssetDailyPriceChangeBySymbol(symbol);
//    }

    @GetMapping("/dataManagement/assets/getAll")
    public List<AssetDto> getAllAssets() {
        return assetService.getAllAssets();
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

    @GetMapping("/dataManagement/assets/import/cryptocurrencies")
    public List<AssetDto> saveCryptocurrenciesFromApi() {
        return assetService.createAssetsFromCryptocurrencies();
    }

    @GetMapping("/dataManagement/assets/import/usaAssets")
    public List<AssetDto> saveUsaAssetsFromApi() {
        return assetService.createAssetsFromUsaAssetApi();
    }

    @GetMapping("/dataManagement/assets/import/gpwAssets")
    public List<AssetDto> saveGpwAssetsFromFile() {
        return assetService.createAssetsFromGpwAssetFile();
    }

    @PostMapping("/dataManagement/assets/update/usaAssets/setPrice/{symbol}")
    public GlobalQuote savePriceToUsaAsset(@PathVariable String symbol) {
        return assetService.savePriceToUsaAsset(symbol);
    }

    @PostMapping("/dataManagement/assets/update/{symbol}")
    public AssetPrice savePriceOfAsset(@PathVariable String symbol){
        return scrapperService.getAssetPriceBySymbol(symbol);
    }
}
