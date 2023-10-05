package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.entity.Currency;
import com.endava.expensesmanager.model.entity.Expense;


import java.util.List;

public interface CurrencyService {
    List<Expense> changeCurrencyTo(String code, List<Expense> expenseList);
     List<Currency> getCurrencies();
 }
