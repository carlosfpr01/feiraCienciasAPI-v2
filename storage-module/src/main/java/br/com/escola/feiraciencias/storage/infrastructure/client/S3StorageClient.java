package br.com.escola.feiraciencias.storage.infrastructure.client;

import java.net.URI;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Client de armazenamento S3 compatível com provedores S3-like
 * (Cloudflare R2, Supabase Storage, MinIO, etc.).
 *
 * Usa o AWS SDK padrão com endpoint customizado e path-style access.
 */
@ApplicationScoped
public class S3StorageClient {

    @ConfigProperty(name = "storage.s3.endpoint")
    String endpoint;

    @ConfigProperty(name = "storage.s3.access-key-id")
    String accessKeyId;

    @ConfigProperty(name = "storage.s3.secret-access-key")
    String secretAccessKey;

    @ConfigProperty(name = "storage.s3.bucket")
    String bucket;

    @ConfigProperty(name = "storage.s3.public-url")
    String publicUrl;

    @ConfigProperty(name = "storage.s3.region", defaultValue = "us-east-1")
    String region;

    private S3Client s3Client;

    @PostConstruct 
    void init() {
        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ))
                .region(Region.of(region))
                .forcePathStyle(true)
                .build();

        // Auto-criar bucket se não existir (útil para LocalStack/MinIO em dev)
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (NoSuchBucketException e) {
            try {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            } catch (Exception ex) {
                // Silencioso em caso de falta de permissão (ex: em prod)
            }
        } catch (Exception e) {
            // Ignorar erros de conexão/resolução de DNS na inicialização para evitar que o app quebre se o S3 estiver temporariamente offline
        }
    }


    public void upload(String chave, byte[] conteudo, String mimeType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(chave)
                .contentType(mimeType)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(conteudo));
    }


    public void delete(String chave) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(chave)
                .build();

        s3Client.deleteObject(request);
    }


    public String gerarUrl(String chave) {
        String base = publicUrl.endsWith("/") ? publicUrl : publicUrl + "/";
        return base + chave;
    }
}
