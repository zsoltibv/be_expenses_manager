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
    private String connectionString;
}
