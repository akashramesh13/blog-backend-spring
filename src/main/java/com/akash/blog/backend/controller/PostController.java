package com.akash.blog.backend.controller;

import com.akash.blog.backend.dto.PostDto;
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

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public Page<PostDto> getPosts(
        @RequestParam(required = false) String category, 
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getPostsByCategory(category, page, size);
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
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, Authentication authentication) {
        return ResponseEntity.ok(postService.createPost(postDto, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable String id, @RequestBody PostDto postDto, Authentication authentication) {
        return ResponseEntity.ok(postService.updatePost(id, postDto, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id, Authentication authentication) {
        postService.deletePost(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
