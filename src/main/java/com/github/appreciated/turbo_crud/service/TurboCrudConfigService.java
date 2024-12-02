package com.github.appreciated.turbo_crud.service;

import com.github.appreciated.turbo_crud.config.model.*;
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
        Map<String, Route> forms = Map.of("task", FormRoute.Builder.of()
                .withRepository("tasks")
                .withFactory("form")
                .withConfiguration(FormConfiguration.Builder.of()
                        .withTitleField("title")
                        .withChildren(List.of(
                                new FormElement("title", "field", "route.tasks.labels.title"),
                                new FormElement("description", "field", "route.tasks.labels.description"),
                                new FormElement("status", "field", "route.tasks.labels.status"),
                                new FormElement("due_date", "field", "route.tasks.labels.due_date"),
                                new FormElement("assigned_to", "field", "route.tasks.labels.assigned_to")
                        ))
                        .build())
                .build(), "project", FormRoute.Builder.of()
                .withRepository("projects")
                .withFactory("form")
                .withTitle("route.projects.title-cards")
                .withConfiguration(FormConfiguration.Builder.of()
                        .withTitleField("name")
                        .withChildren(List.of(
                                new FormElement("name", "field", "route.projects.labels.name"),
                                new FormElement("description", "field", "route.projects.labels.description"),
                                new FormElement("start_date", "field", "route.projects.labels.start_date"),
                                new FormElement("end_date", "field", "route.projects.labels.end_date")
                        ))
                        .build())
                .build(), "image", FormRoute.Builder.of()
                .withRepository("images")
                .withFactory("form")
                .withTitle("route.images.title-cards")
                .withConfiguration(FormConfiguration.Builder.of()
                        .withTitleField("title")
                        .withChildren(List.of(
                                new FormElement("title", "field", "route.images.labels.title"),
                                new FormElement("url", "field", "route.images.labels.image")
                        ))
                        .build())
                .build());

        Map<String, Repository> repositories = Map.of(
                "projects", Repository.Builder.of()
                        .withFactory("jpa")
                        .withFields(Map.of(
                                "id", new Field("id", true),
                                "name", new Field("text", true, true, Validation.Builder.of()
                                        .withMaxLength(255)
                                        .build()),
                                "description", new Field("textarea", false, false, Validation.Builder.of()
                                        .withMaxLength(500)
                                        .build()),
                                "start_date", new Field("date"),
                                "end_date", new Field("date"),
                                "created_at", new Field("datetime"),
                                "updated_at", new Field("datetime"))
                        )
                        .build(),
                "tasks", Repository.Builder.of()
                        .withFactory("jpa")
                        .withFields(
                                Map.of(
                                        "id", new Field("id", true),
                                        "title", new Field("text", true, true, Validation.Builder.of()
                                                .withMaxLength(255)
                                                .build()),
                                        "description", new Field("textarea", false, false, Validation.Builder.of()
                                                .withMaxLength(1000)
                                                .build()),
                                        "assigned_to", new Field("reference", "users", "id", "username", List.of("username")),
                                        "status", new Field("select", "task-status"),
                                        "due_date", Field.Builder.of("date")
                                                .withReadOnlyForRoles(List.of("developer"))
                                                .build(),
                                        "created_at", new Field("datetime"),
                                        "updated_at", new Field("datetime")))
                        .build(),
                "task_has_task", Repository.Builder.of()
                        .withFactory("jpa")
                        .withFields(Map.of("task_id", new Field("id"),
                                "related_task_id", new Field("id")))
                        .build(), "task_comments", Repository.Builder.of()
                        .withFactory("jpa")
                        .withFields(Map.of("id", new Field("id", true),
                                "comment_text", new Field("textarea", false, false, Validation.Builder.of()
                                        .withMaxLength(1000)
                                        .build()),
                                "user_id", new Field("number"),
                                "created_at", Field.Builder.of("datetime")
                                        .withDefaultValue("now()")
                                        .build()))
                        .build(), "images", Repository.Builder.of()
                        .withFactory("jpa")
                        .withFields(Map.of(
                                "id", new Field("id", true),
                                "title", Field.Builder.of("text")
                                        .withRequired(true)
                                        .withValidation(Validation.Builder.of().withMaxLength(255).build())
                                        .build(),
                                "url", Field.Builder.of("text")
                                        .withConfiguration(ImageFieldConfiguration.Builder.of()
                                                .withFactory("default")
                                                .build())
                                        .build())
                        ).build());
        Map<String, Route> routes = Map.of("projects-cards", Route.Builder.of()
                .withDefaultRoute(true)
                .withFactory("grid")
                .withRepository("projects")
                .withIcon("FACTORY")
                .withTitle("route.projects.title-cards")
                .withConfiguration(GridOrListConfiguration.Builder.of()
                        .withFactory("card")
                        .withTitleField("name")
                        .withDescriptionField("description")
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(FormRoute.Builder.of().withRepository("projects").withFactory("form").build())
                .build(), "images-grid", Route.Builder.of()
                .withFactory("grid")
                .withRepository("images")
                .withIcon("CAMERA")
                .withTitle("route.images.title-cards")
                .withConfiguration(GridOrListConfiguration.Builder.of()
                        .withFactory("card")
                        .withTitleField("title")
                        .withImageField("url")
                        .withImageFactory("default")
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(FormRoute.Builder.of().withRepository("images").withFactory("form").build())
                .build());

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
                .withSelects(Selects.Builder.of()
                        .withConfigs(Map.of("task-status",
                                Map.of("open", "selects.task-status.open",
                                        "todo", "selects.task-status.todo",
                                        "work-in-progress", "selects.task-status.progress",
                                        "closed", "selects.task-status.closed")))
                        .build())
                .withVersioning(Versioning.Builder.of()
                        .withEnabled(true)
                        .withRepositories(List.of("projects", "tasks", "task_comments"))
                        .build())
                .withAuditing(Auditing.Builder.of()
                        .withEnabled(true)
                        .withActions(List.of("create", "update", "delete", "login", "logout"))
                        .build())
                .withRepositories(repositories)
                .withRoutes(routes)
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
