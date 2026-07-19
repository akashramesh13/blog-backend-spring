package com.akash.blog.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String avatar;
    private String bio;
    private String profileTitle;
    
    public UserDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }
}