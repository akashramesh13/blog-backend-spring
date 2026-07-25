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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
        return postRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
    
    public Page<PostDto> getPostsByCategory(String categoryName, String search, int page, int size, String sortBy, String username) {
        String sortProperty = "likes".equalsIgnoreCase(sortBy) ? "likesCount" : "createdAt";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortProperty).descending());
        Page<Post> postPage;

        boolean hasCategory = categoryName != null && !categoryName.isEmpty();
        boolean hasSearch = search != null && !search.isEmpty();

        if (hasCategory) {
            Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found: " + categoryName));
            
            logger.info("Found category: {} with id: {}", categoryName, category.getId());
            if (hasSearch) {
                postPage = postRepository.findByCategoryIdAndTitleContainingIgnoreCase(category.getId(), search, pageable);
            } else {
                postPage = postRepository.findByCategoryId(category.getId(), pageable);
            }
            logger.info("Fetching posts for category: {}", categoryName);
        } else {
            if (hasSearch) {
                postPage = postRepository.findByTitleContainingIgnoreCase(search, pageable);
            } else {
                postPage = postRepository.findAll(pageable);
            }
            logger.info("Fetching all posts");
        }

        return postPage.map(post -> convertToDto(post, username));
    }

    public PostDto getPostById(String id, String username) {
        return postRepository.findById(id)
            .map(post -> convertToDto(post, username))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    public PostDto createPost(PostDto postDto, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        Category category = null;
        if (postDto.getCategory() != null && postDto.getCategory().getId() != null) {
            category = categoryRepository.findById(postDto.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        }

        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setSubtitle(postDto.getSubtitle());
        post.setContent(postDto.getContent());
        post.setUser(user);
        post.setCategory(category);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        if (postDto.getCoverImage() != null && !postDto.getCoverImage().isEmpty()) {
            post.setCoverImage(extractBase64Data(postDto.getCoverImage()));
        }

        post = postRepository.save(post);
        return convertToDto(post);
    }

    public PostDto updatePost(String id, PostDto postDto, String username) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!post.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized to update this post");
        }

        Category category = null;
        if (postDto.getCategory() != null && postDto.getCategory().getId() != null) {
            category = categoryRepository.findById(postDto.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        }

        post.setTitle(postDto.getTitle());
        post.setSubtitle(postDto.getSubtitle());
        post.setContent(postDto.getContent());
        post.setCategory(category);
        post.setUpdatedAt(LocalDateTime.now());

        if (postDto.getCoverImage() != null && !postDto.getCoverImage().isEmpty()) {
            post.setCoverImage(extractBase64Data(postDto.getCoverImage()));
        }

        post = postRepository.save(post);
        return convertToDto(post);
    }

    public void deletePost(String id, String username) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!post.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized to delete this post");
        }

        postRepository.deleteById(id);
    }
    
    public PostDto toggleLike(String id, String username) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }
        
        if (post.getLikedBy() == null) {
            post.setLikedBy(new java.util.HashSet<>());
        }
        
        if (post.getLikedBy().contains(user.getId())) {
            post.getLikedBy().remove(user.getId());
            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        } else {
            post.getLikedBy().add(user.getId());
            post.setLikesCount(post.getLikesCount() + 1);
        }
        
        post = postRepository.save(post);
        return convertToDto(post, username);
    }
    
    public Page<PostDto> getPostsByUserId(String userId, int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByUserId(userId, pageable).map(post -> convertToDto(post, username));
    }
    
    public Page<PostDto> getLikedPostsByUserId(String userId, int page, int size, String username) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByLikedByContaining(userId, pageable).map(post -> convertToDto(post, username));
    }
    
    private String extractBase64Data(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }
        int commaIndex = base64Image.indexOf(",");
        if (commaIndex != -1 && commaIndex < base64Image.length() - 1) {
            return base64Image.substring(commaIndex + 1).trim();
        }
        return base64Image.trim();
    }

    private PostDto convertToDto(Post post, String username) {
        PostDto dto = convertToDto(post);
        if (username != null && !username.isEmpty()) {
            dto.setOwner(post.getUser() != null && post.getUser().getUsername().equals(username));
            User user = userRepository.findByUsername(username);
            if (user != null && post.getLikedBy() != null) {
                dto.setLiked(post.getLikedBy().contains(user.getId()));
            }
        }
        return dto;
    }

    private UserDTO convertUserToUserDto(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getAvatar(), user.getBio(), user.getProfileTitle());
    }

    private PostDto convertToDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSubtitle(post.getSubtitle());
        dto.setContent(post.getContent());
        dto.setUser(convertUserToUserDto(post.getUser()));
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        
        if (post.getCategory() != null) {
            dto.setCategory(new CategoryDto(post.getCategory().getId(), post.getCategory().getName()));
        }
        
        if (post.getCoverImage() != null) {
            String base64Image = post.getCoverImage();
            if (!base64Image.startsWith("data:image")) {
                base64Image = "data:image/jpeg;base64," + base64Image;
            }
            dto.setCoverImage(base64Image);
        }
        
        if (post.getThumbnail() != null) {
            String base64Thumb = post.getThumbnail();
            if (!base64Thumb.startsWith("data:image")) {
                base64Thumb = "data:image/jpeg;base64," + base64Thumb;
            }
            dto.setThumbnail(base64Thumb);
        }
        
        dto.setLikesCount(post.getLikesCount());
        
        return dto;
    }
}
