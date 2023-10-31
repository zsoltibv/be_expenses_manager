package com.endava.expensesmanager.exception;

public class FileNotFoundException extends RuntimeException{
    public FileNotFoundException(String filename) {
        super("File with name " + filename + " not found.");
    }
}
