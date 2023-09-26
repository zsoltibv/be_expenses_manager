package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;


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
    public List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate){

        Stream<Expense> expensesStream = expenseRepository.findAll().stream()
                .filter(expense -> expense.getUser().getUserId().equals(userId));

        if(startDate != null){
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().toLocalDate().isAfter(startDate)
                    || expense.getExpenseDate().toLocalDate().isEqual(startDate));
        }

        if(endDate != null){
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().toLocalDate().isBefore(endDate)
                    || expense.getExpenseDate().toLocalDate().isEqual(endDate));
        }
        else {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().toLocalDate().isBefore(LocalDate.now())
                    || expense.getExpenseDate().toLocalDate().isEqual(LocalDate.now()));
        }

        return expensesStream
                .map(ExpenseMapper::toDto)
                .toList();
    }
}
