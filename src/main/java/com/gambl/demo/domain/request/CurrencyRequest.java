package com.gambl.demo.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public class CurrencyRequest {
    @Schema(example = "USD")
    private String baseCurrencyCode;

    public CurrencyRequest() {
    }

    public CurrencyRequest(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyRequest that = (CurrencyRequest) o;
        return Objects.equals(baseCurrencyCode, that.baseCurrencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrencyCode);
    }

    @Override
    public String toString() {
        return "CurrencyRequest{" +
                "baseCurrencyCode='" + baseCurrencyCode + '\'' +
                '}';
    }
}
