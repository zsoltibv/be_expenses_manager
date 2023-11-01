package com.endava.expensesmanager.controller;

import com.endava.expensesmanager.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<String> downloadDocument(@PathVariable Integer documentId) {
        String documentBlobUrl = documentService.downloadDocumentById(documentId);
        return new ResponseEntity<>(documentBlobUrl, HttpStatus.OK);
    }
}
