package com.endava.expensesmanager.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer userId) {
        super("User with ID " + userId + " not found.");
    }
}