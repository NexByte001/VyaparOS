 # VYPAAR OS- Management System

 
 
 ## Week 1
 
 
 ## Project Administration

Project Guide: Er. Ram Babu Buri

***Research Area:*** Software Engineering & Web Application Development

***Specializations:*** Java, Spring Boot, MySQL, Spring Security, HTML, CSS, and JavaScript.

***Project live:*** https://vyaaparos.netlify.app/



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


 ## Week 2


### Requirement Analysis and Existing-System Review

***Duration: 13 July – 19 July 2026***

During this phase, the functional requirements, non-functional requirements, and user roles of VyaparOS were identified to define the expected behavior, usability, security, and performance of the application.

#### 1. Functional Requirements

Functional requirements describe the operations that the system is expected to perform.

- **User Authentication:** The system should provide registration and login functionality for authorized users.
- **Role-Based Access Control:** Admin and Staff users should have access to features according to their assigned permissions.
- **Customer Management:** The system should allow authorized users to add, view, update, and manage customer records.
- **Appointment Management:** The system should support creating, viewing, updating, and managing salon appointments.
- **Service Management:** The system should maintain salon service information, including service names, descriptions, and prices.
- **Billing and Finance:** The system should support generating bills, recording payments, and maintaining financial information.
- **Inventory Management:** The system should maintain product details, stock quantities, and relevant inventory records.
- **Staff Management:** The system should maintain staff information and support authorized staff-management operations.
- **Attendance Management:** The system should record staff attendance and relevant attendance statuses.
- **Dashboard:** The system should provide a centralized interface for accessing relevant operational information.
- **Data Management:** The system should store, retrieve, and update application records through the configured database.

#### 2. Non-Functional Requirements

Non-functional requirements define the quality attributes and operational constraints of the application.

- **Security:** Authentication, appropriate access controls, and secure handling of user credentials should protect the application.
- **Usability:** The interface should be simple, clear, and easy for salon administrators and staff to navigate.
- **Performance:** Common operations, such as viewing customers and managing appointments, should respond within a reasonable time under normal operating conditions.
- **Reliability:** The application should handle valid operations consistently and provide appropriate feedback when errors occur.
- **Data Integrity:** Validation and appropriate database constraints should help prevent invalid, incomplete, or inconsistent records.
- **Maintainability:** The application should use a structured codebase that makes debugging, updates, and future development easier.
- **Compatibility:** The web interface should work with commonly used modern browsers.
- **Scalability:** The system architecture should allow additional features and increased data volumes to be accommodated as the application grows.
- **Availability:** The application should be accessible whenever its hosting environment and required services are operational.
- **Error Handling:** The system should provide meaningful error messages without unnecessarily exposing sensitive technical information.

#### 3. User Roles

VyaparOS defines two primary user roles.

**A. Admin**

The Admin manages the overall operations and has access to administrative functions according to the configured permissions.

Responsibilities include:
- Accessing the administrative dashboard.
- Managing customer records and appointments.
- Managing services and pricing.
- Accessing billing and financial records.
- Managing products and inventory.
- Managing staff information and attendance.
- Monitoring available business information.

**B. Staff**

The Staff role supports day-to-day operations with access limited to the functions permitted by the application.

Responsibilities may include:
- Logging into the application.
- Viewing relevant customer and appointment information.
- Accessing assigned salon services and operational records.
- Performing permitted appointment-related activities.
- Viewing or recording attendance information where authorized.

*The exact permissions depend on the application's implemented access-control rules.*

#### 4. Expected Outcome

The requirement analysis established the main system functions, quality expectations, and user responsibilities for VyaparOS. These requirements provide a foundation for system design, database planning, backend development, interface development, and subsequent testing.


## Week 3: System Architecture and Module Planning

Duration: 20 July – 26 July 2026



### Week 3: Technology Selection

During this phase, suitable technologies, development tools, and the software environment were evaluated and selected for VyaparOS. The primary objective was to establish a reliable, maintainable, and scalable technology stack for a web-based multi-business management platform.

#### 1. Technology Stack

