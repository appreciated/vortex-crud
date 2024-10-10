# FlowCMS (Current working title)
<img width="100" alt="image" src="./flow-cms.png"/> 

FlowCMS is a flexible high level abstraction for creating CRUD-style applications using Vaadin Flow much faster. It is designed to be easily extendable. Below is an overview of the architecture, technologies used, and configuration guidelines.  
Note that it is not meant to replace Flow at all, since not everything is CRUD. It is meant to speed up specific tasks.

## Tech-Stack
- **Spring Boot** for backend API development and dependency injection
- **Vaadin Flow** for frontend UI components
- **HOCON** for a compact and readable configuration (also imports!)

## Core architecture and basic functions:
 -  **Modular UI system** - The UI has a modular structure, with factories working at different levels (e.g. Component List Factory, factory System).
 -  **Configuration system** - The use of HOCON for flexible configuration is implemented, which is made clear by the configuration examples provided.
 -  **Database validation** - There is a FlowCmsDatabaseSchemaValidator that validates the database schema against the application configuration.
 -  **Dynamic routing** - The DynamicRoute class enables dynamic routing based on the configuration.
 -  **UI components and factories** - Several factories and factories are available, such as DefaultEntityDetailfactoryFactoryImpl, DefaultEntityItemCardfactoryImpl, and DefaultRoutefactoryFactoryImpl...
 -  **Entity management** - The GenericEntity and FlowCmsEntityManagerService classes enable generic entity management.
 -  **Translations** - Translations thought of from the start
 -  **Icons** - Icons are interchangeable

## Roadmap (in no specific order):
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
- **Generic Block Route factory** - Currently this Framework only supports 
  - **Add Generic Blocks**
- **Custom Repositories**
  
## How is the data problem solved?
Currently, for fast development purposes an H2 Database is being used. Since Repositories etc. cannot be dynamically create a self written Class `FlowCmsEntityManagerService` is being used. 
Also before startup the `FlowCmsDatabaseSchemaValidator` will check if the current database schema matches the HOCON configuration. 

Note: It is planned allow hooking alternative Spring Service implementing an interface to provide the same functionality `FlowCmsEntityManagerService` but for a specific table. This way pretty much any storage can be attached to FlowCms.

## Configuration via HOCON
The system supports view configuration where routes and tables are defined in a HOCON file.   

Note: In theory, you could also use Java Classes to do the same since the configuration file is anyway parsed as Java Classes. But the readability would suffer quite significantly.

Below is an example how a route and the related table is configured:

```hocon
application {
   #...
   tables = {
      "projects" = {
         columns = {
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
         factory = "grid"
         render-configuration {
            item-factory = {
               type = "card"
               title-column = "name"
               description-column = "description"
            }
            detail-factory {
               title-column = "name"
               type = "form", children = [
                  {column = "name", label = "route.projects.labels.name"},
                  {column = "description", label = "route.projects.labels.description"},
                  {column = "start_date", label = "route.projects.labels.start_date"},
                  {column = "end_date", label = "route.projects.labels.end_date"},
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
      factory = "grid"
      icon = "FACTORY"
      render-configuration {
        item-factory = {
          type = "item-card-factory"
          title-column = "name"
          description-column = "description"
        }
        detail-factory {
          title-column = "name"
          type = "form", children = [
            {column = "name", label = "route.projects.labels.name"},
            {column = "description", label = "route.projects.labels.description"},
            {column = "start_date", label = "route.projects.labels.start_date"},
            {column = "end_date", label = "route.projects.labels.end_date"},
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
      factory = "master-detail"
      render-configuration = {
        item-factory = {
          type = "item-card-factory"
          title-column = "title"
          description-column = "description"
        }
        detail-factory {
          title-column = "title"
          type = "form", children = [
            {column = "title", label = "route.tasks.labels.title"},
            {column = "description", label = "route.tasks.labels.description"},
            {column = "status", label = "route.tasks.labels.status"},
            {column = "due_date", label = "route.tasks.labels.due_date"}
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