package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentService {
    Integer addDocumentAndGetId(MultipartFile file) throws IOException;

    void deleteDocumentById(Integer documentId);

    Integer editDocumentAndGetId(ExpenseDto expenseDto, MultipartFile file) throws IOException;

    String downloadDocumentById(Integer documentId);
}
