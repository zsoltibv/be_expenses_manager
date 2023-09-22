package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    ExpenseRepository expenseRepository;
    @Override
    public List<Expense> getExpensesByBeginDateAndEndDate(LocalDate beginDate, LocalDate endDate, Integer userId) {
       return  expenseRepository.findExpensesBetweenDatesForUser(beginDate,endDate,userId);
    }

    @Override
    public List<Expense> getExpensesByUserId( Integer userId) {
        return expenseRepository.findByUserId(userId);
    }
}
