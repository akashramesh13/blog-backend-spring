package com.akash.blog.backend.repository;

import com.akash.blog.backend.entity.Post;
import com.akash.blog.backend.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserId(String userId);
    
    Page<Post> findByCategoryId(String categoryId, Pageable pageable);
}
