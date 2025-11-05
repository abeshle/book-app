📚 Book App – Backend API
🚀 Overview

Book App is a RESTful backend application built with Spring Boot that powers a full-featured online bookstore.
It supports user registration, authentication via JWT tokens, browsing books and categories, managing a shopping cart, and placing orders.
This project was created to demonstrate a modular, secure, and scalable backend architecture for modern e-commerce systems.

💡 Features

🔐 JWT-based authentication and role-based access control

👥 User roles: ROLE_USER and ROLE_ADMIN

📚 CRUD operations for Books and Categories

🛒 Shopping Cart management

💳 Order creation and history tracking

🧾 Interactive API documentation via Swagger UI

🧩 Tech Stack
Layer	Technology
Language	Java 17+
Framework	Spring Boot
Security	Spring Security + JWT
Database	MySQL
ORM	Hibernate, Spring Data JPA
Mapping	MapStruct
Utilities	Lombok
API Docs	Swagger / OpenAPI
Build Tool	Maven
🗂️ Core Entities

User — contains authentication and role data

Role — defines access level (USER / ADMIN)

Book — represents a book in the store

Category — groups books logically

ShoppingCart — holds selected items for purchase

CartItem — individual book entries in the cart

Order — user purchase record

OrderItem — item details within an order

⚙️ Installation & Setup
1️⃣ Clone the Repository
git clone https://github.com/abeshle/book-app.git
cd book-app

2️⃣ Create a MySQL Database
CREATE DATABASE book_app_db;

3️⃣ Configure Application Properties

Edit src/main/resources/application.properties:

spring.application.name=book-app

spring.datasource.url=jdbc:mysql://localhost:3306/book_app_db?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourPassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

jwt.secret=mySuperSecretKey123
jwt.expiration=3600000

4️⃣ Build and Run
mvn clean install
mvn spring-boot:run

5️⃣ Access Swagger UI

Visit:
👉 http://localhost:8080/swagger-ui/index.html

📖 Controller Overview
🔐 AuthenticationController (/auth)

Handles registration and login.

POST /auth/registration — Register new user

POST /auth/login — Authenticate and return JWT token

📚 BookController (/books)

Manage books in the store.

GET /books — Get all books (USER)

GET /books/{id} — Get books by category ID (USER)

POST /books — Create a book (ADMIN)

PUT /books/{id} — Update a book (ADMIN)

DELETE /books/{id} — Delete a book (ADMIN)

GET /books/search — Search books (USER)

🏷️ CategoryController (/categories)

Manage book categories.

POST /categories — Create new category (ADMIN)

GET /categories — Get all categories (USER)

GET /categories/{id} — Get category by ID (USER)

PUT /categories/{id} — Update category (ADMIN)

DELETE /categories/{id} — Delete category (ADMIN)

GET /categories/{id}/books — Get books by category (USER)

🛒 CartController (/cart)

Manage user’s shopping cart.

GET /cart — View cart (USER)

POST /cart — Add item to cart (USER)

PUT /cart/cart-items/{cartItemId} — Update item quantity (USER)

DELETE /cart/cart-items/{cartItemId} — Remove item (USER)

💳 OrderController (/orders)

Handle user orders.

GET /orders — View order history (USER)

POST /orders — Place new order (USER)

GET /orders/{orderId} — Get order by ID (USER)

GET /orders/{orderId}/items — Get all items in an order (USER)

GET /orders/{orderId}/items/{itemId} — Get a specific order item (USER)

PATCH /orders/{orderId} — Update order status (ADMIN)

💬 Example API Usage (Postman)
1️⃣ Register
POST /auth/registration
Content-Type: application/json

{
"email": "john@example.com",
"password": "123456",
"firstName": "John",
"lastName": "Doe"
}

2️⃣ Login
POST /auth/login

{
"email": "john@example.com",
"password": "123456"
}


Response:

{
"token": "eyJhbGciOiJIUzI1NiJ9..."
}


Use this in all protected requests:

Authorization: Bearer <your_token>

3️⃣ Add Book to Cart
POST /cart
Authorization: Bearer <your_token>
Content-Type: application/json

{
"bookId": 1,
"quantity": 2
}

4️⃣ Place Order
POST /orders
Authorization: Bearer <your_token>

🧭 Application Flow

[User Registers or Logs In]

↓

[JWT Token Generated]

↓

[User Browses Books & Adds to Cart]

↓

[User Places Order]

↓

[Order Saved, Cart Emptied]

🧗 Challenges & Solutions

Challenge	Solution

Implementing JWT authentication securely	Added a JwtAuthenticationFilter and stateless sessions

Mapping DTOs and Entities	Used MapStruct for efficient conversions

Role-based access control	Used @PreAuthorize annotations with Spring Security

Managing lazy-loaded relationships	Used @Transactional and pagination via Spring Data JPA

🧑‍💻 Author

Abeshle
🔗 GitHub Repository