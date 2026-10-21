package com.akash.blog.backend.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lightweight DTO for post list endpoints.
 * Excludes content, coverImage, and thumbnail to keep responses small.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSummaryDto {
    private String id;
    private String title;
    private String subtitle;
    private UserDTO user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isOwner;
    private CategoryDto category;
    private int likesCount;
    private boolean isLiked;
}
