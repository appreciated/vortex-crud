# Vortex CRUD Linking Guide

This guide explains how to create links to Vortex CRUD configured views and navigate between them programmatically.

## Table of Contents

1. [Understanding URL Structure](#understanding-url-structure)
2. [Available Examples](#available-examples)
3. [Navigation Methods](#navigation-methods)
4. [Common Patterns](#common-patterns)
5. [Best Practices](#best-practices)
6. [Example Code](#example-code)

## Understanding URL Structure

Vortex CRUD uses a hierarchical URL structure where each segment represents a route or entity:

```
Format: /base-path/route-key/entity-id/child-route-key/child-entity-id

Examples:
/test/projects-cards                    → Grid view of all projects
/test/projects-cards/123                → Edit form for project with ID 123
/test/projects-cards/123/submenu        → Submenu within project 123 context
/test/open-tasks                        → Kanban board of all tasks
/test/open-tasks/456                    → Edit form for task with ID 456
/test/profile                           → Single form route (user profile)
```

### URL Segments Explained

| Segment | Description | Example |
|---------|-------------|---------|
| `/test` | Base path configured in application | `/test` |
| `projects-cards` | Route key (from configuration) | `projects-cards` |
| `123` | Entity ID | `123` |
| `submenu` | Child route key | `submenu` |
| `project-form` | Submenu child key | `project-form` |

## Available Examples

Both JPA and jOOQ examples provide the same route configurations:

### Collection Views (Multiple Items)

| Route Key | View Type | Description | URL Example |
|-----------|-----------|-------------|-------------|
| `projects-cards` | Grid (Cards) | Projects displayed as cards | `/test/projects-cards` |
| `projects-list` | List | Projects in tabular list with inline editing | `/test/projects-list` |
| `open-tasks` | Kanban | Task management kanban board | `/test/open-tasks` |
| `done-tasks` | Master-Detail | Completed tasks with split view | `/test/done-tasks` |
| `images-grid` | Grid (Gallery) | Image gallery with thumbnails | `/test/images-grid` |
| `images-list` | List | Images in list format | `/test/images-list` |
| `images-slide` | Grid (Slide-out) | Images with slide-out form | `/test/images-slide` |
| `videos-grid` | Grid | Video gallery | `/test/videos-grid` |
| `videos-list` | List | Videos in list format | `/test/videos-list` |

### Special Routes

| Route Key | Type | Description | URL Example |
|-----------|------|-------------|-------------|
| `submenu` | Submenu | Multi-child navigation menu | `/test/submenu/project-form` |
| `profile` | Single Form | User profile (no entity ID) | `/test/profile` |

### Submenu Children

The `submenu` route has multiple child forms:
- `project-form` - Project editing form
- `image-form` - Image editing form

Access them via:
- `/test/submenu/project-form`
- `/test/submenu/image-form`

## Navigation Methods

### 1. Programmatic Navigation (Server-Side)

Use `UI.getCurrent().navigate()` for server-side navigation:

```java
import com.vaadin.flow.component.UI;

// Navigate to a collection view
UI.getCurrent().navigate("test/projects-cards");

// Navigate to a specific entity
Integer projectId = 123;
UI.getCurrent().navigate("test/projects-cards/" + projectId);

// Navigate to nested route
UI.getCurrent().navigate("test/submenu/project-form");
```

### 2. Button Navigation

Create buttons that navigate when clicked:

```java
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.UI;

Button viewProjectsBtn = new Button("View Projects", event -> {
    UI.getCurrent().navigate("test/projects-cards");
});

Button editProjectBtn = new Button("Edit Project #1", event -> {
    UI.getCurrent().navigate("test/projects-cards/1");
});
```

### 3. Hyperlinks (Anchors)

Create traditional HTML links:

```java
import com.vaadin.flow.component.html.Anchor;

// Link to collection view
Anchor projectsLink = new Anchor("test/projects-cards", "Browse All Projects");

// Link to specific entity
Anchor editLink = new Anchor("test/projects-cards/123", "Edit Project #123");

// Link opens in new tab
Anchor externalLink = new Anchor("test/projects-cards", "Projects");
externalLink.setTarget("_blank");
```

### 4. Router Links (Client-Side Navigation)

Use RouterLink for client-side navigation (better UX):

```java
import com.vaadin.flow.router.RouterLink;
import com.github.appreciated.vortex_crud.core.ui.routes.VortexCrudRoute;

RouterLink link = new RouterLink("View Projects", VortexCrudRoute.class);
link.setRoute(VortexCrudRoute.class, "test/projects-cards");
```

### 5. Back Navigation

Navigate back to parent view:

```java
import com.vaadin.flow.component.UI;

// Browser back button behavior
Button backBtn = new Button("Back", event -> {
    UI.getCurrent().getPage().getHistory().back();
});

// Navigate to specific parent route
Button backToGridBtn = new Button("Back to Grid", event -> {
    UI.getCurrent().navigate("test/projects-cards");
});
```

## Common Patterns

### Pattern 1: Link to Collection View

Navigate to a view showing all entities:

```java
// Grid view (cards)
UI.getCurrent().navigate("test/projects-cards");

// List view
UI.getCurrent().navigate("test/projects-list");

// Kanban view
UI.getCurrent().navigate("test/open-tasks");

// Master-detail view
UI.getCurrent().navigate("test/done-tasks");

// Gallery view
UI.getCurrent().navigate("test/images-grid");
```

### Pattern 2: Link to Specific Entity

Navigate to edit a specific entity:

```java
// Edit project with ID 123
Integer projectId = 123;
UI.getCurrent().navigate("test/projects-cards/" + projectId);

// Edit task with ID 456
Integer taskId = 456;
UI.getCurrent().navigate("test/open-tasks/" + taskId);

// Edit image with ID 10
Integer imageId = 10;
UI.getCurrent().navigate("test/images-grid/" + imageId);
```

### Pattern 3: Dynamic Link Building

Create reusable helper methods:

```java
public class NavigationHelper {
    private static final String BASE_PATH = "test";

    public static String buildViewPath(String routeKey) {
        return String.format("%s/%s", BASE_PATH, routeKey);
    }

    public static String buildEntityPath(String routeKey, Object entityId) {
        return String.format("%s/%s/%s", BASE_PATH, routeKey, entityId);
    }

    public static String buildNestedPath(String routeKey, Object entityId, String childKey) {
        if (entityId == null) {
            return String.format("%s/%s/%s", BASE_PATH, routeKey, childKey);
        }
        return String.format("%s/%s/%s/%s", BASE_PATH, routeKey, entityId, childKey);
    }
}

// Usage:
UI.getCurrent().navigate(NavigationHelper.buildViewPath("projects-cards"));
UI.getCurrent().navigate(NavigationHelper.buildEntityPath("projects-cards", 123));
UI.getCurrent().navigate(NavigationHelper.buildNestedPath("submenu", null, "project-form"));
```

### Pattern 4: View Type Switching

Switch between different view types for the same data:

```java
// Switch from card view to list view
Button switchToList = new Button("Switch to List", event -> {
    UI.getCurrent().navigate("test/projects-list");
});

// Switch from list to kanban
Button switchToKanban = new Button("Switch to Kanban", event -> {
    UI.getCurrent().navigate("test/open-tasks");
});

// Switch between image views
Button toImageGrid = new Button("Grid View", event -> {
    UI.getCurrent().navigate("test/images-grid");
});

Button toImageList = new Button("List View", event -> {
    UI.getCurrent().navigate("test/images-list");
});

Button toImageSlide = new Button("Slide-out View", event -> {
    UI.getCurrent().navigate("test/images-slide");
});
```

### Pattern 5: Adding Links to Grid Columns

Create action columns with links:

```java
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;

Grid<Project> grid = new Grid<>(Project.class);

// Add column with edit link
grid.addComponentColumn(project -> {
    Anchor editLink = new Anchor(
        String.format("test/projects-cards/%s", project.getId()),
        "Edit"
    );
    return editLink;
}).setHeader("Actions");

// Add column with button
grid.addComponentColumn(project -> {
    Button editBtn = new Button("Edit", event -> {
        UI.getCurrent().navigate(
            String.format("test/projects-cards/%s", project.getId())
        );
    });
    return editBtn;
}).setHeader("Actions");
```

### Pattern 6: Submenu Navigation

Navigate to submenu child routes:

```java
// Navigate to submenu with project form
UI.getCurrent().navigate("test/submenu/project-form");

// Navigate to submenu with image form
UI.getCurrent().navigate("test/submenu/image-form");

// If submenu has entity context (less common)
Integer entityId = 123;
UI.getCurrent().navigate("test/submenu/" + entityId + "/project-form");
```

### Pattern 7: Single Form Routes

Navigate to singular entity forms (like user profile):

```java
// Profile doesn't require entity ID - shows current user's profile
UI.getCurrent().navigate("test/profile");

// These routes filter by user automatically based on security context
```

## Best Practices

### 1. Use Constants for Route Keys

Define route keys as constants to avoid typos:

```java
public class Routes {
    public static final String PROJECTS_CARDS = "projects-cards";
    public static final String PROJECTS_LIST = "projects-list";
    public static final String OPEN_TASKS = "open-tasks";
    public static final String DONE_TASKS = "done-tasks";
    // ... etc
}

// Usage:
UI.getCurrent().navigate("test/" + Routes.PROJECTS_CARDS);
```

### 2. Create Helper Methods

Encapsulate navigation logic in reusable helpers:

```java
public static void navigateToView(String routeKey) {
    UI.getCurrent().navigate("test/" + routeKey);
}

public static void navigateToEntity(String routeKey, Object entityId) {
    UI.getCurrent().navigate(String.format("test/%s/%s", routeKey, entityId));
}
```

### 3. Validate Entity IDs

Always validate entity IDs before navigation:

```java
Integer projectId = getProjectId();
if (projectId != null && projectId > 0) {
    UI.getCurrent().navigate("test/projects-cards/" + projectId);
} else {
    Notification.show("Invalid project ID");
}
```

### 4. Use Client-Side Navigation When Possible

RouterLink provides better UX than programmatic navigation:

```java
// Prefer this (client-side)
RouterLink link = new RouterLink("Projects", VortexCrudRoute.class);

// Over this (server-side)
Button btn = new Button("Projects", e -> UI.getCurrent().navigate("test/projects-cards"));
```

### 5. Consider Security

Navigation respects role-based access control:

```java
// Route configuration with role restrictions
routes.put("projects-cards", JpaGridRoute.builder()
    .writeRoles(List.of("admin", "manager"))
    .build());

// Users without proper roles will be denied access
// Even if they navigate directly via URL
```

### 6. Handle Navigation Errors

Add error handling for navigation:

```java
try {
    UI.getCurrent().navigate("test/projects-cards/" + projectId);
} catch (Exception e) {
    Notification.show("Navigation failed: " + e.getMessage());
}
```

## Example Code

### Complete Navigation Component

See the following files for comprehensive examples:

#### JPA Example
- **Configuration**: `examples/jpa-sqlite-example/src/main/java/com/github/appreciated/vortex_crud/example/jpa/ExampleJpaConfiguration.java`
- **Navigation Examples**: `examples/jpa-sqlite-example/src/main/java/com/github/appreciated/vortex_crud/example/jpa/NavigationExamplesComponent.java`

#### jOOQ Example
- **Configuration**: `examples/jooq-sqlite-example/src/main/java/com/github/appreciated/vortex_crud/example/jooq/ExampleJooqConfiguration.java`
- **Navigation Examples**: `examples/jooq-sqlite-example/src/main/java/com/github/appreciated/vortex_crud/example/jooq/NavigationExamplesComponent.java`

#### Shared Helper
- **Linking Helper**: `examples/shared/src/main/java/com/github/appreciated/vortex_crud/example/shared/LinkingExamplesHelper.java`

### Quick Start Example

```java
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class QuickStartNavigation extends VerticalLayout {

    public QuickStartNavigation() {
        // Button to view all projects
        Button viewAllBtn = new Button("View All Projects", event -> {
            UI.getCurrent().navigate("test/projects-cards");
        });

        // Button to edit specific project
        Button editProjectBtn = new Button("Edit Project #1", event -> {
            UI.getCurrent().navigate("test/projects-cards/1");
        });

        // Link to kanban board
        Anchor kanbanLink = new Anchor("test/open-tasks", "Task Kanban Board");

        // Link to profile
        Anchor profileLink = new Anchor("test/profile", "My Profile");

        add(viewAllBtn, editProjectBtn, kanbanLink, profileLink);
    }
}
```

## Advanced Usage

### Accessing VortexCrudPathToRouteResolver

For complex navigation scenarios, use the framework's path resolver:

```java
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import org.springframework.beans.factory.annotation.Autowired;

@Autowired
private VortexCrudPathToRouteResolver routeResolver;

// Build path relative to current route
String path = routeResolver.buildPathUpToIndex(
    routeResolver.determineActiveRouteIndex(),
    entityId
);
UI.getCurrent().navigate(path);
```

### Getting Current Path

```java
// Get current path from resolver
String currentPath = routeResolver.getPath();

// Get path variables
PathVariables pathVars = routeResolver.getPathVariables();
```

## Troubleshooting

### Link Not Working

1. **Check route key**: Ensure the route key matches what's configured
2. **Verify base path**: Base path should be "test" in examples
3. **Check entity ID**: Ensure entity ID is valid and exists
4. **Review permissions**: User must have proper roles to access route

### 404 Not Found

1. **Route not configured**: Check if route exists in configuration
2. **Wrong base path**: Verify you're using correct base path
3. **Typo in route key**: Double-check spelling of route key

### Access Denied

1. **Missing roles**: User doesn't have required roles
2. **Check configuration**: Review `writeRoles()` and `readRoles()` in route config
3. **Authentication**: Ensure user is logged in

## Additional Resources

- **Main Documentation**: `/README.md`
- **Architecture Guide**: `/docs/route-config-refactor-plan.md`
- **JPA Example**: `/examples/jpa-sqlite-example/`
- **jOOQ Example**: `/examples/jooq-sqlite-example/`

## Support

For issues or questions:
- Check the main README
- Review example configurations
- Examine NavigationExamplesComponent for working code
- Review LinkingExamplesHelper for utility methods
