package com.endava.expensesmanager.exception;

public class FileExistsException  extends RuntimeException {
    public FileExistsException(String filename) {
        super("File with name " + filename + " already exists.");
    }
}
