package com.endava.expensesmanager.exception.handler;

import com.endava.expensesmanager.exception.NotFoundException;
import com.endava.expensesmanager.exception.response.ApiError;
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
    public ResponseEntity<ApiError> handleInvalidArgumentException(MethodArgumentNotValidException e,
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e,
                                                            ServletWebRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(e.getField(), e.getMessage());

        ApiError apiError = new ApiError(
                request.getRequest().getRequestURI(),
                errorMap,
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}

