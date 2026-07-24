package com.akash.blog.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.akash.blog.backend.filter.CustomSessionFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests()
            // Restrict edit, create, and delete to authenticated users only
            .requestMatchers(HttpMethod.POST, "/posts", "/posts/").authenticated()
            .requestMatchers(HttpMethod.PUT, "/posts/{id}").authenticated()
            .requestMatchers(HttpMethod.DELETE, "/posts/{id}").authenticated()
            // Public routes (anyone can access these)
            .requestMatchers("/", "/posts/", "/posts/**", "/login", "/register", "/category/**", "/actuator/health", "/rss.xml").permitAll()
            .anyRequest().authenticated()  // Require authentication for any other requests
            .and()
            .formLogin().disable()
            .addFilterBefore(new CustomSessionFilter(), SecurityContextPersistenceFilter.class);

        return http.build();
    }
}
