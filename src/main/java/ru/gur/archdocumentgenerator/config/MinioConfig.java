package ru.gur.archdocumentgenerator.config;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final ApplicationProperties applicationProperties;

    @Bean("configuredMinioClient")
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(applicationProperties.getMinio().getUrl())
                .credentials(applicationProperties.getMinio().getAccessKey(), applicationProperties.getMinio().getSecretKey())
                .build();
    }
}
