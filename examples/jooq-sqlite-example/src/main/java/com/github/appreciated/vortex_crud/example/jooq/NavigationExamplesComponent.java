package com.github.appreciated.vortex_crud.example.jooq;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Example component demonstrating various navigation patterns for Vortex CRUD routes.
 *
 * This class shows practical examples of how to create links and navigate between
 * different views configured in ExampleJooqConfiguration.
 *
 * <p><b>Usage:</b> Add this component to any view to demonstrate navigation capabilities.
 * This is a reference implementation showing best practices for linking to Vortex CRUD views.</p>
 *
 * <p><b>Configured Routes in this example:</b></p>
 * <ul>
 *   <li>projects-cards - Card grid view of projects</li>
 *   <li>projects-list - List view of projects with inline editing</li>
 *   <li>open-tasks - Kanban board for task management</li>
 *   <li>done-tasks - Master-detail view for completed tasks</li>
 *   <li>images-grid - Image gallery grid</li>
 *   <li>images-list - Image list with inline editing</li>
 *   <li>images-slide - Image grid with slide-out form</li>
 *   <li>videos-grid - Video gallery grid</li>
 *   <li>videos-list - Video list with inline editing</li>
 *   <li>submenu - Submenu with multiple child forms</li>
 *   <li>profile - User profile single form</li>
 * </ul>
 */
public class NavigationExamplesComponent extends VerticalLayout {

    private static final String BASE_PATH = "test";

    public NavigationExamplesComponent() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Vortex CRUD Navigation Examples (jOOQ)"));
        add(new Paragraph("This component demonstrates various ways to navigate between Vortex CRUD views."));

