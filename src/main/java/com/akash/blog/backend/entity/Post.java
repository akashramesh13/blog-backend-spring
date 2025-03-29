package com.akash.blog.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.time.LocalDateTime;

@Document(collection = "posts")
@Data
public class Post {
    @Id
    private String id;
    
    private String title;
    
    private String content;
    
    @DBRef
    private User user;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @DBRef
    private Category category;
    
    private String coverImage;
}