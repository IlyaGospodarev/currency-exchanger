package com.gambl.demo.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@IdClass(CurrencyId.class)
@Table(name = "currency")
public class CurrencyEntity {
    @Id
    private String baseCurrencyCode;
    @Id
    private String currencyCode;
    private BigDecimal exchangeRate;

    public CurrencyEntity() {
    }

    public CurrencyEntity(String baseCurrencyCode, String currencyCode, BigDecimal exchangeRate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
    }

    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyEntity that = (CurrencyEntity) o;
        return Objects.equals(baseCurrencyCode, that.baseCurrencyCode) && Objects.equals(currencyCode, that.currencyCode) && Objects.equals(exchangeRate, that.exchangeRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrencyCode, currencyCode, exchangeRate);
    }
}
