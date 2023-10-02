package com.endava.expensesmanager.service;

import com.endava.expensesmanager.exception.NotFoundException;
import com.endava.expensesmanager.model.dto.ExpenseDto;

import java.time.LocalDate;
import java.util.List;


public interface ExpenseService {
        void addExpense(ExpenseDto expenseDto) throws NotFoundException;
        void editExpense(Integer expenseId, ExpenseDto expenseDto) throws NotFoundException;
        List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate);
}
