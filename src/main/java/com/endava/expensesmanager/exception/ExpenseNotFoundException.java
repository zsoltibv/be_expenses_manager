package com.endava.expensesmanager.exception;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException(Integer expenseId) {
        super("Expense with ID " + expenseId + " not found.");
    }
}
