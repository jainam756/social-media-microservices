package com.example.authservice.repository;

import com.example.authservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email);
}
