# Linking to Vortex CRUD Views

## URL Pattern

```
/test/{route-key}              → View list/grid
/test/{route-key}/{id}         → Edit entity
/test/{route-key}/{id}/{child} → Nested routes
```

## Examples

### Navigate Programmatically
```java
// View all projects
UI.getCurrent().navigate("test/projects-cards");

// Edit project #123
UI.getCurrent().navigate("test/projects-cards/123");

// Submenu child
UI.getCurrent().navigate("test/submenu/project-form");
```

### Create Links
```java
// Button
Button btn = new Button("View Projects", e ->
    UI.getCurrent().navigate("test/projects-cards"));

// Hyperlink
Anchor link = new Anchor("test/projects-cards/123", "Edit Project");
```

## Available Routes

| Route Key | Type | URL Example |
|-----------|------|-------------|
| projects-cards | Grid | /test/projects-cards |
| projects-list | List | /test/projects-list |
| open-tasks | Kanban | /test/open-tasks |
| done-tasks | Master-Detail | /test/done-tasks |
| images-grid | Grid | /test/images-grid |
| submenu | Submenu | /test/submenu/project-form |
| profile | Single Form | /test/profile |
