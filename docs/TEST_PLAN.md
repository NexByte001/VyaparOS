# VyaparOS Automated Test Plan

## 1. Test Scenarios

### 1.1 Tenant Isolation & Security
- **Cross-tenant Customer Isolation**: User in Business A cannot fetch or update Customer in Business B.
- **Cross-tenant Attendance Protection**: Staff member cannot fetch or clock in/out for another staff member.
- **Role Boundary Enforcement**: Staff member attempting to access `/api/businesses/{id}/reports/dashboard` or `/expenses` receives `403 Forbidden`.
- **Unauthenticated Access**: Direct REST calls without Bearer JWT receive `401 Unauthorized`.

### 1.2 Financial Calculations & Transactions
- **Invoice Line Item Math**: Verify subtotal = sum(quantity * unitPrice), tax = subtotal * (taxRate/100), total = subtotal + tax - discount using `BigDecimal`.
- **Partial Payment Balance**: Recording a payment of 50 on an invoice of 100 sets status to `PARTIALLY_PAID` and `balance_due = 50.00`. Recording remainder sets status to `PAID` and `balance_due = 0.00`.
- **Overpayment Rejection**: Attempting to pay 120 on a 100 balance is rejected.

### 1.3 Inventory Atomic Updates
- **Stock Movement Trigger**: Invoicing a product line item atomically reduces product stock and inserts a `SALE` stock movement record.
- **Negative Stock Prevention**: Attempting to sell more stock than currently available fails transactionally.

### 1.4 Appointment Scheduling & Conflicts
- **Conflict Detection**: Booking an overlapping appointment for the same staff member returns `409 Conflict`.
- **Status Lifecycle**: Valid transitions `SCHEDULED -> CONFIRMED -> IN_PROGRESS -> COMPLETED`.
