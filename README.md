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

---
 ## Week 2
---

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

---

### Week 3: Technology Selection

---
Duration: 20 July – 26 July 2026


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

---

## Week 4: System Design and UML Diagrams

---

### Duration: 27 July – 2 August 2026

During this phase, the system architecture, application workflows, database structure, and user interface of VyaparOS were planned. The primary objective was to define how different components interact to support business operations through a centralized multi-business management platform.

***1. System Architecture Design***

<p align="center">
  <img src="VyaparOS%20System%20Architecture%20Diagram.png"
       alt="VyaparOS System Architecture Diagram"
       width="100%">
</p>

***2. UML Diagram Planning***


**3. Database Structure Planning**

The database structure was planned to support the platform's major functional modules.

| Entity | Purpose |
|---|---|
| `User` | Stores user account information and authentication-related data. |
| `Business` | Represents a business registered or managed through the platform. |
| `Customer` | Stores customer information associated with relevant business operations. |
| `Appointment` | Stores appointment details where applicable to the business. |
| `Service` | Maintains service information and pricing where applicable. |
| `Product` | Stores product and inventory information. |
| `Invoice` | Maintains billing records. |
| `Payment` | Records payment-related information. |
| `Staff` | Stores staff-related information. |
| `Attendance` | Maintains staff attendance records. |

The proposed structure aims to reduce unnecessary duplication and maintain consistency between related records.

Because VyaparOS is intended for multiple businesses, business-specific data should be associated with the appropriate business wherever applicable. Access controls should prevent users from accessing records belonging to businesses they are not authorized to manage.

The final entities and relationships must match the actual database schema implemented in VyaparOS.

**4. User Interface Planning**

The interface was planned to provide a consistent and accessible experience across the application's major modules.

The principal interface components include:

Login and Registration: Interfaces for user authentication and account creation, where supported.

Dashboard: A centralized view of relevant business information.

Customer Management: Forms and views for managing customer records.

Appointment Management: Interfaces for scheduling and tracking appointments.

Billing and Finance: Screens for managing invoices and financial records.

Inventory Management: Views for maintaining products and stock information.

Staff Management: Interfaces for staff records and attendance.

The design emphasizes clear navigation, consistent layouts, readable information, and appropriate form validation.

**5. Design Considerations**

The system design considered the following principles:

Modularity: Organize functionality into identifiable business-management modules.

Separation of Concerns: Separate frontend presentation, backend logic, security, and data access.

Data Integrity: Define appropriate relationships and validation rules for stored records.

Security: Restrict access to authorized users and business-specific data.

Maintainability: Use a structured design that supports debugging and future enhancements.

Extensibility: Allow additional business-management features to be introduced without unnecessarily redesigning the entire application.

Consistency: Maintain common navigation and interaction patterns across modules.

**6. Expected Outcome**

The system design phase established the architectural approach, major application components, proposed database entities, user workflows, and interface structure for VyaparOS.

These design specifications provide a foundation for backend implementation, database integration, frontend development, security configuration, and subsequent testing.

Deliverables: System architecture, UML diagrams, database design, and preliminary user interface plans.


## Week 5: Backend Setup and Authentication Planning

### Duration: 3 August – 9 August 2026

### Week 5: Backend Development

During this phase, the backend structure of VyaparOS was established using Java and Spring Boot. The focus was on configuring the application, managing Maven dependencies, organizing backend components, and preparing the foundation for business logic, REST APIs, database connectivity, and application security.

***1. Spring Boot Project Setup***
- Configured the Spring Boot backend project using Maven.
- Organized the backend source code and application configuration files.
- Established the initial structure for developing a modular multi-business management platform.

***2. Maven Dependencies and Configuration***
- Configured the `pom.xml` file to manage project dependencies.
- Integrated the dependencies required for Spring Boot application development and web services.
- Prepared the build configuration to support application compilation and execution.

***3. Backend Architecture***
- Organized the backend components to separate application logic, API handling, and data access responsibilities.
- Planned the implementation of controllers, services, repositories, and entity classes.
- Established the foundation for integrating different business modules within a common backend.

***4. Database Connectivity***
- Prepared the backend configuration for database connectivity.
- Evaluated H2 for development and testing and MySQL for the intended production environment.
- Planned database operations for users, businesses, customers, appointments, products, billing, and staff records.

***5. REST API Preparation***
- Established the foundation for REST API development.
- Planned HTTP-based communication between the frontend and backend using JSON.
- Identified the need for request validation, appropriate response handling, and error management.

***6. Multi-Business Application Support***
- Organized the backend design around the platform's different business-management modules.
- Considered business-specific data relationships and access restrictions.
- Prepared the foundation for integrating customer management, appointments, services, billing, inventory, and staff management.

