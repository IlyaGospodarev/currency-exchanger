package com.gambl.demo.controller;

import com.gambl.demo.domain.model.CurrencyData;
import com.gambl.demo.domain.request.CurrencyRequest;
import com.gambl.demo.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Currency Controller")
@RestController
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(
            summary = "Get a list of currencies used in the project."
    )
    @GetMapping("/currencies")
    public ResponseEntity<Set<String>> getAllCurrencies() {
        Set<String> currencies = currencyService.getAllCurrencies();
        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    @Operation(
            summary = "Get exchange rates for a currency."
    )
    @GetMapping("/exchange-rates")
    public ResponseEntity<List<CurrencyData>> getExchangeRates(
            @Parameter(description = "Base Currency", example = "USD")
            @RequestParam String baseCurrency
    ) {
        List<CurrencyData> exchangeRates = currencyService.getExchangeRates(baseCurrency);
        return new ResponseEntity<>(exchangeRates, HttpStatus.OK);
    }

    @Operation(
            summary = "Add new currency for getting exchange rates."
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/currencies")
    public void addCurrency(@RequestBody CurrencyRequest currencyRequest) {
        currencyService.addCurrency(currencyRequest.getBaseCurrencyCode());
    }

}

