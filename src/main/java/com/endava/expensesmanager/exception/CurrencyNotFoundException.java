package com.endava.expensesmanager.exception;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(Integer currencyId) {
        super("Currency with ID " + currencyId + " not found.");
    }
}
