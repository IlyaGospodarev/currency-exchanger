package com.gambl.demo.service;

import com.gambl.demo.domain.entity.CurrencyEntity;
import com.gambl.demo.domain.model.CurrencyData;
import com.gambl.demo.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private RestTemplate restTemplate;

    @Spy
    private Map<String, List<CurrencyData>> currenciesMap = new HashMap<>();

    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyService(currencyRepository, restTemplate);
        currencyService.setCurrenciesMap(currenciesMap);
        CurrencyData currencyData = new CurrencyData("USD", BigDecimal.ONE);
        currenciesMap.put("USD", new ArrayList<>(List.of(currencyData)));
    }

    @Test
    void initializeCurrenciesMapFromDatabase() {
        List<CurrencyEntity> currencies = new ArrayList<>();

        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setBaseCurrencyCode("USD");
        currencyEntity.setCurrencyCode("USD");
        currencyEntity.setExchangeRate(BigDecimal.ONE);

        currencies.add(currencyEntity);

        when(currencyRepository.findAll()).thenReturn(currencies);

        currencyService.initializeCurrenciesMapFromDatabase();

        Set<String> currenciesSet = currencyService.getAllCurrencies();
        assertEquals(1, currenciesSet.size());
        assertTrue(currenciesSet.contains("USD"));
    }

    @Test
    void getAllCurrencies() {
        Set<String> baseCurrencies = new HashSet<>();
        baseCurrencies.add("USD");

        Set<String> allCurrencies = currencyService.getAllCurrencies();

        assertEquals(baseCurrencies, allCurrencies);
    }

    @Test
    void getExchangeRates() {
        String baseCurrency = "USD";
        CurrencyData currencyData = new CurrencyData("USD", BigDecimal.ONE);

        List<CurrencyData> exchangeRates = currencyService.getExchangeRates(baseCurrency);

        assertEquals(List.of(currencyData), exchangeRates);
    }

    @Test
    void addCurrency() {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setBaseCurrencyCode("USD");
        currencyEntity.setCurrencyCode("USD");
        currencyEntity.setExchangeRate(BigDecimal.valueOf(1));

        currencyService.addCurrency("USD");

        verify(currencyRepository).save(currencyEntity);
        verify(currenciesMap).put("USD", List.of(new CurrencyData("USD", BigDecimal.ONE)));
    }

}