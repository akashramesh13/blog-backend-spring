package com.akash.blog.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.akash.blog.backend.dto.UserDTO;
import com.akash.blog.backend.service.UserService;

import java.util.Map;

@RequestMapping("/profile")
@RestController
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    UserService userService;
    
    @GetMapping("/check")
    public ResponseEntity<Void> checkAuth() {
        return ResponseEntity.ok().build();
    }

    @GetMapping({"/{profileId}", "/"})
    public ResponseEntity<UserDTO> getProfile(@PathVariable(required = false) String profileId) {
        logger.info("Fetching profile for user ID: {}", profileId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication != null ? authentication.getName() : null;

        String loggedInUserId = null;
        if (loggedInUsername != null) {
            UserDTO loggedInUser = userService.getUserProfileByUsername(loggedInUsername);
            if (loggedInUser != null) {
                loggedInUserId = loggedInUser.getId();
            }
        }
        
        // If profileId is null (i.e., "/profile/" was accessed), default to the logged-in user's ID
        String actualProfileId = profileId != null ? profileId : loggedInUserId;

        if (actualProfileId == null) {
            return ResponseEntity.notFound().build();
        }

        UserDTO userProfile = userService.getUserProfileById(actualProfileId);
        if (userProfile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/avatar")
    public ResponseEntity<UserDTO> updateAvatar(@RequestBody Map<String, String> payload) {
        String avatarData = payload.get("avatar");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication != null ? authentication.getName() : null;
        if (loggedInUsername == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            UserDTO updatedUser = userService.updateAvatar(loggedInUsername, avatarData);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/bio")
    public ResponseEntity<UserDTO> updateBio(@RequestBody Map<String, String> payload) {
        String bioData = payload.get("bio");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication != null ? authentication.getName() : null;
        if (loggedInUsername == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            UserDTO updatedUser = userService.updateBio(loggedInUsername, bioData);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/title")
    public ResponseEntity<UserDTO> updateProfileTitle(@RequestBody Map<String, String> payload) {
        String profileTitle = payload.get("profileTitle");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication != null ? authentication.getName() : null;
        if (loggedInUsername == null) {
            return ResponseEntity.status(401).build();
        }
        try {
            UserDTO updatedUser = userService.updateProfileTitle(loggedInUsername, profileTitle);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
