package com.akash.blog.backend.controller;

import com.akash.blog.backend.dto.PostDto;
import com.akash.blog.backend.dto.PostSummaryDto;
import com.akash.blog.backend.entity.Post;
import com.akash.blog.backend.service.PostService;

import jakarta.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public Page<PostSummaryDto> getPosts(
        @RequestParam(required = false) String category, 
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "latest") String sortBy,
        Authentication authentication
    ) {
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : "";
        return postService.getPostsByCategory(category, search, page, size, sortBy, username);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable String id, Authentication authentication) {
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : "";
        PostDto post = postService.getPostById(id, username);
        if (post.getCoverImage() != null) {
            logger.info("Sending post with cover image that starts with: {}", 
                post.getCoverImage().substring(0, Math.min(50, post.getCoverImage().length())));
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, Authentication authentication) {
        return ResponseEntity.ok(postService.createPost(postDto, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable String id, @Valid @RequestBody PostDto postDto, Authentication authentication) {
        return ResponseEntity.ok(postService.updatePost(id, postDto, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id, Authentication authentication) {
        postService.deletePost(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<PostDto> toggleLike(@PathVariable String id, Authentication authentication) {
        return ResponseEntity.ok(postService.toggleLike(id, authentication.getName()));
    }

    @GetMapping("/user/{userId}")
    public Page<PostSummaryDto> getPostsByUser(
        @PathVariable String userId,
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication
    ) {
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : "";
        return postService.getPostsByUserId(userId, page, size, username);
    }

    @GetMapping("/liked/{userId}")
    public Page<PostSummaryDto> getLikedPostsByUser(
        @PathVariable String userId,
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "10") int size,
        Authentication authentication
    ) {
        String username = (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : "";
        return postService.getLikedPostsByUserId(userId, page, size, username);
    }
}
