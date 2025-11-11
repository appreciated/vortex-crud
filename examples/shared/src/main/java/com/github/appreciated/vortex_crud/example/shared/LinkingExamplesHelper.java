package com.github.appreciated.vortex_crud.example.shared;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;

/**
 * Comprehensive guide and examples for creating links to Vortex CRUD configured views.
 *
 * <h2>Understanding Vortex CRUD Navigation</h2>
 *
 * Vortex CRUD uses a hierarchical URL structure where each segment represents a route or entity:
 * <pre>
 * Format: /base-path/route-key/entity-id/child-route-key/child-entity-id
 *
 * Examples:
 * - /test/projects-cards                    → Grid view of all projects
 * - /test/projects-cards/123                → Edit form for project with ID 123
 * - /test/projects-cards/123/submenu        → Submenu within project 123 context
 * - /test/open-tasks                        → Kanban board of all tasks
 * - /test/open-tasks/456                    → Edit form for task with ID 456
 * - /test/profile                           → Single form route (user profile)
 * </pre>
 *
 * <h2>Navigation Methods</h2>
 *
 * <h3>1. Programmatic Navigation</h3>
 * Use {@link UI#navigate(String)} for server-side navigation:
 * <pre>{@code
 * // Navigate to a grid view
 * UI.getCurrent().navigate("test/projects-cards");
 *
 * // Navigate to a specific entity
 * UI.getCurrent().navigate("test/projects-cards/123");
 *
 * // Navigate to nested route
 * UI.getCurrent().navigate("test/projects-cards/123/submenu/project-form");
 * }</pre>
 *
 * <h3>2. Router Links (Client-Side Navigation)</h3>
 * Use {@link com.vaadin.flow.router.RouterLink} for client-side navigation:
 * <pre>{@code
 * RouterLink link = new RouterLink("View Projects", VortexCrudRoute.class);
 * link.setRoute(VortexCrudRoute.class, "test/projects-cards");
 * }</pre>
 *
 * <h3>3. HTML Anchors (Traditional Links)</h3>
 * Use {@link Anchor} for traditional hyperlinks:
 * <pre>{@code
 * Anchor anchor = new Anchor("test/projects-cards", "View Projects");
 * }</pre>
 *
 * <h2>Common Linking Patterns</h2>
 *
 * <h3>Pattern 1: Link to Grid/List View</h3>
 * <pre>{@code
 * Button viewProjectsBtn = new Button("View All Projects", event -> {
 *     UI.getCurrent().navigate("test/projects-cards");
 * });
 *
 * Button viewTasksKanban = new Button("View Tasks Kanban", event -> {
 *     UI.getCurrent().navigate("test/open-tasks");
 * });
 * }</pre>
 *
 * <h3>Pattern 2: Link to Specific Entity</h3>
 * <pre>{@code
 * // Link to edit a specific project
 * Integer projectId = 123;
 * Button editProjectBtn = new Button("Edit Project", event -> {
 *     UI.getCurrent().navigate("test/projects-cards/" + projectId);
 * });
 *
 * // Link to edit a specific task
 * Integer taskId = 456;
 * Button editTaskBtn = new Button("Edit Task", event -> {
 *     UI.getCurrent().navigate("test/open-tasks/" + taskId);
 * });
 * }</pre>
 *
 * <h3>Pattern 3: Link to Submenu Routes</h3>
 * <pre>{@code
 * // Navigate to submenu with specific child selected
 * Integer projectId = 123;
 * Button viewSubmenuBtn = new Button("View Submenu", event -> {
 *     UI.getCurrent().navigate("test/submenu/project-form");
 * });
 *
 * // If submenu has entity context
 * Button viewSubmenuWithEntityBtn = new Button("View Project Submenu", event -> {
 *     UI.getCurrent().navigate("test/submenu/" + projectId + "/project-form");
 * });
 * }</pre>
 *
 * <h3>Pattern 4: Link to Single Form Routes (Profile)</h3>
 * <pre>{@code
 * // Single form routes don't require an entity ID
 * Button viewProfileBtn = new Button("View Profile", event -> {
 *     UI.getCurrent().navigate("test/profile");
 * });
 * }</pre>
 *
 * <h3>Pattern 5: Link Between Different View Types</h3>
 * <pre>{@code
 * // Switch from card view to list view
 * Button switchToListBtn = new Button("Switch to List View", event -> {
 *     UI.getCurrent().navigate("test/projects-list");
 * });
 *
 * // Switch from grid to kanban
 * Button switchToKanbanBtn = new Button("Switch to Kanban", event -> {
 *     UI.getCurrent().navigate("test/open-tasks");
 * });
 * }</pre>
 *
 * <h3>Pattern 6: Dynamic Link Building</h3>
 * <pre>{@code
 * public class NavigationHelper {
 *
 *     private static final String BASE_PATH = "test";
 *
 *     public static String buildGridPath(String routeKey) {
 *         return String.format("%s/%s", BASE_PATH, routeKey);
 *     }
 *
 *     public static String buildEntityPath(String routeKey, Object entityId) {
 *         return String.format("%s/%s/%s", BASE_PATH, routeKey, entityId);
 *     }
 *
 *     public static String buildNestedPath(String routeKey, Object entityId,
 *                                          String childRouteKey) {
 *         return String.format("%s/%s/%s/%s", BASE_PATH, routeKey, entityId, childRouteKey);
 *     }
 *
 *     public static String buildNestedEntityPath(String routeKey, Object entityId,
 *                                                String childRouteKey, Object childEntityId) {
 *         return String.format("%s/%s/%s/%s/%s", BASE_PATH, routeKey, entityId,
 *                            childRouteKey, childEntityId);
 *     }
 * }
 *
 * // Usage:
 * UI.getCurrent().navigate(NavigationHelper.buildEntityPath("projects-cards", 123));
 * UI.getCurrent().navigate(NavigationHelper.buildNestedPath("projects-cards", 123, "submenu"));
 * }</pre>
 *
 * <h3>Pattern 7: Creating Hyperlinks in Grid/List Columns</h3>
 * <pre>{@code
 * // In a custom renderer or component
 * grid.addComponentColumn(project -> {
 *     Anchor link = new Anchor(
 *         String.format("test/projects-cards/%s", project.getId()),
 *         "Edit"
 *     );
 *     return link;
 * }).setHeader("Actions");
 * }</pre>
 *
 * <h3>Pattern 8: Back Navigation</h3>
 * <pre>{@code
 * // Navigate back to parent (grid/list view)
 * Button backBtn = new Button("Back to List", event -> {
 *     UI.getCurrent().getPage().getHistory().back();
 * });
 *
 * // Or navigate to specific parent route
 * Button backToGridBtn = new Button("Back to Grid", event -> {
 *     UI.getCurrent().navigate("test/projects-cards");
 * });
 * }</pre>
 *
 * <h2>Route Configuration Reference</h2>
 *
 * Based on the example configurations, the following routes are available:
 * <ul>
 *   <li><b>projects-cards</b> - Grid view with cards displaying projects</li>
 *   <li><b>projects-list</b> - List view with inline editing for projects</li>
 *   <li><b>open-tasks</b> - Kanban board for task management</li>
 *   <li><b>done-tasks</b> - Master-detail view for completed tasks</li>
 *   <li><b>images-grid</b> - Grid view for image gallery</li>
 *   <li><b>images-list</b> - List view for images with inline editing</li>
 *   <li><b>images-slide</b> - Grid view with slide-out form for images</li>
 *   <li><b>videos-grid</b> - Grid view for video gallery</li>
 *   <li><b>videos-list</b> - List view for videos with inline editing</li>
 *   <li><b>submenu</b> - Submenu route with multiple child forms</li>
 *   <li><b>profile</b> - Single form route for user profile</li>
 * </ul>
 *
 * <h2>Advanced: Accessing VortexCrudPathToRouteResolver</h2>
 *
 * For more complex scenarios, you can use the framework's path resolver:
 * <pre>{@code
 * @Autowired
 * private VortexCrudPathToRouteResolver routeResolver;
 *
 * // Build path up to current route index with entity ID
 * String path = routeResolver.buildPathUpToIndex(
 *     routeResolver.determineActiveRouteIndex(),
 *     entityId
 * );
 * UI.getCurrent().navigate(path);
 * }</pre>
 *
 * <h2>Best Practices</h2>
 * <ol>
 *   <li>Use constants for route keys to avoid typos</li>
 *   <li>Create helper methods for frequently used navigation patterns</li>
 *   <li>Use RouterLink for client-side navigation when possible (better UX)</li>
 *   <li>Use UI.navigate() for server-side navigation after actions (e.g., save)</li>
 *   <li>Always validate entity IDs before constructing navigation paths</li>
 *   <li>Consider security: ensure users have permission to access target routes</li>
 * </ol>
 *
 * <h2>Security Considerations</h2>
 *
 * Navigation respects the role-based access control defined in route configurations:
 * <pre>{@code
 * // This route requires "admin" or "manager" roles
 * routes.put("projects-cards", JpaGridRoute.builder()
 *     .writeRoles(List.of("admin", "manager"))
 *     // ...
 *     .build());
 *
 * // Users without proper roles will be denied access even if they navigate directly
 * }</pre>
 *
 * @see com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver
 * @see com.github.appreciated.vortex_crud.core.ui.routes.VortexCrudRoute
 */
