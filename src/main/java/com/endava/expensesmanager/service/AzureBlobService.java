package com.endava.expensesmanager.service;

import java.io.InputStream;

public interface AzureBlobService {
    void storeFile(String filename, InputStream content, Long length);

    void deleteFile(String filename);

    String downloadFile(String filename);
}
