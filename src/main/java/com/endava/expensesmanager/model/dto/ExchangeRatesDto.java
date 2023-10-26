package com.endava.expensesmanager.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.HashMap;

public class ExchangeRatesDto {
    private String result;
    @JsonProperty("conversion_rates")
    private HashMap<String,BigDecimal> conversionRates;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HashMap<String, BigDecimal> getConversionRates() {
        return conversionRates;
    }

    public void setConversionRates(HashMap<String, BigDecimal> conversionRates) {
        this.conversionRates = conversionRates;
    }
}
