package com.endava.expensesmanager.exception;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(Integer documentId) {
        super("Document with ID " + documentId + " not found.");
    }

}