***7. Expected Outcome***
The backend setup established the technical foundation for implementing business logic, database operations, authentication, REST APIs, and frontend-backend integration in VyaparOS.

## Week 6: Customer and Appointment Management

### Duration: 10 August – 16 August 2026

*During this phase, the authentication and security components of VyaparOS were developed to support controlled access to the platform. The focus was on user authentication, role-based permissions, and protecting business information.

***1. User Authentication***
- Worked on user registration and login functionality.
- Planned the validation of user credentials during authentication.
- Established the authentication workflow between the frontend and backend.

***2. Role-Based Access Control***
- Defined the Admin and Staff roles.
- Identified the permissions required for different user roles.
- Worked on restricting access to administrative functions according to assigned permissions.

***3. Spring Security Integration***
- Worked on configuring Spring Security for backend protection.
- Planned security rules for protected API endpoints.
- Considered appropriate handling of unauthorized requests and authentication failures.

***4. Business Data Protection***
- Identified the need to restrict access to business-specific information.
- Planned access checks to prevent users from accessing records belonging to businesses they are not authorized to manage.
- Considered secure handling of authentication credentials and sensitive information.

***5. Expected Outcome***
The authentication and security phase established the foundation for user identity verification, role-based access control, and protection of business operations within VyaparOS.

---

## Week 7: Billing and Financial Management

---

### Duration: 17 August – 23 August 2026

During this phase, the customer and appointment management components of VyaparOS were developed or integrated to organize customer information and scheduled business activities. The design supports businesses that require customer records, appointments, or service scheduling.

#### 1. Customer Management
- Worked on maintaining customer information in the application.
- Planned functionality for adding, viewing, updating, and managing customer records.
- Considered input validation to improve the accuracy and consistency of customer data.

#### 2. Appointment Management
- Worked on appointment creation and management functionality.
- Planned the storage and retrieval of appointment details.
- Considered appointment status tracking and updates where applicable.

#### 3. Service Management
- Worked on organizing business service information.
- Planned the maintenance of service names, descriptions, and pricing where applicable.
- Considered the relationship between services, customers, and appointments.

#### 4. Backend and Database Integration
- Worked on connecting customer and appointment operations with the backend.
- Considered the database relationships required to maintain consistent records.
- Planned communication between frontend interfaces and backend REST APIs.

#### 5. Multi-Business Support
- Considered the association of customer and appointment records with the appropriate business.
- Planned access restrictions for business-specific records.
- Designed the modules to support different business workflows without limiting the platform to a single industry.

#### 6. Expected Outcome
The customer and appointment management phase established the foundation for organizing customer records, scheduling activities, and maintaining business-related information within VyaparOS.

---

## Week 8: Billing and Inventory Management

---

### Duration: 24 August – 30 August 2026

During this phase, the billing, financial record, and inventory management components of VyaparOS were developed or integrated to support essential business operations.

***1. Billing Management***
- Worked on organizing billing records and invoice-related information.
- Planned the recording of invoice details, amounts, and applicable charges.
- Considered invoice status tracking and access to billing information.

***2. Financial Record Management***
- Worked on organizing payment and transaction records.
- Planned the association of financial records with relevant invoices and business accounts.
- Considered validation of financial data to maintain consistency.

***3. Inventory Management***
- Worked on maintaining product information and inventory records.
- Planned functionality for adding, viewing, updating, and managing products.
- Considered tracking stock quantities and maintaining accurate product records.

***4. Database Integration***
- Planned database entities and relationships for invoices, payments, and products.
- Considered data validation and consistency during record creation and updates.
- Worked on preparing the backend structure for communication between application modules and the database.

***5. Multi-Business Support***
- Considered the association of billing and inventory records with the relevant business.
- Planned access restrictions to protect business-specific financial and inventory information.
- Designed the modules to accommodate different business requirements where applicable.

***6. Expected Outcome***
The billing and inventory phase established the foundation for maintaining invoice records, organizing financial information, and managing product inventory through the VyaparOS platform.

---

### Week 9: Staff and Attendance Management

---
### Duration: 31 August –  6 Sepetmber 2026

During this phase, the staff management and attendance components of VyaparOS were developed or integrated to support employee-related operations and maintain organized staff records across businesses.

***1. Staff Management***
- Worked on maintaining staff information and employee records.
- Planned functionality for adding, viewing, updating, and managing staff details.
- Considered associating staff members with their respective businesses.

***2. Attendance Management***
- Worked on organizing staff attendance records.
- Planned attendance statuses such as Present, Absent, On Leave, and Half Day, where applicable.
- Considered maintaining attendance dates and working-hour information.

