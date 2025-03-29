package com.akash.blog.backend.service;

import com.akash.blog.backend.dto.UserDTO;
import com.akash.blog.backend.entity.User;
import com.akash.blog.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO getUserProfileByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? new UserDTO(user.getId(), user.getUsername()) : null;
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public UserDTO createUser(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getUsername());
    }
    
    public UserDTO getUserProfileById(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(user -> new UserDTO(user.getId(), user.getUsername())).orElse(null);
    }

    public UserDTO updateUser(String id, User updatedUser) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Check if new username is already taken by another user
            if (!user.getUsername().equals(updatedUser.getUsername())) {
                User existingUser = userRepository.findByUsername(updatedUser.getUsername());
                if (existingUser != null) {
                    throw new RuntimeException("Username already exists");
                }
            }
            
            user.setUsername(updatedUser.getUsername());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            userRepository.save(user);
            return new UserDTO(user.getId(), user.getUsername());
        }
        return null;
    }
}