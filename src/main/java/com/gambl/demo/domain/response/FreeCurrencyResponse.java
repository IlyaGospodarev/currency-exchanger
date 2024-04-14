package com.gambl.demo.domain.response;

import java.math.BigDecimal;
import java.util.Map;

public class FreeCurrencyResponse {
    private Map<String, BigDecimal> data;

    public Map<String, BigDecimal> getData() {
        return data;
    }

    public void setData(Map<String, BigDecimal> data) {
        this.data = data;
    }
}
