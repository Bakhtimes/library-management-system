# Advanced Library Management System

A production-ready, full-stack Library Management System built using Spring Boot, Spring Security, Thymeleaf, and PostgreSQL. The platform handles secure user onboarding, distinct role-based permissions, automated inventory processing, and global error handling.

Tech Stack & Architecture
Backend: Java 21, Spring Boot 3.x, Spring Data JPA

Security: Spring Security 6.x (Form Login, Role-Based Access Control)

Frontend: Thymeleaf template engine, Bootstrap 5 (CSS framework)

Database: PostgreSQL (with native UUID tracking)

Testing: JUnit 5, Mockito

## Core Features
Identity & Role Management
Authentication Pipeline: Secure custom login and user registration via encrypted passwords using PasswordEncoder.

Distinct Roles:

Reader: Can browse books, view item details, place allocation requests, and access a personal dashboard to track rental deadlines or cancel pending items.

Librarian: Controls inventory desk operations. Can issue pending requests, register new book titles, create initial physical copies (BookCopy), and edit book metadata.

Administrator: System master desk. Can view the system-wide directory registry and globally suspend or activate user accounts to stop bad actors.

## Production-Grade Mechanics
Catalog Pagination: Integrated server-side pagination (Pageable) that merges with search parameters to chunk data loads cleanly.

Global Exception Advisor: Centralized air-traffic error interceptor using @ControllerAdvice to map business logic exceptions, raw 404 Not Found paths, and 403 Access Denied traps directly into a beautiful unified fallback layout page.

## Configuration & Installation
### Prerequisites
   Java JDK 17 or higher installed.

Maven package manager installed.

PostgreSQL server running locally or on a cloud instance.

Database Setup
Log into your PostgreSQL instance (via pgAdmin or psql terminal CLI) and execute the creation query:

SQL

`CREATE DATABASE library_db;`

Application Properties Alignment
   Navigate to src/main/resources/application.properties and configure your database driver connection settings:

Properties

`spring.datasource.url=jdbc:postgresql://localhost:5432/library_db
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password
spring.datasource.driver-class-name=org.postgresql.Driver`

JPA / Hibernate schema sync setting

`spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect`

Intercept unmapped paths for 404 handling

`spring.mvc.throw-exception-if-no-handler-found=true
spring.web.resources.add-mappings=false`

## Running the Application
Execute the following Maven commands in your terminal root project directory:

Bash

`mvn clean install
mvn spring-boot:run`

Once the server boots up safely, open your preferred web browser and navigate to:

`http://localhost:8080/catalog`

## Testing
The service layer and authentication mechanics are fully isolated and tested using Mockito framework mocks. To run unit test suites locally, type:

Bash

`mvn test`