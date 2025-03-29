package com.akash.blog.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String username;
    private String password;
}
