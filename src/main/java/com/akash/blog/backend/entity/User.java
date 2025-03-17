package com.akash.blog.backend.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Data
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Automatically generate UUID using Java or PostgreSQL
    @Column(name = "id", columnDefinition = "UUID") // PostgreSQL UUID type
    private UUID id;

    private String username;
    private String password;
}
