package com.akash.blog.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/logout")
public class LogoutController {
	@PostMapping("/")
	public ResponseEntity<?> logout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }
	    return ResponseEntity.ok("Logged out successfully");
	}

}
