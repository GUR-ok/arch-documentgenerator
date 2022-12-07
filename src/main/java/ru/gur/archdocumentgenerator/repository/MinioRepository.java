package ru.gur.archdocumentgenerator.repository;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.gur.archdocumentgenerator.config.ApplicationProperties;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MinioRepository implements FileRepository {

    private final ApplicationProperties applicationProperties;
    private final MinioClient minioClient;

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @SneakyThrows
    @PostConstruct
    private void initializeBucket() {
        final String BUCKET_NAME = applicationProperties.getMinio().getBucketName();
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());
        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(BUCKET_NAME).build());
            log.info("New bucket '{}' created", BUCKET_NAME);
        }
    }

    @Override
    public void save(byte[] buffer, String fileName) {
        Assert.hasText(fileName, "fileName must not be empty");
        Assert.notNull(buffer, "buffer must not be null");

        log.info("{} saving.", fileName);
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(applicationProperties.getMinio().getBucketName())
                    .object(fileName)
                    .stream(new ByteArrayInputStream(buffer), buffer.length, -1)
                    .contentType("pdf")
                    .build();

            minioClient.putObject(objectArgs);
            log.info("{} saved.", fileName);
        } catch (Exception e) {
            log.error("Minio exception. ", e);
        }
    }

    @Override
    public void remove(String fileName) {
        Assert.hasText(fileName, "fileName must not be empty");

        try {
            RemoveObjectArgs objectArgs = RemoveObjectArgs.builder()
                    .bucket(applicationProperties.getMinio().getBucketName())
                    .object(fileName)
                    .build();

            minioClient.removeObject(objectArgs);
            log.info("{} removed.", fileName);
        } catch (Exception e) {
            log.error("Minio exception. ", e);
        }
    }

    @Override
    public byte[] find(String fileName) {
        Assert.hasText(fileName, "fileName must not be empty");

        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(applicationProperties.getMinio().getBucketName())
                .object(fileName)
                .build();

        try (InputStream inputStream = minioClient.getObject(objectArgs)) {

            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            log.error("Minio exception. ", e);
        }

        return new byte[0];
    }

    @Override
    public String getUrl(String name) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket("agreementbucket")
                    .object(name)
                    .expiry(2, TimeUnit.HOURS)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
