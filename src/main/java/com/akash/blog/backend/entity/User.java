package com.akash.blog.backend.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {

	   @Id
	    @GeneratedValue
	    @Column(name = "id", columnDefinition = "BINARY(16)")
	    private UUID id;

    private String username;
    private String password;
}
