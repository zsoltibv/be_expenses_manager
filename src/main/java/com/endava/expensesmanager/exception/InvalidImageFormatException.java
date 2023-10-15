package com.endava.expensesmanager.exception;

public class InvalidImageFormatException extends RuntimeException{
    public InvalidImageFormatException(String contentType) {
        super("Invalid file format: " + contentType + ". Only image files are allowed.");
    }
}
