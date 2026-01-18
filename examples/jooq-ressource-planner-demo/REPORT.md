# Missing Features Report: Jooq Resource Planner Demo

This report outlines key features and integrations that are currently missing or incomplete in the `jooq-ressource-planner-demo` application.

## 1. Critical Functional Gaps

### 1.1. Search Route Configuration
**Issue:** The `SearchRoute` is instantiated in `ResourcePlannerConfig.java` but is not fully configured.
**Details:**
- The current implementation is:
  ```java
  routes.put("search", SearchRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
          .title("route.search.title")
          .iconFactory(VaadinIcon.SEARCH::create)
          .build());
  ```
- **Missing:** It requires the `.searchableRoutes(...)` method to be called with a list of routes (e.g., appointments, persons, customers) that should be indexed for search. Without this, the search functionality will yield no results or behave unexpectedly.

### 1.2. Email Template Integration
**Issue:** The application defines `EMAIL_TEMPLATES` and `SETTINGS` (with a default template reference), but the business logic ignores them.
**Details:**
- The `AppointmentBusinessService.sendEmailNotification` method uses hardcoded strings:
  ```java
  String subject = "Appointment Update";
  String body = String.format("Your appointment on %s is %s.", record.getStartTime(), record.getStatus());
  ```
- **Missing:** Logic to:
  1. Fetch the `SETTINGS` record to find the `DEFAULT_EMAIL_TEMPLATE_ID`.
  2. Fetch the corresponding `EMAIL_TEMPLATES` record.
  3. Replace placeholders in the template (e.g., `{{startTime}}`, `{{status}}`, `{{customerName}}`) with actual data from the `AppointmentRecord`.
  4. Fallback to a default message if no template is configured.

## 2. Feature Enhancements

### 2.1. Dashboard View
**Issue:** The application launches directly into the Calendar view.
**Details:**
- **Missing:** A dedicated Dashboard view that provides an overview of the system status.
- **Suggested Metrics:**
  - Upcoming appointments for the day/week.
  - Room utilization status (Active/Inactive, Occupied/Free).
  - Quick actions (New Appointment, New Customer).
  - Recent notifications.

### 2.2. Reporting Module
**Issue:** No functionality exists to generate aggregate reports.
**Details:**
- **Missing:** Reports for:
  - Appointments per Customer/Person/Room.
  - Revenue estimation based on `AppointmentType` prices.
  - Cancellation rates.

### 2.3. Advanced Status Workflow
**Issue:** The appointment status is a simple text field (`STATUS`).
**Details:**
- **Missing:**
  - A structured workflow (e.g., Requested -> Confirmed -> In Progress -> Completed/Cancelled).
  - UI Actions (Buttons) in the `AppointmentForm` to transition states (e.g., "Confirm", "Cancel") instead of manually typing the status string.

### 2.4. User Agreement Visibility
**Issue:** The `Appointment` form has a "User Agreement Accepted" checkbox, but the user cannot see what they are accepting.
**Details:**
- The text exists in `SETTINGS.USER_AGREEMENT_TEXT`.
- **Missing:** A mechanism (e.g., a Dialog or a Link) next to the checkbox in the form to display the content of `SETTINGS.USER_AGREEMENT_TEXT`.

### 2.5. Time Picker Components
**Issue:** "Working Hours" for Rooms and Persons are stored as strings.
**Details:**
- **Missing:** A dedicated Time Picker component for `HH:mm` format. Currently, these are `JooqTextField`s, relying on manual entry. *Note: This may require upstream library support (JooqTimePickerField).*

## 3. Technical Debt

### 3.1. Audit Logging
**Issue:** Changes to sensitive data (Appointments, Settings) are not logged for audit purposes.
**Details:**
- **Missing:** An `AuditLog` table and corresponding hooks to record WHO changed WHAT and WHEN, beyond the basic `afterUpdates` notification hook.
