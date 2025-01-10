package com.akash.blog.backend.controller;

import com.akash.blog.backend.dto.PostDto;
import com.akash.blog.backend.entity.Post;
import com.akash.blog.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(Authentication authentication) {
        return ResponseEntity.ok(postService.getAllPosts(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(postService.getPostById(id, authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, Authentication authentication) {
        return ResponseEntity.ok(postService.createPost(postDto, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody PostDto postDto, Authentication authentication) {
        return ResponseEntity.ok(postService.updatePost(id, postDto, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Authentication authentication) {
        postService.deletePost(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
