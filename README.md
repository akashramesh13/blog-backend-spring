# **Pixel Pursuit - Blog Backend API**

A **modern, scalable, and secure** backend for **Pixel Pursuit**, built with **Spring Boot**, **MongoDB**, and **Redis**. Designed to deliver a seamless blogging experience with robust authentication, efficient post management, and optimized performance.

## 🌟 **Features**

- **User Authentication & Authorization** – Secure login, registration, and session management
- **Post Management** – Full CRUD operations for blog posts
- **Category Management** – Organize posts with customizable categories
- **Image Upload Support** – Store images using Base64 encoding
- **Session Management with Redis** – Ensuring high performance and scalability
- **Environment-Based Configurations** – Easily switch between local and production setups
- **Spring Security Integration** – Protecting API endpoints with authentication

## 🛠️ **Tech Stack**

- **Backend**:
  - Java 17
  - Spring Boot
  - MongoDB (Local/Atlas)
  - Redis (Local/Cloud)
  - Spring Security
  - Maven

## 🎯 **Core Functionality**

- **Authentication & Security**

  - User registration, login, and logout
  - Secure session handling with Redis
  - Protected API endpoints

- **Post Management**

  - Create, edit, delete, and retrieve blog posts
  - Image uploads via Base64 encoding
  - Category tagging for better organization

- **User Features**

  - Profile management
  - Personalized post management

- **Performance & Scalability**
  - Optimized database queries for fast response times
  - Redis-backed session storage for high scalability

## 📝 **Project Structure**

```
src/
├── config/         # Application configuration
├── controllers/    # API controllers
├── models/         # Data models
├── repositories/   # MongoDB repositories
├── services/       # Business logic and services
├── security/       # Authentication and authorization
└── utils/          # Utility classes
```

## 🔧 **Configuration**

Create a `.env` file with the following variables:

```ini
# Server Configuration
PORT=10000
SPRING_PROFILES_ACTIVE=prod/local

# MongoDB
MONGODB_URI=your_mongodb_uri

# Redis
REDIS_HOST=your_redis_host
REDIS_PORT=your_redis_port
REDIS_USERNAME=your_redis_username
REDIS_PASSWORD=your_redis_password

# Session
SESSION_TIMEOUT=1800s
```

## 🚀 **Local Development Setup**

1️⃣ **Clone the repository**

```bash
git clone https://github.com/akashramesh13/blog-backend-spring.git
cd blog-backend-spring
```

2️⃣ **Start MongoDB & Redis**

```bash
sudo systemctl start mongodb
sudo systemctl start redis
```

3️⃣ **Set environment variables for local development**

```bash
export SPRING_PROFILES_ACTIVE=local
```

4️⃣ **Run the application**

```bash
./mvnw spring-boot:run
```

## 📡 **API Endpoints**

### 🔐 Authentication

- `POST /login` – User login
- `POST /register` – User registration
- `POST /logout` – User logout

### 📝 Posts

- `GET /posts/` – Fetch all posts (Paginated)
- `GET /posts/{id}` – Get post by ID
- `POST /posts/` – Create a new post
- `PUT /posts/{id}` – Update a post
- `DELETE /posts/{id}` – Delete a post

### 🏷 Categories

- `GET /category/` – Get all categories
- `POST /category/` – Create a new category

### 👤 User Profile

- `GET /profile/` – Get current user profile
- `GET /profile/{id}` – Fetch user profile by ID

## 🔒 **Security & Authentication**

- **Session-based authentication** using Spring Security
- **Redis-backed session storage** for performance and scalability
- **API protection** for authenticated users

## 🌍 **Environment Profiles**

### **Local Profile**

- MongoDB running locally
- Redis running locally
- Debug logging enabled
- No authentication for Redis

### **Production Profile**

- MongoDB Atlas connection
- Redis Cloud connection
- Optimized logging
- Secure Redis authentication

## 🏗 **Building for Production**

```bash
./mvnw clean package
java -jar target/blog-backend-0.0.1-SNAPSHOT.jar
```

## 🤝 **Contributing**

1. **Fork the repository**
2. **Create a feature branch**
3. **Commit & push your changes**
4. **Submit a Pull Request**

## 📄 **License**

This project is licensed under the [MIT License](LICENSE).

## 👤 **Author**

- GitHub: [@akashramesh13](https://github.com/akashramesh13)
- Portfolio: [akashramesh.in](https://www.akashramesh.in)

---

Built with :heart: using Spring Boot and MongoDB
