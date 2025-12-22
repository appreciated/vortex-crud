# Resource Planner Demo

A resource planning application built with Vortex CRUD and jOOQ, managing appointments, rooms, and personnel.

## Features

- **Appointments**: Scheduling with conflict detection
- **Rooms**: Resource management with capacity
- **Persons**: Staff/Service provider management
- **Services**: Appointment types with duration and pricing

## Database Schema

Core tables: `appointment`, `appointment_type`, `room`, `person`, `person_appointment_type`

## Missing Features in Core

The following features are currently missing in the Vortex CRUD core but would greatly benefit this demo:

- **Calendar Resource View**: Support for "Scheduler" views (Timeline with resources on Y-axis) to easily visualize Room or Person availability side-by-side.
- **Structured Validation from Hooks**: Better mechanism to propagate validation errors from DataStore hooks (e.g., availability checks) to specific form fields in the UI.
- **Field Dependency/Calculation**: Native support for auto-calculating field values (e.g., updating 'End Time' automatically based on 'Start Time' and selected 'Service Duration').

## Missing Demo Features

Features that would be expected in a full implementation:

- **Recurring Appointments**: Logic to handle repeating events (daily, weekly).
- **Email Notifications**: Confirmation and reminder emails for appointments.
- **Drag-and-Drop Rescheduling**: Full interaction support on the calendar.
- **Working Hours**: Defining availability windows for Persons/Rooms.

## Technology

- jOOQ
- SQLite
- Spring Boot
- Java 21

## Getting Started

```bash
cd examples/jooq-ressource-planner-demo
mvn clean generate-sources
mvn spring-boot:run
```
