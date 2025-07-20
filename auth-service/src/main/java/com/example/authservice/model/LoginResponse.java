package com.example.authservice.model;

public record LoginResponse(String token,String name,String email,String profilePhoto) {
}
