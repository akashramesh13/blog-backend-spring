package com.akash.blog.backend.service;

import com.akash.blog.backend.dto.PostDto;
import com.akash.blog.backend.entity.Post;
import com.akash.blog.backend.entity.User;
import com.akash.blog.backend.repository.PostRepository;
import com.akash.blog.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

	public List<PostDto> getAllPosts(String username) {
		return postRepository.findAll().stream().map(post -> convertToDto(post, username)).collect(Collectors.toList());
	}

	public PostDto getPostById(Long id, String username) {
		return postRepository.findById(id).map(post -> convertToDto(post, username))
				.orElseThrow(() -> new RuntimeException("Post not found"));
	}

	public PostDto createPost(PostDto postDto, String username) {
		User user = userRepository.findByUsername(username);
		Post post = new Post();
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setUser(user);
		post.setCreatedAt(LocalDateTime.now());
		post.setUpdatedAt(LocalDateTime.now());
		post = postRepository.save(post);
		return convertToDto(post, username);
	}

	public PostDto updatePost(Long id, PostDto postDto, String username) {
		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
		if (!post.getUser().getUsername().equals(username)) {
			throw new RuntimeException("Unauthorized to update this post");
		}
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setUpdatedAt(LocalDateTime.now());
		post = postRepository.save(post);
		return convertToDto(post, username);
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
		return postDto;
	}
}