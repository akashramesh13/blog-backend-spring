package com.akash.blog.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        .csrf().disable()
        .authorizeHttpRequests()
        .requestMatchers("/login", "/register").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin().disable()
        .addFilterBefore(new CustomSessionFilter(), SecurityContextPersistenceFilter.class);

    return http.build();

	}
}
