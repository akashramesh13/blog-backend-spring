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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	public List<PostDto> getAllPosts(String username) {
		return postRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}
	
	public Page<PostDto> getPostsByCategory(String categoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> postPage;

        if (categoryName != null && !categoryName.isEmpty()) {
            postPage = postRepository.findByCategory_Name(categoryName, pageable);
        } else {
            postPage = postRepository.findAll(pageable);
        }

        return postPage.map(this::convertToDto);
    }

	public PostDto getPostById(Long id, String username) {
		return postRepository.findById(id).map(post -> convertToDto(post, username))
				.orElseThrow(() -> new RuntimeException("Post not found"));
	}

	public PostDto createPost(PostDto postDto, String username) {
	    User user = userRepository.findByUsername(username);

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

	    postRepository.save(post);
	    return new PostDto(post.getId(), post.getTitle(), post.getContent(), 
	            new UserDTO(user.getId(), user.getUsername()), post.getCreatedAt(), post.getUpdatedAt(), true, 
	            category != null ? new CategoryDto(category.getId(), category.getName()) : null);
	}

	public PostDto updatePost(Long id, PostDto postDto, String username) {
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

	    postRepository.save(post);
	    return new PostDto(post.getId(), post.getTitle(), post.getContent(), 
	            new UserDTO(post.getUser().getId(), post.getUser().getUsername()), 
	            post.getCreatedAt(), post.getUpdatedAt(), true, 
	            category != null ? new CategoryDto(category.getId(), category.getName()) : null);
	}

	
	public void deletePost(Long id, String username) {
		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
		if (!post.getUser().getUsername().equals(username)) {
			throw new RuntimeException("Unauthorized to delete this post");
		}
		postRepository.delete(post);
	}

	private PostDto convertToDto(Post post, String username) {
	    PostDto postDto = new PostDto();
	    postDto.setId(post.getId());
	    postDto.setTitle(post.getTitle());
	    postDto.setContent(post.getContent());
	    postDto.setCreatedAt(post.getCreatedAt());
	    postDto.setUpdatedAt(post.getUpdatedAt());
	    postDto.setOwner(post.getUser().getUsername().equals(username));
	    if(post.getCategory() != null ) {
	    CategoryDto categoryDto = new CategoryDto(post.getCategory().getId(), post.getCategory().getName());
	    postDto.setCategory(categoryDto);
	    }
	    return postDto;
	}
	
	private PostDto convertToDto(Post post) {
	    PostDto postDto = new PostDto();
	    postDto.setId(post.getId());
	    postDto.setTitle(post.getTitle());
	    postDto.setContent(post.getContent());
	    postDto.setCreatedAt(post.getCreatedAt());
	    postDto.setUpdatedAt(post.getUpdatedAt());
	    
	    if (post.getCategory() != null) {
	        postDto.setCategory(new CategoryDto(post.getCategory().getId(), post.getCategory().getName()));
	    }
	    
	    return postDto;
	}


}