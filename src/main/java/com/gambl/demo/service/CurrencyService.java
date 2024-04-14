package com.gambl.demo.service;

import com.gambl.demo.domain.entity.CurrencyEntity;
import com.gambl.demo.domain.model.CurrencyData;
import com.gambl.demo.domain.response.FreeCurrencyResponse;
import com.gambl.demo.repository.CurrencyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final RestTemplate restTemplate;

    private Map<String, List<CurrencyData>> currenciesMap = new HashMap<>();

    @Value("${freecurrencyapi.api.key}")
    private String apiKey;

    public CurrencyService(CurrencyRepository currencyRepository, RestTemplate restTemplate) {
        this.currencyRepository = currencyRepository;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void initializeCurrenciesMapFromDatabase() {
        List<CurrencyEntity> allCurrencies = currencyRepository.findAll();

        allCurrencies.forEach(currency -> {
            String baseCurrencyCode = currency.getBaseCurrencyCode();
            currenciesMap.computeIfAbsent(baseCurrencyCode, k -> new ArrayList<>())
                    .add(new CurrencyData(currency.getCurrencyCode(), currency.getExchangeRate()));
        });
    }

    public Set<String> getAllCurrencies() {
        return currenciesMap.keySet();
    }

    public List<CurrencyData> getExchangeRates(String baseCurrency) {
        return currenciesMap.get(baseCurrency);
    }

    public void addCurrency(String code) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setBaseCurrencyCode(code);
        currencyEntity.setCurrencyCode(code);
        currencyEntity.setExchangeRate(BigDecimal.valueOf(1));

        currenciesMap.put(code, new ArrayList<>());
        currencyRepository.save(currencyEntity);
    }

    public void fetchAndStoreExchangeRates() {
        for (String baseCurrencyCode : currenciesMap.keySet()) {
            String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=" + apiKey + "&base_currency=" + baseCurrencyCode;
            FreeCurrencyResponse response = restTemplate.getForObject(apiUrl, FreeCurrencyResponse.class);

            if (response != null && response.getData() != null) {
                updateRatesInMemoryMapAndDatabase(baseCurrencyCode, response.getData());
            } else {
                throw new RuntimeException("Exchange rates not found in the API response");
            }
        }
    }

    private void updateRatesInMemoryMapAndDatabase(String baseCurrencyCode, Map<String, BigDecimal> rates) {
        List<CurrencyEntity> currencies = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : rates.entrySet()) {
            CurrencyEntity currencyEntity = new CurrencyEntity();

            currencyEntity.setBaseCurrencyCode(baseCurrencyCode);
            currencyEntity.setCurrencyCode(entry.getKey());
            currencyEntity.setExchangeRate(entry.getValue());

            currencies.add(currencyEntity);
        }

        currencyRepository.saveAll(currencies);

        currenciesMap.put(baseCurrencyCode, currencies.stream()
                .map(currency -> new CurrencyData(currency.getCurrencyCode(), currency.getExchangeRate()))
                .collect(Collectors.toList()));
    }

    public void setCurrenciesMap(Map<String, List<CurrencyData>> currenciesMap) {
        this.currenciesMap = currenciesMap;
    }
}
