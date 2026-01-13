# 📚 BookStore FullStack Application

[![Java](https://img.shields.io/badge/Java-21-orange? style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2-blue?style=flat-square&logo=react)](https://react.dev/)
[![MongoDB](https://img.shields.io/badge/MongoDB-6.0-green?style=flat-square&logo=mongodb)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Latest-blue?style=flat-square&logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

> 🎯 A comprehensive full-stack web application for an online bookstore with modern architecture, session-based authentication, and Docker containerization.

## 🎨 Screenshots

 
---

## ✨ Key Features

### 🛍️ For Customers
- ✅ **Registration & Authentication** - Secure registration with session-based authentication
- ✅ **Book Catalog** - Browse, search, and filter books by title, author, and genre
- ✅ **Rating System** - Leave and view reviews about books
- ✅ **Shopping Cart** - Add, modify quantity, and remove items
- ✅ **Checkout Process** - Enter shipping address and select payment method
- ✅ **Order History** - View all your orders and their statuses
- ✅ **Wishlist** - Save favorite books for later
- ✅ **Order Tracking** - Check delivery status of your orders

### 👨‍💼 For Administrators
- ✅ **Book Management** - Add, edit, and delete books
- ✅ **Order Management** - Update delivery status and tracking numbers
- ✅ **User Management** - View and manage user accounts
- ✅ **Analytics** - View sales statistics and book popularity
- ✅ **Discounts & Promotions** - Set discounted prices for books

### 🔐 Security
- ✅ **Session-Based Authentication** - Secure session management in MongoDB
- ✅ **CSRF Protection** - Protection against cross-site request forgery attacks
- ✅ **CORS Configuration** - Proper handling of cross-origin requests
- ✅ **Password Encryption** - BCrypt password hashing
- ✅ **Role-Based Access Control** - Separation of privileges (USER/ADMIN)
- ✅ **HttpOnly & SameSite Cookies** - Protection against XSS attacks

---

## 🏗️ Architecture

BookStore FullStack │ ├── Frontend (React + Vite) │ ├── Components (Header, BookCard, Cart, etc.) │ ├── Pages (Home, Login, Books, Checkout) │ ├── Contexts (AuthContext, CartContext) │ └── API Client (Axios) │ ├── Backend (Spring Boot 3 + Java 21) │ ├── Controllers (REST API) │ ├── Services (Business Logic) │ ├── Repositories (MongoDB) │ ├── Security (Session-Based Auth) │ └── Models (Entities) │ ├── Database (MongoDB 6.0) │ ├── Collections (users, books, carts, orders, reviews) │ └── Sessions (spring_session) │ └── Docker (Containerization) ├── Frontend Container ├── Backend Container └── MongoDB Container
---

## 🚀 Quick Start

### Prerequisites
- **Java 21** or higher
- **Node.js 18+** and npm
- **Docker & Docker Compose** (for containerization)
- **Maven 3.8+**
- **MongoDB 6.0** (if running locally without Docker)

### Installation with Docker (Recommended)

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/bookstore. git
cd bookstore

# 2. Create . env file
cp .env.example .env

# 3. Run with Docker Compose
docker-compose up --build

# Wait a few minutes for initialization... 

# 4. Access the application
Frontend:    http://localhost
Backend:   http://localhost:8080
MongoDB:   localhost:27017

cd backend

# Install dependencies
mvn clean install

# Run MongoDB locally (requires MongoDB installed)
mongod

# Run the application (in another terminal)
mvn spring-boot:run

# Backend will be available at http://localhost:8080

cd frontend

# Install dependencies
npm install

# Run dev server
npm run dev

# Frontend will be available at http://localhost:5173
📋 REST API Endpoints
POST   /register         - Register a new user
POST   /login            - Login to account
POST   /logout           - Logout from account
GET    /me               - Get current user profile
GET    /check            - Check authentication status
📚 Books (/api/books) - PUBLIC
GET    /                 - Get all books (pagination)
GET    /search           - Search by title, author, genre
GET    /featured         - Get featured books
GET    /{id}             - Get book details
POST   /                 - Create book (ADMIN)
PUT    /{id}             - Update book (ADMIN)
DELETE /{id}             - Delete book (ADMIN)
🛒 Cart (/api/cart) - AUTHENTICATED
GET    /                 - Get user's cart
POST   /add              - Add item to cart
PUT    /items/{bookId}   - Update item quantity
DELETE /items/{bookId}   - Remove item from cart
DELETE /                 - Clear cart
GET    /count            - Get number of items in cart
📦 Orders (/api/orders) - AUTHENTICATED
POST   /                 - Create new order
GET    /                 - Get my orders
GET    /{id}             - Get order details
POST   /{id}/cancel      - Cancel order
PUT    /{id}/status      - Update order status (ADMIN)
PUT    /{id}/tracking    - Update tracking number (ADMIN)
⭐ Reviews (/api/reviews)
GET    /book/{bookId}    - Get book reviews (PUBLIC)
POST   /                 - Add review (AUTHENTICATED)
GET    /user             - Get my reviews (AUTHENTICATED)
PUT    /{id}             - Update review (AUTHENTICATED)
DELETE /{id}             - Delete review (AUTHENTICATED)
POST   /{id}/helpful     - Mark as helpful (AUTHENTICATED)
POST   /{id}/unhelpful   - Mark as unhelpful (AUTHENTICATED)
👤 Users (/api/users) - AUTHENTICATED
GET    /profile          - Get profile
PUT    /profile          - Update profile
POST   /favorites/{id}   - Add to favorites
DELETE /favorites/{id}   - Remove from favorites
GET    /favorites        - Get favorite books list
🧪 Testing
cd backend

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=AuthControllerTest

# Run with coverage
mvn test jacoco:report
Frontend Tests (Vitest)
cd frontend

# Run all tests
npm test

# Run in watch mode
npm run test:watch

# Run with UI interface
npm run test:ui

# Generate coverage report
npm run test:coverage
Integration Tests
# Uses Testcontainers for MongoDB
cd backend
mvn test -Dtest=*IntegrationTest
📊 Technology Stack
Backend
Technology	Version	Purpose
Java	21	Programming Language
Spring Boot	3.2.0	Web Framework
Spring Security	6.1.0	Authentication & Authorization
Spring Data MongoDB	Latest	Database ORM
Spring Session	Latest	Session Management
Maven	3.8+	Package Manager
JUnit 5	Latest	Testing Framework
Testcontainers	1.19.0	Docker for Tests
Frontend
Technology	Version	Purpose
React	18.2	UI Framework
Vite	5.0	Build Tool
TypeScript	Latest	Type Safety
Axios	1.6	HTTP Client
React Router	6.20	Routing
Vitest	Latest	Testing Framework
React Testing Library	14.0	Component Testing
Database & Infrastructure
Technology	Version	Purpose
MongoDB	6.0	NoSQL Database
Docker	Latest	Containerization
Docker Compose	Latest	Orchestration
Nginx	Alpine	Reverse Proxy
Database & Infrastructure
Technology	Version	Purpose
MongoDB	6.0	NoSQL Database
Docker	Latest	Containerization
Docker Compose	Latest	Orchestration
Nginx	Alpine	Reverse Proxy
📁 Project Structure
bookstore/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/bookstore/
│   │   │   │   ├── BookstoreApplication.java
│   │   │   │   ├── controller/          (REST Endpoints)
│   │   │   │   ├── service/             (Business Logic)
│   │   │   │   ├── repository/          (Data Access)
│   │   │   │   ├── model/               (Entities)
│   │   │   │   ├── dto/                 (Data Transfer Objects)
│   │   │   │   ├── security/            (Authentication)
│   │   │   │   ├── config/              (Configuration)
│   │   │   │   └── exception/           (Error Handling)
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── application-docker.yml
│   │   └── test/
│   │       └── java/com/example/bookstore/
│   │           ├── controller/          (Controller Tests)
│   │           └── service/             (Service Tests)
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/
│   ├── src/
│   │   ├── components/                  (Reusable Components)
│   │   ├── pages/                       (Page Components)
│   │   ├── context/                     (Global State)
│   │   ├── api/                         (API Integration)
│   │   ├── hooks/                       (Custom Hooks)
│   │   ├── styles/                      (CSS Files)
│   │   ├── __tests__/                   (Tests)
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── public/
│   ├── index.html
│   ├── vite.config.ts
│   ├── vitest.config.ts
│   ├── package.json
│   ├── Dockerfile
│   └── nginx.conf
│
├── docker-compose.yml
├── . env. example
└── README.md
🔄 Authentication Flow
1️⃣ REGISTRATION
   User → /register → Create Account → Auto-Login → Home

2️⃣ LOGIN
   User → /login → Verify Credentials → Create Session → Home

3️⃣ SESSION MANAGEMENT
   ├─ Session stored in MongoDB (spring_session collection)
   ├─ JSESSIONID cookie sent to browser
   ├─ Each request includes JSESSIONID in cookie
   └─ Server loads session and authenticates user

4️⃣ AUTHENTICATED REQUESTS
   User → API Call (with JSESSIONID) → Verify Session → Execute → Response

5️⃣ LOGOUT
   User → /logout → Invalidate Session → Clear Cookie → Login Page
🐳 Docker Deployment
System Requirements
Docker Engine 20.10+
Docker Compose 2.0+
4GB RAM minimum
10GB free disk space
Running
# 1. Check Docker is installed
docker --version
docker-compose --version

# 2. Create . env file
cp .env.example .env

# 3. Start containers
docker-compose up --build

# 4. Check status
docker-compose ps

# 5. View logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mongo
Stopping
# Stop containers (keep data)
docker-compose stop

# Remove containers (keep data)
docker-compose down

# Remove everything including data
docker-compose down -v
Useful Commands
# View logs of specific service
docker-compose logs -f backend    # Backend logs
docker-compose logs -f frontend   # Frontend logs
docker-compose logs -f mongo      # MongoDB logs

# Access container shell
docker-compose exec backend bash  # Backend shell
docker-compose exec mongo mongosh # MongoDB shell

# Restart service
docker-compose restart backend

# Rebuild image
docker-compose build --no-cache backend

# View resource usage
docker stats
🌍 Production Deployment
AWS EC2 Deployment
# 1. Connect to server
ssh -i your-key.pem ec2-user@your-ec2-ip

# 2. Install Docker and Docker Compose
sudo apt-get update
sudo apt-get install docker.io docker-compose

# 3. Clone repository
git clone https://github.com/yourusername/bookstore.git
cd bookstore

# 4. Update . env for production
nano .env
# VITE_API_URL=https://yourdomain.com/api
# JWT_SECRET=your-super-secret-production-key-change-this
# SECURE_COOKIES=true

# 5. Run
docker-compose -f docker-compose.prod.yml up -d

# 6. Setup Nginx (SSL/TLS)
# ...  (HTTPS configuration)
🐛 Troubleshooting
Backend Issues
Problem	Solution
MongoDB connection fails	Check URI in application.yml and MongoDB is running
Port 8080 already in use	Change port in application.yml or docker-compose.yml
Session not created	Verify Spring Session MongoDB configuration
CORS errors	Check CorsConfig for correct allowedOrigins
Frontend Issues
Problem	Solution
API calls fail	Check VITE_API_URL and backend is running
Cookies not sent	Ensure withCredentials: true in axios client
Session expired	User needs to login again
Docker Issues
Problem	Solution
Containers won't start	Run docker-compose logs service-name for details
Out of memory	Increase Docker memory limit
Port already in use	Change port mapping in docker-compose.yml
📈 Performance Optimization
Backend
✅ Database indexing on frequently used fields
✅ Connection pooling for MongoDB
✅ Request/Response compression
✅ Caching strategy for books and reviews
Frontend
✅ Code splitting with React Router
✅ Lazy loading for book images
✅ Optimized bundle size (Vite)
✅ Local storage for data caching
Database
✅ Indexed queries for searching
✅ Aggregation pipelines for statistics
✅ TTL indexes for session history
🤝 Contributing
Contributions are welcome! Please follow these steps:

bash
# 1. Fork the repository
# 2. Create feature branch
git checkout -b feature/amazing-feature

# 3. Make commits
git commit -m 'Add amazing feature'

# 4. Push to branch
git push origin feature/amazing-feature

# 5. Open Pull Request
Contributor Guidelines
Follow Java and JavaScript coding standards
Add tests for new features
Update README if necessary
Use meaningful commit messages
📝 License
This project is licensed under the MIT License - see LICENSE file for details.

Code
MIT License

Copyright (c) 2026 BookStore

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction... 
👨‍💻 Author
[Andrii Shevchenko]

GitHub: @gunners2004
Email: sheva1988andrei@gmail.com

