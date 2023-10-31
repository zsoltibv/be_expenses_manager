package com.endava.expensesmanager.exception.handler;


import com.endava.expensesmanager.exception.*;
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorSingle> handleMethodArgumentNotValidException(IllegalArgumentException e,
                                                                          ServletWebRequest request) {
        ApiErrorSingle apiError = new ApiErrorSingle(
                request.getRequest().getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ExpenseNotFoundException.class, UserNotFoundException.class, FileNotFoundException.class})
    public ResponseEntity<ApiErrorSingle> handleNotFoundExceptions(Exception e, ServletWebRequest request) {

        ApiErrorSingle apiError = new ApiErrorSingle(
                request.getRequest().getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidImageFormatException.class, FileSizeExceededException.class})
    public ResponseEntity<ApiErrorSingle> handleImageUploadExceptions(Exception e, ServletWebRequest request) {

        ApiErrorSingle apiError = new ApiErrorSingle(
                request.getRequest().getRequestURI(),
                e.getMessage(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}

