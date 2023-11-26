package com.example.wealthFund.mapper;

import com.example.wealthFund.exception.CsvProcessingException;
import com.example.wealthFund.model.AssetDirectory;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileCsvToAssetDirectoryMapper {

    private final ResourceLoader resourceLoader;

    @Autowired
    public FileCsvToAssetDirectoryMapper(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<AssetDirectory> processCsvFile() {
        List<AssetDirectory> gpwAssets = new ArrayList<>();

        Resource resource = resourceLoader.getResource("classpath:gpw.csv");
        try (Reader reader = new InputStreamReader(resource.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String symbol = line[11];
                if (!symbol.isEmpty()) {
                    AssetDirectory gpwAsset = new AssetDirectory();
                    gpwAsset.setName(formatAssetName(line[10]));
                    gpwAsset.setSymbol(symbol);
                    gpwAsset.setCurrency(line[18]);
                    gpwAsset.setAssetType("Stock");
                    gpwAsset.setExchange("GPW");
                    gpwAssets.add(gpwAsset);
                }
            }
        } catch (CsvValidationException | IOException e) {
            throw new CsvProcessingException();
        }
        return gpwAssets;
    }

    private String formatAssetName(String originalName) {
        return Character.toUpperCase(originalName.charAt(0)) + originalName.substring(1).toLowerCase();
    }


}

