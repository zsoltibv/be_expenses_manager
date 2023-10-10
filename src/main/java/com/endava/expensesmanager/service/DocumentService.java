package com.endava.expensesmanager.service;

import com.endava.expensesmanager.exception.FileSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentService {
    Integer addDocumentAndGetId(MultipartFile file) throws IOException;
}
