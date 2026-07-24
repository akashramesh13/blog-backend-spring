package com.akash.blog.backend.repository;

import com.akash.blog.backend.entity.Post;
import com.akash.blog.backend.entity.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findByUserId(String userId, Pageable pageable);
    
    Page<Post> findByLikedByContaining(String userId, Pageable pageable);
    
    List<Post> findByUserId(String userId);
    
    Page<Post> findByCategoryId(String categoryId, Pageable pageable);
    
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    Page<Post> findByCategoryIdAndTitleContainingIgnoreCase(String categoryId, String title, Pageable pageable);
}