| Technology      | Purpose in VyaparOS                                                                                   |
| --------------- | ----------------------------------------------------------------------------------------------------- |
| **Java**        | Implements backend business logic, application operations, and data processing.                       |
| **Spring Boot** | Provides the framework for developing backend services, REST APIs, and web application functionality. |
| **Maven**       | Manages project dependencies, build configuration, and application packaging.                         |
| **HTML5**       | Defines the structure of the application's web pages.                                                 |
| **CSS3**        | Handles styling, layouts, responsiveness, and visual presentation.                                    |
| **JavaScript**  | Implements client-side interactions, dynamic interfaces, and communication with backend APIs.         |
| **H2 / MySQL**  | Supports database operations, depending on the environment and active database configuration.         |
| **Git**         | Tracks source-code changes and supports version control.                                              |
| **GitHub**      | Hosts the project repository and supports team collaboration and documentation.                       |
|                 |                                                                                                       |

#### 2. Backend Technology Evaluation

**Java and Spring Boot**

Java was selected as the primary backend programming language because it supports object-oriented programming, structured application development, and maintainable business logic.

Spring Boot simplifies backend development by providing application configuration, dependency management integration, and support for building RESTful APIs. These capabilities are useful for connecting the frontend with business modules such as customer management, billing, inventory, and staff management.

**Maven**

Maven was selected to manage external libraries, dependency versions, project builds, and packaging. Its standardized project structure also makes the application easier to maintain and build across development environments.

#### 3. Frontend Technology Evaluation

**HTML5, CSS3, and JavaScript**

These technologies were selected to develop a browser-based interface for VyaparOS.

- HTML5 structures the application's pages and forms.
- CSS3 provides consistent layouts, styling, and responsive presentation.
- JavaScript handles user interactions, dynamic updates, and API communication.

This combination supports an accessible interface through which authorized users can interact with the platform's different business-management modules.

#### 4. Database Technology Evaluation

**H2 and MySQL**

Database technology was considered for storing and retrieving application data, including users, customers, appointments, products, billing records, and staff information.

- **H2:** A lightweight database suitable for development, testing, and environments configured to use an embedded database.
- **MySQL:** A relational database suitable for persistent storage of structured business information.

The active database depends on the application's configuration. Persistent storage and database migration must be configured appropriately for production use.

#### 5. Development Tools and Environment

The following tools support application development, testing, and collaboration:

- **JDK:** Provides the Java development and runtime environment.
- **Apache Maven:** Builds the backend and resolves dependencies.
- **Visual Studio Code:** Supports editing source files and project documentation.
- **Web Browser:** Enables interface testing and interaction with the application.
- **Git and GitHub:** Support version control, repository hosting, and project documentation.

The backend development environment was configured to support Java and Maven-based application execution.

#### 6. Technology Selection Criteria

The technologies were evaluated according to the following criteria:

- Compatibility with the project's requirements.
- Ease of development and maintenance.
- Support for modular application architecture.
- Database integration capabilities.
- Security and access-control support.
- Availability of documentation and development resources.
- Suitability for future enhancements and increased business requirements.

#### 7. Expected Outcome

The technology selection phase established the proposed development stack and tools for VyaparOS. This provided a foundation for system architecture design, database planning, backend implementation, frontend development, and integration of the platform's business-management modules.


## Week 4: System Design and UML Diagrams

### Duration: 27 July – 2 August 2026

System Design and UML Diagrams



## Week 5: Backend Setup and Authentication Planning

### Duration: 3 August – 9 August 2026

***1. Objective***

The objective of this week was to focus on the backend structure, authentication requirements, and communication between the frontend and backend.

***2. Activities Performed***

Reviewed the Spring Boot application structure.

Examined Maven configuration and dependency management.

Reviewed the organization of backend packages and classes.

Considered the database connection configuration.

Reviewed login and registration requirements.

Planned administrative and staff access levels.

Identified the need for input validation and error handling.

***3. Backend Responsibilities***

The backend is responsible for receiving requests, processing business logic, interacting with the database, and returning appropriate responses to the frontend.

The planned API structure supports operations such as creating, retrieving, updating, and deleting records where those operations are required and implemented.

***4. Security Considerations***

The application requires suitable authentication and authorization mechanisms to restrict access to administrative and staff functionality.

Sensitive configuration values should be managed appropriately, and database credentials should not be exposed in publicly accessible source files.

***5. Outcome***

The backend requirements and security considerations were reviewed in preparation for integrating the business modules.

## Week 6: Customer and Appointment Management

### Duration: 10 August – 16 August 2026

***1. Objective***

The objective of this week was to focus on the customer management and appointment management requirements of the salon system.

***2. Activities Performed***

Reviewed the customer registration workflow.

Identified the information required for customer profiles.

Examined appointment creation and update requirements.

