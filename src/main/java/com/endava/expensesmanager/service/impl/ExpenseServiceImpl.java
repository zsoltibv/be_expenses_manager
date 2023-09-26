package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense addExpense(ExpenseDto expense) {
        return expenseRepository.save(ExpenseMapper.toExpense(expense));
    }

    @Override
    public List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDateTime startDate, LocalDateTime endDate){

        return ExpenseMapper.toDtoList(expenseRepository.findAll().stream()
                .filter(expense -> expense.getUser().getUserId() == userId)
                .filter(expense -> startDate == null || expense.getExpenseDate().isAfter(startDate) || expense.getExpenseDate().isEqual(startDate))
                .filter(expense -> endDate == null || expense.getExpenseDate().isBefore(endDate)  || expense.getExpenseDate().isEqual(endDate))
                .toList());
    }
}
