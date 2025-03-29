package com.akash.blog.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akash.blog.backend.dto.UserDTO;
import com.akash.blog.backend.entity.User;
import com.akash.blog.backend.service.UserService;

import java.util.Collections;

@RestController
@RequestMapping("/login")
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> login(HttpServletRequest request, @RequestBody User reqUser) {
		try {
			if (reqUser.getUsername() == null || reqUser.getPassword() == null) {
				return ResponseEntity.badRequest().body("Username and password are required");
			}

			User user = userService.authenticate(reqUser.getUsername(), reqUser.getPassword());
			if (user != null) {
				Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null,
						Collections.emptyList());

				SecurityContextHolder.getContext().setAuthentication(auth);

				request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
						SecurityContextHolder.getContext());

				request.getSession().setAttribute("userId", user.getId());
				UserDTO userDto = new UserDTO(user.getId(), user.getUsername());
				logger.info("User {} logged in successfully", user.getUsername());
				return ResponseEntity.ok(userDto);
			} else {
				logger.warn("Failed login attempt for username: {}", reqUser.getUsername());
				return ResponseEntity.status(401).body("Invalid username or password");
			}
		} catch (Exception e) {
			logger.error("Error during login for username: {}", reqUser.getUsername(), e);
			return ResponseEntity.status(500).body("An error occurred during login");
		}
	}
}
