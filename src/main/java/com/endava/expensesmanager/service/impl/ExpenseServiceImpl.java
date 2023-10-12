package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.dto.ExchangeRatesDto;
import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.endava.expensesmanager.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository,CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository=categoryRepository;
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

    public List<ExpenseDto> getExpensesByBeginDateAndEndDate(LocalDate beginDate, LocalDate endDate, Integer userId) {
         List <Expense>  expenses=expenseRepository.findExpensesBetweenDatesForUser(beginDate.atStartOfDay(), endDate.atStartOfDay(), userId);
         List<ExpenseDto> expenseDto= new ArrayList<>();
         for(Expense expense:expenses)
         {
             expenseDto.add(ExpenseMapper.toDto(expense));
         }
         return expenseDto;
    }



    public Map<String, BigDecimal> sortExpenses(List<ExpenseDto> expenses) {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> categoryRepository.findById(expense.getCategoryId()).get().getDescription(),
                        Collectors.mapping(ExpenseDto::getAmount, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }
}
