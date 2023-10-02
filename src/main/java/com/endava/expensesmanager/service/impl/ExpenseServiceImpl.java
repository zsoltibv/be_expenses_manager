package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.NotFoundException;
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
import com.endava.expensesmanager.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
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
    public void addExpense(ExpenseDto expenseDto) throws NotFoundException {
        if (!userRepository.existsById(expenseDto.getUserId())) {
            throw new NotFoundException("userId", "User with ID " + expenseDto.getUserId() + " not found");
        } else if (!categoryRepository.existsById(expenseDto.getCategoryId())) {
            throw new NotFoundException("categoryId", "Category with ID " + expenseDto.getCategoryId() + " not found");
        } else if (!currencyRepository.existsById(expenseDto.getCurrencyId())) {
            throw new NotFoundException("currencyId", "Currency with ID " + expenseDto.getCurrencyId() + " not found");
        }

        expenseRepository.save(ExpenseMapper.toExpense(expenseDto));
    }

    @Override
    public void editExpense(Integer expenseId, ExpenseDto expenseDto) throws NotFoundException {
        Optional<Expense> existingExpense = expenseRepository.findById(expenseId);

        if (existingExpense.isPresent()) {
            Expense existingExpenseEntity = existingExpense.get();
            existingExpenseEntity.setDescription(expenseDto.getDescription());
            existingExpenseEntity.setAmount(expenseDto.getAmount());
            existingExpenseEntity.setExpenseDate(expenseDto.getExpenseDate());

            User user = userRepository.findById(expenseDto.getUserId())
                    .orElseThrow(() -> new NotFoundException("userId", "User with ID " + expenseDto.getUserId() + " not found"));
            existingExpenseEntity.setUser(user);

            Category category = categoryRepository.findById(expenseDto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("categoryId", "Category with ID " + expenseDto.getCategoryId() + " not found"));
            existingExpenseEntity.setCategory(category);

            Currency currency = currencyRepository.findById(expenseDto.getCurrencyId())
                    .orElseThrow(() -> new NotFoundException("currencyId", "Currency with ID " + expenseDto.getCurrencyId() + " not found"));
            existingExpenseEntity.setCurrency(currency);

            expenseRepository.save(existingExpenseEntity);
        } else {
            throw new NotFoundException("expenseId", "Expense with ID " + expenseId + " not found.");
        }
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
}
