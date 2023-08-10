package com.example.wealthFund.service;

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
}

