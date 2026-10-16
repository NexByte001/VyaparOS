 # VYPAAR OS- Management System

 
 
 ## Week 1
 
 
 ## Project Administration

Project Guide: Er. Ram Babu Buri

***Research Area:*** Software Engineering & Web Application Development

***Specializations:*** Java, Spring Boot, MySQL, Spring Security, HTML, CSS, and JavaScript.



## TEAM MEMBERS




| **S. No.** | **Name** | **Enrollment Number** | **Email Address** |
|:---:|---|---|---|
| 1 | Jagrati Sahu | ACEIT24E1ARADF30P064 | [sahujagrati822@gmail.com](mailto:sahujagrati822@gmail.com) |
| 2 | Koshi Jain | ACEIT24E1ARADF40P082 | [gowadiakoshi@gmail.com](mailto:gowadiakoshi@gmail.com) |
| 3 | Navya Raj | ACEIT24E1ARADF30P0109 | [navyahipe18@gmail.com](mailto:navyahipe18@gmail.com) |
| 4 | Palak Sahani | ACEIT24E1ARADF30P117 | [sahanipalak07@gmail.com](mailto:sahanipalak07@gmail.com) |
| 5 | Kumkum Rathore | ACEIT24E1ARADF40P086 | [kumkumrathore@gmail.com](mailto:kumkumrathore@gmail.com) |

---


## Abstract


Vypaar OS is a web-based Management System designed to simplify and streamline the daily operations of salons through a centralized digital platform. Traditional management often involves manual record-keeping and disconnected processes, making it difficult to organize customer information, manage appointments, and maintain operational efficiency. Vypaar OS aims to address these challenges by providing a structured, secure, and user-friendly software solution.

The system is developed using Java and Spring Boot for backend processing, MySQL for database management, and HTML, CSS, and JavaScript for the user interface. Spring Security is incorporated to support secure authentication and role-based access control for administrators and staff members. The application is designed to organize salon-related information and provide a foundation for efficient management of daily business activities.

By integrating backend services, database operations, and security mechanisms into a unified application, Vypaar OS aims to reduce manual effort, improve data organization, and simplify salon administration. The system also provides a foundation for future enhancements, including advanced appointment scheduling, customer management, service tracking, and business reporting.

Ultimately, Vypaar OS seeks to provide a practical and maintainable digital solution for management, helping owners and staff improve operational efficiency and deliver a more organized customer experience.



## WEEK 2 – VYAPAROS
## User Roles, System Architecture, SRS and Database Design

---

## 1. Introduction

VyaparOS is a business management platform designed to organize and simplify daily business operations through a centralized dashboard. It integrates customer management, appointment scheduling, salon service management, billing, inventory, and staff attendance into a unified application.

The system follows a modular architecture to support organized development, maintainability, and collaboration among team members.

## 2. User Roles and Access Management

VyaparOS defines two primary user roles to manage access to different application features.

### 2.1 Admin

The Admin is responsible for managing the business and its operations. Administrative functions include:

- Managing customers and appointments.
- Managing salon services and billing.
- Maintaining product and inventory information.
- Managing staff information and attendance.
- Accessing administrative dashboard features.

### 2.2 Staff

Staff members can access operational features relevant to their responsibilities, depending on the permissions implemented in the application.

These activities may include:

- Viewing assigned appointments.
- Accessing relevant customer information.
- Performing staff-related operational activities.
- Using authorized features of the platform.

### 2.3 Authentication and Authorization

The backend is developed using Java and Spring Boot. The security layer provides the foundation for user registration, login, authentication, and role-based access control.

The purpose of this module is to ensure that users access the features permitted by their assigned roles.

## 3. Software Requirements Specification (SRS)

The Software Requirements Specification defines the objectives, functional requirements, non-functional requirements, user roles, and technical requirements of VyaparOS.

### 3.1 Project Objectives

The primary objectives of VyaparOS are:

- To centralize daily business operations.
- To simplify customer and appointment management.
- To organize salon services and billing activities.
- To provide a foundation for inventory and product management.
- To support staff information and attendance management.
- To provide a centralized dashboard for business administration.
- To organize application development into manageable modules.

### 3.2 Technology Stack

| Component | Technology |
|---|---|
| Backend | Java, Spring Boot |
| Frontend | HTML, CSS, JavaScript |
| Build Tool | Apache Maven |
| Database | H2 for development; MySQL as the intended relational database |
| Security | Spring Security and role-based access management |
| Version Control | Git and GitHub |
| Frontend Deployment | Netlify |

**Note:** The database configuration used in development and the final deployment configuration may differ. Production deployment requires a properly configured persistent database.

## 4. System Architecture

VyaparOS follows a client-server architecture consisting of three primary layers.

### 4.1 Frontend Layer

