package com.endava.expensesmanager.blob;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties("azure.document.blob")
public class AzureBlobProperties {
    private String containerName;
    private String accountName;
    private String accountKey;
    private String defaultProtocol;
    private String endpointSuffix;
    public String getConnectionString() {
        return String.format("DefaultEndpointsProtocol=%s;AccountName=%s;AccountKey=%s;EndpointSuffix=%s",
                defaultProtocol, accountName, accountKey, endpointSuffix);
    }
}
