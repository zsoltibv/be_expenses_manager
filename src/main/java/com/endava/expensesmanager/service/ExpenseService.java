package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.entity.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
     List<Expense> getExpensesByBeginDateAndEndDate(LocalDate beginDate, LocalDate endDate, Integer userId);
    List<Expense> getExpensesByUserId( Integer userId);
}
