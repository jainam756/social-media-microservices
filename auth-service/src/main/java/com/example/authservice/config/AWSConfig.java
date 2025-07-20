package com.example.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfig {
    public static final String BUCKET_NAME="social-media-app-jainam";
    @Bean
    public S3Client s3Client(
            @Value("${aws.s3.accessKey}")String accessKey,
            @Value("${aws.s3.secretKey}")String secretKey
    ){
        AwsCredentials credential= AwsBasicCredentials.builder()
                .accessKeyId(accessKey)
                .secretAccessKey(secretKey)
                .build();
        return S3Client.builder().credentialsProvider(()->credential).region(Region.AP_SOUTH_1).build();
    }
}
