package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Document;
import com.endava.expensesmanager.repository.DocumentRepository;
import com.endava.expensesmanager.service.DocumentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Value("${document.upload.directory}")
    private String uploadDirectory;
    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public ExpenseDto processFileUpload(MultipartFile file, ExpenseDto expenseDto) throws IOException {
        Path uploadDirectoryPath = Path.of(uploadDirectory);
        Files.createDirectories(uploadDirectoryPath);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetPath = Path.of(uploadDirectory, fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        Document document = new Document();
        document.setPath(fileName);
        documentRepository.save(document);

        expenseDto.setDocumentId(document.getDocumentId());
        return expenseDto;
    }
}
