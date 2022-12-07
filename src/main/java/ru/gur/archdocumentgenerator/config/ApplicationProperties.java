package ru.gur.archdocumentgenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    @NotNull
    private Minio minio;

    @Data
    public static class Minio {

        @NotBlank
        private String accessKey;

        @NotBlank
        private String secretKey;

        @NotBlank
        private String url;

        @NotBlank
        private String bucketName;
    }
}
