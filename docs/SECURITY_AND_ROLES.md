# Security Model & Role-Based Access Control (RBAC)

## 1. Authentication Engine
- **Password Storage**: Passwords hashed with BCrypt (12 rounds) on creation and updates. Plaintext passwords never logged or stored.
- **JWT Authentication**:
  - Stateless tokens signed using HMAC-SHA256 (`HS256`) with a cryptographically strong secret.
  - Claims include: `userId`, `email`, subject, issued-at, and expiration (default 24 hours).
  - Every incoming HTTP request through `JwtAuthenticationFilter` validates token integrity and loads the `UserPrincipal`.

---

## 2. Multi-Tenant Business Isolation & Authorization Guard
- Every tenant resource route has pattern `/api/businesses/{businessId}/...`.
- The `TenantSecurityGuard` or `@PreAuthorize` checks:
  1. The authenticated user has an active record in `business_memberships` for `businessId`.
  2. The user's role satisfies the endpoint permission (`OWNER`, `ADMIN`, or `STAFF`).
  3. Attempting to access another business's ID or resource causes immediate access denial (`403 Forbidden` or `404 Not Found` to prevent enumeration).

---

## 3. Role Matrix

| Resource / Action | OWNER | ADMIN | STAFF |
|---|:---:|:---:|:---:|
| Create / Configure Business | ✅ | ❌ | ❌ |
| Invite / Manage Staff | ✅ | ✅ | ❌ |
| View Customer & Service Catalog | ✅ | ✅ | ✅ |
| Create / Manage Appointments | ✅ | ✅ | ✅ |
| Create Invoices & Collect Payments | ✅ | ✅ | ✅ |
| Void Invoices / Refund | ✅ | ✅ | ❌ |
| View Financial Reports & Profit | ✅ | ✅ | ❌ |
| Manage Expenses | ✅ | ✅ | ❌ |
| Manage Inventory & Stock Movements | ✅ | ✅ | View Only / Sale |
| Clock-in / Clock-out (Self) | ✅ | ✅ | ✅ |
| View Own Attendance | ✅ | ✅ | ✅ |
| View Team Attendance Reports | ✅ | ✅ | ❌ |
| Correct Attendance (With Reason) | ✅ | ✅ | ❌ |
| View Audit Logs | ✅ | ✅ | ❌ |
| Update Business Settings & Tax | ✅ | ✅ | ❌ |
