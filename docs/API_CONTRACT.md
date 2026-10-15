# VyaparOS REST API Contract

All endpoints use base path `/api` and respond with JSON formatted as:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": { ... },
  "timestamp": "2026-10-09T14:30:00Z"
}
```

Error responses:
```json
{
  "success": false,
  "message": "Validation failed / Unauthorized / Resource not found",
  "errors": {
    "fieldName": "Must not be blank"
  },
  "errorCode": "RESOURCE_NOT_FOUND",
  "timestamp": "2026-10-09T14:30:00Z"
}
```

---

## 1. Authentication & User Profile
- `POST /api/auth/register` — Public. Body: `{ email, password, fullName, phone }`
- `POST /api/auth/login` — Public. Body: `{ email, password }` -> Returns `{ token, user: { id, email, fullName } }`
- `GET /api/auth/me` — Authenticated. Returns current authenticated user and their active business memberships.

---

## 2. Business Workspaces & Staff Memberships
- `GET /api/businesses` — Authenticated. Returns list of businesses user belongs to.
- `POST /api/businesses` — Authenticated. Body: `{ name, businessType, currency, timezone, address, phone, email }` -> Creates business and assigns user as `OWNER`.
- `GET /api/businesses/{businessId}` — Member. Returns business details.
- `PATCH /api/businesses/{businessId}` — `OWNER/ADMIN`. Updates business details.
- `GET /api/businesses/{businessId}/members` — Member. Returns staff list.
- `POST /api/businesses/{businessId}/members` — `OWNER/ADMIN`. Body: `{ email, role }` -> Invites/adds member.
- `PATCH /api/businesses/{businessId}/members/{memberId}` — `OWNER/ADMIN`. Updates role or status (`is_active`).

---

## 3. Customer Management
- `GET /api/businesses/{businessId}/customers?search=&page=&size=` — Member.
- `POST /api/businesses/{businessId}/customers` — Member. Body: `{ name, phone, email, address, notes }`
- `GET /api/businesses/{businessId}/customers/{customerId}` — Member. Includes history (appointments & invoices).
- `PATCH /api/businesses/{businessId}/customers/{customerId}` — Member.
- `DELETE /api/businesses/{businessId}/customers/{customerId}` — `OWNER/ADMIN`. Archives customer.

---

## 4. Services Catalog
- `GET /api/businesses/{businessId}/services` — Member.
- `POST /api/businesses/{businessId}/services` — `OWNER/ADMIN`. Body: `{ name, description, durationMinutes, price }`
- `PATCH /api/businesses/{businessId}/services/{serviceId}` — `OWNER/ADMIN`.
- `DELETE /api/businesses/{businessId}/services/{serviceId}` — `OWNER/ADMIN`. Deactivates service.

---

## 5. Appointments & Scheduling
- `GET /api/businesses/{businessId}/appointments?date=&startDate=&endDate=&status=&staffId=` — Member.
- `POST /api/businesses/{businessId}/appointments` — Member. Body: `{ customerId, serviceId, staffMembershipId, startTime, notes }` -> Backend calculates endTime and checks staff collision.
- `GET /api/businesses/{businessId}/appointments/{appointmentId}` — Member.
- `PATCH /api/businesses/{businessId}/appointments/{appointmentId}` — Member. Reschedule/update.
- `POST /api/businesses/{businessId}/appointments/{appointmentId}/status` — Member. Body: `{ status: "COMPLETED"|"CANCELLED"|"NO_SHOW"|"IN_PROGRESS" }`

---

## 6. Invoices & Billing
- `GET /api/businesses/{businessId}/invoices?page=&size=&status=&customerId=` — Member.
- `POST /api/businesses/{businessId}/invoices` — Member. Body: `{ customerId, invoiceDate, dueDate, discountAmount, notes, items: [{ itemType, itemId, itemName, quantity, unitPrice }] }` -> Server computes subtotal, taxes, discount, total.
- `GET /api/businesses/{businessId}/invoices/{invoiceId}` — Member. Returns full invoice with line items and payments.
- `POST /api/businesses/{businessId}/invoices/{invoiceId}/payments` — Member. Body: `{ amount, paymentMethod, transactionReference, notes }` -> Atomically updates invoice paid amount and balance due.
- `POST /api/businesses/{businessId}/invoices/{invoiceId}/void` — `OWNER/ADMIN`. Body: `{ reason }` -> Voids invoice and creates audit record.

---

## 7. Expenses & Financial Reports
- `GET /api/businesses/{businessId}/expenses?startDate=&endDate=&category=` — `OWNER/ADMIN`.
- `POST /api/businesses/{businessId}/expenses` — `OWNER/ADMIN`. Body: `{ category, description, amount, expenseDate, paymentMethod, vendor }`
- `GET /api/businesses/{businessId}/reports/dashboard` — `OWNER/ADMIN`. Returns summary KPIs: invoiced revenue, cash collected, expenses, profit, appointment stats.
- `GET /api/businesses/{businessId}/reports/financial-summary?startDate=&endDate=` — `OWNER/ADMIN`.

---

## 8. Inventory & Suppliers
- `GET /api/businesses/{businessId}/products` — Member.
- `POST /api/businesses/{businessId}/products` — `OWNER/ADMIN`. Body: `{ sku, name, description, category, supplierId, purchaseCost, sellingPrice, currentStock, reorderThreshold }`
- `PATCH /api/businesses/{businessId}/products/{productId}` — `OWNER/ADMIN`.
- `POST /api/businesses/{businessId}/products/{productId}/stock-movement` — `OWNER/ADMIN`. Body: `{ movementType: "RECEIVED"|"ADJUSTMENT"|"RETURN"|"DAMAGE", quantityChange, reason }`
- `GET /api/businesses/{businessId}/stock-movements?productId=` — `OWNER/ADMIN`.
- `GET /api/businesses/{businessId}/suppliers` — Member.
- `POST /api/businesses/{businessId}/suppliers` — `OWNER/ADMIN`.

---

## 9. Attendance & Work Hours
- `POST /api/businesses/{businessId}/attendance/clock-in` — Authenticated staff member.
- `POST /api/businesses/{businessId}/attendance/clock-out` — Authenticated staff member.
- `GET /api/businesses/{businessId}/attendance/me` — Authenticated staff member. Returns own attendance sessions.
- `GET /api/businesses/{businessId}/admin/attendance?staffId=&date=` — `OWNER/ADMIN`.
- `POST /api/businesses/{businessId}/admin/attendance/{attendanceId}/correct` — `OWNER/ADMIN`. Body: `{ clockInTime, clockOutTime, reason }` -> Audited correction.

---

## 10. Audit Logs & Business Settings
- `GET /api/businesses/{businessId}/admin/audit-logs?page=&size=` — `OWNER/ADMIN`.
- `GET /api/businesses/{businessId}/settings` — Member.
- `PATCH /api/businesses/{businessId}/settings` — `OWNER/ADMIN`. Body: `{ taxRate, taxName, invoicePrefix, defaultAppointmentDuration, openingTime, closingTime }`
