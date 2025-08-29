⚠️ **Disclaimer:** This document is stale and has drifted significantly from the current state of the project.

**Detailed Concept for a vortex-crud**

1. **Overview**

The presented framework is built on a flexible, modular approach that allows both the backend and frontend to be dynamic and customizable. The key aspect is code generation and customization based on metadata and configurations stored in a relational database. Developers can easily modify the system using hook points and custom extensions. The use of Spring Boot for the backend API, Vaadin for UI rendering, and HOCON for configuration management creates a highly flexible and extensible system.

2. **Architecture of the Tech Stack**

- **Spring Boot**: Backend service for managing collections and providing the API for dynamic rendering of UI components as well as view configuration management.
- **Vaadin**: Vaadin Flow is used as the frontend framework for dynamically rendering the user interface based on configuration data provided by the backend.
- **HOCON**: Flexible configuration of application settings, collection fields, relationships, view configurations, and UI components via HOCON (Human-Optimized Config Object Notation).
- **Authentik**: An optional authentication and authorization platform supporting OAuth2, OpenID Connect, and WebAuthn. Authentik can centralize role-based access management and manage JWTs for API calls.

3. **Extensibility and Hook Points**

The framework offers developers various ways to hook into the system logic and implement custom logic or extensions. The main hook points include:

- **Data Manipulation**: Developers can customize how data is processed and stored, allowing them to implement specific manipulations for particular fields or data operations.
- **Validations**: Custom validation rules can be added before saving or editing records.
- **Custom Events**: The ability to define custom events triggered by specific actions, such as creating, editing, or deleting records.

4. **Modular UI Component System**

The framework uses a modular user interface, where each component is defined through a central **Component List Factory**. This factory ensures that UI components such as text fields, dropdowns, or tables can be dynamically loaded and customized.

- **Component List Factory**: Defines interfaces for all UI components. Each component can be replaced or extended by registering it in the factory.
- **Standard Components**: The framework provides a variety of standard components suitable for most use cases. These can be replaced by custom components if needed.

5. **Dynamic Rendering of the UI**

The user interface is dynamically generated based on view configurations stored in the database. These configurations contain metadata about the fields to be displayed and the UI components to be used.
Process:

- The Spring Boot API provides the view configurations (including fields, layouts, and UI components) to the Vaadin frontend.
- The Vaadin frontend dynamically renders the UI components based on these configurations.
- Changes in the view configuration lead to an immediate update of the UI without requiring additional programming.

Example: A view configuration might specify that a text field is used for the "title" field, while a textarea is displayed for the "description" field.

6. **Database Structure and SQL Script**

The data model is designed to be flexible, supporting dynamic collections, fields, relationships, and view configurations. An SQL script is provided to create the database structure, which includes the following core components:

- **User Management (users)**: Table for managing users.
- **Role Management (roles)**: Dynamic management of roles and permissions.
- **Collections (collections)**: Table structure for storing dynamic data points.
- **Fields (fields)**: Definition of fields within a collection, including type, options, and position.
- **Records (records)**: Storage of dynamic records as JSON documents.
- **View Configurations (view_configs)**: Tables for defining the layouts and UI components for each view.
- **Relationships (relationships)**: Management of relationships between collections (e.g., One-to-Many, Many-to-One).
- **Audit Log**: Logging of user actions such as creating, editing, or deleting records.

7. **Database Migration and Initialization**

The framework supports automatic initialization and migration of the database at startup using H2 (as an in-memory database) and Liquibase for managing database changes. This allows flexible management of the database structure and automatic adjustments.

8. **Authentik for Authentication and Authorization**

Authentik can optionally be integrated to provide centralized management of user roles and authentication methods. This disables the internal role-based management and routes authentication requests to Authentik. Authentik manages OAuth2, OpenID Connect, and WebAuthn.

- Spring Boot acts as an OAuth2 Resource Server, validating JWTs issued by Authentik.
- Vaadin integrates seamlessly with Authentik for user login and authentication.

9. **HOCON Configuration Example**

The framework is fully controlled via a HOCON configuration file. This configuration defines:

- **User Management**: Enable user management and roles.
- **Select Fields**: Multilingual definition of selection options for specific fields (e.g., "Open", "Closed").
- **Versioning**: Enable versioning for specific collections.
- **Auditing**: Enable audit logging for user actions.
- **Collection Configurations**: Definition of collections and their fields.
- **View Configurations**: Dynamic definition of UI views with access control.
- **Relationships**: Definition of relational links between collections.

10. **Summary**

This framework provides a flexible and extensible foundation for the dynamic development of backend and frontend applications. The approach of generated code, dynamic UI design, and centralized authentication enables quick and easy adaptation to specific requirements without extensive manual coding. It combines the benefits of Spring Boot, Vaadin, and HOCON to deliver a fully dynamic and highly customizable system.