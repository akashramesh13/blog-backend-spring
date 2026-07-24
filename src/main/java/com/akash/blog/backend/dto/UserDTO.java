package com.akash.blog.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    
    @NotBlank(message = "Username cannot be blank")
    private String username;
    private String avatar;
    private String bio;
    private String profileTitle;
    
    public UserDTO(String id, String username) {
        this.id = id;
        this.username = username;
    }
}