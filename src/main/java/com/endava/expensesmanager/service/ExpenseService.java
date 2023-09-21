package com.endava.expensesmanager.service;


import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Expense;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;



public interface ExpenseService {
    Expense addExpense(ExpenseDto expense);

    List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate);

    List<Expense> getExpensesByBeginDateAndEndDate(LocalDateTime beginDate, LocalDateTime endDate, Integer userId);

}
