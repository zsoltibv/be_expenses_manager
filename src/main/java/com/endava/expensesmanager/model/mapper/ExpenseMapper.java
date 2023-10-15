package com.endava.expensesmanager.model.mapper;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.*;

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

        if (expenseDto.getDocumentId() != null) {
            Document document = new Document();
            document.setDocumentId(expenseDto.getDocumentId());
            expense.setDocument(document);
        }

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
        expenseDto.setDocumentId(expense.getDocument().getDocumentId());

        return expenseDto;
    }

    public static Expense toUpdatedExpense(Expense existingExpense, ExpenseDto expenseDto, User user, Category category, Currency currency) {

        existingExpense.setDescription(expenseDto.getDescription());
        existingExpense.setAmount(expenseDto.getAmount());
        existingExpense.setExpenseDate(expenseDto.getExpenseDate());

        existingExpense.setUser(user);
        existingExpense.setCategory(category);
        existingExpense.setCurrency(currency);

        return existingExpense;
    }
}
