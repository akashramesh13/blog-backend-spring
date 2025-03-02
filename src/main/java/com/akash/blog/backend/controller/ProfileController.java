package com.akash.blog.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.akash.blog.backend.dto.UserDTO;
import com.akash.blog.backend.service.UserService;

@RequestMapping("/profile")
@RestController
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    UserService userService;

    @GetMapping({"/{profileId}", "/"})
    public ResponseEntity<UserDTO> getProfile(@PathVariable(required = false) UUID profileId) {
        logger.info("Fetching profile for user ID: " + profileId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication != null ? authentication.getName() : null;

        UUID loggedinUserId = userService.getUserProfileByUsername(loggedInUsername).getId();
        
        // If profileId is null (i.e., "/profile/" was accessed), default to the logged-in user's ID
        UUID actualProfileId = profileId != null ? profileId : loggedinUserId;

        UserDTO userProfile = userService.getUserProfileById(actualProfileId); 

        return ResponseEntity.ok(userProfile);
    }

}