The frontend is developed using HTML, CSS, and JavaScript. It provides the user interface through which administrators and staff interact with the application's features.

### 4.2 Backend Layer

The backend is developed using Java and Spring Boot. It processes application requests, implements business logic, manages authentication and authorization, and provides APIs for communication with the frontend.

### 4.3 Database Layer

The database layer stores and organizes application records, including user accounts, customers, appointments, services, billing information, products, staff details, and attendance records, according to the implemented schema.

### 4.4 Request Flow

The general interaction between the application layers is as follows:

```text
+----------------------------------+
|          User Interface          |
|       HTML, CSS, JavaScript      |
+----------------------------------+
                 |
                 v
+----------------------------------+
|       Spring Boot Backend        |
|                                  |
|   REST APIs and Business Logic   |
|   Authentication and Security    |
+----------------------------------+
                 |
                 v
+----------------------------------+
|          Database Layer          |
|       H2 / MySQL Configuration   |
+----------------------------------+
```

The frontend sends requests to the backend APIs. The backend processes the requests and interacts with the database when data needs to be retrieved or modified.

## 5. Functional Modules

VyaparOS is organized into the following functional modules.

### Module 1: Authentication and Security

This module provides the foundation for user registration, login, authentication, and role-based access management. It distinguishes between Admin and Staff roles and supports controlled access to application features.

### Module 2: Customer and Appointment Management

This module manages customer information and appointment records. It supports organizing appointments, associating appointments with relevant customers, and maintaining information required for daily scheduling.

### Module 3: Billing and Financial Management

This module is intended to organize billing activities and financial records. It provides the foundation for maintaining transaction information and supporting business financial monitoring.

### Module 4: Inventory and Product Management

This module is designed to maintain product information and support inventory-related operations. It provides the foundation for managing product records and tracking stock information.

### Module 5: Dashboard and Frontend Integration

This module provides a centralized interface for accessing the platform's business management features. JavaScript communicates with the Spring Boot backend through APIs to exchange application data.

## 6. Database Design and Data Management

VyaparOS follows a relational database approach to organize application data and represent relationships between different business entities.

### 6.1 Database Purpose

The database is intended to store records associated with:

- User accounts and roles.
- Customer information.
- Appointments and salon services.
- Billing and financial transactions.
- Products and inventory.
- Staff information and attendance.

### 6.2 Database Structure

Data is organized into structured tables containing relevant fields, identifiers, and relationships according to the requirements of individual modules.

The database schema provides the foundation for storing and retrieving business information through the backend.

### 6.3 Data Relationships

The database design may include relationships such as:

- Customer records associated with appointments.
- Appointments linked to relevant services.
- Staff records associated with attendance information.
- Billing records associated with relevant business transactions.
- Product records used for inventory management.

The exact relationships depend on the implemented database schema.

### 6.4 Database Integration

The Spring Boot backend connects application functionality to the configured database through its database connectivity and persistence mechanisms.

H2 can be used for development and testing, while MySQL is the intended relational database for deployment where persistent storage is required.

### 6.5 Data Persistence

A persistent database configuration is necessary to ensure that business records remain available after application restarts.

The deployment environment must use the appropriate database connection settings and storage configuration rather than relying on temporary in-memory storage for production data.

## 7. Non-Functional Requirements

Non-functional requirements define the expected quality, reliability, security, and usability of the application.

### 7.1 Performance

The application should provide responsive access to the dashboard and business modules. Backend APIs and database queries should be designed to support routine operations efficiently.

### 7.2 Security

The system should provide authentication and role-based authorization to control access to administrative and staff functions.

Passwords should be stored using an appropriate secure hashing mechanism, and backend APIs should validate incoming requests before processing sensitive operations.

### 7.3 Reliability and Data Persistence

The system should maintain business records reliably. Customer, appointment, service, billing, inventory, and attendance data should be stored using an appropriate persistent database configuration.

### 7.4 Maintainability

The project separates frontend and backend responsibilities to support easier maintenance. Apache Maven manages backend dependencies and builds, while Git and GitHub support version control and collaborative development.

### 7.5 Scalability

The modular architecture provides a foundation for extending existing functionality and introducing additional business features as project requirements evolve.

### 7.6 Usability

The platform should provide a clear and accessible dashboard that allows administrators and staff to navigate authorized features and perform routine business activities efficiently.

## 8. Version Control and Collaboration

Git and GitHub are used to maintain the project source code and support collaborative development.

The project repository provides a centralized location for maintaining source files, tracking changes, and coordinating development activities among team members.

