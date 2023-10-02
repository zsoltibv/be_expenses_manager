package com.endava.expensesmanager.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends Exception {
    private final String field;
    public NotFoundException(String field, String message) {
        super(message);
        this.field = field;
    }
}
