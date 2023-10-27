package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.FileSizeExceededException;
import com.endava.expensesmanager.exception.InvalidImageFormatException;
import com.endava.expensesmanager.model.entity.Document;
import com.endava.expensesmanager.repository.DocumentRepository;
import com.endava.expensesmanager.service.DocumentBlobService;
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
    private final Integer MAX_SIZE_IN_MB = 5;
    private final DocumentBlobService documentBlobService;
    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentBlobService documentBlobService) {
        this.documentRepository = documentRepository;
        this.documentBlobService = documentBlobService;
    }

    @Override
    public Integer addDocumentAndGetId(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidImageFormatException(contentType);
        }

        long maxSize = MAX_SIZE_IN_MB * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new FileSizeExceededException(MAX_SIZE_IN_MB);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        documentBlobService.storeFile(fileName, file.getInputStream(), file.getSize());

        Document document = new Document();
        document.setName(fileName);
        documentRepository.save(document);
        return document.getDocumentId();
    }
}
