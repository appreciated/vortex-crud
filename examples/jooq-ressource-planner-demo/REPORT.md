# Resource Planner Demo - Missing Features Report

This report outlines the key features, functional gaps, and technical improvements identified for the `jooq-ressource-planner-demo` application.

## 1. Missing Core UI/UX Features

These features are critical for a seamless user experience in a resource planning application but are currently absent or limited.

### 1.1 Calendar Resource View (Timeline)
- **Current State**: The application provides a standard Calendar view (`JooqCalendarRoute`) for appointments and a Master-Detail view (`JooqMasterDetailRoute`) for rooms.
- **Missing**: A **Scheduler/Timeline View** where resources (Rooms or Persons) are listed on the Y-axis and time on the X-axis. This is essential for:
  - Visualizing availability across multiple resources simultaneously.
  - Identifying gaps or overlaps at a glance.
  - Drag-and-drop scheduling between resources.

### 1.2 Field Dependencies & Auto-Calculation
- **Current State**: Users must manually select both `Start Time` and `End Time`.
- **Missing**:
  - **Auto-calculation**: Selecting a `Service` (Appointment Type) should automatically calculate the `End Time` based on the `Start Time` and the service's defined duration.
  - **Dynamic Filtering**: Selecting a `Room` should ideally filter `Persons` (or vice-versa) based on who is available/qualified for that room, though the current availability check happens only upon save.

### 1.3 Structured Validation
- **Current State**: Business logic validation (e.g., availability checks, working hours) throws `RuntimeException`s in `AppointmentBusinessService`. This results in a generic error notification displayed to the user.
- **Missing**: **Field-level validation**. Errors should be propagated to the specific form fields (e.g., highlighting the `Room` field in red if the room is double-booked) to provide immediate and clear feedback.

### 1.4 Drag-and-Drop Rescheduling
- **Current State**: Appointments can be viewed on the calendar but likely require opening the form to edit the date/time.
- **Missing**: Ability to drag an appointment to a new time slot or resource on the calendar/timeline and have the changes persisted automatically (with validation).

## 2. Missing Functional Features

### 2.1 Native Time Field Support
- **Current State**: Working hours (`WORKING_HOURS_START`, `WORKING_HOURS_END`) are stored as `VARCHAR` and managed via `JooqTextField`. Validation relies on parsing strings (e.g., "09:00").
- **Missing**: Integration of a dedicated **Time Picker** component or `JooqTimeField` to ensure consistent data entry and better UX, removing the reliance on regex/string parsing for time values.

### 2.2 Advanced Dashboard
- **Current State**: A Kanban board (`JooqKanbanRoute`) exists for visualizing appointments by status.
- **Missing**: A comprehensive **Dashboard** providing high-level metrics, such as:
  - Resource utilization rates (e.g., Room A is 80% booked).
  - Upcoming appointment counts.
  - Revenue projections based on service prices.

### 2.3 Comprehensive Notification System
- **Current State**: Email sending is implemented using `JavaMailSender` (or mocked via logging).
- **Missing**:
  - **In-App Notifications**: While a `NotificationRecord` table exists, a dedicated UI center for users to view history, mark as read, etc., is basic (`NotificationPanel` exists but might need more integration).
  - **SMS/Push Notifications**: Integration with other channels beyond email.

## 3. Technical Debt & Improvements

### 3.1 Test Coverage
- **Current State**: Only a `SmokeTest` exists.
- **Missing**:
  - **Unit Tests**: Specifically for `AppointmentBusinessService` to cover edge cases in recurrence and availability logic (e.g., leap years, time zone shifts).
  - **Integration Tests**: verifying the full flow from API/Form to Database and back.

### 3.2 Exception Handling
- **Current State**: `availabilityCheck` throws generic `RuntimeException`.
- **Improvement**: Define specific custom exceptions (e.g., `ResourceUnavailableException`) that can be caught and mapped to specific UI responses or HTTP status codes if an API is exposed.

### 3.3 Hardcoded Configuration
- **Current State**: Recurrence frequencies and roles are somewhat hardcoded or defined in code maps.
- **Improvement**: Move these to database lookup tables or strictly typed Enums to ensure consistency and referential integrity.
