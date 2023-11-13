package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentService {
    Integer addDocumentAndGetId(MultipartFile file) throws IOException;

    void deleteDocument(Document document);

    Integer editDocumentAndGetId(ExpenseDto expenseDto, MultipartFile file) throws IOException;

    String downloadDocumentById(Integer documentId);

    Document getDocumentById(Integer documentId);
}
