package com.endava.expensesmanager.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        String path,
        Map<String, String> errors,
        int statusCode,
        LocalDateTime localDateTime) {
}
