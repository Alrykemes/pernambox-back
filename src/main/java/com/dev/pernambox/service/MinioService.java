package com.dev.pernambox.service;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MinioService {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioService(
            @Value("${MINIO_ENDPOINT}") String endpoint,
            @Value("${MINIO_ACCESS_KEY}") String accessKey,
            @Value("${MINIO_SECRET_KEY}") String secretKey,
            @Value("${MINIO_BUCKET}") String bucket
    ) throws MinioException {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucket = bucket;

        inicializarBucket();
    }

    /**
     * Cria o bucket se ele não existir
     */
    private void inicializarBucket() throws MinioException {
        try {
            boolean existe = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );

            if (!existe) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucket).build()
                );
                log.info("[MinIO] Bucket criado: {}", bucket);
                // Define política de acesso privada
                String privatePolicy = """
                        {
                          "Version": "2012-10-17",
                          "Statement": [
                            {
                              "Effect": "Deny",
                              "Principal": "*",
                              "Action": ["s3:GetObject"],
                              "Resource": ["arn:aws:s3:::%s/*"]
                            }
                          ]
                        }
                        """.formatted(bucket);

                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder()
                                .bucket(bucket)
                                .config(privatePolicy)
                                .build()
                );
                log.info("[MinIO] Política privada aplicada ao bucket: {}", bucket);
            } else {
                log.info("[MinIO] Bucket já existente: {}", bucket);
            }
        } catch (Exception e) {
            log.info("Falha ao inicializar bucket MinIO: {}", e.getMessage());
            throw new MinioException("Falha ao inicializar bucket MinIO: " + e.getMessage());
        }
    }

    /**
     * Upload de arquivo
     */
    public void uploadFile(String nomeArquivo, InputStream conteudo, String contentType) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(nomeArquivo)
                        .stream(conteudo, -1, 10485760)
                        .contentType(contentType)
                        .build()
        );
    }

    /**
     * Gera URL temporária (válida apenas dentro da rede Docker)
     */
    public String gerarUrlTemporaria(String nomeArquivo, int duracaoSegundos) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(nomeArquivo)
                        .expiry(duracaoSegundos, TimeUnit.SECONDS)
                        .build()
        );
    }
}