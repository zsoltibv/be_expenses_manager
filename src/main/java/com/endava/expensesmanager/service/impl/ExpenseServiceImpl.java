package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.dto.ExchangeRatesDto;
import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
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
    public List<ExpenseDto> getExpensesByUserId(Integer userId, LocalDate startDate, LocalDate endDate) {

        Stream<Expense> expensesStream = expenseRepository.findAll().stream()
                .filter(expense -> expense.getUser().getUserId().equals(userId));

        if (startDate != null) {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().toLocalDate().isAfter(startDate)
                    || expense.getExpenseDate().toLocalDate().isEqual(startDate));
        }

        if (endDate != null) {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().toLocalDate().isBefore(endDate)
                    || expense.getExpenseDate().toLocalDate().isEqual(endDate));
        } else {
            expensesStream = expensesStream.filter(expense -> expense.getExpenseDate().toLocalDate().isBefore(LocalDate.now())
                    || expense.getExpenseDate().toLocalDate().isEqual(LocalDate.now()));
        }

        return expensesStream
                .map(ExpenseMapper::toDto)
                .toList();

    }

    public List<Expense> getExpensesByBeginDateAndEndDate(LocalDate beginDate, LocalDate endDate, Integer userId) {
        return expenseRepository.findExpensesBetweenDatesForUser(beginDate.atStartOfDay(), endDate.atStartOfDay(), userId);
    }

    @Override
    public List<Expense> getExpensesByDates(LocalDate beginDate, LocalDate endDate, Integer userId) {
        return this.getExpensesByBeginDateAndEndDate(beginDate, endDate, userId);
    }

    public Map<String, BigDecimal> sortExpenses(List<Expense> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getDescription(),
                        Collectors.mapping(Expense::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }
}
