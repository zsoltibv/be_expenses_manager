package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.entity.Expense;

import java.util.List;

public interface CurrencyService {
    public List<Expense> changeCurrencyTo(String code, List<Expense> expenseList);
    public List<String> getCurrencies();
 }
