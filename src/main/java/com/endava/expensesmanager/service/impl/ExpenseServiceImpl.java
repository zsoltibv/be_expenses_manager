package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.CategoryNotFoundException;
import com.endava.expensesmanager.exception.CurrencyNotFoundException;
import com.endava.expensesmanager.exception.ExpenseNotFoundException;
import com.endava.expensesmanager.exception.UserNotFoundException;
import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Currency;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.model.entity.User;
import com.endava.expensesmanager.model.mapper.ExpenseMapper;
import com.endava.expensesmanager.repository.CategoryRepository;
import com.endava.expensesmanager.repository.CurrencyRepository;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.repository.UserRepository;
import com.endava.expensesmanager.generator.ExpenseGenerator;
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyRepository currencyRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository, CurrencyRepository currencyRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public void addExpense(ExpenseDto expenseDto) {
        if (!userRepository.existsById(expenseDto.getUserId())) {
            throw new UserNotFoundException(expenseDto.getUserId());
        }

        if (!categoryRepository.existsById(expenseDto.getCategoryId())) {
            throw new CategoryNotFoundException(expenseDto.getCategoryId());
        }

        if (!currencyRepository.existsById(expenseDto.getCurrencyId())) {
            throw new CurrencyNotFoundException(expenseDto.getCurrencyId());
        }

        expenseRepository.save(ExpenseMapper.toExpense(expenseDto));
    }

    @Override
    public void editExpense(Integer expenseId, ExpenseDto expenseDto) {
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(expenseId));

        User user = userRepository.findById(expenseDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(expenseDto.getUserId()));

        Category category = categoryRepository.findById(expenseDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(expenseDto.getCategoryId()));

        Currency currency = currencyRepository.findById(expenseDto.getCurrencyId())
                .orElseThrow(() -> new CurrencyNotFoundException(expenseDto.getCurrencyId()));

        Expense updatedExpense = ExpenseMapper.toUpdatedExpense(existingExpense, expenseDto, user, category, currency);

        expenseRepository.save(updatedExpense);
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

    @Override
    public void seedExpenses(Integer nrOfExpenses, Integer nrOfDays) {
        expenseRepository.deleteAll();

        nrOfExpenses = Optional.ofNullable(nrOfExpenses)
                .filter(n -> n <= 2000)
                .orElse(200);

        nrOfDays = Optional.ofNullable(nrOfDays)
                .filter(n -> n <= 540)
                .orElse(90);

        List<Expense> expensesList = new ArrayList<>();
        for (int i = 0; i < nrOfExpenses; i++) {
            expensesList.add(ExpenseGenerator.generateFakeExpense(nrOfDays));
        }

        expenseRepository.saveAll(expensesList);
    }
}
