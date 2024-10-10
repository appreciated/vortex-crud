# FlowCMS
<img width="100" alt="image" src="./flow-cms.png"/> 

FlowCMS is a flexible framework for building applications of many kinds. The framework is designed to be easily extendable, enabling developers to customize both the backend and frontend to meet specific requirements. Below is an overview of the architecture, technologies used, and configuration guidelines.

## Tech-Stack
- **Spring Boot** for backend API development and dependency injection
- **Vaadin Flow** for frontend UI components
- **HOCON** for flexible configuration

## Features

### High to Low Level Components
The UI is built modular, from high (Route Renderer) to low level (Field Renderer), while each renderer on every level is being produced by factories. 
The factories are Injected indirectly using CI and can all be replaced with specific implementations. If only minor changes to the existing default implementations are wanted these can be extended.
- **Component List Factory**: Manages all UI components, defining how they should behave and appear.
- **Renderer System**: Includes default renderers like **grid**, **master-detail**, and **entity-item-card-renderer** and more.
- **Standard Implementation**: A set of default UI components is provided, which can be extended or replaced.

### User Management (TODO)

### Extensibility and Hook Points (TODO)
Developers can hook into various parts of the system to add custom logic:
- **Data Manipulation**: Customize how data is processed and stored.
- **Validation**: Add custom validation logic before saving or editing data.
- **Custom Events**: Define events triggered by user actions (e.g., record creation, updates).

### Dynamic UI Rendering
The UI is dynamically rendered based on view configurations defined in the configuration. This allows for flexibility in adjusting what fields and components are shown for different tables.

## Configuration via HOCON
The system supports view configuration where layouts and fields are defined in a HOCON file. 
In theory, you could also use Java since the configuration file is simply parsed into Java Classes but this wouldn't be that readable.

Below is an example how a route and the related table is configured:

```hocon
application {
   #...
   tables = {
      "projects" = {
         fields = {
            id = {type = "id", primary = true},
            name = {type = "text", required = true, max-length = 255},
            description = {type = "text", max-length = 500},
            start_date = {type = "date"},
            end_date = {type = "date"},
            created_at = {type = "datetime"},
            updated_at = {type = "datetime"}
         }
      }
   }
   #...  
   routes = {
      projects = {
         name: "project_view",
         table: "projects",
         renderer = "grid"
         renderer = "grid"
         render-configuration {
            item-renderer = {
               type = "entity-item-card-renderer"
               title-field = "name"
               description-field = "description"
            }
            detail-renderer {
               title-field = "name"
               type = "form", children = [
                  {field = "name", label = "route.projects.labels.name"},
                  {field = "description", label = "route.projects.labels.description"},
                  {field = "start_date", label = "route.projects.labels.start_date"},
                  {field = "end_date", label = "route.projects.labels.end_date"},
               ]
            }
         }
      }
   }
}
```

## Application Configuration (HOCON Format)
Below is a more complete sample configuration for setting up the project management application:

```hocon
application {
  name = "Project Management Application"

  user-management {
    enabled = true
    access-control = {
      roles = ["manager", "admin"]
    }
    registration = true
    additional-table-fields = [{name = "start_date", type = "date"}]
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
    tables = ["projects", "tasks", "employees", "comments"]
  }

  auditing {
    enabled = true
    actions = ["create", "update", "delete", "login", "logout"]
  }

  tables = {
    "projects" = {
      fields = {
        id = {type = "id", primary = true},
        name = {type = "text", required = true, max-length = 255},
        description = {type = "text", max-length = 500},
        start_date = {type = "date"},
        end_date = {type = "date"},
        created_at = {type = "datetime"},
        updated_at = {type = "datetime"}
      }
    },
    "tasks" = {
      fields = {
        id = {type = "id", primary = true},
        title = {type = "text", required = true, max-length = 255},
        description = {type = "text", max-length = 1000},
        assigned_to = {type = "number"},
        status = {type = "select", values = "task-status"},
        due_date = {type = "date", read-only-for-roles = ["developer"]},
        created_at = {type = "datetime"},
        updated_at = {type = "datetime"}
      }
    },
    "comments" = {
      fields = {
        id = {type = "id", primary = true},
        comment_text = {type = "text", max-length = 1000},
        employee_id = {type = "number"},
        created_at = {type = "datetime", default = "now()"}
      }
    }
  }

  routes = {
    "projects" = {
      default-route = true
      table = "projects"
      title = "route.projects.title"
      renderer = "grid"
      render-configuration {
        item-renderer = {
          type = "entity-item-card-renderer"
          title-field = "name"
          description-field = "description"
        }
        detail-renderer {
          title-field = "name"
          type = "form", children = [
            {field = "name", label = "route.projects.labels.name"},
            {field = "description", label = "route.projects.labels.description"},
            {field = "start_date", label = "route.projects.labels.start_date"},
            {field = "end_date", label = "route.projects.labels.end_date"},
          ]
        }
        access-control = {
          roles = ["manager", "admin"]
        }
      }
    }
  }
}
```

## Authentication with Authentik (TODO)

The project can be integrated with **Authentik** for authentication and authorization. This setup centralizes user management, OAuth2, and role-based access control.

## Getting Started with development

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