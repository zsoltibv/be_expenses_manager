package com.endava.expensesmanager.service;

import java.io.InputStream;

public interface DocumentBlobService {
    void storeFile(String filename, InputStream content, Long length);

    void deleteFile(String filename);

    void downloadFile(String filename);
}
