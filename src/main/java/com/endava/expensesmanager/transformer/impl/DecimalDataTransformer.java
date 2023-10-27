package com.endava.expensesmanager.transformer.impl;

import com.endava.expensesmanager.transformer.DataTransformer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

public class DecimalDataTransformer implements DataTransformer<BigDecimal> {
    private final DecimalFormat formatter;
    public DecimalDataTransformer(String decimalFormat) {
        this.formatter = new DecimalFormat(decimalFormat);
    }
    @Override
    public boolean isValidData(String decimalStr) {
        try {
            formatter.parse(decimalStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    @Override
    public BigDecimal getData(String decimalStr) {
        if(!decimalStr.isEmpty()) {
            decimalStr = decimalStr.substring(1);
        }

        try {
            return BigDecimal.valueOf(formatter.parse(decimalStr).doubleValue());
        } catch (ParseException e) {
            return BigDecimal.ZERO;
        }
    }
}
