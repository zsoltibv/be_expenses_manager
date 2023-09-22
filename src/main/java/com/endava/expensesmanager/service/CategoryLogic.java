package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CategoryLogic {
    public List<Expense> getExpensesList(LocalDate startDate, LocalDate endDate, int userId);
    public Map<Integer, BigDecimal> sortExpenses(List<Expense> expenses, List<Category>categories);
}
