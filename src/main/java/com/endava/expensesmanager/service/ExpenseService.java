package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExpenseService {
    Expense addExpense(ExpenseDto expense);
    List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate);
    List<Expense> getExpensesByBeginDateAndEndDate(LocalDate beginDate, LocalDate endDate, Integer userId);
    List<Expense> getExpensesByDates(LocalDate beginDate, LocalDate endDate, Integer userId);
    Map<String, BigDecimal> sortExpenses(List<Expense> expenses);
    List<List<Expense>> getExpensesByBeginDateAndEndDateSortedBy(LocalDate beginDate, LocalDate endDate, Integer userId);


}