Considered appointment dates, times, and associated services.

Reviewed the relationship between customers and appointments.

Considered validation for required customer and appointment fields.

Planned the display of appointment records.

***3. Customer Management***

The customer module is intended to organize customer details and make records accessible when managing salon services and appointments.

***4. Appointment Management***

The appointment module is intended to support the creation and tracking of bookings. Appointment information should be associated with the relevant customer and service where supported by the application.

5. Outcome

The customer and appointment workflows were defined and reviewed for integration with the broader salon management system.

Week 7: Billing and Financial Management

Duration: 17 August – 23 August 2026

1. Objective

The objective of this week was to review the billing workflow and requirements for managing salon transactions.

2. Activities Performed

Identified the information needed for generating bills.

Reviewed the relationship between services and billing.

Considered the calculation of totals and applicable discounts.

Reviewed the storage of transaction details.

Identified the need for accurate financial records.

Considered the presentation of billing information in the interface.

Reviewed possible validation requirements for billing entries.

3. Billing Module

The billing module is intended to help salon administrators organize customer charges and transaction details. Its design should support accurate calculations and consistent record-keeping.

4. Financial Record Management

Financial information should be organized so that authorized users can review relevant transactions and identify business activity.

5. Outcome

The billing and finance requirements were reviewed, and the module's expected role in the complete application was documented.

Week 8: Inventory and Product Management

Duration: 24 August – 30 August 2026

1. Objective

The objective of this week was to focus on inventory requirements and the management of salon products.

2. Activities Performed

Identified the information needed for product records.

Reviewed product names, prices, quantities, and related details.

Considered the need for adding and updating product information.

Reviewed inventory quantity tracking.

Identified the importance of validating product entries.

Considered how inventory information should be displayed.

Reviewed the relationship between the inventory module and the main dashboard.

3. Inventory Management

The inventory module is intended to organize salon products and maintain their associated information. Where implemented, quantity tracking can help administrators monitor stock levels and identify products that require replenishment.

4. Data Management Considerations

Product records should be validated before saving. The application should also provide appropriate feedback when a product is added, updated, or cannot be saved.

5. Outcome

The inventory module requirements and expected product-management workflow were documented for integration and verification.

Week 9: Staff Management, Attendance and Dashboard

Duration: 31 August – 6 September 2026

1. Objective

The objective of this week was to review staff management requirements and the presentation of business information through the dashboard.

2. Activities Performed

Reviewed the requirements for staff records.

Identified the need to record attendance information.

Considered attendance statuses such as present, absent, and on leave.

Reviewed the information required for attendance history.

Considered the display of important business metrics.

Reviewed navigation between different application modules.

Considered how the interface should respond to missing or unavailable data.

3. Staff and Attendance Management

The staff management module is intended to organize staff-related information. Attendance functionality may record daily attendance and display relevant attendance history according to the features available in the final application.

4. Dashboard

The dashboard is intended to provide convenient access to the main modules and display useful information about salon operations.

Depending on the implemented functionality, dashboard information may include customer records, appointments, billing summaries, inventory information, and staff details.

5. Outcome

The requirements for staff attendance and dashboard organization were reviewed in preparation for application-level integration.

Week 10: Module Integration and Application Configuration

Duration: 7 September – 13 September 2026

1. Objective

The objective of this week was to review how the application modules work together and identify configuration requirements.

2. Activities Performed

Reviewed communication between the frontend and backend.

Examined the API requirements for individual modules.

Reviewed database connectivity and persistence settings.

Considered cross-module relationships between customers, appointments, services, and billing.

Reviewed authentication and access restrictions.

Identified configuration requirements for local execution.

Considered the need for consistent error responses.

3. Integration Considerations

Integration requires the frontend to send requests in the expected format and the backend to process those requests correctly.

Database configuration must also support the intended storage behavior. In particular, data should persist across application restarts when persistent storage is required.

4. Configuration Review

The project configuration should be checked for appropriate port settings, database connection details, cross-origin access rules, and environment-specific properties.

5. Outcome

The integration and configuration requirements were documented, establishing the basis for functional verification and preparation for deployment.

Week 11: Functional Testing and Error Identification

Duration: 14 September – 20 September 2026

1. Objective

The objective of this week was to review the application for functional issues, incorrect behavior, and integration problems.

2. Activities Performed

Reviewed navigation between application modules.

Considered login and access-control scenarios.

Reviewed customer and appointment operations.

