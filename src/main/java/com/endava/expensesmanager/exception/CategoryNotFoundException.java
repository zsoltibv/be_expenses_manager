package com.endava.expensesmanager.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Integer categoryId) {
        super("Category with ID " + categoryId + " not found.");
    }
}