package com.endava.expensesmanager.transformer.impl;
import com.endava.expensesmanager.transformer.DataTransformer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateDataTransformer implements DataTransformer<LocalDate> {
    DateTimeFormatter formatter;
    public DateDataTransformer(String dateFormat) {
        this.formatter = DateTimeFormatter.ofPattern(dateFormat);
    }
    @Override
    public boolean isValidData(String dateStr) {
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    @Override
    public LocalDate getData(String dateStr) {
        if (isValidData(dateStr)) {
            return LocalDate.parse(dateStr, formatter);
        }
        return null;
    }
}