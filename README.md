┌─────────────────────────────────────────────────────────────────────────────┐
│                         BOOKSTORE FULLSTACK APPLICATION                     │
│                      Java 21 + Spring Boot 3 + React Vite                   │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                            FRONTEND (React + Vite)                           │
│                         http://localhost:5173                                │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                        App. tsx (Router)                            │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                          │                                                   │
│        ┌─────────────────┼─────────────────┬─────────────────┐              │
│        │                 │                 │                 │              │
│    ┌───▼────┐      ┌────▼────┐      ┌────▼────┐      ┌────▼────┐         │
│    │ Home   │      │ Login   │      │ Books   │      │ Cart    │         │
│    │Page    │      │ Page    │      │ Page    │      │ Page    │         │
│    └────────┘      └────────┘      └────────┘      └────────┘         │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    CONTEXTS (Global State)                         │    │
│  ├─────────────────────────────────────────────────────────────────────┤    │
│  │                                                                    │    │
│  │  ┌──────────────────────┐      ┌──────────────────────┐          │    │
│  │  │ AuthContext          │      │ CartContext          │          │    │
│  │  ├──────────────────────┤      ├──────────────────────┤          │    │
│  │  │ • user               │      │ • cart               │          │    │
│  │  │ • isAuthenticated    │      │ • items              │          │    │
│  │  │ • login()            │      │ • addToCart()        │          │    │
│  │  │ • logout()           │      │ • removeFromCart()   │          │    │
│  │  │ • register()         │      │ • updateQuantity()   │          │    │
│  │  └──────────────────────┘      └──────────────────────┘          │    │
│  │                                                                    │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    COMPONENTS (Reusable UI)                        │    │
│  ├─────────────────────────────────────────────────────────────────────┤    │
│  │ Header │ BookCard │ BookList │ Pagination │ Cart │ ReviewList  │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                    API CLIENT (Axios)                             │    │
│  ├─────────────────────────────────────────────────────────────────────┤    │
│  │                                                                    │    │
│  │  • client.ts          (Base Axios instance)                       │    │
│  │  • auth.ts            (login, register, logout)                   │    │
│  │  • books.ts           (CRUD для книг)                             │    │
│  │  • cart.ts            (Управление корзиной)                       │    │
│  │  • orders.ts          (Создание заказов)                          │    │
│  │  • reviews.ts         (Отзывы о книгах)                           │    │
│  │                                                                    │    │
│  │  Все запросы с cookies:  { withCredentials: true }                │    │
│  │  (Сессия отправляется автоматически)                            │    │
│  │                                                                    │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ HTTP Requests
                                    │ + JSESSIONID Cookie
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│                     BACKEND (Spring Boot 3 + Java 21)                        │
│                         http://localhost:8080/api                           │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                  SECURITY (Spring Security 6.x)                   │    │
│  ├─────────────────────────────────────────────────────────────────────┤    │
│  │                                                                    │    │
│  │  SecurityConfig.java                                             │    │
│  │  ├─ Session Management                                           │    │
│  │  │  ├─ sessionCreationPolicy = IF_REQUIRED                       │    │
│  │  │  ├─ sessionFixation = migrateSession() ← Защита от атак      │    │
│  │  │  └─ maximumSessions = 1                                       │    │
│  │  │                                                               │    │
│  │  ├─ Authorization Rules                                          │    │
│  │  │  ├─ /api/auth/** → PERMITALL                                 │    │
│  │  │  ├─ /api/books/** → PERMITALL                                │    │
│  │  │  ├─ /api/cart/** → AUTHENTICATED                             │    │
│  │  │  ├─ /api/orders/** → AUTHENTICATED                           │    │
│  │  │  └─ /api/admin/** → HASROLE(ADMIN)                           │    │
│  │  │                                                               │    │
│  │  ├─ CORS Configuration                                           │    │
│  │  │  ├─ allowedOrigins:  localhost:5173, localhost:3000          │    │
│  │  │  ├─ allowedMethods:  GET, POST, PUT, DELETE                   │    │
│  │  │  ├─ allowCredentials: true ← Для cookies                     │    │
│  │  │  └─ allowedHeaders: *                                        │    │
│  │  │                                                               │    │
│  │  └─ Session Cookies                                              │    │
│  │     ├─ Name: JSESSIONID                                          │    │
│  │     ├─ HttpOnly: true ← Защита от XSS                           │    │
│  │     ├─ SameSite: lax ← Защита от CSRF                           │    │
│  │     └─ MaxAge: 1800 сек (30 мин)                                │    │
│  │                                                                    │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                    │                                         │
│                                    ▼                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                     CONTROLLERS (REST API)                         │    │
│  ├─────────────────────────────────────────────────────────────────────┤    │
│  │                                                                    │    │
│  │  AuthController                                                  │    │
│  │  ├─ POST   /register      → Регистрация + Auto-login             │    │
│  │  ├─ POST   /login         → Login + Создание сессии              │    │
│  │  ├─ POST   /logout        → Logout + Удаление сессии             │    │
│  │  ├─ GET    /me            → Текущий пользователь                 │    │
│  │  └─ GET    /check         → Проверка статуса                     │    │
│  │                                                                    │    │
│  │  BookController                                                  │    │
│  │  ├─ GET    /              → Все книги (PUBLIC)                   │    │
│  │  ├─ GET    /search        → Поиск (PUBLIC)                       │    │
│  │  ├─ GET    /{id}          → Деталь книги (PUBLIC)                │    │
│  │  ├─ POST   /              → Создать (ADMIN)                      │    │
│  │  ├─ PUT    /{id}          → Обновить (ADMIN)                     │    │
│  │  └─ DELETE /{id}          → Удалить (ADMIN)                      │    │
│  │                                                                    │    │
│  │  CartController                                                  │    │
│  │  ├─ GET    /              → Получить корзину (AUTH)              │    │
│  │  ├─ POST   /add           → Добавить товар (AUTH)                │    │
│  │  ├─ PUT    /items/{id}    → Обновить кол-во (AUTH)               │    │
│  │  ├─ DELETE /items/{id}    → Удалить товар (AUTH)                 │    │
│  │  └─ DELETE /              → Очистить корзину (AUTH)              │    │
│  │                                                                    │    │
│  │  OrderController                                                 │    │
│  │  ├─ POST   /              → Создать заказ (AUTH)                 │    │
│  │  ├─ GET    /              → Мои заказы (AUTH)                    │    │
│  │  ├─ GET    /{id}          → Деталь заказа (AUTH)                 │    │
│  │  ├─ POST   /{id}/cancel   → Отменить (AUTH)                      │    │
│  │  └─ PUT    /{id}/status   → Статус (ADMIN)                       │    │
│  │                                                                    │    │
│  │  ReviewController                                                │    │
│  │  ├─ POST   /              → Добавить отзыв (AUTH)                │    │
│  │  ├─ GET    /book/{id}     → Отзывы о книге (PUBLIC)              │    │
│  │  ├─ GET    /user          → Мои отзывы (AUTH)                    │    │
│  │  ├─ PUT    /{id}          → Обновить (AUTH)                      │    │
│  │  └─ DELETE /{id}          → Удалить (AUTH)                       │    │
│  │                                                                    │    │
│  │  UserController                                                  │    │
│  │  ├─ GET    /profile       → Мой профиль (AUTH)                   │    │
│  │  ├─ PUT    /profile       → Обновить профиль (AUTH)              │    │
│  │  ├─ POST   /favorites/{id} → В избранное (AUTH)                  │    │
│  │  └─ GET    /favorites     → Мои избранные (AUTH)                 │    │
│  │                                                                    │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                    │                                         │
│                                    ▼                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                   SERVICES (Business Logic)                        │    │
│  ├─────────────────────────────────────────────────────────────────────┤    │
│  │                                                                    │    │
│  │  UserService                                                     │    │
│  │  ├─ registerUser()         → Создание пользователя               │    │
│  │  ├─ getUserByEmail()       → Поиск по email                      │    │
│  │  └─ updateUserProfile()    → Обновление профиля                  │    │
│  │                                                                    │    │
│  │  BookService                                                     │    │
│  │  ├─ getAllBooks()          → Все книги (пагинация)               │    │
│  │  ├─ searchByTitle()        → Поиск по названию                   │    │
│  │  ├─ searchByAuthor()       → Поиск по автору                     │    │
│  │  └─ getFeaturedBooks()     → Избранные книги                     │    │
│  │                                                                    │    │
│  │  CartService                                                     │    │
│  │  ├─ getOrCreateCart()      → Получить/Создать корзину            │    │
│  │  ├─ addToCart()            → Добавить товар                      │    │
│  │  ├─ updateCartItem()       → Изменить количество                 │    │
│  │  └─ recalculateTotal()     → Пересчет сумм                       │    │
│  │                                                                    │    │
│  │  OrderService                                                    │    │
│  │  ├─ createOrder()          → Создание заказа                     │    │
│  │  ├─ getUserOrders()        → Заказы пользователя                 │    │
│  │  └─ updateOrderStatus()    → Обновление статуса (ADMIN)          │    │
│  │                                                                    │    │
│  │  ReviewService                                                   │    │
│  │  ├─ addReview()            → Добавить отзыв                      │    │
│  │  ├─ getReviewsByBook()     → Отзывы о книге                      │    │
│  │  └─ updateBookRating()     → Пересчет рейтинга                   │    │
│  │                                                                    │    │
│  │  CustomUserDetailsService                                        │    │
│  │  └─ loadUserByUsername()   → Загрузить для Spring Security       │    │
│  │                                                                    │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                    │                                         │
│                                    ▼                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                   REPOSITORIES (Data Access)                       │    │
│  ├─────────────────────────────────────────────────────────────────────┤    │
│  │                                                                    │    │
│  │  UserRepository extends MongoRepository<User, String>            │    │
│  │  ├─ findByEmail(email)                                            │    │
│  │  └─ findByUsername(username)                                      │    │
│  │                                                                    │    │
│  │  BookRepository extends MongoRepository<Book, String>            │    │
│  │  ├─ findByTitleContaining(title, pageable)                        │    │
│  │  ├─ findByAuthor(author, pageable)                                │    │
│  │  └─ findByFeaturedTrue(pageable)                                  │    │
│  │                                                                    │    │
│  │  CartRepository extends MongoRepository<Cart, String>            │    │
│  │  └─ findByUserId(userId)                                          │    │
│  │                                                                    │    │
│  │  OrderRepository extends MongoRepository<Order, String>          │    │
│  │  ├─ findByUserId(userId, pageable)                                │    │
│  │  └─ findByOrderNumber(orderNumber)                                │    │
│  │                                                                    │    │
│  │  ReviewRepository extends MongoRepository<Review, String>        │    │
│  │  ├─ findByBookId(bookId, pageable)                                │    │
│  │  └─ findByUserId(userId, pageable)                                │    │
│  │                                                                    │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                    │                                         │
│                                    ▼                                         │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │                   MODELS (Domain Entities)                         │    │
│  ├─────────────────────────────────────────────────────────────────────┤    │
│  │                                                                    │    │
│  │  User {                                                           │    │
│  │    id, email, username, password, firstName, lastName,           │    │
│  │    phoneNumber, address, role, active, favoriteBooks,            │    │
│  │    createdAt, lastLogin                                           │    │
│  │  }                                                                │    │
│  │                                                                    │    │
│  │  Book {                                                           │    │
│  │    id, title, author, description, publisher, isbn,              │    │
│  │    genre, price, discountPrice, quantityInStock, imageUrl,       │    │
│  │    averageRating, ratingCount, featured, active                  │    │
│  │  }                                                                │    │
│  │                                                                    │    │
│  │  Cart {                                                           │    │
│  │    id, userId, items[], subtotal, tax, total,                    │    │
│  │    createdAt, updatedAt                                           │    │
│  │  }                                                                │    │
│  │                                                                    │    │
│  │  Order {                                                          │    │
│  │    id, orderNumber, items[], total, status,                      │    │
│  │    shippingAddress, billingAddress, paymentMethod,               │    │
│  │    trackingNumber, shippedDate, deliveredDate                    │    │
│  │  }                                                                │    │
│  │                                                                    │    │
│  │  Review {                                                         │    │
│  │    id, bookId, userId, rating, title, comment,                   │    │
│  │    verified, helpfulCount, unhelpfulCount                         │    │
│  │  }                                                                │    │
│  │                                                                    │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    │ MongoDB Queries
                                    │
                                    ▼
┌──────────────────────────────────────────────────────────────────────────────┐
│                    DATABASE (MongoDB 6.0)                                    │
│                         mongodb://mongo:27017                               │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  Database:  bookstoredb                                                      │
│  │                                                                          │
│  ├─ Collection: users                                                      │
│  │  ├─ _id (ObjectId)                                                      │
│  │  ├─ email (Indexed, Unique)                                             │
│  │  ├─ username (Indexed, Unique)                                          │
│  │  ├─ password (BCrypt)                                                   │
│  │  ├─ role (USER | ADMIN)                                                 │
│  │  └─ ...  other fields                                                    │
│  │                                                                          │
│  ├─ Collection: books                                                      │
│  │  ├─ _id (ObjectId)                                                      │
│  │  ├─ title (Indexed)                                                     │
│  │  ├─ author (Indexed)                                                    │
│  │  ├─ genre (Indexed)                                                     │
│  │  ├─ price, quantityInStock                                              │
│  │  └─ ... other fields                                                    │
│  │                                                                          │
│  ├─ Collection: carts                                                      │
│  │  ├─ _id (ObjectId)                                                      │
│  │  ├─ userId (Indexed)                                                    │
│  │  ├─ items[] (CartItem documents)                                        │
│  │  └─ totals (subtotal, tax, total)                                       │
│  │                                                                          │
│  ├─ Collection: orders                                                     │
│  │  ├─ _id (ObjectId)                                                      │
│  │  ├─ userId (Indexed)                                                    │
│  │  ├─ orderNumber                                                         │
│  │  ├─ items[] (OrderItem documents)                                       │
│  │  ├─ status (PENDING | PROCESSING | SHIPPED | DELIVERED)                │
│  │  └─ ...  other fields                                                    │
│  │                                                                          │
│  ├─ Collection: reviews                                                    │
│  │  ├─ _id (ObjectId)                                                      │
│  │  ├─ bookId (Indexed)                                                    │
│  │  ├─ userId (Indexed)                                                    │
│  │  ├─ rating (1-5)                                                        │
│  │  └─ ... other fields                                                    │
│  │                                                                          │
│  └─ Collection: spring_session (Session Storage)                           │
│     ├─ _id (Session ID)                                                    │
│     ├─ SPRING_SECURITY_CONTEXT (Authentication)                           │
│     ├─ creationTime                                                        │
│     ├─ lastAccessedTime                                                    │
│     └─ expiryTime (1800 сек = 30 мин)                                      │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                    DOCKER CONTAINERS (docker-compose)                       │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ┌──────────────────────┐  ┌──────────────────────┐  ┌──────────────────┐  │
│  │   mongo: 6.0          │  │ spring-boot: 21       │  │   nginx:alpine   │  │
│  │  (MongoDB)           │  │   (Backend)          │  │   (Frontend)     │  │
│  ├──────────────────────┤  ├──────────────────────┤  ├──────────────────┤  │
│  │ Port:  27017          │  │ Port: 8080           │  │ Port: 80         │  │
│  │ Volume: mongo_data   │  │ Depends on: mongo    │  │ Depends on:       │  │
│  │ Network: bookstore   │  │ Network: bookstore   │  │ backend          │  │
│  │                      │  │                      │  │ Network: bookstore
│  │ Env:                  │  │ Env:                 │  │                  │  │
│  │ • MONGO_ROOT_USERNAME│  │ • SPRING_... =...      │  │ Env:             │  │
│  │ • MONGO_ROOT_PASSWORD│  │ • JWT_SECRET         │  │ • VITE_API_URL   │  │
│  │ • MONGO_INITDB_DB    │  │ • SERVER_PORT        │  │                  │  │
│  │                      │  │ • PROFILES=docker    │  │                  │  │
│  └──────────────────────┘  └──────────────────────┘  └──────────────────┘  │
│           │                          │                         │           │
│           └──────────────────────────┼─────────────────────────┘           │
│                                      │                                      │
│                          Docker Network:  bookstore                         │
│                          (Internal communication)                          │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                           AUTHENTICATION FLOW                                │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  1. REGISTRATION                                                            │
│     ┌─────────────┐           ┌────────────────┐           ┌───────────┐   │
│     │  Frontend   │──POST──→  │  AuthController│──POST──→  │ UserService│  │
│     │ /register   │ (JSON)    │  /register     │           │ Register  │   │
│     └─────────────┘           └────────────────┘           └───────────┘   │
│             ↑                          ↓                           ↓        │
│             │                    Auto-Login                   Save to DB    │
│             │                          ↓                           ↓        │
│             │                  Create Auth                    MongoDB       │
│             │                          ↓                           ↓        │
│             │─────────────────── JSESSIONID Cookie ←──────────────┘        │
│                                                                              │
│  2. LOGIN                                                                   │
│     ┌─────────────┐           ┌────────────────┐           ┌──────────┐   │
│     │  Frontend   │──POST──→  │  AuthController│──POST──→  │ AuthMgr  │   │
│     │  /login     │ (JSON)    │  /login        │           │ Authenticate
│     └─────────────┘           └────────────────┘           └──────────┘   │
│             ↑                          ↓                           ↓        │
│             │                  Create SecurityContext        UserDetails   │
│             │                          ↓                           ↓        │
│             │─────────────────── JSESSIONID Cookie ←──────────────┘        │
│                                (httpSession. id)                             │
│             │─────────────────── Store in MongoDB (spring_session)        │
│                                                                              │
│  3. AUTHENTICATED REQUESTS                                                  │
│     ┌─────────────┐           ┌────────────────┐           ┌───────────┐   │
│     │  Frontend   │           │ SecurityFilter │           │ MongoDB   │   │
│     │ /api/cart   │           │     Chain      │           │ Session   │   │
│     │ + JSESSIONID│──GET──→   │ Check Session  │──Query──→ │ Store     │   │
│     │   Cookie    │           │ Load Auth      │           │           │   │
│     └─────────────┘           └────────────────┘           └───────────┘   │
│             ↑                          ↓                                    │
│             │────────────────────────Authorized─────────────────────────┘   │
│                                                                              │
│  4. LOGOUT                                                                  │
│     ┌─────────────┐           ┌────────────────┐           ┌───────────┐   │
│     │  Frontend   │──POST──→  │  AuthController│──DELETE→  │ MongoDB   │   │
│     │  /logout    │           │  /logout       │           │ Session   │   │
│     └─────────────┘           └────────────────┘           └───────────┘   │
│             ↑                          ↓                                    │
│             │────────────────Clear Cookie + Auth─────────────────────────┘   │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                      KEY IMPROVEMENTS & FEATURES                             │
├──────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│  ✅ Session-based Authentication (NO JWT)                                   │
│     • JSESSIONID cookie управляется браузером                              │
│     • Сессии хранятся в MongoDB (распределённые сессии)                   │
│     • HttpOnly & SameSite защита от XSS/CSRF                               │
│                                                                              │
│  ✅ Spring Security 6.x                                                     │
│     • Современный Lambda DSL синтаксис                                     │
│     • sessionFixation() защита от атак                                      │
│     • Одна сессия на пользователя (максимально 1)                          │
│                                                                              │
│  ✅ MongoDB Sessions (Spring Session)                                       │
│     • Сессии хранятся в БД, не в памяти                                    │
│     • Работает в распределённых системах                                   │
│     • Автоматическое управление сроком жизни                              │
│                                                                              │
│  ✅ Role-Based Access Control (RBAC)                                        │
│     • USER роль для обычных пользователей                                  │
│     • ADMIN роль для администраторов                                       │
│     • @PreAuthorize("hasRole('ADMIN')") для контроля доступа               │
│                                                                              │
│  ✅ CORS Configuration                                                      │
│     • allowCredentials: true для отправки cookies                          │
│     • Поддержка фронтенда на другом localhost порту                        │
│                                                                              │
│  ✅ Docker Containerization                                                 │
│     • Микросервисная архитектура (MongoDB, Backend, Frontend)             │
│     • Лёгкое развёртывание и масштабирование                              │
│     • Health checks для каждого сервиса                                    │
│                                                                              │
│  ✅ Comprehensive Testing                                                   │
│     • Integration Tests (Testcontainers + MockMvc)                          │
│     • Frontend Tests (Vitest + React Testing Library)                       │
│     • SecurityContext testing для сессий                                    │
│                                                                              │
└──────────────────────────────────────────────────────────────────────────────┘
