package com.akash.blog.backend.repository;

import com.akash.blog.backend.entity.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUserId(UUID userId);
    Page<Post> findByCategory_Name(String categoryName, Pageable pageable);
}
