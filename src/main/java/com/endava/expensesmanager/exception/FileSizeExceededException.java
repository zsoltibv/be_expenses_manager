package com.endava.expensesmanager.exception;

public class FileSizeExceededException extends RuntimeException {
    public FileSizeExceededException(Integer sizeInMB) {
        super("File size limit exceeded. Maximum allowed size is " + sizeInMB + " MB");
    }
}
