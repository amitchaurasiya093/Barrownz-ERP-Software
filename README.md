# Barrownz ERP Software

A comprehensive **Enterprise Resource Planning (ERP) Software** developed using **Java and Spring Boot** to manage employees, departments, projects, tasks, leaves, clients, salary information, and other organizational operations through a centralized web-based system.

## 📌 Project Overview

Barrownz ERP Software is designed to simplify and centralize day-to-day organizational management.

The system provides separate functionalities for different organizational roles and includes secure authentication, employee management, leave management, project and task management, client management, reporting manager management, salary information, and dashboard-based operations.

The application follows a layered architecture using Spring Boot, REST APIs, JPA/Hibernate, MySQL, JWT-based authentication, and a web-based frontend.

---

## ✨ Key Features

### 🔐 Authentication & Security
- User login and authentication
- JWT-based authentication
- Role-based access control
- Secure request filtering
- JWT token management and blacklist support
- Spring Security integration

### 👨‍💼 Employee Management
- Employee registration and management
- Employee personal information
- Employee education details
- Employee salary information
- Employee dashboard
- Employee-related organizational records

### 🏢 Organization Management
- Department management
- Role management
- Shift management
- Reporting Manager management
- Level management

### 📅 Leave Management
- Leave application
- Leave types
- Leave balance management
- Leave approval/processing
- HR leave management
- Admin leave management
- Leave balance calculation
- Automatic leave balance scheduling
- Holiday management

### 📋 Project & Task Management
- Project management
- Employee project assignment
- Task management
- Task status management
- Task priority management
- Task type management
- Task subject management
- Project and task tracking

### 🤝 Client Management
- Client management
- Client-related project information
- Project details management
- Quote management

### 📧 Email Service
- Email service integration
- Application email configuration
- SMTP-based email functionality

### 📊 Web Dashboards
- Admin dashboard
- Employee dashboard
- User management interfaces
- Leave management interfaces
- Project management interfaces
- Task management interfaces

---

## 🛠️ Technology Stack

### Backend
- Java
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate
- REST APIs
- JWT Authentication

### Database
- MySQL

### Frontend
- HTML5
- CSS3
- JavaScript
- Bootstrap
- AJAX
- Thymeleaf / Server-side web pages

### Tools & Development
- Maven
- Git
- GitHub
- Postman
- Eclipse / Spring Tool Suite
- VS Code

---

## 🏗️ Project Architecture

The project follows a layered Spring Boot architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity / Model
    ↓
MySQL Database
