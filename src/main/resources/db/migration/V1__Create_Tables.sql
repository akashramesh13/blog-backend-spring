-- Create user table
CREATE TABLE user (
    id BINARY(16) NOT NULL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create category table
CREATE TABLE category (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Create post table
CREATE TABLE post (
    id BINARY(16) NOT NULL PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    category_id BIGINT NOT NULL,
    user_id BINARY(16) NOT NULL,
    cover_image LONGBLOB,
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);

-- Insert default categories
INSERT INTO category (id, name) VALUES (1, 'technology'), (2, 'food'), (3, 'gaming');
