# FlowCMS

This repository contains a dynamic and flexible framework for building project management applications. The framework is designed to be easily extendable, enabling developers to customize both the backend and frontend to meet specific requirements. Below is an overview of the architecture, technologies used, and configuration guidelines.

## Tech Stack

- **Spring Boot** for backend API development
- **Vaadin** for frontend UI development
- **HOCON** for flexible configuration

## Features

### Extensibility and Hook Points
Developers can hook into various parts of the system to add custom logic:
- **Data Manipulation**: Customize how data is processed and stored.
- **Validation**: Add custom validation logic before saving or editing data.
- **Custom Events**: Define events triggered by user actions (e.g., record creation, updates).

### UI Component System
The UI is built modularly, with each component easily replaceable to provide specific implementations for different use cases.
- **Component List Factory**: Manages all UI components, defining how they should behave and appear.
- **Standard Implementation**: A set of default UI components is provided, which can be extended or replaced.

### Dynamic UI Rendering
The UI is dynamically rendered based on view configurations stored in the database. This allows for flexibility in adjusting what fields and components are shown for different collections.

## Dynamic View Configuration Example

The system supports dynamic view configuration where layouts and fields are defined in JSON schema. Below is an example of a view configuration:

```json
{
  "name": "project_view",
  "collection": "projects",
  "layout": [
    {"component": "text", "field": "name"},
    {"component": "textarea", "field": "description"},
    {"component": "date", "field": "start_date"},
    {"component": "date", "field": "end_date"}
  ],
  "access_control": {
    "roles": ["manager", "admin"]
  }
}
```

## Application Configuration (HOCON Format)

Below is a sample configuration for setting up the project management application:

```hocon
application {
  name = "Project Management Application"

  user_management {
    enabled = true
    access_control = {
      roles = ["manager", "admin"]
    }
    registration = true
    additional_collection_fields = [{name = "start_date", type = "date"}]
  }

  selects {
    task_status {
       open = { en="Open", de="Offen" }
       todo = { en="ToDo", de="Zu erledigen" }
       work_in_progress = { en="Work in progress", de="In Arbeit" }
       closed = { en="Closed", de="Geschlossen" }
    }
  }

  versioning {
    enabled = true
    collections = ["projects", "tasks", "employees", "comments"]
  }

  auditing {
    enabled = true
    actions = ["create", "update", "delete", "login", "logout"]
  }

  collections = [
    {
      name = "projects"
      fields = {
        id = {type = "id", primary = true},
        name = {type = "text", required = true, max_length = 255},
        description = {type = "text", max_length = 500},
        start_date = {type = "date"},
        end_date = {type = "date"},
        created_at = {type = "datetime", default = "now()"},
        updated_at = {type = "datetime", default = "now()"}
      }
    },
    {
      name = "tasks"
      fields = {
        id = {type = "id", primary = true},
        title = {type = "text", required = true, max_length = 255},
        description = {type = "text", max_length = 1000},
        assigned_to = {type = "number"},
        status = {type = "task_status"},
        due_date = {type = "date"},
        created_at = {type = "datetime", default = "now()"},
        updated_at = {type = "datetime", default = "now()"}
      }
    },
    {
      name = "comments"
      fields = {
        id = {type = "id", primary = true},
        comment_text = {type = "text", max_length = 1000},
        employee_id = {type = "number"},
        created_at = {type = "datetime", default = "now()"}
      }
    }
  ]

  views = [
    {
      name = "project_view"
      collection = "projects"
      layout = [
        {component = "text", field = "name"},
        {component = "textarea", field = "description"},
        {component = "date", field = "start_date"},
        {component = "date", field = "end_date"}
      ]
      access_control = {
        roles = ["manager", "admin"]
      }
    }
  ]
}
```

## Authentication with Authentik

The project can be integrated with **Authentik** for authentication and authorization. This setup centralizes user management, OAuth2, and role-based access control.

- **Authentik** manages user roles and authentication (OAuth2, OpenID Connect, WebAuthn).
- **Spring Boot** acts as the resource server, validating JWTs.
- **Vaadin** uses JWTs for API access after user authentication through Authentik.

## Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/appreciated/flow-cms.git
   ```
2. **Run the application**:
  - Use the provided SQL schema to set up the database.
  - Configure application properties for H2 or other databases.
  - Start the Spring Boot server:
    ```bash
    ./mvnw spring-boot:run
    ```

## License
This project is licensed under the MIT License.

---

This README outlines the key components, architecture, and setup required for running the project management application framework.