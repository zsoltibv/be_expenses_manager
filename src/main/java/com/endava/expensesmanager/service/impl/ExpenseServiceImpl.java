package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.Expense;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ExpenseServiceImpl implements ExpenseService {
    @Autowired
    ExpenseRepository expenseRepository;
    @Override
    public List<Expense> getExpensesByBeginDateAndEndDate(LocalDateTime beginDate, LocalDateTime endDate,Integer userId) {
       return  expenseRepository.findExpensesBetweenDatesForUser(beginDate,endDate,userId);
    }
}
