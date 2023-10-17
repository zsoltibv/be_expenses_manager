package com.endava.expensesmanager.service;

import com.azure.storage.blob.BlobContainerClient;

import java.io.InputStream;

public interface DocumentBlobService {
    BlobContainerClient containerClient();
    void storeFile(String filename, InputStream content, Long length);
}
