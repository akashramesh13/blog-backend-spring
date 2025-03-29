package com.akash.blog.backend.service;

import com.akash.blog.backend.dto.CategoryDto;
import com.akash.blog.backend.dto.PostDto;
import com.akash.blog.backend.dto.UserDTO;
import com.akash.blog.backend.entity.Category;
import com.akash.blog.backend.entity.Post;
import com.akash.blog.backend.entity.User;
import com.akash.blog.backend.repository.CategoryRepository;
import com.akash.blog.backend.repository.PostRepository;
import com.akash.blog.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<PostDto> getAllPosts(String username) {
        try {
            return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all posts", e);
            throw new RuntimeException("Failed to fetch posts");
        }
    }
    
    public Page<PostDto> getPostsByCategory(String categoryName, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Post> postPage;

            if (categoryName != null && !categoryName.isEmpty()) {
                // First find the category by name
                Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));
                
                logger.info("Found category: {} with id: {}", categoryName, category.getId());
                postPage = postRepository.findByCategoryId(category.getId(), pageable);
                logger.info("Fetching posts for category: {}", categoryName);
            } else {
                postPage = postRepository.findAll(pageable);
                logger.info("Fetching all posts");
            }

            return postPage.map(this::convertToDto);
        } catch (Exception e) {
            logger.error("Error fetching posts by category: {}", categoryName, e);
            throw new RuntimeException("Failed to fetch posts: " + e.getMessage());
        }
    }

    public PostDto getPostById(String id, String username) {
        try {
            return postRepository.findById(id)
                .map(post -> convertToDto(post, username))
                .orElseThrow(() -> new RuntimeException("Post not found"));
        } catch (Exception e) {
            logger.error("Error fetching post: {}", id, e);
            throw new RuntimeException("Failed to fetch post");
        }
    }

    public PostDto createPost(PostDto postDto, String username) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            Category category = null;
            if (postDto.getCategory() != null && postDto.getCategory().getId() != null) {
                category = categoryRepository.findById(postDto.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            }

            Post post = new Post();
            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setUser(user);
            post.setCategory(category);
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());

            if (postDto.getCoverImage() != null && !postDto.getCoverImage().isEmpty()) {
                String base64Image = postDto.getCoverImage();
                logger.info("Received cover image with length: {}", base64Image.length());
                logger.info("Cover image starts with: {}", base64Image.substring(0, Math.min(50, base64Image.length())));
                if (base64Image.contains(",")) {
                    base64Image = base64Image.split(",")[1];
                    logger.info("Stripped prefix, new length: {}", base64Image.length());
                }
                post.setCoverImage(base64Image.trim());
                logger.info("Final cover image length: {}", base64Image.trim().length());
            }

            post = postRepository.save(post);
            return convertToDto(post);
        } catch (Exception e) {
            logger.error("Error creating post", e);
            throw new RuntimeException("Failed to create post");
        }
    }

    public PostDto updatePost(String id, PostDto postDto, String username) {
        try {
            Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

            if (!post.getUser().getUsername().equals(username)) {
                throw new RuntimeException("Unauthorized");
            }

            Category category = null;
            if (postDto.getCategory() != null && postDto.getCategory().getId() != null) {
                category = categoryRepository.findById(postDto.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            }

            post.setTitle(postDto.getTitle());
            post.setContent(postDto.getContent());
            post.setCategory(category);
            post.setUpdatedAt(LocalDateTime.now());

            if (postDto.getCoverImage() != null && !postDto.getCoverImage().isEmpty()) {
                String base64Image = postDto.getCoverImage();
                logger.info("Received cover image with length: {}", base64Image.length());
                logger.info("Cover image starts with: {}", base64Image.substring(0, Math.min(50, base64Image.length())));
                if (base64Image.contains(",")) {
                    base64Image = base64Image.split(",")[1];
                    logger.info("Stripped prefix, new length: {}", base64Image.length());
                }
                post.setCoverImage(base64Image.trim());
                logger.info("Final cover image length: {}", base64Image.trim().length());
            }

            post = postRepository.save(post);
            return convertToDto(post);
        } catch (Exception e) {
            logger.error("Error updating post: {}", id, e);
            throw new RuntimeException("Failed to update post");
        }
    }

    public void deletePost(String id, String username) {
        try {
            Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

            if (!post.getUser().getUsername().equals(username)) {
                throw new RuntimeException("Unauthorized");
            }

            postRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting post: {}", id, e);
            throw new RuntimeException("Failed to delete post");
        }
    }

    private PostDto convertToDto(Post post, String username) {
        PostDto dto = convertToDto(post);
        dto.setOwner(post.getUser().getUsername().equals(username));
        return dto;
    }

    private UserDTO convertUserToUserDto(User user) {
        return new UserDTO(user.getId(), user.getUsername());
    }

    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setUser(convertUserToUserDto(post.getUser()));
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        
        if (post.getCategory() != null) {
            dto.setCategory(new CategoryDto(post.getCategory().getId(), post.getCategory().getName()));
        }
        
        if (post.getCoverImage() != null) {
            String base64Image = post.getCoverImage();
            logger.info("Original image from DB: {}", base64Image.substring(0, Math.min(50, base64Image.length())));
            
            if (!base64Image.startsWith("data:image")) {
                base64Image = "data:image/jpeg;base64," + base64Image;
                logger.info("Added prefix, image now starts with: {}", base64Image.substring(0, Math.min(50, base64Image.length())));
            }
            dto.setCoverImage(base64Image);
            logger.info("Final image in DTO starts with: {}", base64Image.substring(0, Math.min(50, base64Image.length())));
        }
        
        return dto;
    }
}
