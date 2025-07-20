package com.example.authservice.model;

import org.springframework.web.multipart.MultipartFile;

public record UserDto(String name, String email, String password, MultipartFile profilePhoto) {
}
