package com.dinesh.project.repository;

import com.dinesh.project.model.Auth;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthRepository extends MongoRepository<Auth, String> {
    Optional<Auth> findByUserId(String id);

    Optional<Auth> findBySessionToken(String token);
}
