package com.endava.expensesmanager.service;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentService {
    ExpenseDto processFileUpload(MultipartFile file, ExpenseDto expenseDto) throws IOException;
}