        add(createGridViewExamples());
        add(createEntityNavigationExamples());
        add(createHyperlinkExamples());
        add(createSubmenuExamples());
        add(createViewSwitchingExamples());
    }

    /**
     * Example 1: Navigating to Grid/List/Kanban Views
     * These are the main collection views where you see multiple items
     */
    private Div createGridViewExamples() {
        Div section = new Div();
        section.add(new H3("Example 1: Navigate to Collection Views"));
        section.add(new Paragraph(
            "Collection views display multiple items. Click these buttons to navigate to different view types."
        ));

        // Navigate to grid view (cards)
        Button projectsGridBtn = new Button("View Projects (Grid)", event -> {
            UI.getCurrent().navigate(buildPath("projects-cards"));
        });
        projectsGridBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Navigate to list view
        Button projectsListBtn = new Button("View Projects (List)", event -> {
            UI.getCurrent().navigate(buildPath("projects-list"));
        });

        // Navigate to kanban view
        Button tasksKanbanBtn = new Button("View Tasks (Kanban)", event -> {
            UI.getCurrent().navigate(buildPath("open-tasks"));
        });

        // Navigate to master-detail view
        Button tasksMasterDetailBtn = new Button("View Done Tasks (Master-Detail)", event -> {
            UI.getCurrent().navigate(buildPath("done-tasks"));
        });

        // Navigate to image gallery
        Button imagesGridBtn = new Button("View Images (Gallery)", event -> {
            UI.getCurrent().navigate(buildPath("images-grid"));
        });

        section.add(projectsGridBtn, projectsListBtn, tasksKanbanBtn, tasksMasterDetailBtn, imagesGridBtn);
        return section;
    }

    /**
     * Example 2: Navigating to Specific Entity Forms
     * Shows how to open edit forms for specific entities by ID
     */
    private Div createEntityNavigationExamples() {
        Div section = new Div();
        section.add(new H3("Example 2: Navigate to Specific Entity Forms"));
        section.add(new Paragraph(
            "To edit a specific entity, append its ID to the route path. " +
            "These examples demonstrate navigation to forms for entities with specific IDs."
        ));

        // Navigate to edit specific project
        Button editProject1Btn = new Button("Edit Project #1", event -> {
            Integer projectId = 1;
            UI.getCurrent().navigate(buildEntityPath("projects-cards", projectId));
        });
        editProject1Btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Navigate to edit specific task
        Button editTask5Btn = new Button("Edit Task #5", event -> {
            Integer taskId = 5;
            UI.getCurrent().navigate(buildEntityPath("open-tasks", taskId));
        });

        // Navigate to edit specific image
        Button editImage10Btn = new Button("Edit Image #10", event -> {
            Integer imageId = 10;
            UI.getCurrent().navigate(buildEntityPath("images-grid", imageId));
        });

        // Navigate to edit specific video
        Button editVideo3Btn = new Button("Edit Video #3", event -> {
            Integer videoId = 3;
            UI.getCurrent().navigate(buildEntityPath("videos-grid", videoId));
        });

        section.add(editProject1Btn, editTask5Btn, editImage10Btn, editVideo3Btn);
        return section;
    }

    /**
     * Example 3: Creating Hyperlinks (Anchors)
     * Shows how to create traditional HTML links instead of buttons
     */
    private Div createHyperlinkExamples() {
        Div section = new Div();
        section.add(new H3("Example 3: Creating Hyperlinks"));
        section.add(new Paragraph(
            "Use Anchor components to create traditional hyperlinks. " +
            "These allow users to open links in new tabs or copy link addresses."
        ));

        // Create anchor to grid view
        Anchor projectsLink = new Anchor(buildPath("projects-cards"), "Browse All Projects");
        projectsLink.getStyle().set("margin-right", "1rem");

        // Create anchor to specific entity
        Anchor editProjectLink = new Anchor(buildEntityPath("projects-cards", 1), "Edit Project #1");
        editProjectLink.getStyle().set("margin-right", "1rem");

        // Create anchor to kanban
        Anchor kanbanLink = new Anchor(buildPath("open-tasks"), "Open Tasks Kanban Board");
        kanbanLink.getStyle().set("margin-right", "1rem");

        // Create anchor to profile
        Anchor profileLink = new Anchor(buildPath("profile"), "View My Profile");

        section.add(new Div(projectsLink, editProjectLink, kanbanLink, profileLink));
        return section;
    }

    /**
     * Example 4: Navigating to Submenu Routes
     * Shows how to work with submenu routes that have multiple child forms
     */
    private Div createSubmenuExamples() {
        Div section = new Div();
        section.add(new H3("Example 4: Navigate to Submenu Routes"));
        section.add(new Paragraph(
            "Submenu routes allow multiple child forms to be displayed within a single route context. " +
            "Navigate to specific child forms by appending the child key to the route."
        ));

        // Navigate to submenu with project form selected
        Button submenuProjectBtn = new Button("Submenu → Project Form", event -> {
            UI.getCurrent().navigate(buildPath("submenu/project-form"));
        });
        submenuProjectBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Navigate to submenu with image form selected
        Button submenuImageBtn = new Button("Submenu → Image Form", event -> {
            UI.getCurrent().navigate(buildPath("submenu/image-form"));
        });

        section.add(submenuProjectBtn, submenuImageBtn);
        return section;
    }

    /**
     * Example 5: Switching Between View Types
     * Shows how to navigate between different view types for the same data
     */
    private Div createViewSwitchingExamples() {
        Div section = new Div();
        section.add(new H3("Example 5: Switch Between View Types"));
        section.add(new Paragraph(
            "The same data can be viewed in different ways. " +
            "These examples show how to switch between grid, list, and other view types."
        ));

        // Project view switcher
        Button projectsCardsBtn = new Button("Projects: Card View", event -> {
            UI.getCurrent().navigate(buildPath("projects-cards"));
        });

        Button projectsListBtn = new Button("Projects: List View", event -> {
            UI.getCurrent().navigate(buildPath("projects-list"));
        });

        // Images view switcher
        Button imagesGridBtn = new Button("Images: Grid View", event -> {
            UI.getCurrent().navigate(buildPath("images-grid"));
        });

        Button imagesListBtn = new Button("Images: List View", event -> {
            UI.getCurrent().navigate(buildPath("images-list"));
        });

        Button imagesSlideBtn = new Button("Images: Slide-out View", event -> {
            UI.getCurrent().navigate(buildPath("images-slide"));
        });

        // Videos view switcher
        Button videosGridBtn = new Button("Videos: Grid View", event -> {
            UI.getCurrent().navigate(buildPath("videos-grid"));
        });

        Button videosListBtn = new Button("Videos: List View", event -> {
            UI.getCurrent().navigate(buildPath("videos-list"));
        });

        section.add(
            new Paragraph("Projects:"),
            new Div(projectsCardsBtn, projectsListBtn),
            new Paragraph("Images:"),
            new Div(imagesGridBtn, imagesListBtn, imagesSlideBtn),
            new Paragraph("Videos:"),
            new Div(videosGridBtn, videosListBtn)
        );

        return section;
    }

    /**
     * Helper method: Build path to a view
     *
     * @param routeKey the route key (e.g., "projects-cards")
     * @return full path (e.g., "test/projects-cards")
     */
    private String buildPath(String routeKey) {
        return String.format("%s/%s", BASE_PATH, routeKey);
    }

    /**
     * Helper method: Build path to an entity
     *
     * @param routeKey the route key (e.g., "projects-cards")
     * @param entityId the entity ID
     * @return full path (e.g., "test/projects-cards/123")
     */
    private String buildEntityPath(String routeKey, Object entityId) {
        return String.format("%s/%s/%s", BASE_PATH, routeKey, entityId);
    }

    /**
     * Helper method: Build path to nested route
     *
     * @param routeKey the parent route key
     * @param childPath the child path segment(s)
     * @return full path
     */
    private String buildNestedPath(String routeKey, String childPath) {
        return String.format("%s/%s/%s", BASE_PATH, routeKey, childPath);
    }
}
