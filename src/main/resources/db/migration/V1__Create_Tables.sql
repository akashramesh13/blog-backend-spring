-- Create user table
CREATE TABLE "user" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Auto-generate UUID using PostgreSQL function
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create category table
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,  -- Use BIGSERIAL for auto-incrementing
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Create post table
CREATE TABLE post (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Auto-generate UUID for post id
    title VARCHAR(255),
    content TEXT,
    created_at TIMESTAMP(6),  -- Use TIMESTAMP for date-time
    updated_at TIMESTAMP(6),  -- Use TIMESTAMP for date-time
    category_id BIGINT NOT NULL,
    user_id UUID NOT NULL,  -- Use UUID for foreign key reference
    cover_image BYTEA,  -- Use BYTEA for binary data
    FOREIGN KEY (category_id) REFERENCES category (id),
    FOREIGN KEY (user_id) REFERENCES "user" (id)
);

-- Insert default categories
INSERT INTO category (name) VALUES ('technology'), ('food'), ('gaming');
