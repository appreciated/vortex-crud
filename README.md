# TurboCRUD (Current Working Title)
<img width="100" alt="TurboCRUD Logo" src="./turbo-crud.png"/> 

TurboCRUD is a framework engineered to speed up the development of CRUD-style applications utilizing Vaadin Flow. This is archived by providing a high-level abstraction layer, it empowers developers to super fast and while also retaining flexibility. Designed with extensibility at its core, TurboCRUD makes it simple to adapt and expand features as project needs evolve.

Rather than replacing Vaadin Flow, TurboCRUD enhances it by streamlining repetitive tasks and promoting reusability of components. This enables developers to focus more on application-specific logic and customization, maximizing productivity and efficiency.

## Tech Stack
- **Spring Boot**: Backend API development and dependency injection
- **Vaadin Flow**: Frontend UI components for building interactive applications
- **HOCON**: Configuration format that is compact, readable, and supports imports

## Key Features
- **Modular Architecture**: The Architecture is modular and flexible on every level (see [Architecture](#Architecture))
- **Flexible Configuration System**: Uses HOCON for dynamic configuration, making it easy to set up various parts of the application.
- **Database Schema Validation**: The `TurboCRUDDatabaseSchemaValidator` checks that the database schema matches the configuration at startup.
- **Dynamic Routing**: The `DynamicRoute` class enables routing based on configuration files, allowing flexible route management.
- **UI Components and Factories**: Factory implementations like `DefaultEntityDetailFactoryImpl` and `DefaultEntityItemCardFactoryImpl` dynamically configure UI components.
- **Low Code Entity Management**: The `GenericEntity` and `TurboCRUDEntityManagerService` handle generic entity management based on the database schema.
- **i18n Support**
- **Customizable Icons**
- **Entity Relationship Support**: Manage relationships between entities (1:1, 1:N).
- **Nested Hierarchies**
- **Custom Repositories**: Enable integration of custom data repositories.
- **Multiple Forms in Detail Views**: Support so a Detail can contain multiple forms (see [Architecture](#Architecture)).
- **[WIP] Additional Routes**:
  - **Kanban Route**: [Directus example](https://directus.pizza/admin/content/posts?bookmark=44)
- **Route Filters**: Filter entity lists in "master-detail" routes.

## Roadmap (in no particular order)
- **Extended Entity Relationship Support**: Add, remove, and view relationships (N:M).
- **Form Navigation**: Enable navigation in forms to other / custom routes.
- **Field Validation**: Add support for basic and advanced field validation hooks.
- **Media Support**: Enable adding, removing, and viewing media (as field and as a collection)
- **User and Role Management & Authentication**: (optionally using Authentik)
- **Additional Form Controls**: Add controls like Radio Button Groups and Select Groups, Links etc.
- **Role-Based Access Control (RBAC)**
- **Entity Versioning**
- **Entity Auditing**
- **Hook Points**: Add custom hook points for further flexibility.
- **Prefiltered Routes**: Show only specific items in routes as needed.
- **Additional Routes**:
    - **Calendar Route**: [Directus example](https://directus.pizza/admin/content/posts?bookmark=45)
    - **Map Route**: A route with a map where entities are shown on based on a latitude column and a longitude column
    - **Generic Block Route**: Support for generic blocks with flexible factory systems.
- **Custom Menu Routes**: Allow adding custom routes to the menu.
- **Alternative Collection Editing**: Provide alternative ways to edit Collections (see [Architecture](#Architecture)).
- **Configuration Pre-Checks**: Add checks to validate configuration before runtime.
- **Improve ability to validate the Configuration**: The configuration is not designed optimally for validation, requiring improvements to allow an effective validation.
- **Styling**: Improve styling options.
- **Check Database Index**: Since the UI and the Database is defined in a machine parsable format it is possible to check if fitting indices are available
- **Route Filters**: Add filtering for entity lists in "grid", "list" routes.

## Data Handling and Management
TurboCRUD uses an H2 database for development, managed by the custom class `TurboCRUDEntityManagerService`. The `TurboCRUDDatabaseSchemaValidator` ensures the database schema matches the HOCON configuration at startup.

### Core Concept: User-Defined Database Model
The database model is defined by the user, and TurboCRUD verifies that the view representation fits this model. However, some system-defined tables are exceptions, such as those for auditing, user, and role management:

```sql
-- Predefined system tables (examples)
CREATE TABLE users (...);
CREATE TABLE roles (...);
CREATE TABLE user_roles (...);
CREATE TABLE audit_log (...);
```

### Example User-Defined Tables
Users can define tables like `projects`, `tasks`, and `task_comments` to fit their needs:

```sql
CREATE TABLE projects (...);
CREATE TABLE tasks (...);
CREATE TABLE task_comments (...);
```

This version is concise, focusing on the key points while providing essential examples.
## Architecture

The diagram below presents a simplified version of the architecture, illustrating the relationships between the different components. The main difference between this representation and the actual architecture is that classes are not instantiated directly. Instead, instantiation is determined by the type specified in the configuration (e.g., "factory" = "grid" or "type" = "form"). A FactoryRegistry is used to retrieve and return the appropriate component factory based on this configuration.

```mermaid
classDiagram
   class Table {
   }

   class Column {
   }

   class Route {
   }

   class AppLayout {
   }

   class MenuItem {
   }

   class MasterDetailRoute {
   }

   class ListRoute {
   }

   class GridRoute {
   }

   class Detail {
   }

   class Form {
   }

   class Field {
   }

   class Collection {
   }

   class Dialog {
   }

   Route <|-- MasterDetailRoute: "extends"
   Route <|-- ListRoute: "extends"
   Route <|-- GridRoute: "extends"
   Route --> AppLayout: "contains"
   AppLayout --> MenuItem: "contains"
   MasterDetailRoute --> Detail: "is"
   ListRoute --> Detail: "forwards to"
   GridRoute --> Detail: "forwards to"
   Detail --> Form: "contains"
   Form --> Field: "contains"
   Form --> Collection: "contains"
   Route --> Table: "references"
   Table --> Column: "contains"
   Field --> Column: "references"
   Collection --> Column: "references"
   Collection --> Dialog: "creates"
   Dialog --> Detail: "creates"
```

## Configuration via HOCON
TurboCRUD supports configuration through HOCON files where routes and tables are defined.

Note: While Java classes could theoretically be used for configuration, as HOCON files are parsed into Java classes, this approach is not currently supported. HOCON is preferred as it enhances both readability and maintainability.

### Example Configuration

Below is an example of configuring a route and the associated table:

```hocon
application {
  #...
  tables = {
    "projects" = {
      columns = {
        id = {factory = "id", primary = true},
        name = {factory = "text", required = true, validation = {max-length = 255}},
        description = {factory = "textarea", validation = {max-length = 500}},
        start_date = {factory = "date"},
        end_date = {factory = "date"},
        created_at = {factory = "datetime"},
        updated_at = {factory = "datetime"}
      }
    },
    # ...
  }
  # ...  
  routes = {
    "projects-list" = {
      default-route = true
      factory = "grid"
      table = "projects"
      icon = "FACTORY"
      title = "route.projects.title-card"
      configuration = {
        factory = "card"
        title-column = "name"
        description-column = "description"
      }
      child = {
        table = "projects"
        title = "route.tasks.title"
        factory = "form"
        configuration = {
          title-column = "name"
          children = [
            {type = "field", column = "name", label = "route.projects.labels.name"},
            {type = "field", column = "description", label = "route.projects.labels.description"},
            {type = "field", column = "start_date", label = "route.projects.labels.start_date"},
            {type = "field", column = "end_date", label = "route.projects.labels.end_date"}
          ]
        }
      }
      roles = ["manager", "admin"]
    }
    # ...
  }
}
```

## Application Configuration (HOCON Format)
Here’s a more complete sample configuration for setting up a project management application:

```hocon
application {
  name = "application.name"

  i18n-bundle-prefix = "some_i18n"

  user-management {
    enabled = true
    access-control {
      roles = ["manager", "admin"]
    }
    sign-up = true
    additional-fields = [{name = "start_date", type = "date"}]
  }

  selects {
    task-status {
      open = "selects.task-status.open"
      todo = "selects.task-status.todo"
      work-in-progress = "selects.task-status.progress"
      closed = "selects.task-status.closed"
    }
  }

  versioning {
    enabled = true
    repositories = ["projects", "tasks", "task_comments"]
  }

  auditing {
    enabled = true
    actions = ["create", "update", "delete", "login", "logout"]
  }

  repositories {
    "projects" {
      fields {
        id {factory = "id", primary = true},
        name {factory = "text", required = true, validation {max-length = 255}},
        description {factory = "textarea", validation {max-length = 500}},
        start_date {factory = "date"},
        end_date {factory = "date"},
        created_at {factory = "datetime"},
        updated_at {factory = "datetime"}
      }
    },
    "tasks" {
      fields {
        id {factory = "id", primary = true},
        title {factory = "text", required = true, validation {max-length = 255}},
        description {factory = "textarea", validation {max-length = 1000}},
        assigned_to {factory = "reference", repository = "users", field = "id", filter-field = "username", children = ["username"]},  # 1:1 Relation
        status {factory = "select", values = "task-status"},
        due_date {factory = "date", read-only-for-roles = ["developer"]},
        created_at {factory = "datetime"},
        updated_at {factory = "datetime"}
      }
    },
    "task_comments" {
      fields {
        id {factory = "id", primary = true},
        comment_text {factory = "textarea", validation {max-length = 1000}},
        user_id {factory = "number"},
        created_at {factory = "datetime", default = "now()"}
      }
    }
  }
  routes {
    "projects-list" {
      default-route = true
      factory = "grid"
      repository = "projects"
      icon = "FACTORY"
      title = "route.projects.title-card"
      configuration {
        factory = "card"
        title-field = "name"
        description-field = "description"
      }
      child {
        repository = "projects"
        factory = "form"
        configuration {
          title-field = "name"
          children = [
            {type = "field", field = "name", label = "route.projects.labels.name"},
            {type = "field", field = "description", label = "route.projects.labels.description"},
            {type = "field", field = "start_date", label = "route.projects.labels.start_date"},
            {type = "field", field = "end_date", label = "route.projects.labels.end_date"}
          ]
        }
      }
      roles = ["manager", "admin"]
    }
    "tasks" {
      icon = "TASKS"
      repository = "tasks"
      title = "route.tasks.title"
      factory = "submenu"
      children {
        "tasks-done" {
          icon = "CHECK_CIRCLE"
          repository = "tasks"
          title = "route.tasks.title"
          factory = "master-detail"
          configuration {
            factory = "card"
            title-field = "title"
            description-field = "description"
          }
          child {
            repository = "tasks"
            factory = "multi-form"
            configuration {
              title-field = "title"
              children = [
                {
                  title-field = "title"
                  children = [
                    {type = "field", field = "title", label = "route.tasks.labels.title"},
                    {type = "field", field = "description", label = "route.tasks.labels.description"},
                    {type = "field", field = "status", label = "route.tasks.labels.status"},
                    {type = "field", field = "due_date", label = "route.tasks.labels.due_date"},
                    {type = "field", field = "assigned_to", label = "route.tasks.labels.assigned_to"}, # 1:1 Relation
                    {
                      type = "collection"  # 1:N Relation
                      factory = "list"
                      repository = "task_comments"
                      reference = "task_id"
                      label = "route.tasks.labels.comments"
                      children = [
                        "comment_text"
                      ]
                      dialog {
                        factory = "form"
                        empty-message = "route.tasks.labels.comments-empty-message"
                        child {
                          factory = "form"
                          configuration {
                            title-field = "name"
                            children = [
                              {type = "field", field = "comment_text", label = "route.tasks.labels.comment"}
                            ]
                          }
                        }
                      }
                    }
                  ]
                }
              ]
            }
          }
        }
        "other-tasks" {
          icon = "TASKS"
          repository = "tasks"
          title = "route.tasks.title"
          factory = "master-detail"
          configuration {
            factory = "card"
            title-field = "title"
            description-field = "description"
          }
          child {
            repository = "tasks"
            factory = "form"
            configuration {
              title-field = "title"
              children = [
                {type = "field", field = "title", label = "route.tasks.labels.title"},
                {type = "field", field = "description", label = "route.tasks.labels.description"},
                {type = "field", field = "status", label = "route.tasks.labels.status"},
                {type = "field", field = "due_date", label = "route.tasks.labels.due_date"},
                {type = "field", field = "assigned_to", label = "route.tasks.labels.assigned_to"}, # 1:1 Relation
                {
                  type = "collection"  # 1:N Relation
                  factory = "list"
                  repository = "task_comments"
                  reference = "task_id"
                  label = "route.tasks.labels.comments"
                  children = [
                    "comment_text"
                  ]
                  dialog {
                    factory = "form"
                    empty-message = "route.tasks.labels.comments-empty-message"
                    child {
                      factory = "form"
                      configuration {
                        title-field = "name"
                        children = [
                          {type = "field", field = "comment_text", label = "route.tasks.labels.comment"}
                        ]
                      }
                    }
                  }
                }
              ]
            }
          }
        }
      }
    }
  }
}
```

## Getting Started with Development

1. **Clone the repository**
2. **Run the application**:
   - Use the provided SQL schema to set up the database.
   - Configure application properties for H2 or other databases.
   - Start the Spring Boot server:
     ```bash
     ./mvnw spring-boot:run
     ```