public class LinkingExamplesHelper {

    /**
     * Base path for all Vortex CRUD routes (configured in application)
     */
    public static final String BASE_PATH = "test";

    /**
     * Route keys from the example configurations
     */
    public static class Routes {
        public static final String PROJECTS_CARDS = "projects-cards";
        public static final String PROJECTS_LIST = "projects-list";
        public static final String OPEN_TASKS = "open-tasks";
        public static final String DONE_TASKS = "done-tasks";
        public static final String IMAGES_GRID = "images-grid";
        public static final String IMAGES_LIST = "images-list";
        public static final String IMAGES_SLIDE = "images-slide";
        public static final String VIDEOS_GRID = "videos-grid";
        public static final String VIDEOS_LIST = "videos-list";
        public static final String SUBMENU = "submenu";
        public static final String PROFILE = "profile";

        /**
         * Submenu child routes
         */
        public static class SubmenuChildren {
            public static final String PROJECT_FORM = "project-form";
            public static final String IMAGE_FORM = "image-form";
        }
    }

    /**
     * Builds a path to a grid/list/kanban view (no entity ID)
     *
     * @param routeKey the route key (e.g., "projects-cards")
     * @return the full path (e.g., "test/projects-cards")
     */
    public static String buildViewPath(String routeKey) {
        return String.format("%s/%s", BASE_PATH, routeKey);
    }

