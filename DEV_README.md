# Developer Guide

## Overview

`vortex-crud` is a modular framework built on top of Vaadin Flow and Spring Boot. It simplifies the creation of CRUD
applications by letting developers declare routes, UI components, entities, and data bindings. These declarations are
registered automatically at runtime, so most of the heavy lifting is handled by the framework.

The stack includes Spring Boot and Vaadin Flow, with optional jOOQ or JPA implementations for data access.

## Project Structure

The root Maven module defines five sub-modules:

| Module           | Purpose                                                                                                                                               |
|------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| **core**         | Core framework functionality such as configuration, UI factories, and data store interfaces.                                                          |
| **jpa**          | `VortexCrud` implementation using JPA and helper classes for configuration. Also contains the ui test implementations based on the ui-test-base       |
| **jooq**         | `VortexCrud` implementation using jOOQ and related helpers. Also contains the ui test implementations based on the ui-test-base                        |
| **examples**     | Ready-to-run example applications for both jOOQ and JPA.                                                                                              |
| **ui-test-base** | Contains the ui test base for vortex-crud. This only contains the interaction using the chrome driver and the shared i18n tuples for the translation. |

### Core Module

The `core` module contains the building blocks of the framework. Important packages include:

- `config` – Interfaces for declarative configuration (e.g., `VortexCrudConfigService`).
- `service` – Services that orchestrate runtime behavior such as `DynamicRouteGenerator`.
- `data_provider` – Defines the `VortexCrudDataStore` interface with CRUD and filtering methods.
- `ui` – Factories and renderers for dynamic UI generation, including `InternalDynamicRoute` that resolves the correct
  view based on URL paths.

### jpa & jooq Modules

These modules provide concrete `DataStore` implementations and syntactic helpers for using either JPA or jOOQ to access
the database.

### examples Module

Contains runnable demo applications illustrating how to configure `vortex-crud` with jOOQ or JPA.

### UI Tests

UI tests are split across dedicated modules:

- `ui-test-base` – shared Page Objects, test scaffolding, and i18n tuples. Page
  objects live in `ui-test-base/src/main/java/com/github/appreciated/vortex_crud/ui_test_base/pages`,
  abstract test classes in `ui-test-base/src/main/java/com/github/appreciated/vortex_crud/ui_test_base/tests`,
  and common configuration in `ui-test-base/src/main/java/com/github/appreciated/vortex_crud/ui_test_base/config`.
- `jpa` and `jooq` – implementation‑specific tests that extend the base
  classes. Test classes are located under
  `jpa/src/test/java/com/github/appreciated/vortex_crud/test/jpa/ui` and
  `jooq/src/test/java/com/github/appreciated/vortex_crud/test/jooq/ui`.
- **Data seeding** – each test suite loads its dataset from SQL files placed
  next to the tests in
  `jpa/src/test/resources/com/github/appreciated/vortex_crud/test/jpa/ui/*_test.sql`
  and
  `jooq/src/test/resources/com/github/appreciated/vortex_crud/test/jooq/ui/*_test.sql`.

This layout centralizes shared UI test infrastructure while allowing each
persistence module to define its own test cases and seed data.

## Key Concepts for Beginners

1. **Configuration Objects**  
   Understand `Application`, `DataStoreConfig`, and `RouteRenderer`. Your application implements
   `VortexCrudConfigService` to supply these objects.

2. **Routing and UI Factories**  
   Route factories generate UI components dynamically. Components such as `DetailRouteSetting` and
   `VortexCrudRouteFactoryRegistry` help build navigation hierarchies.

3. **Data Modeling**  
   A `DataStore` represents a table or entity. Fields and relations are described declaratively, and the jpa/jooq
   modules provide helpers for configuration.

4. **Suggested Learning Path**
    - Basics of Spring Boot and dependency injection
    - Vaadin Flow routing and components
    - jOOQ or JPA for database access
    - Builder pattern and declarative configuration styles

With these elements, developers can incrementally define routes, forms, and data sources to quickly assemble functional
CRUD interfaces.
