package com.endava.expensesmanager.exception;

import jdk.jfr.ContentType;

public class InvalidImageFormatException extends RuntimeException{
    public InvalidImageFormatException(String contentType) {
        super("Invalid file format: " + contentType + ". Only image files are allowed.");
    }
}
