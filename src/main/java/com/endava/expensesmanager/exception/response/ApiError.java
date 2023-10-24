package com.endava.expensesmanager.exception.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        String path,
        Map<String, String> errors,
        int statusCode,
        LocalDateTime localDateTime) {
}
