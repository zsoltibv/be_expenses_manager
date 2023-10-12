package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.service.CurrencyService;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TotalServiceImpl {
    private CurrencyService currencyService;
    private ExpenseService expenseService;

    public TotalServiceImpl(CurrencyService currencyService, ExpenseService expenseService) {
        this.currencyService = currencyService;
        this.expenseService = expenseService;
    }

    public BigDecimal totalExpenseSum(int userId, LocalDate startDate, LocalDate endDate, String code)
    {
        List<ExpenseDto> expenses = currencyService.changeCurrencyTo(code, expenseService.getExpensesByBeginDateAndEndDate(startDate, endDate, userId));
        BigDecimal sum = BigDecimal.ZERO;
        for (ExpenseDto expens : expenses) {
            sum = sum.add(expens.getAmount());
        }
        return sum;
    }
    public Map<String,BigDecimal> totalExpenseCategory(int userId,LocalDate startDate,LocalDate endDate,String code)
    {
        List<ExpenseDto> expenses = currencyService.changeCurrencyTo(code, expenseService.getExpensesByBeginDateAndEndDate(startDate, endDate, userId));


        return expenseService.sortExpenses(expenses);
    }

}
