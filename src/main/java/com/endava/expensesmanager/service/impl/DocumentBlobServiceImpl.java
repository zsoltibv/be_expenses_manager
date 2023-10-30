package com.endava.expensesmanager.service.impl;

import com.azure.storage.blob.*;
import com.endava.expensesmanager.blob.AzureBlobProperties;
import com.endava.expensesmanager.exception.FileExistsException;
import com.endava.expensesmanager.exception.FileNotFoundException;
import com.endava.expensesmanager.service.DocumentBlobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class DocumentBlobServiceImpl implements DocumentBlobService {
    private final AzureBlobProperties azureBlobProperties;

    public BlobContainerClient containerClient(){
        return new BlobContainerClientBuilder()
                .endpoint(azureBlobProperties.getConnectionString())
                .buildClient();
    }

    @Override
    public void storeFile(String filename, InputStream content, Long length){
        BlobClient client = containerClient().getBlobClient(filename);

        if(client.exists()){
            throw new FileExistsException(filename);
        }
        client.upload(content, length);
    }

    @Override
    public void deleteFile(String filename) {
        BlobClient client = containerClient().getBlobClient(filename);

        if(!client.exists()){
            throw new FileNotFoundException(filename);
        }
        client.delete();
    }

    @Override
    public void downloadFile(String filename) {
        BlobClient client = containerClient().getBlobClient(filename);

        if(!client.exists()){
            throw new FileNotFoundException(filename);
        }
        client.downloadContent();
    }
}
