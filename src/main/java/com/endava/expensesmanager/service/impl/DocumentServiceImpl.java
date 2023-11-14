package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.DocumentNotFoundException;
import com.endava.expensesmanager.exception.FileSizeExceededException;
import com.endava.expensesmanager.exception.InvalidImageFormatException;
import com.endava.expensesmanager.model.dto.ExpenseDto;
import com.endava.expensesmanager.model.entity.Document;
import com.endava.expensesmanager.repository.DocumentRepository;
import com.endava.expensesmanager.service.AzureBlobService;
import com.endava.expensesmanager.service.DocumentService;
import jakarta.transaction.Transactional;
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
    public void deleteDocument(Document document) {
        if (document != null) {
            azureBlobService.deleteFile(document.getName());
            documentRepository.delete(document);
        } else {
            throw new DocumentNotFoundException(document.getDocumentId());
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

    @Override
    public Document getDocumentById(Integer documentId) {
        return documentRepository.getReferenceById(documentId);
    }

    @Override
    public Integer editDocumentAndGetId(ExpenseDto expenseDto, MultipartFile file) throws IOException {
        if (expenseDto.getDocumentId() != null) {
            Integer documentId = expenseDto.getDocumentId();
            Optional<Document> existingDocument = documentRepository.findById(documentId);

            if (existingDocument.isPresent()) {
                return addDocumentAndGetId(file);
            }
            throw new DocumentNotFoundException(documentId);
        }

        return addDocumentAndGetId(file);
    }
}
