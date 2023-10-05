package com.endava.expensesmanager.exception.handler;

import com.endava.expensesmanager.exception.CategoryNotFoundException;
import com.endava.expensesmanager.exception.CurrencyNotFoundException;
import com.endava.expensesmanager.exception.ExpenseNotFoundException;
import com.endava.expensesmanager.exception.UserNotFoundException;
import com.endava.expensesmanager.exception.response.ApiError;
import com.endava.expensesmanager.exception.response.ApiErrorSingle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                   ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });

        ApiError apiError = new ApiError(
                request.getRequest().getRequestURI(),
                errorMap,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ApiErrorSingle> handleExpenseNotFoundException(ExpenseNotFoundException e,
                                                            ServletWebRequest request) {
        return getApiErrorResponseEntity(request, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiErrorSingle> handleCategoryNotFoundException(CategoryNotFoundException e,
                                                                   ServletWebRequest request) {
        return getApiErrorResponseEntity(request, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ApiErrorSingle> handleCurrencyNotFoundException(CurrencyNotFoundException e,
                                                                          ServletWebRequest request) {
        return getApiErrorResponseEntity(request, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorSingle> handleUserNotFoundException(UserNotFoundException e,
                                                                          ServletWebRequest request) {
        return getApiErrorResponseEntity(request, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiErrorSingle> getApiErrorResponseEntity(ServletWebRequest request, String message, HttpStatus status) {
        ApiErrorSingle apiError = new ApiErrorSingle(
                request.getRequest().getRequestURI(),
                message,
                status.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, status);
    }
}

