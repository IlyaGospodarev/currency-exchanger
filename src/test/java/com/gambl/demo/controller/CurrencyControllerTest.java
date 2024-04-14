package com.gambl.demo.controller;

import com.gambl.demo.domain.model.CurrencyData;
import com.gambl.demo.domain.request.CurrencyRequest;
import com.gambl.demo.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    @Mock
    CurrencyService currencyService;

    @Test
    void getAllCurrencies() {
        Set<String> mockCurrencies = new HashSet<>();
        mockCurrencies.add("USD");

        when(currencyService.getAllCurrencies()).thenReturn(mockCurrencies);

        CurrencyController controller = new CurrencyController(currencyService);

        ResponseEntity<Set<String>> response = controller.getAllCurrencies();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockCurrencies, response.getBody());
    }

    @Test
    void getExchangeRates() {
        String baseCurrency = "USD";

        List<CurrencyData> mockExchangeRates = new ArrayList<>();
        mockExchangeRates.add(new CurrencyData("EUR", BigDecimal.valueOf(0.93)));
        mockExchangeRates.add(new CurrencyData("GBP", BigDecimal.valueOf(0.81)));

        when(currencyService.getExchangeRates(baseCurrency)).thenReturn(mockExchangeRates);

        CurrencyController controller = new CurrencyController(currencyService);

        ResponseEntity<List<CurrencyData>> response = controller.getExchangeRates(baseCurrency);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockExchangeRates, response.getBody());
    }

    @Test
    void addCurrency() {
        CurrencyController controller = new CurrencyController(currencyService);

        CurrencyRequest currencyRequest = new CurrencyRequest("USD");
        controller.addCurrency(currencyRequest);

        verify(currencyService).addCurrency(currencyRequest.getBaseCurrencyCode());
    }
}