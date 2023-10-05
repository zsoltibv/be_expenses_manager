package com.endava.expensesmanager.service;

import com.endava.expensesmanager.exception.CategoryNotFoundException;
import com.endava.expensesmanager.exception.CurrencyNotFoundException;
import com.endava.expensesmanager.exception.ExpenseNotFoundException;
import com.endava.expensesmanager.exception.UserNotFoundException;
import com.endava.expensesmanager.model.dto.ExpenseDto;

import java.time.LocalDate;
import java.util.List;


public interface ExpenseService {
        void addExpense(ExpenseDto expenseDto) throws RuntimeException;
        void editExpense(Integer expenseId, ExpenseDto expenseDto) throws RuntimeException;
        List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate);
}
