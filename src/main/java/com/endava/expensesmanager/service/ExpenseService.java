package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Expense;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;


public interface ExpenseService {
        void addExpense(ExpenseDto expenseDto);
        void editExpense(Integer expenseId, ExpenseDto expenseDto);
        List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate);
        void seedExpenses(Integer nrOfExpenses, Integer nrOfDays);
        ResponseEntity<?> deleteExpenseById(Integer expenseId);
}
