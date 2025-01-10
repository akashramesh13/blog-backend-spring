package com.akash.blog.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
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

import com.akash.blog.backend.entity.User;
import com.akash.blog.backend.service.UserService;

import java.util.Collections;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<String> login(HttpServletRequest request, @RequestBody User reqUser) {
		User user = userService.authenticate(reqUser.getUsername(), reqUser.getPassword());
		if (user != null) {
			Authentication auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null,
					Collections.emptyList());

			SecurityContextHolder.getContext().setAuthentication(auth);

			request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());

			request.getSession().setAttribute("userId", user.getId());
			return ResponseEntity.ok("Login successful");
		} else {
			return ResponseEntity.status(401).body("Invalid credentials");
		}
	}
}
