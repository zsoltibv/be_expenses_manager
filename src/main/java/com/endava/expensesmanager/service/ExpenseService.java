package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Expense;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;


public interface ExpenseService {
        Expense addExpense(ExpenseDto expense);
        List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate);
        ResponseEntity<?> deleteExpenseById(Integer expenseId);
}
