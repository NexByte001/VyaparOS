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



# VyaparOS — One platform. Your entire business.

**VyaparOS** is a multi-tenant, full-stack business management SaaS platform designed for service and product businesses (salons, spas, gyms, repair shops, boutiques, studios, and coaching centres).

---

## 🎨 Branding & Logo Assets

The VyaparOS brand identity represents connected business operations, upward transactional growth, and modern commerce.

| Asset | Path | Description |
|---|---|---|
| **Primary Logo (Dark)** | `frontend/assets/brand/logo.svg` | Main horizontal brand logo for dark backgrounds & headers |
| **Light Logo (Print/Invoice)** | `frontend/assets/brand/logo-light.svg` | Optimized for white background print invoices and PDF exports |
| **Emblem / Mark** | `frontend/assets/brand/logo-mark.svg` | Transparent geometric V emblem with growth diamond |
| **Browser Favicon** | `frontend/assets/brand/favicon.svg` | SVG favicon with glowing rounded squircle |
| **Windows App Icon** | `frontend/assets/brand/app-icon.ico` | Multi-resolution icon for Windows desktop shortcut |
| **App Icon PNG** | `frontend/assets/brand/app-icon.png` | High-res 128x128 icon image |

---

## 🛠️ Root Cause of "Page Not Found" & How It Is Fixed

When deploying VyaparOS to platforms like Vercel, Netlify, GitHub Pages, or Render, visitors encountered **"404: Page Not Found"** due to:

1. **Missing Root `index.html`**: The repository root previously contained only `backend/`, `frontend/`, `docs/`, and `.env.example`. Hosting providers defaulted to serving from the root `.`, where no `index.html` existed.
2. **Missing Deployment Publish Configuration**: Static hosts did not know to publish `frontend/` as the document root or rewrite clean URLs (`/login`, `/dashboard`, `/customers`).
3. **Hardcoded Root Paths**: Assets and internal links were referencing root-absolute paths (`/assets/...`, `/login.html`), which failed on custom hosting paths or preview URLs.

### What Was Fixed:
- ✅ **Root Gateway `index.html`**: Created at repository root to immediately forward any visitor or server directly to the application.
- ✅ **`vercel.json`**: Added root routing rewrites for all pages, assets, and clean URLs.
- ✅ **`netlify.toml` & `frontend/_redirects`**: Configured `publish = "frontend"` with clean routing rules.
- ✅ **Resilient Paths**: Converted all HTML page references to resilient relative paths and dynamic base path resolution (`API.getBasePath()`).
- ✅ **Dynamic API Configuration**: Frontend can dynamically connect to localhost during development or remote backend URLs in production without code edits.

---

## 🖥️ Opening VyaparOS from Your Windows Desktop

### 1. Create the Desktop Shortcut (One-Time Setup)
Run this PowerShell script from the project folder:
```powershell
powershell -ExecutionPolicy Bypass -File .\create-desktop-shortcut.ps1
```
This generates:
- **`VyaparOS.lnk`** directly on your Windows Desktop (`C:\Users\aditya\OneDrive\Desktop\VyaparOS.lnk`), styled with the custom VyaparOS logo icon!

*(Optional: To create a desktop shortcut that opens your deployed live website, pass the `-LiveUrl` argument:)*
```powershell
powershell -ExecutionPolicy Bypass -File .\create-desktop-shortcut.ps1 -LiveUrl "https://your-production-domain.com"
```

### 2. Launching Locally (1-Click)
Double-click the **`VyaparOS`** icon on your Desktop, or run:
```cmd
start-vyparos.bat
```
This launcher:
1. Detects the project directory reliably.
2. Starts the frontend server on port `3000`.
3. Verifies Java/Maven and starts the Spring Boot backend on port `8080`.
4. Waits for the application to be ready and opens `http://localhost:3000` automatically.

To safely stop the local services when you're done:
```cmd
stop-vyparos.bat
```

---

## 🚀 Running Locally via Terminal

### Prerequisites
- **Node.js** (v20+) or **Python** (3.10+) — used to serve the frontend.
- **Java Development Kit (JDK 17 or 21 LTS)** — required for the Spring Boot backend.
- **Apache Maven 3.8+** — required to build and run the backend.

> [!TIP]
> If you don't have Java or Maven installed yet, install them in seconds via Windows Package Manager:
> ```powershell
> winget install EclipseAdoptium.Temurin.17.JDK
> winget install Apache.Maven
> ```

### Step 1: Start the Backend (Spring Boot)
```powershell
cd backend
mvn clean spring-boot:run
```
- Base API URL: `http://localhost:8080/api`
- Default dev profile: Uses an embedded **H2** database stored in `backend/data/vyparos_db`. **No MySQL configuration is required for rapid offline testing!**
- H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/vyparos_db`, User: `sa`, Password: empty)

### Step 2: Start the Frontend
In another terminal:
```powershell
cd frontend
python -m http.server 3000
```
*(Or using Node: `npx serve frontend -p 3000`)*

Open **`http://localhost:3000`** in your browser.