**Project Repository:** [NexByte001/VyaparOS](https://github.com/NexByte001/VyaparOS)

## WEEK 3 – VYAPAROS
## UML

---

## System Architecture

The following diagram illustrates the overall architecture of VyaparOS, including its frontend, Spring Boot backend, functional modules, database layer, and deployment tools.
<p align="center">
  <img src="./VyaparOS%20System%20Architecture%20Diagram.png"
       alt="VyaparOS System Architecture Diagram"
       width="100%">

## Technology Stack

| Component | Technology |
|---|---|
| Frontend | HTML, CSS, JavaScript |
| Backend | Java, Spring Boot |
| Build Tool | Apache Maven |
| Database | H2 (Development), MySQL (Intended Production Database) |
| Security | Spring Security, Role-Based Access Control |
| Version Control | Git, GitHub |
| Frontend Hosting | Netlify |

## Core Modules

### 1. Authentication and Security
- User registration and login.
- Admin and Staff roles.
- Authentication and access control.

### 2. Customer and Appointment Management
- Customer record management.
- Appointment scheduling.
- Customer and appointment organization.

### 3. Service Management
- Service catalog.
- Service information management.
- Association of services with appointments where supported.

### 4. Billing and Financial Management
- Billing and invoice management.
- Financial transaction records.
- Business financial tracking.

### 5. Inventory and Product Management
- Product information management.
- Inventory records.
- Stock management functionality.

### 6. Staff and Attendance Management
- Staff information management.
- Attendance records.
- Staff-related operational activities.

### 7. Dashboard and API Layer
- Centralized dashboard.
- REST API communication.
- Backend business logic and request validation.

## Architecture Overview

VyaparOS follows a client-server architecture:

1. **Frontend Layer:** Provides the interface through which administrators and staff interact with the system.
2. **Backend Layer:** Uses Java and Spring Boot to process requests, implement business logic, and expose APIs.
3. **Security Layer:** Supports authentication and role-based access management.
4. **Database Layer:** Stores application records using the configured database and persistence mechanisms.
5. **Deployment and Version Control:** GitHub manages source code, while Netlify can host the frontend. The backend and database require appropriate hosting and configuration.

## Database Design

The application uses a relational database approach to organize business information.

### WEEK 4: DATABASE DESIGN & UI MOCK-UPS

## 1. Database Architecture and Data Management Strategy

VyaparOS follows a modular database architecture designed to organize business information and support the integration of its core functional modules. The application uses Java and Spring Boot for backend processing and a relational database approach for managing structured business records.

H2 is used for development and testing where configured, while MySQL is the intended relational database for production deployment.

### A. Relational Database Design

The database is designed to organize the information required for authentication, customer management, appointment scheduling, service management, billing, inventory, and staff attendance.

#### 1. Users and Authentication

**Purpose:** Manages user accounts and authentication-related information.

Expected fields may include:

- `id` — Primary key identifying the user.
- `username` — Username used to identify the account.
- `email` — User's email address.
- `password` — Securely hashed password.
- `role` — Assigned user role, such as Admin or Staff.
- `created_at` — Account creation timestamp.

#### 2. Customers

**Purpose:** Maintains customer information required for business operations.

Expected fields may include:

- `id` — Unique customer identifier.
- `name` — Customer name.
- `phone` — Contact number.
- `email` — Email address, where applicable.
- `created_at` — Record creation timestamp.

#### 3. Appointments

**Purpose:** Stores appointment information and supports daily scheduling.

Expected fields may include:

- `id` — Unique appointment identifier.
- `customer_id` — Reference to the associated customer.
- `service_id` — Reference to the selected service.
- `staff_id` — Reference to the assigned staff member, where applicable.
- `appointment_date` — Scheduled date and time.
- `status` — Appointment status.

#### 4. Services

**Purpose:** Maintains the catalog of salon services offered by the business.

Expected fields may include:

- `id` — Unique service identifier.
- `name` — Service name.
- `description` — Service description.
- `price` — Service price.
- `duration` — Service duration, where supported.

#### 5. Products and Inventory

**Purpose:** Organizes product information and supports inventory management.

Expected fields may include:

- `id` — Unique product identifier.
- `name` — Product name.
- `price` — Product price.
- `quantity` — Available stock quantity.
- `description` — Product description, where applicable.

#### 6. Billing and Financial Records

**Purpose:** Supports the organization of invoices, billing details, and transaction information.

Expected fields may include:

- `id` — Unique billing record identifier.
- `customer_id` — Associated customer, where applicable.
- `amount` — Total amount.
- `payment_status` — Payment status.
- `transaction_date` — Date of the transaction.

#### 7. Staff and Attendance

**Purpose:** Maintains staff-related information and attendance records.

Expected fields may include:

- `id` — Unique attendance record identifier.
- `staff_id` — Reference to the staff member.
- `attendance_date` — Date of attendance.
- `check_in` — Check-in time.
- `check_out` — Check-out time.
- `status` — Attendance status, where supported.

**Note:** These field lists describe the intended data model. The final table names, field definitions, relationships, and constraints must match the actual backend entities and database schema.

---

