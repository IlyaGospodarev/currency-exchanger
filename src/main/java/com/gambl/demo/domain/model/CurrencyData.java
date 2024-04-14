package com.gambl.demo.domain.model;

import java.math.BigDecimal;

public record CurrencyData(
        String currencyCode,
        BigDecimal exchangeRate
) {
}
