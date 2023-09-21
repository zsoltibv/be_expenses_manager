package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.Expense;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseService {
    public List<Expense> getExpensesByBeginDateAndEndDate(LocalDateTime beginDate,LocalDateTime endDate,Integer userId);
}
