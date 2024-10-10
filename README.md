# FlowCMS
<img width="100" alt="image" src="./flow-cms.png"/> 

FlowCMS is a flexible high level extension for building applications using Vaadin Flow. It is designed to be easily extendable, enabling developers to customize both the backend and frontend to meet specific requirements. Below is an overview of the architecture, technologies used, and configuration guidelines.

## Tech-Stack
- **Spring Boot** for backend API development and dependency injection
- **Vaadin Flow** for frontend UI components
- **HOCON** for flexible configuration

## Core architecture and basic functions:
 -  **Modular UI system** - The UI has a modular structure, with renderers and factories working at different levels (e.g. Component List Factory, Renderer System).
 -  **Configuration system** - The use of HOCON for flexible configuration is implemented, which is made clear by the configuration examples provided.
 -  **Database validation** - There is a FlowCmsDatabaseSchemaValidator that validates the database schema against the application configuration.
 -  **Dynamic routing** - The DynamicRoute class enables dynamic routing based on the configuration.
 -  **UI components and renderers** - Several renderers and factories are available, such as DefaultEntityDetailRendererFactoryImpl, DefaultEntityItemCardRendererImpl, and DefaultRouteRendererFactoryImpl...
 -  **Entity management** - The GenericEntity and DynamicEntityManagerService classes indicate generic entity management.
 -  **Translations** - Translations thought of from the start
 -  **Icons** - Icons are interchangeable

## Roadmap (in no specific order):
- **List View Support**
- **Support Adding, Removing, Viewing relationships between entities (1:1, 1:n, n:n)**
- **Nested hierarchies**
- **Field Validation** - Custom hooks for more complex cases
- **Support for media**
- **User and Role management & Authentication** (optionally with Authentik)
- **Support more Form controls** Radiobutton Group, Select Group, ... 
- **RBAC via Configuration**
- **Entity Versioning**
- **Entity Auditing**
- **Extensibility and hook points**
- **Generic Block Route Renderer** - Currently this Framework only supports 
  - **Add Generic Blocks**
  
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
  name = "application.name"

  i18n-bundle-prefix = "some_i18n"

  selects {
    task-status {
      open = "selects.task-status.open"
      todo = "selects.task-status.todo"
      work-in-progress = "selects.task-status.progress"
      closed = "selects.task-status.closed"
    }
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
    }
  }
  routes = {
    "projects" = {
      default-route = true
      table = "projects"
      title = "route.projects.title"
      renderer = "grid"
      icon = "FACTORY"
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
    },
    "tasks" = {
      table = "tasks"
      icon = "TASKS"
      title = "route.tasks.title"
      renderer = "master-detail"
      render-configuration = {
        item-renderer = {
          type = "entity-item-card-renderer"
          title-field = "title"
          description-field = "description"
        }
        detail-renderer {
          title-field = "title"
          type = "form", children = [
            {field = "title", label = "route.tasks.labels.title"},
            {field = "description", label = "route.tasks.labels.description"},
            {field = "status", label = "route.tasks.labels.status"},
            {field = "due_date", label = "route.tasks.labels.due_date"}
          ]
        }
      }
      access-control = {
        roles = ["developer", "manager", "admin"]
      }
    }
  }
}
```

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