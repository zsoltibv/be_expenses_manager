package com.endava.expensesmanager.exception.response;

import java.time.LocalDateTime;

public record ApiErrorSingle(
        String path,
        String error,
        int statusCode,
        LocalDateTime localDateTime) {
}