Considered the validation of service and billing information.

Reviewed product management and inventory operations.

Considered staff attendance operations.

Examined possible frontend and backend communication errors.

Reviewed database saving and retrieval behavior.

3. Functional Testing

The main areas for functional verification include:

Test Area

Verification Objective

Authentication

Check login behavior and access restrictions

Customer Management

Verify customer record operations

Appointments

Verify appointment creation and retrieval

Services

Verify service information and updates

Billing

Review calculations and transaction records

Inventory

Verify product operations and stock information

Attendance

Review attendance recording and retrieval

Dashboard

Check navigation and displayed information

Database

Check saving, retrieval, and persistence

Deployment

Check application accessibility and service connectivity

4. Error Handling

The application should provide meaningful feedback when a request fails or required information is missing. Backend errors, invalid data, and connectivity problems should be investigated during testing.

5. Outcome

A functional verification checklist was established to assess the application and identify areas requiring correction before final submission.

Testing note: The table describes the testing scope. Individual test cases should be marked as passed or failed only after they have actually been executed.

Week 12: Final Review, Documentation and Project Completion

Duration: 21 September – 27 September 2026

1. Objective

The objective of the final week was to consolidate the project, review the available functionality, prepare the documentation, and organize the final deliverables.

2. Activities Performed

Reviewed the overall project structure.

Examined the available functionality of each module.

Reviewed the relationship between the frontend and backend.

Checked the project configuration required for execution.

Reviewed the application interface for consistency.

Organized the project documentation.

Prepared the final project summary.

Reviewed the deployment information and identified any remaining configuration requirements.

Organized the final deliverables for academic submission.

3. Final Project Review

The final review covers the major modules of Vypaar OS, including customer management, appointments, services, billing, inventory, staff attendance, authentication, and dashboard functionality.

The availability and completeness of individual features should be verified against the final application.

4. Project Deliverables

The expected project deliverables include:

Project source code

Frontend files

Backend application

Database configuration

Project abstract and introduction

Requirement analysis

System architecture and module descriptions

Weekly project documentation

Testing checklist

Deployment information

Final conclusion and future scope

5. Outcome

The project documentation and available application components were consolidated for final academic review. Any remaining deployment, persistence, or module-specific issues should be recorded transparently rather than described as successfully resolved without verification.

3. Individual Team Responsibilities

3.1 Jagrati Sahu — Backend and Security

The backend and security responsibilities include reviewing the Spring Boot application structure, database connectivity, authentication, access control, and communication between the frontend and backend.

The role also includes examining API behavior, supporting module integration, reviewing error handling, and checking backend configuration.

3.2 Koshi Jain — Customers and Appointments

The customer and appointment responsibilities include reviewing customer information management, appointment creation, appointment tracking, and the relationship between customer records and salon services.

The module is intended to support organized customer information and appointment workflows.

3.3 Navya Raj — Billing and Finance

The billing and finance responsibilities include reviewing transaction requirements, service charges, billing calculations, and the organization of financial records.

The module aims to support accurate billing and convenient access to transaction information.

3.4 Palak Sahani — Inventory Management

The inventory responsibilities include reviewing product information, stock quantities, product updates, and inventory record management.

The module aims to help salon administrators organize products and monitor available stock.

3.5 Kumkum Rathore — Frontend and Dashboard

The frontend and dashboard responsibilities include reviewing page structure, interface styling, navigation, dashboard organization, and communication between user-interface components and backend services.

The role aims to provide a consistent and accessible interface for the system's major functions.

4. System Modules

4.1 Authentication and Authorization

This module supports controlled access to the application. Authentication verifies user identity, while authorization determines which functions a user can access.

4.2 Customer Management

This module organizes customer information and supports access to customer records for salon operations.

4.3 Appointment Management

This module organizes appointment details and helps users manage salon bookings.

4.4 Service Management

This module maintains information about salon services and their associated details.

4.5 Billing and Finance

This module organizes customer charges and financial transaction information.

4.6 Inventory Management

This module maintains product-related information and supports inventory tracking where implemented.

4.7 Staff Attendance

This module supports staff-related information and attendance tracking according to the implemented functionality.

4.8 Dashboard

The dashboard provides a central interface for navigating between the application's major modules and reviewing relevant information.

5. Challenges and Considerations

During the planning, development, and review of a full-stack management application, several technical areas require attention.

5.1 Frontend and Backend Communication

