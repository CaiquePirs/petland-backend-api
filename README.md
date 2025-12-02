# 🐾 Petland Backend API

Welcome to **Petland**, a robust and scalable backend API developed to manage the operations of a modern pet shop. Built with Java and Spring Boot, this application handles everything: from managing customers and pets to product sales, service scheduling, and vaccine control. It is modular, secure, and production-ready.

---

## 🚀 Project Overview

Petland is a backend solution designed for pet shop businesses. It centralizes operations such as:

- 📦 Inventory control (products and vaccines)
- 👥 Customer and employee management
- 🐾 Pet registration and tracking
- 🛁 Scheduling and management of pet services (grooming, consultations, exams)
- 💰 Product sales and billing
- 💉 Vaccination control
- 📊 Revenue and service reports
- 🗓️ PDF generation for appointments and financial reports

---

## 🧩 Tech Stack

| Layer              | Technology                             |
|--------------------|----------------------------------------|
| Language           | Java                                   |
| Framework          | Spring Boot                            |
| Security           | Spring Security + JWT                  |
| Architecture       | Layered (Domain-Driven Design)         |
| Containerization   | Docker + Docker Compose                |
| Documentation      | Swagger                                |
| Testing            | JUnit + Unit and Integration Tests     |
| Database           | Relational (e.g., PostgreSQL)          |

---

## 📚 Features

### 🐶 Pet Management
- Register multiple pets per customer
- Track data: name, breed, age, species, weight
- Associate pets with services and purchases

### 👤 Customer Management
- Email validation and secure login
- View history of services, purchases, and appointments

### 🧼 Services & Scheduling
- Schedule services such as grooming, bathing, medical consultations
- Time rules (minimum 1h before, maximum 30 days ahead)
- Cancel or reschedule
- Generate appointment PDFs

### 💼 Employee Management
- Assign employees to departments (MEDICAL, CARE, etc.)
- Audit actions and updates

### 🛍️ Products & Sales
- Manage inventory, expiration dates, and prices
- Sales with multiple items and payment methods (CASH, CARD)
- Update inventory after purchase

### 💉 Vaccination Control
- Register vaccines and vaccination events
- Associate vaccines with pets and customers

### 📊 Reports & Dashboards
- Sales reports by product, vaccine, or period
- Service and vaccination sales reports
- Export financial summaries in PDF

### 🔐 Security
- Role-based access (ADMIN, CUSTOMER)
- Authentication with JWT and token expiration
- Soft delete for data integrity and traceability

---

## 📘 API Endpoints Overview

### Customer
- Create, Get by ID, Update, Delete
- List all customers
- Retrieve pets, sales, services, consultations, and appointments by customer

### Pet
- Create, Get by ID, Update, Delete
- List all pets

### Employee
- Create, Get by ID, Update, Delete
- List employees with filters

### Product
- Create, Get by ID, Update, Delete
- Update inventory
- List all products

### Sale & SaleItem
- Create, Get by ID, Delete
- List all sales
- Manage sale items

### Vaccination & Vaccine
- Create, Get by ID, Update, Delete
- List all vaccinations and vaccines

### PetCare Services
- Create, Get by ID or Customer, Delete
- Paginated service listing

### Consultations
- Create, Get by ID
- List by customer ID or with paginated filters

### Appointment
- Schedule, Reschedule, Cancel
- Toggle status
- Get by ID
- List by filters or customer

---

## 📂 Business Rules

- **Soft Delete**: Entities marked as `DELETED` to preserve history
- **Auditing**: Tracks who modified what and when
- **Status Control**: Entities use `EntityStatus` to manage lifecycle
- **Error Handling**: Structured responses via `ErrorMessageDTO` and `ErrorResponseDTO`

---

## 🛠️ Development Activities

- ✅ Definition of business rules and system requirements
- ✅ Choice of layered backend architecture
- ✅ Backend code development and refactoring
- ✅ Implementation of unit and integration tests
- ✅ API documentation with Swagger
- ✅ Application containerization and publishing on Docker Hub
- ✅ System modeling with UML diagrams

---

## 🎯 Challenges Faced

- 🔐 Implementation of Spring Security with JWT
- 🧱 Building layered architecture with domain modularization
- 🧠 Applying SOLID principles and design patterns

---

## 📦 Infrastructure

- Modular architecture based on domains
- Services containerized with Docker Compose
- Swagger UI for interactive API documentation
- UML diagrams for system modeling

---

## 📄 Getting Started

```bash
# Clone the repository
https://github.com/CaiquePirs/petland-backend-api.git

# Add your environment variables
- create a .env file

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
- Implement dashboards by user type
- Add notification system for appointments

## 🐾 Author
Petland Backend API was developed by Caique Pires. Contributions are welcome!
