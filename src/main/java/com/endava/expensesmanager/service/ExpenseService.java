package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {
        Expense addExpense(ExpenseDto expense);
        List<ExpenseDto> getExpensesByUserId(Integer userId);
}