---

## 🗄️ Database Configuration (H2 vs MySQL)

### Development Mode (Default)
VyaparOS defaults to the `dev` Spring profile with an embedded H2 file database. All schema tables are auto-managed and persisted across restarts under `./backend/data/`.

### Production / MySQL Mode
Your machine already has `MySQL80` installed and running. To use MySQL:
1. Copy `.env.example` to `.env` and configure your credentials:
   ```properties
   DB_HOST=localhost
   DB_PORT=3306
   DB_NAME=vyparos_db
   DB_USERNAME=root
   DB_PASSWORD=your_mysql_password
   SPRING_PROFILES_ACTIVE=prod
   ```
2. Create the database in MySQL:
   ```sql
   CREATE DATABASE IF NOT EXISTS vyparos_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. Run the backend with the MySQL profile:
   ```powershell
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```
   Flyway will automatically execute `V1__init_schema.sql` to initialize all 10 core business tables.

---

## 🌐 Local vs Production URLs

| Environment | Frontend URL | Backend API URL |
|---|---|---|
| **Local Development** | `http://localhost:3000` | `http://localhost:8080/api` |
| **Production Website** | `https://your-domain.com` (or Vercel / Netlify URL) | `https://api.your-domain.com/api` (or Render / Railway / AWS) |

### How to Point Frontend to a Remote Backend
If your frontend is hosted on Vercel or Netlify, and your Spring Boot backend is hosted on Render, Railway, or EC2:
1. Go to the login page (`/login.html`).
2. Click **⚙️ Backend API** at the bottom.
3. Enter your deployed backend URL (e.g. `https://my-vyparos-api.onrender.com/api`) and click **Update API URL**.
4. The setting is saved in your browser's local storage and used across all workspace views!

---

## 🚀 Deployment Guide

### Deploying the Frontend (Vercel / Netlify)
- **Vercel**: Import repository. Vercel automatically reads `vercel.json` and routes all pages correctly.
- **Netlify**: Import repository. Set Publish Directory to `frontend`. Netlify reads `netlify.toml` and `_redirects` automatically.
- **GitHub Pages / Custom Static Hosting**: Publish the repository directly. The root `index.html` seamlessly redirects visitors to `frontend/index.html`.

### Deploying the Backend (Spring Boot)
1. Build an executable production JAR:
   ```powershell
   cd backend
   mvn clean package -DskipTests
   ```
   JAR output: `backend/target/vyparos-backend-1.0.0.jar`
2. Run on your server / container:
   ```bash
   java -jar vyparos-backend-1.0.0.jar --spring.profiles.active=prod
   ```
3. Set environment variables on your cloud host:
   - `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
   - `JWT_SECRET` (minimum 256-bit secure secret)
   - `CORS_ALLOWED_ORIGINS` (include your frontend domain, e.g. `https://my-vyparos.vercel.app`)

---

---

## 👥 Staff Management & Team Onboarding Workflow

### Why Staff Registration Previously Failed (Root Cause)
Previously, adding a team member required the user to **already exist in the database**. If a business owner entered an employee's email address who hadn't yet registered on VyaparOS, the backend threw a hard `ResourceNotFoundException`:
```
"User with email ... is not registered yet. Please have them register first."
```
The frontend modal only accepted an email and role, displaying a blocker requiring employees to sign up independently before being added.

### How It Works Now (Dual Onboarding Flow)

#### 1. Adding an Existing Registered User
1. Workspace Owner / Admin opens **Staff & Team** -> clicks **Add Team Member**.
2. Enters employee's Name, Email, Role (`OWNER`, `ADMIN`, `STAFF`), and optional Job Title and Department.
3. If the user already has a VyaparOS account:
   - If they are already an active member: returns `409 Conflict` ("User is already an active member of this business workspace").
   - If previously deactivated: reactivates them and updates their role/title.
   - If not yet in this workspace: immediately creates a `BusinessMembership` linking the user directly.
   - Shows instant confirmation: *"Staff member added directly to workspace team!"*

#### 2. Inviting a New Unregistered Employee
1. Owner / Admin enters an unregistered email address with their name, role, and department.
2. The backend generates a **cryptographically secure random invitation token** stored in `staff_invitations` with status `PENDING` and a 7-day expiration.
3. The dashboard opens an **Invitation Created!** modal displaying the employee onboarding link:
   ```
   http://localhost:3000/staff/accept-invite.html?token=<SECURE_TOKEN>
   ```
4. A 1-click **Copy Link** button allows the owner to immediately copy and send the link via WhatsApp, Slack, or Email.
5. In the **Pending Invitations** tab, the owner can view sent invitations, check expiration dates, **Resend** (which issues a fresh secure token and extends expiration by 7 days), or **Revoke** invitations at any time.

#### 3. Invitation Acceptance Workflow
1. The employee clicks their unique onboarding link (`/staff/accept-invite.html?token=...`).
2. The page verifies the invitation token with `GET /api/invitations/{token}` (public endpoint):
   - Confirms workspace name, intended role, and pre-fills the recipient's name and email.
   - Verifies the invitation is not expired, revoked, or already accepted.
