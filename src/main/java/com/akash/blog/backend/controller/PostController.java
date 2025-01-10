package com.akash.blog.backend.controller;

import com.akash.blog.backend.entity.Post;
import com.akash.blog.backend.entity.User;
import com.akash.blog.backend.service.PostService;
import com.akash.blog.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserProfileByUsername(username);

        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post createdPost = postService.createPost(post);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);
        return post.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        Optional<Post> postOptional = postService.getPostById(id);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            if (!post.getUser().getUsername().equals(username)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setUpdatedAt(LocalDateTime.now());

            Post savedPost = postService.createPost(post);
            return ResponseEntity.ok(savedPost);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Optional<Post> postOptional = postService.getPostById(id);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            if (!post.getUser().getUsername().equals(username)) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            postService.deletePost(post);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
