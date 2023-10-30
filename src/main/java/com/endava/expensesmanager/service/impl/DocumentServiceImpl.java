package com.endava.expensesmanager.service.impl;

import com.endava.expensesmanager.exception.FileSizeExceededException;
import com.endava.expensesmanager.exception.InvalidImageFormatException;
import com.endava.expensesmanager.model.entity.Document;
import com.endava.expensesmanager.repository.DocumentRepository;
import com.endava.expensesmanager.service.DocumentBlobService;
import com.endava.expensesmanager.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
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
        validateDocument(file);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        documentBlobService.storeFile(fileName, file.getInputStream(), file.getSize());

        return documentRepository.save(new Document(fileName)).getDocumentId();
    }

    @Override
    public void deleteDocumentById(Integer documentId) {
        Optional<Document> document = documentRepository.findById(documentId);

        if (document.isPresent()) {
            documentBlobService.deleteFile(document.get().getName());
            documentRepository.deleteById(documentId);
        }
    }

    @Override
    public Integer editDocumentAndGetId(Integer documentId, MultipartFile file) {
        Optional<Document> existingDocument = documentRepository.findById(documentId);

        if (existingDocument.isPresent()) {
            Document document = existingDocument.get();
            documentBlobService.deleteFile(document.getName());

            validateDocument(file);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            documentBlobService.storeFile(fileName, file.getInputStream(), file.getSize());

            document.setName(fileName);
            documentRepository.save(document);
            return
        }
        throw new DocumentNotFoundException(documentId);
    }

    private void validateDocument(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidImageFormatException(contentType);
        }

        if (file.getSize() > MAX_SIZE_IN_MB * 1024 * 1024) {
            throw new FileSizeExceededException(MAX_SIZE_IN_MB);
        }
    }
}