***3. Attendance Tracking***
- Planned the recording and retrieval of attendance history.
- Considered calculating working hours from available attendance information.
- Identified the need for validating attendance entries and preventing duplicate records.

***4. Backend and Database Integration***
- Worked on preparing backend operations for staff and attendance records.
- Considered the relationships between staff, businesses, and attendance information.
- Planned appropriate access restrictions for staff-related data.

***5. Expected Outcome***
The staff and attendance management phase established the foundation for maintaining employee records, organizing attendance information, and supporting staff-related operations within VyaparOS.

---

### Week 10: Frontend and Backend Integration

---
### Duration: 7 Sepetmber  – 13 Sepetmber 2026

During this phase, the frontend interfaces and Spring Boot backend of VyaparOS were integrated to enable communication between user-facing components, backend APIs, and the configured database.

***1. Frontend Integration***
- Connected available frontend interfaces with the application's backend services.
- Worked on integrating dashboard components with relevant business-management modules.
- Reviewed navigation and user interactions across the application.

***2. REST API Integration***
- Worked on connecting frontend operations with backend REST APIs.
- Used HTTP requests and JSON data to exchange information between the frontend and backend.
- Considered handling successful responses, validation errors, and failed requests.

***3. Database Integration***
- Connected application operations with the configured database.
- Worked on retrieving and displaying stored business information.
- Reviewed data creation and update workflows across the available modules.

***4. Module Integration***
- Worked on integrating customer and appointment management.
- Connected available billing and inventory functionality.
- Integrated staff management and attendance features where implemented.
- Reviewed the interaction between authentication, authorization, and protected application functions.

***5. Integration Challenges***
- Investigated communication issues between the frontend and backend.
- Reviewed API endpoints, request formats, and application configuration.
- Worked on resolving integration errors and improving consistency across modules.

***6. Expected Outcome***
The integration phase connected the available application components and established a more unified workflow across the VyaparOS platform.

---

### Week 11: Testing and Debugging

---
### Duration: 14 Sepetmber  – 20 Sepetmber 2026

During this phase, the available VyaparOS features were tested to identify functional errors, validate application behavior, and improve reliability.

***1. Functional Testing***
- Tested user registration and login where implemented.
- Checked customer and appointment management workflows.
- Reviewed billing, inventory, staff management, and attendance functionality.
- Verified the behavior of available dashboard components.

***2. API Testing***
- Tested available backend endpoints with valid and invalid inputs.
- Checked API responses and error-handling behavior.
- Reviewed communication between frontend components and backend services.

***3. Database Testing***
- Checked whether application records were stored and retrieved correctly.
- Tested record creation and updates in the configured database.
- Investigated data consistency issues and unexpected database behavior.

***4. Security Testing***
- Reviewed access restrictions for Admin and Staff roles.
- Checked the handling of unauthorized requests.
- Considered whether business-specific records were protected from unauthorized access.

***5. Bug Fixing and Validation***
- Identified errors encountered during application testing.
- Investigated issues involving data handling, API communication, and interface behavior.
- Worked on correcting identified defects and validating the affected functionality.

***6. Expected Outcome***
The testing phase helped identify application issues, validate available workflows, and improve the stability and reliability of VyaparOS.

---

## Week 12: Finalization and Project Documentation

---

### Duration: 21 September – 27 September 2026

During this phase, the project documentation and presentation materials were organized, the available application features were reviewed, and the overall project status was evaluated.

***1. Application Review***
- Reviewed the overall structure and available functionality of VyaparOS.
- Examined the integration of the frontend, backend, and database.
- Identified completed features and remaining limitations.

***2. Documentation Preparation***
- Organized the Software Requirements Specification (SRS).
- Prepared or finalized the project report.
- Documented the system architecture and UML diagrams.
- Updated the GitHub README with the project overview, technology stack, features, and development progress.

***3. Presentation and Research Paper***
- Organized the project presentation describing the problem statement, objectives, system design, and implementation.
- Prepared or reviewed the research paper related to the project.
- Organized supporting documentation for academic evaluation.

***4. Demonstration Preparation***
- Reviewed the application workflows for the project demonstration.
- Organized screenshots and demonstration video materials.
- Prepared setup instructions and repository documentation for reviewers.

***5. Final Project Assessment***
- Reviewed the project against its original objectives.
- Documented the implemented functionality and known limitations.
- Identified possible future enhancements, including improved reporting, additional business-management features, and production deployment improvements.

***6. Expected Outcome***
The finalization phase organized the project source code, academic documentation, diagrams, presentation materials, and demonstration resources for project submission and evaluation.

---
---

W
