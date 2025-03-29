package com.akash.blog.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.bson.types.Binary;
import org.bson.Document;
import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class MongoMigration implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        try {
            // Find all posts with Binary coverImage
            Query query = new Query(Criteria.where("coverImage").exists(true));
            mongoTemplate.find(query, Document.class, "posts")
                .forEach(doc -> {
                    Object coverImage = doc.get("coverImage");
                    if (coverImage instanceof Binary) {
                        Binary binary = (Binary) coverImage;
                        String base64String = Base64.getEncoder().encodeToString(binary.getData());
                        
                        // Update the document with the base64 string
                        Query updateQuery = new Query(Criteria.where("_id").is(doc.get("_id")));
                        Update update = new Update().set("coverImage", base64String);
                        mongoTemplate.updateFirst(updateQuery, update, "posts");
                        
                        System.out.println("Migrated image for post: " + doc.get("_id"));
                    }
                });
        } catch (Exception e) {
            System.err.println("Error during migration: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 