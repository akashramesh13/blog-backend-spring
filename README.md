# Blog Backend API

A Spring Boot-based backend API for a blog platform with MongoDB for data storage and Redis for session management.

## Features

- User authentication and authorization
- Blog post management (CRUD operations)
- Category management
- Image upload support (Base64)
- Session management with Redis
- MongoDB database integration
- Environment-based configuration (local/prod)

## Tech Stack

- Java 17
- Spring Boot
- MongoDB
- Redis
- Spring Security
- Maven

## Prerequisites

- Java 17 or higher
- MongoDB (local or MongoDB Atlas)
- Redis (local or Redis Cloud)
- Maven

## Configuration

The application uses environment-based configuration. Create a `.env` file in the root directory with the following variables:

```env
# Server Configuration
PORT=10000
SPRING_PROFILES_ACTIVE=prod/local

# MongoDB Configuration
MONGODB_URI=your_mongodb_uri

# Logging Configuration
LOG_LEVEL=INFO
APP_LOG_LEVEL=INFO

# Redis Configuration
REDIS_HOST=your_redis_host
REDIS_PORT=your_redis_port
REDIS_USERNAME=your_redis_username
REDIS_PASSWORD=your_redis_password

# Session Configuration
SESSION_TIMEOUT=1800s
```

## Local Development Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd blog-backend-spring
```

2. Set up local MongoDB:
```bash
# Start MongoDB service
sudo systemctl start mongodb
```

3. Set up local Redis:
```bash
# Start Redis service
sudo systemctl start redis
```

4. Set environment variables for local development:
```bash
export SPRING_PROFILES_ACTIVE=local
```

5. Run the application:
```bash
./mvnw spring-boot:run
```

## API Endpoints

### Authentication
- `POST /login` - User login
- `POST /register` - User registration
- `POST /logout` - User logout

### Posts
- `GET /posts/` - Get all posts (with pagination)
- `GET /posts/{id}` - Get post by ID
- `POST /posts/` - Create new post
- `PUT /posts/{id}` - Update post
- `DELETE /posts/{id}` - Delete post

### Categories
- `GET /category/` - Get all categories
- `POST /category/` - Create new category

### Profile
- `GET /profile/` - Get current user profile
- `GET /profile/{id}` - Get user profile by ID

## Security

The application implements Spring Security with the following features:
- Session-based authentication
- Redis session storage
- Protected endpoints for authenticated users
- CSRF protection disabled for API endpoints

## Environment Profiles

### Local Profile
- MongoDB running locally
- Redis running locally
- Debug logging enabled
- No authentication for Redis

### Production Profile
- MongoDB Atlas connection
- Redis Cloud connection
- Basic logging
- Authenticated Redis connection

## Building for Production

1. Set production environment variables
2. Build the application:
```bash
./mvnw clean package
```

3. Run the built JAR:
```bash
java -jar target/blog-backend-0.0.1-SNAPSHOT.jar
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

[MIT License](LICENSE) 