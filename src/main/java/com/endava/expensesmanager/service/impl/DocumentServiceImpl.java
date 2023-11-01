package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.DocumentNotFoundException;
import com.endava.expensesmanager.exception.FileNotFoundException;
import com.endava.expensesmanager.exception.FileSizeExceededException;
import com.endava.expensesmanager.exception.InvalidImageFormatException;
import com.endava.expensesmanager.model.entity.Document;
import com.endava.expensesmanager.repository.DocumentRepository;
import com.endava.expensesmanager.service.AzureBlobService;
import com.endava.expensesmanager.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final Integer MAX_SIZE_IN_MB = 5;
    private final AzureBlobService azureBlobService;
    private final DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository, AzureBlobService documentBlobService) {
        this.documentRepository = documentRepository;
        this.azureBlobService = documentBlobService;
    }

    @Override
    public Integer addDocumentAndGetId(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidImageFormatException(contentType);
        }

        if (file.getSize() > MAX_SIZE_IN_MB * 1024 * 1024) {
            throw new FileSizeExceededException(MAX_SIZE_IN_MB);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        azureBlobService.storeFile(fileName, file.getInputStream(), file.getSize());

        return documentRepository.save(new Document(fileName)).getDocumentId();
    }

    @Override
    public void deleteDocumentById(Integer documentId) {
        Optional<Document> document = documentRepository.findById(documentId);

        if (document.isPresent()) {
            azureBlobService.deleteFile(document.get().getName());
            documentRepository.deleteById(documentId);
        } else {
            throw new DocumentNotFoundException(documentId);
        }
    }

    @Override
    public String downloadDocumentById(Integer documentId) {
        Optional<Document> document = documentRepository.findById(documentId);

        if (document.isPresent()) {
            return azureBlobService.downloadFile(document.get().getName());
        }
        throw new DocumentNotFoundException(documentId);
    }
}
