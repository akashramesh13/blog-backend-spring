package com.akash.blog.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.akash.blog.backend.entity.Category;

@Component
public class MongoInitializer implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        // Create indexes
        createIndexes();
        // Create default categories
        createDefaultCategories();
    }

    private void createIndexes() {
        // Create indexes for better query performance
        mongoTemplate.indexOps("posts").ensureIndex(
            new Index().on("title", org.springframework.data.domain.Sort.Direction.ASC).background()
        );

        mongoTemplate.indexOps("posts").ensureIndex(
            new Index().on("createdAt", org.springframework.data.domain.Sort.Direction.DESC).background()
        );

        mongoTemplate.indexOps("users").ensureIndex(
            new Index().on("username", org.springframework.data.domain.Sort.Direction.ASC).unique().background()
        );

        mongoTemplate.indexOps("categories").ensureIndex(
            new Index().on("name", org.springframework.data.domain.Sort.Direction.ASC).unique().background()
        );
    }

    private void createDefaultCategories() {
        // Create default categories if they don't exist
        String[] defaultCategories = {"Technology", "Lifestyle", "Travel"};
        for (String categoryName : defaultCategories) {
            Query query = new Query(Criteria.where("name").is(categoryName));
            if (!mongoTemplate.exists(query, Category.class)) {
                Category category = new Category();
                category.setName(categoryName);
                mongoTemplate.save(category);
            }
        }
    }
} 