package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.CurrencyDto;
import com.endava.expensesmanager.model.dto.ExpenseDto;

import java.util.List;

public interface CurrencyService {
    List<ExpenseDto> changeCurrencyTo(String code, List<ExpenseDto> expenseList);

    List<CurrencyDto> getCurrencies();
}