3. The employee chooses a password (minimum 6 characters) and submits the form.
4. The backend securely executes `POST /api/invitations/{token}/register-and-accept` within a transaction:
   - Hashes password using BCrypt.
   - Creates the `User` account.
   - Creates active `BusinessMembership` for the specified workspace and role.
   - Marks the invitation `ACCEPTED` with timestamp `accepted_at` (preventing token reuse).
   - Generates a JWT authentication session.
5. The employee is automatically logged in and redirected directly to their active workspace dashboard!

---

## 🔒 Roles, Permissions & Security

| Role | Permissions & Capabilities | Restrictions |
|---|---|---|
| **OWNER** | Full control over business settings, billing, attendance, expenses, inventory, services, and staff. Can assign and remove `OWNER`, `ADMIN`, and `STAFF`. | Cannot be removed if they are the sole active Owner of the workspace. |
| **ADMIN** | Can manage team (`STAFF`), services, appointments, customers, inventory stock, invoices, and attendance. | Cannot assign or modify `OWNER` memberships. Cannot delete business settings. |
| **STAFF** | Operational access: appointments calendar, customer records, service catalog, time clock-in / clock-out, view attendance. | Cannot invite staff, change permissions, modify financial reports, or access owner settings. |

### Security Measures Implemented:
- **Server-Side Enforcement**: All authorization is strictly enforced by `TenantSecurityGuard` and Spring Security method guards on the backend. No security reliance on frontend button states.
- **Tenant Isolation**: Every API request verifies `user_id` membership against the target `business_id`. A user from Business A cannot inspect or manipulate Business B's staff or invitations.
- **One-Time Token Security**: Invitation tokens expire in 7 days, cannot be reused after acceptance, and cannot be claimed if revoked.
- **Audit Preservation**: Deactivating staff members uses soft-deactivation (`is_active = false`), preserving all historical invoices, attendance logs, and appointment records.
- **Sole Owner Protection**: The backend actively prevents deactivating or removing the last active Owner of a workspace.

---

## 🧪 Automated Testing

VyaparOS includes comprehensive automated integration tests for authentication, tenant isolation, business logic, and the new staff onboarding workflow.

To run the complete test suite:
```powershell
cd backend
.\mvnw.cmd test
```

### Test Scenarios Covered:
1. `shouldAddExistingRegisteredUserDirectlyToWorkspace`: Direct addition, role assignment, and 409 conflict prevention on duplicate adds.
2. `shouldCreateInvitationForUnregisteredUserAndAllowAcceptance`: Full invitation lifecycle, public inspection, registration with BCrypt hashing, workspace linkage, and token reuse prevention.
3. `shouldRevokeInvitationAndPreventAcceptance`: Revocation verification and 400 Bad Request prevention on revoked token usage.
4. `AuthControllerTest`, `AppointmentSchedulingTest`, `BillingAndFinanceTest`, `CustomerAndServiceTest`, `InventoryAndStockTest`, `AttendanceAndAuditTest`, `HealthControllerTest`.

---

## 🗄️ Database Migrations

VyaparOS uses Flyway for schema versioning:

| Migration | File | Description |
|---|---|---|
| **V1** | `V1__init_schema.sql` | 10 core tables: users, businesses, business_memberships, business_settings, customers, services, appointments, products, invoices, attendance. |
| **V2** | `V2__staff_invitations.sql` | Adds `job_title` and `department` to `business_memberships`. Creates `staff_invitations` table with status indexing and foreign key constraints. |

### Applying to MySQL in Production:
When running with `SPRING_PROFILES_ACTIVE=prod`, Flyway automatically applies pending migrations against your MySQL database during application boot:
```bash
java -jar vyparos-backend-1.0.0.jar --spring.profiles.active=prod
```

---

## ❓ Common Errors & Solutions

| Error | Root Cause | Solution |
|---|---|---|
| **Page Not Found (404)** | Hosting provider looking for `index.html` at repository root or client routing not rewritten | Fixed by root `index.html`, `vercel.json`, and `netlify.toml`. Verify publish directory is set to `frontend` or root with rewrites. |
| **User is not registered yet** | **Fixed**: Previously blocked unregistered emails when adding staff | Now automatically triggers the staff invitation workflow with secure onboarding link generation. |
| **An active invitation is already pending (409)** | An invite has already been issued for this email | Go to the **Pending Invitations** tab to Resend, Copy Link, or Revoke the pending invite. |
| **Invitation has expired or been revoked (400)** | The 7-day token validity elapsed or owner cancelled the invite | Business owner can click **Resend** or **Re-invite** in the Pending Invitations tab to generate a fresh link. |
| **Session Expired (401)** | JWT expired after 24h or invalid credentials | Log in again at `/login.html` to issue a fresh JWT token. |
| **Port 3000 or 8080 Already in Use** | An old instance is still running | Run `stop-vyparos.bat` to cleanly terminate existing instances. |

