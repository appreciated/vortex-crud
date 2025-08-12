# Developer Guide

## Overview

`vortex-crud` is a modular framework built on top of Vaadin Flow and Spring Boot. It simplifies the creation of CRUD applications by letting developers declare routes, UI components, entities, and data bindings. These declarations are registered automatically at runtime, so most of the heavy lifting is handled by the framework.

The stack includes Spring Boot and Vaadin Flow, with optional jOOQ or JPA implementations for data access.

## Project Structure

The root Maven module defines five sub-modules:

| Module | Purpose |
|--------|---------|
| **core** | Core framework functionality such as configuration, UI factories, and data store interfaces. |
| **jpa** | `DataStore` implementation using JPA and helper classes for configuration. |
| **jooq** | `DataStore` implementation using jOOQ and related helpers. |
| **examples** | Ready-to-run example applications for both jOOQ and JPA. |
| **ui-test** | Vaadin UI tests and related configuration. |

### Core Module

The `core` module contains the building blocks of the framework. Important packages include:

- `config` – Interfaces for declarative configuration (e.g., `VortexCrudConfigService`).
- `service` – Services that orchestrate runtime behavior such as `DynamicRouteGenerator`.
- `data_provider` – Defines the `VortexCrudDataStore` interface with CRUD and filtering methods.
- `ui` – Factories and renderers for dynamic UI generation, including `InternalDynamicRoute` that resolves the correct view based on URL paths.

### jpa & jooq Modules

These modules provide concrete `DataStore` implementations and syntactic helpers for using either JPA or jOOQ to access the database.

### examples Module

Contains runnable demo applications illustrating how to configure `vortex-crud` with jOOQ or JPA.

### ui-test Module

Holds Vaadin UI tests and their configuration.

## Key Concepts for Beginners

1. **Configuration Objects**  
   Understand `Application`, `DataStoreConfig`, and `RouteRenderer`. Your application implements `VortexCrudConfigService` to supply these objects.

2. **Routing and UI Factories**  
   Route factories generate UI components dynamically. Components such as `DetailRouteSetting` and `VortexCrudRouteFactoryRegistry` help build navigation hierarchies.

3. **Data Modeling**  
   A `DataStore` represents a table or entity. Fields and relations are described declaratively, and the jpa/jooq modules provide helpers for configuration.

4. **Suggested Learning Path**  
   - Basics of Spring Boot and dependency injection  
   - Vaadin Flow routing and components  
   - jOOQ or JPA for database access  
   - Builder pattern and declarative configuration styles

With these elements, developers can incrementally define routes, forms, and data sources to quickly assemble functional CRUD interfaces.
