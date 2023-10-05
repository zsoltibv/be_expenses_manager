package com.endava.expensesmanager.model.mapper;

import com.endava.expensesmanager.exception.CategoryNotFoundException;
import com.endava.expensesmanager.exception.CurrencyNotFoundException;
import com.endava.expensesmanager.exception.ExpenseNotFoundException;
import com.endava.expensesmanager.exception.UserNotFoundException;
import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Category;
import com.endava.expensesmanager.model.entity.Currency;
import com.endava.expensesmanager.model.entity.Expense;
import com.endava.expensesmanager.model.entity.User;
import com.endava.expensesmanager.repository.CategoryRepository;
import com.endava.expensesmanager.repository.CurrencyRepository;
import com.endava.expensesmanager.repository.ExpenseRepository;
import com.endava.expensesmanager.repository.UserRepository;

import java.util.List;

public class ExpenseMapper {

    public static Expense toExpense(ExpenseDto expenseDto) {
        Expense expense = new Expense();
        expense.setDescription(expenseDto.getDescription());
        expense.setExpenseDate(expenseDto.getExpenseDate());
        expense.setAmount(expenseDto.getAmount());

        User user = new User();
        user.setUserId(expenseDto.getUserId());
        expense.setUser(user);

        Category category = new Category();
        category.setCategoryId(expenseDto.getCategoryId());
        expense.setCategory(category);

        Currency currency = new Currency();
        currency.setCurrencyId(expenseDto.getCurrencyId());
        expense.setCurrency(currency);

        return expense;
    }

    public static ExpenseDto toDto(Expense expense) {
        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setExpenseId(expense.getExpenseId());
        expenseDto.setDescription(expense.getDescription());
        expenseDto.setExpenseDate(expense.getExpenseDate());
        expenseDto.setAmount(expense.getAmount());
        expenseDto.setUserId(expense.getUser().getUserId());
        expenseDto.setCategoryId(expense.getCategory().getCategoryId());
        expenseDto.setCurrencyId(expense.getCurrency().getCurrencyId());

        return expenseDto;
    }

    public static Expense toUpdatedExpense(Expense existingExpense, ExpenseDto expenseDto, User user, Category category, Currency currency)
            throws ExpenseNotFoundException, UserNotFoundException, CategoryNotFoundException, CurrencyNotFoundException {

        existingExpense.setDescription(expenseDto.getDescription());
        existingExpense.setAmount(expenseDto.getAmount());
        existingExpense.setExpenseDate(expenseDto.getExpenseDate());

        existingExpense.setUser(user);
        existingExpense.setCategory(category);
        existingExpense.setCurrency(currency);

        return existingExpense;
    }
}
