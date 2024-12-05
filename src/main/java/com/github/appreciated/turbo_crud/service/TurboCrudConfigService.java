package com.github.appreciated.turbo_crud.service;

import com.github.appreciated.turbo_crud.config.model.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service for loading and providing access to the Turbo CRUD configuration.
 * Reads configuration from a resource file and offers methods to retrieve route and application settings.
 */

@Service
public class TurboCrudConfigService {

    private final Application configuration;

    public TurboCrudConfigService() {
        Map<String, Route> forms = Map.of(
                "task", FormRoute.Builder.of("form")
                        .withRepository("tasks")
                        .withConfiguration(FormConfiguration.Builder.of("default")
                                .withTitleField("title")
                                .withChildren(List.of(
                                        new FormElement("title", "field", "route.tasks.labels.title"),
                                        new FormElement("description", "field", "route.tasks.labels.description"),
                                        new FormElement("status", "field", "route.tasks.labels.status"),
                                        new FormElement("due_date", "field", "route.tasks.labels.due_date"),
                                        new FormElement("assigned_to", "field", "route.tasks.labels.assigned_to"),
                                        FormElement.Builder.of("comments", "collection", "route.tasks.labels.comments")
                                                .withConfiguration(Collection.Builder.of("list")
                                                        .withData(
                                                                CollectionData.Builder.of("task_comments")
                                                                        .withOneToMany(new OneToMany("task_id"))
                                                                        .withChildren(List.of("comment_text", "field", "route.tasks.labels.comment"))
                                                                        .build()
                                                        ).build()
                                                )
                                                .build(),
                                        FormElement.Builder.of("related-tasks", "collection", "route.tasks.labels.related-tasks")
                                                .withConfiguration(Collection.Builder.of("list")
                                                        .withData(CollectionData.Builder.of("tasks")
                                                                .withManyToMany(new ManyToMany("task_has_task", "task_id", "related_task_id", "id"))
                                                                .withChildren(List.of("title", "field", "route.tasks.labels.title"))
                                                                .build())
                                                        .withEmptyMessage("route.tasks.labels.related-tasks-empty-message")
                                                        .withConfiguration(new CollectionConfig("title"))
                                                        .withFactory("connect")
                                                        .build()
                                                )
                                                .build()
                                ))
                                .build())
                        .build(),
                "project", FormRoute.Builder.of("form")
                        .withRepository("projects")
                        .withTitle("route.projects.title-cards")
                        .withConfiguration(FormConfiguration.Builder.of("default")
                                .withTitleField("name")
                                .withChildren(List.of(
                                        new FormElement("name", "field", "route.projects.labels.name"),
                                        new FormElement("description", "field", "route.projects.labels.description"),
                                        new FormElement("start_date", "field", "route.projects.labels.start_date"),
                                        new FormElement("end_date", "field", "route.projects.labels.end_date")
                                ))
                                .build())
                        .build(),
                "image", FormRoute.Builder.of("form")
                        .withRepository("images")
                        .withTitle("route.projects.title-cards")
                        .withConfiguration(FormConfiguration.Builder.of("default")
                                .withTitleField("title")
                                .withChildren(List.of(
                                        new FormElement("title", "field", "route.images.labels.title"),
                                        new FormElement("url", "field", "route.images.labels.image")
                                ))
                                .build())
                        .build()
        );

        Map<String, Repository> repositories = Map.of(
                "projects", Repository.Builder.of("jpa")
                        .withFields(Map.of(
                                "id", new Field("id", true),
                                "name", new Field("text", true, true, Validation.Builder.of().withMaxLength(255).build()),
                                "description", new Field("textarea", false, false, Validation.Builder.of().withMaxLength(500).build()),
                                "start_date", new Field("date"),
                                "end_date", new Field("date"),
                                "created_at", new Field("datetime"),
                                "updated_at", new Field("datetime")
                        ))
                        .build(),
                "tasks", Repository.Builder.of("jpa")
                        .withFields(Map.of(
                                "id", new Field("id", true),
                                "title", new Field("text", true, true, Validation.Builder.of().withMaxLength(255).build()),
                                "description", new Field("textarea", false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                "assigned_to", new Field("reference", "users", "id", "username", List.of("username")), // 1:1 Relation
                                "status", new Field("select", "task-status"),
                                "due_date", Field.Builder.of("date").withReadOnlyForRoles("developer").build(),
                                "created_at", new Field("datetime"),
                                "updated_at", new Field("datetime")
                        ))
                        .build(),
                "task_has_task", Repository.Builder.of("jpa")
                        .withFields(Map.of(
                                "task_id", new Field("id"),
                                "related_task_id", new Field("id")
                        ))
                        .build(),
                "task_comments", Repository.Builder.of("jpa")
                        .withFields(Map.of(
                                "id", new Field("id", true),
                                "comment_text", new Field("textarea", false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                "user_id", new Field("number"),
                                "created_at", Field.Builder.of("datetime").withDefaultValue("now()").build()
                        ))
                        .build(),
                "images", Repository.Builder.of("jpa")
                        .withFields(Map.of(
                                "id", new Field("id", true),
                                "title", Field.Builder.of("text")
                                        .withRequired(true)
                                        .withValidation(Validation.Builder.of().withMaxLength(255).build())
                                        .build(),
                                "url", Field.Builder.of("text")
                                        .withConfiguration(new ImageFieldConfiguration("default"))
                                        .build()
                        ))
                        .build()
        );

        Map<String, Route> routes = Map.of(
                "projects-cards", Route.Builder.of("grid")
                        .withDefaultRoute(true)
                        .withRepository("projects")
                        .withIcon("FACTORY")
                        .withTitle("route.projects.title-cards")
                        .withConfiguration(GridOrListConfiguration.Builder.of("card")
                                .withTitleField("name")
                                .withDescriptionField("description")
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(FormRoute.Builder.of("form").withRepository("projects").build())
                        .build(),
                "projects-list", Route.Builder.of("list")
                        .withRepository("projects")
                        .withIcon("FACTORY")
                        .withTitle("route.projects.title-list")
                        .withConfiguration(GridOrListConfiguration.Builder.of("default")
                                .withInlineEdit(true)
                                .withFilterField("name")
                                .withChildren(List.of(
                                        new FormElement("name", "field", "route.projects.labels.name"),
                                        new FormElement("description", "field", "route.projects.labels.description"),
                                        new FormElement("start_date", "field", "route.projects.labels.start_date"),
                                        new FormElement("end_date", "field", "route.projects.labels.end_date")
                                ))
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(FormRoute.Builder.of("form").withRepository("projects").build())
                        .build(),
                "tasks", Route.Builder.of("submenu")
                        .withIcon("TASKS")
                        .withRepository("tasks")
                        .withTitle("route.tasks.title")
                        .withChildrenMap(Map.of(
                                "open", Route.Builder.of("kanban")
                                        .withIcon("TASKS")
                                        .withRepository("tasks")
                                        .withTitle("route.open-tasks.title")
                                        .withConfiguration(GridOrListConfiguration.Builder.of("card")
                                                .withTitleField("title")
                                                .withDescriptionField("description")
                                                .withColumnField("status")
                                                .build())
                                        .withChild(FormRoute.Builder.of("form").withRepository("tasks").build())
                                        .build(),
                                "done", Route.Builder.of("master-detail")
                                        .withIcon("CHECK_CIRCLE")
                                        .withRepository("tasks")
                                        .withTitle("route.done-tasks.title")
                                        .withConfiguration(GridOrListConfiguration.Builder.of("card")
                                                .withTitleField("title")
                                                .withDescriptionField("description")
                                                .build())
                                        .withChild(FormRoute.Builder.of("form").withRepository("tasks").build())
                                        .build()
                        ))
                        .build(),
                "images-grid", Route.Builder.of("grid")
                        .withRepository("images")
                        .withIcon("CAMERA")
                        .withTitle("route.images-cards")
                        .withConfiguration(GridOrListConfiguration.Builder.of("card")
                                .withTitleField("title")
                                .withImageField("url")
                                .withImageFactory("default")
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(FormRoute.Builder.of("form").withRepository("images").build())
                        .build(),
                "images-list", Route.Builder.of("list")
                        .withRepository("images")
                        .withIcon("CAMERA")
                        .withTitle("route.images-list")
                        .withConfiguration(GridOrListConfiguration.Builder.of("default")
                                .withInlineEdit(true)
                                .withFilterField("title")
                                .withChildren(List.of(
                                        new FormElement("url", "field", "route.projects.labels.description"),
                                        new FormElement("title", "field", "route.projects.labels.name")
                                ))
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(FormRoute.Builder.of("form").withRepository("images").build())
                        .build()
        );

        configuration = Application.Builder.of()
                .withName("application.name")
                .withI18nBundlePrefix("some_i18n")
                .withUserManagement(UserManagement.Builder.of()
                        .withEnabled(true)
                        .withAccessControl(AccessControl.Builder.of().withRoles(List.of("manager", "admin")).build())
                        .withSignUp(true)
                        .withAdditionalFields(List.of(AdditionalField.Builder.of()
                                .withName("start_date")
                                .withType("date")
                                .build()))
                        .build())
                .withRoutes(routes)
                .withRepositories(repositories)
                .build();
    }

    public Application getConfiguration() {
        return configuration;
    }

    public Route getConfigForRoute(String viewName) {
        return configuration.getRoutes().get(viewName.split("/")[0]);
    }

    public String getApplicationName() {
        return configuration.getName();
    }
}