    /**
     * Builds a path to an entity form
     *
     * @param routeKey the route key (e.g., "projects-cards")
     * @param entityId the entity ID
     * @return the full path (e.g., "test/projects-cards/123")
     */
    public static String buildEntityPath(String routeKey, Object entityId) {
        return String.format("%s/%s/%s", BASE_PATH, routeKey, entityId);
    }

    /**
     * Builds a path to a nested child route
     *
     * @param routeKey the parent route key
     * @param entityId the parent entity ID (can be null for routes without entity context)
     * @param childRouteKey the child route key
     * @return the full path
     */
    public static String buildNestedPath(String routeKey, Object entityId, String childRouteKey) {
        if (entityId == null) {
            return String.format("%s/%s/%s", BASE_PATH, routeKey, childRouteKey);
        }
        return String.format("%s/%s/%s/%s", BASE_PATH, routeKey, entityId, childRouteKey);
    }

    /**
     * Builds a path to a nested child entity form
     *
     * @param routeKey the parent route key
     * @param entityId the parent entity ID
     * @param childRouteKey the child route key
     * @param childEntityId the child entity ID
     * @return the full path
     */
    public static String buildNestedEntityPath(String routeKey, Object entityId,
                                              String childRouteKey, Object childEntityId) {
        return String.format("%s/%s/%s/%s/%s", BASE_PATH, routeKey, entityId,
                           childRouteKey, childEntityId);
    }

    /**
     * Creates a button that navigates to a view
     *
     * @param label button label
     * @param routeKey the route key to navigate to
     * @return configured button
     */
    public static Button createNavigationButton(String label, String routeKey) {
        return new Button(label, event -> {
            UI.getCurrent().navigate(buildViewPath(routeKey));
        });
    }

    /**
     * Creates a button that navigates to an entity
     *
     * @param label button label
     * @param routeKey the route key
     * @param entityId the entity ID
     * @return configured button
     */
    public static Button createEntityButton(String label, String routeKey, Object entityId) {
        return new Button(label, event -> {
            UI.getCurrent().navigate(buildEntityPath(routeKey, entityId));
        });
    }

    /**
     * Creates an anchor (hyperlink) to a view
     *
     * @param label link text
     * @param routeKey the route key to link to
     * @return configured anchor
     */
    public static Anchor createViewAnchor(String label, String routeKey) {
        return new Anchor(buildViewPath(routeKey), label);
    }

    /**
     * Creates an anchor (hyperlink) to an entity
     *
     * @param label link text
     * @param routeKey the route key
     * @param entityId the entity ID
     * @return configured anchor
     */
    public static Anchor createEntityAnchor(String label, String routeKey, Object entityId) {
        return new Anchor(buildEntityPath(routeKey, entityId), label);
    }

    // Private constructor to prevent instantiation
    private LinkingExamplesHelper() {}
}