The frontend and backend must use compatible API endpoints and data formats. Incorrect endpoints or unavailable backend services can prevent application features from working.

5.2 Database Persistence

Database configuration must match the intended storage requirements. An in-memory database may lose its records when the application stops, whereas persistent storage is required for records that must remain available after a restart.

5.3 Authentication and Security

Authentication and authorization must be configured carefully to prevent unauthorized access to restricted functions.

5.4 Module Integration

Individual modules may depend on shared records. For example, appointment information may refer to customers and services. These relationships must be handled consistently.

5.5 Deployment Configuration

A deployed frontend requires a reachable backend and a properly configured database if it depends on server-side operations. Hosting the frontend alone does not automatically deploy the complete full-stack application.

5.6 Feature Verification

A feature should be considered complete only after its behavior has been checked. Saving, updating, retrieving, and deleting records should be tested wherever the relevant operations are supported.

6. Testing Strategy

The application should be evaluated through the following testing activities.

6.1 Functional Testing

Verify that the available functions behave according to their requirements.

6.2 Integration Testing

Verify communication between the frontend, backend, database, and individual modules.

6.3 Validation Testing

Check how the application handles missing fields, invalid input, and incorrect data.

6.4 Security Testing

Check authentication, authorization, and access restrictions.

6.5 Persistence Testing

Create records, restart the application, and check whether the records remain available as expected.

6.6 Deployment Testing

Check whether the deployed interface can reach the backend and whether the backend can access its configured database.

7. Expected Benefits

Vypaar OS is intended to provide the following benefits:

Centralized management of salon information.

Reduced dependence on manual record-keeping.

Better organization of customer and appointment records.

More convenient management of services and billing.

Improved organization of product and inventory information.

Easier access to staff-related information.

A consistent interface for daily salon operations.

A modular foundation for future improvements.

These benefits depend on the correct implementation and use of the relevant application features.

8. Limitations

The application may have limitations depending on its final implementation and deployment configuration.

Some functions may require additional testing or refinement.

Persistent storage depends on the configured database and hosting environment.

Backend-dependent functionality requires the backend service to remain accessible.

Advanced analytics and automated notifications may require further development.

The accuracy of business reports depends on the completeness of stored data.

Additional security testing may be required before production use.

9. Future Scope

The system can be extended with additional features to improve usability and business management.

9.1 Online Appointment Booking

Customers could book appointments through an online interface.

9.2 Automated Notifications

Email or SMS notifications could remind customers about upcoming appointments.

9.3 Advanced Reports

The application could provide daily, weekly, and monthly reports covering revenue, appointments, and inventory.

9.4 Online Payments

Integration with a payment gateway could simplify digital transactions.

9.5 Customer Feedback

A feedback module could help salon administrators understand customer satisfaction.

9.6 Enhanced Inventory Alerts

Automatic low-stock notifications could help staff monitor products requiring replenishment.

9.7 Cloud Database and Backup

A persistent cloud database and regular backups could improve data availability and recovery.

9.8 Mobile-Friendly Interface

Further responsive-design improvements could make the application easier to use on smartphones and tablets.

10. Conclusion

Vypaar OS is a web-based Salon Management System intended to simplify the organization of salon operations through a centralized digital platform. The project brings together major management areas, including customer records, appointments, services, billing, inventory, staff attendance, and administrative functionality.

The project provides an opportunity to apply concepts from software engineering, web development, backend programming, database management, application security, and interface design. Its modular organization also demonstrates the importance of separating responsibilities and coordinating different components within a full-stack application.

The project documentation covers the requirements, proposed architecture, module responsibilities, integration considerations, testing strategy, and opportunities for future development. The final quality of the application depends on the correctness of its implementation, database configuration, security controls, and deployment environment.

In conclusion, Vypaar OS establishes a foundation for digitizing salon management activities. With further testing, reliable persistent storage, and continued improvements to its modules, the application can be developed into a more complete and dependable solution for salon administration.

References

Oracle. Java Documentation. https://docs.oracle.com/en/java/

Spring. Spring Boot Reference Documentation. https://docs.spring.io/spring-boot/

Spring. Spring Security Reference. https://docs.spring.io/spring-security/reference/

Apache Maven. Maven Documentation. https://maven.apache.org/guides/

MDN Web Docs. HTML, CSS and JavaScript Documentation. https://developer.mozilla.org/

MySQL. MySQL Reference Manual. https://dev.mysql.com/doc/
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

