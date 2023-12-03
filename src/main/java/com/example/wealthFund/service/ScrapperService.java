package com.example.wealthFund.service;

import com.example.wealthFund.dto.AssetDto;
import com.example.wealthFund.exception.WealthFundSingleException;
import com.example.wealthFund.model.AssetPrice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScrapperService {


    public AssetPrice getAssetPriceBySymbol(String symbol) {

        String url = "https://www.biznesradar.pl/notowania/" + symbol + "#1d_lin_lin";
        try {
            Document document = Jsoup.connect(url).get();
            Element priceElement = document.selectFirst(".q_ch_act");

            if (priceElement != null) {
                String priceText = priceElement.text();
                double price = Double.parseDouble(priceText.replace(",", "."));
                return new AssetPrice(symbol, price);
            } else {
                throw new WealthFundSingleException("Price element not found for symbol " + symbol);
            }
        } catch (IOException e) {
            throw new WealthFundSingleException("Error fetching asset price for symbol " + symbol);
        }
    }


    public AssetDto actualizeGpwAsset(AssetDto assetDto) {

        String url = "https://www.biznesradar.pl/notowania/" + assetDto.getSymbol() + "#1d_lin_lin";
        return getActualizedAssetByGPWorUSA(assetDto, url);
    }


    public AssetDto actualizeUsaAsset(AssetDto assetDto) {

        String url = "https://www.biznesradar.pl/notowania/" + assetDto.getSymbol() + ".US#1d_lin_lin";
        return getActualizedAssetByGPWorUSA(assetDto, url);
    }


    public AssetDto actualizeCryptoAsset(AssetDto assetDto) {

        String url = "https://coinmarketcap.com/currencies/" + assetDto.getName().toLowerCase();
        try {
            Document document = Jsoup.connect(url).get();
            Element priceElement = document.selectFirst("span.sc-f70bb44c-0.jxpCgO.base-text");
            Element element = document.selectFirst("p[data-change][data-sensors-click=true]");

            if (priceElement != null) {
                String priceText = priceElement.text().replaceAll("[^\\d.,]+", "");
                priceText = priceText.replaceAll("(\\d),(\\d)", "$1$2");
                assetDto.setPrice(Float.parseFloat(priceText));

                String changeText = element.text();
                int percentIndex = changeText.indexOf('%');

                if (percentIndex != -1) {
                    changeText = changeText.substring(0, percentIndex);
                }

                String changeValue = changeText.replaceAll("[^\\d.-]+", "");
                String changeAttribute = element.attr("data-change");

                if ("down".equals(changeAttribute)) {
                    changeValue = "-" + changeValue;  // Dodaj znak minus, jeżeli "down"
                }

                assetDto.setDailyPriceChange(Float.parseFloat(changeValue));


                return assetDto;
            } else {
                throw new WealthFundSingleException("Element not found for symbol " + assetDto.getSymbol());
            }
        } catch (IOException e) {
            throw new WealthFundSingleException("Error fetching asset update for symbol " + assetDto.getSymbol());
        }
    }


    private AssetDto getActualizedAssetByGPWorUSA(AssetDto assetDto, String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Element priceElement = document.selectFirst(".q_ch_act");
            Element changeDailyElement = document.selectFirst(".q_ch_per");

            if (priceElement != null) {
                String priceText = priceElement.text();
                String changeDailyText = changeDailyElement.text();
                assetDto.setPrice(Float.parseFloat(priceText.replace(",", ".")));
                assetDto.setDailyPriceChange(Float.parseFloat(changeDailyText.replaceAll("[^\\d.-]+", "")));
                return assetDto;
            } else {
                throw new WealthFundSingleException("Element not found for symbol " + assetDto.getSymbol());
            }
        } catch (IOException e) {
            throw new WealthFundSingleException("Error fetching asset update for symbol " + assetDto.getSymbol());
        }
    }
}

