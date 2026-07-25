package com.akash.blog.backend.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String id;
    
    @NotBlank(message = "Title cannot be blank")
    private String title;
    
    private String subtitle;
    
    @NotBlank(message = "Content cannot be blank")
    private String content;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isOwner;
    private CategoryDto category;
    private String coverImage;
    private int likesCount;
    private boolean isLiked;
}
