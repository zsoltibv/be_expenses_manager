package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.FileSizeExceededException;
import com.endava.expensesmanager.exception.InvalidImageFormatException;
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
    public Integer addDocumentAndGetId(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidImageFormatException(contentType);
        }

        long maxSize = 5 * 1024 * 1024; // 5MB
        if (file.getSize() > maxSize) {
            throw new FileSizeExceededException(5);
        }

        Path uploadDirectoryPath = Path.of(uploadDirectory);
        Files.createDirectories(uploadDirectoryPath);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetPath = Path.of(uploadDirectory, fileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        Document document = new Document();
        document.setName(fileName);
        documentRepository.save(document);
        return document.getDocumentId();
    }
}
