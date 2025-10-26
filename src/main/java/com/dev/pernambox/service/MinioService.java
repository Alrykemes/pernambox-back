package com.dev.pernambox.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class MinioService {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioService(
            @Value("${MINIO_ENDPOINT}") String endpoint,
            @Value("${MINIO_ACCESS_KEY}") String accessKey,
            @Value("${MINIO_SECRET_KEY}") String secretKey,
            @Value("${MINIO_BUCKET}") String bucket
    ) {
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
    private void inicializarBucket() {
        try {
            boolean existe = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucket).build()
            );

            if (!existe) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucket).build()
                );
                System.out.println("[MinIO] Bucket criado: " + bucket);
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
                System.out.println("[MinIO] Política privada aplicada ao bucket: " + bucket);
            } else {
                System.out.println("[MinIO] Bucket já existente: " + bucket);
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha ao inicializar bucket MinIO: " + e.getMessage(), e);
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