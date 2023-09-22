package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.service.CategoryLogic;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryLogicImpl implements CategoryLogic {
    private ExpenseService expenseService;
    @Autowired
    public void setExpenseService(ExpenseService expenseService)
    {
        this.expenseService=expenseService;
    }
    public List<Expense> getExpensesList(LocalDate startDate,LocalDate endDate,int userId)
    {
        if(startDate ==null)
        {
            return expenseService.getExpensesByUserId(userId);
        }
return expenseService.getExpensesByBeginDateAndEndDate(startDate,endDate,userId);
    }

    public Map<Integer,BigDecimal> sortExpenses(List<Expense> expenses,List<Category>categories){
    return expenses.stream()
            .filter(expense -> categories.stream()
            .anyMatch(category -> category.getCategoryId() == expense.getCategory().getCategoryId()))
            .collect(Collectors.groupingBy(
            expense -> expense.getCategory().getCategoryId(),
                        Collectors.mapping(Expense::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
            ));

    }
}
