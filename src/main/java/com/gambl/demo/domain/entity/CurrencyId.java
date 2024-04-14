package com.gambl.demo.domain.entity;

import java.io.Serializable;
import java.util.Objects;

public class CurrencyId implements Serializable {
    private String baseCurrencyCode;
    private String currencyCode;

    public CurrencyId() {
    }

    public CurrencyId(String baseCurrencyCode, String currencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyId that = (CurrencyId) o;
        return Objects.equals(baseCurrencyCode, that.baseCurrencyCode) && Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrencyCode, currencyCode);
    }
}
