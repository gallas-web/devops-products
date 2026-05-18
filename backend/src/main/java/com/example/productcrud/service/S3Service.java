package com.example.productcrud.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;
    private final String baseUrl;

    public S3Service(
        @Value("${aws.s3.bucket}") String bucketName,
        @Value("${aws.s3.region}") String region,
        @Value("${aws.s3.base-url}") String baseUrl
    ) {
        this.bucketName = bucketName;
        this.baseUrl = baseUrl;
        this.s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(InstanceProfileCredentialsProvider.create())
            .build();
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = "products/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .contentType(file.getContentType())
            .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
        
        return baseUrl + "/" + fileName;
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl != null && imageUrl.contains(bucketName)) {
            String key = imageUrl.substring(imageUrl.indexOf("products/"));
            s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
        }
    }
}
