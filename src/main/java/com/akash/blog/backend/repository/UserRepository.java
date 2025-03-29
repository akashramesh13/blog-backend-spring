package com.akash.blog.backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.akash.blog.backend.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    Optional<User> findById(String id);
}
