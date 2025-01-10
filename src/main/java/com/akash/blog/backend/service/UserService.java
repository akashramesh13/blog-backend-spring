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
        Optional<User> user = Optional.of(userRepository.findByUsername(username));
        return user.map(u -> new UserDTO(u.getId(), u.getUsername())).orElse(null);
    }

    public User authenticate(String username, String password) {
        Optional<User> user = Optional.of(userRepository.findByUsername(username));
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        return null;
    }

    public UserDTO createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getId(), savedUser.getUsername());
    }

    public UserDTO updateUser(Long id, User updatedUser) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
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