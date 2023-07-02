package com.example.wealthFund.mapper;

import com.example.wealthFund.model.AssetDirectory;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CsvToObjectMapper {

    public static List<AssetDirectory> mapCsvAssetDirectoryToObjectAssetDirectory(String csvData) {
        List<AssetDirectory> assetDirectories = new ArrayList<>();

        try (CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(csvData))) {
            for (CSVRecord csvRecord : csvParser) {
                String symbol = csvRecord.get("symbol");
                String name = csvRecord.get("name");
                String exchange = csvRecord.get("exchange");
                String assetType = csvRecord.get("assetType");
                String currency = "USD";

                AssetDirectory assetDirectory = new AssetDirectory(symbol, name, currency, exchange, assetType);
                assetDirectories.add(assetDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return assetDirectories;
    }
}
