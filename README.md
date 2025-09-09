# 🐾 Petland Backend API

Welcome to **Petland**, a robust and scalable backend API designed for managing operations of a modern pet shop. Built with Java and Spring Boot, this system handles everything from customer and pet management to product sales, service scheduling, and vaccination tracking. It’s modular, secure, and production-ready.

---

## 🚀 Project Overview

Petland is a backend solution tailored for pet shop businesses. It centralizes operations such as:

- 📦 Inventory control (products and vaccines)
- 👥 Customer and employee management
- 🐾 Pet registration and tracking
- 🛁 Scheduling and managing pet care services (grooming, checkups, lab tests)
- 💰 Product sales and billing
- 💉 Vaccination tracking
- 📊 Revenue and service reporting

---

## 🧩 Tech Stack

| Layer              | Technology                          |
|-------------------|-------------------------------------|
| Language           | Java                                |
| Framework          | Spring Boot                         |
| Security           | Spring Security + JWT               |
| Architecture       | Layered (Domain-Driven Design)      |
| Containerization   | Docker + Docker Compose             |
| Documentation      | Swagger                             |
| Testing            | JUnit + Unitary e Integration Tests |
| Database           | Relational (e.g., PostgreSQL)       |

---

## 📚 Features

### 🐶 Pet Management
- Register multiple pets per customer
- Track pet details: name, breed, age, species, weight
- Link pets to services and purchases

### 👤 Customer Management
- Email validation and secure login
- View service history, purchases, and appointments

### 🧼 Services & Appointments
- Schedule services like grooming, bathing, medical checkups
- Enforce time constraints (min 1h before, max 30 days ahead)
- Cancel or reschedule appointments
- Generate appointment PDFs

### 💼 Employee Management
- Assign employees to departments (MEDICAL, CARE, etc.)
- Audit employee actions and updates

### 🛍️ Product & Sales
- Manage product inventory, expiration, pricing
- Handle multi-item sales with payment methods (CASH, CREDIT_CARD)
- Update stock on purchase

### 💉 Vaccination Tracking
- Register vaccines and vaccination events
- Link vaccinations to pets and customers

### 📊 Reporting & Dashboards
- Sales reports by product, vaccine, or period
- Service usage reports
- PDF export of financial summaries

### 🔐 Security
- Role-based access (ADMIN, CUSTOMER)
- JWT authentication with token expiration
- Soft delete for data integrity and traceability

---

## 📘 API Endpoints Overview

### Customer
- Create, Read by ID, Update, Delete
- List all customers
- Find pets, sales, services, consultations, appointments by customer

### Pet
- Create, Read by ID, Update, Delete
- List all pets

### Employee
- Create, Read by ID, Update, Delete
- List employees with filters

### Product
- Create, Read by ID, Update, Delete
- Update stock
- List all products

### Sale & ItemSale
- Create, Read by ID, Delete
- List all sales
- Manage sale items

### Vaccination & Vaccine
- Create, Read by ID, Update, Delete
- List all vaccinations and vaccines

### PetCare Services
- Create, Read by ID or Customer, Delete
- Paginated list of services

### Consultations
- Create, Read by ID
- List by customer ID or paginated with filters

### Appointment
- Schedule, Reschedule, Cancel
- Toggle status
- Read by ID
- List by filters or customer

---

## 📂 Business Rules

- **Soft Delete**: Entities marked as `DELETED` to preserve history
- **Audit Logging**: Tracks who modified what and when
- **Status Control**: Entities use `EntityStatus` for lifecycle management
- **Error Handling**: Structured responses via `ErrorMessageDTO` and `ErrorResponseDTO`

---

## 🛠️ Development Activities

- ✅ Defined business rules and system requirements
- ✅ Chose layered backend architecture
- ✅ Developed and refactored backend code
- ✅ Implemented unit and integration tests
- ✅ Documented API with Swagger
- ✅ Dockerized the application and published to Docker Hub
- ✅ Modeled system with UML diagrams

---

## 🎯 Challenges Tackled

- 🔐 Implemented Spring Security with JWT
- 🧱 Built layered architecture with domain modularization
- 🧠 Applied SOLID principles and design patterns

---

## 📦 Infrastructure

- Modular domain-based architecture
- Dockerized services with Compose
- Swagger UI for live API documentation
- UML diagrams for system modeling

---

## 📄 Getting Started

```bash
# Clone the repository
https://github.com/CaiquePirs/petland-backend-api.git

# Add your environment variables

# Navigate to the project directory
cd petland-backend

# Build the project
mvn clean install

# Run with Docker
docker-compose up
```

Access Swagger UI at: http://localhost:8080/swagger-ui.html

## 📌 Future Improvements
- Add CI/CD pipeline
- Implement role-based dashboards
- Add notification system for appointments

## 🐾 Author
Caique Backend Developer passionate about clean architecture, scalable systems, and pet-friendly tech solutions.