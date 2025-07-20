package com.example.authservice.model;

import jakarta.ws.rs.core.MediaType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

public class Post {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String title;
    private String text;
    private MediaType mediaType;
    private String mediaUri;
    @Transient
    private String presignedUri;
    private LocalDateTime createdAt;
    private List<String> tags;
    private int likes;
    private PostCreator creator;
}