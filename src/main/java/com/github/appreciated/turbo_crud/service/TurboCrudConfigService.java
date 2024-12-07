package com.github.appreciated.turbo_crud.service;

import com.github.appreciated.turbo_crud.config.model.*;
import com.github.appreciated.turbo_crud.entity.manager.TCJpaEntityManager;
import com.github.appreciated.turbo_crud.file_provider.TCFileProviderRegistry;
import com.github.appreciated.turbo_crud.file_provider.TCFileProvider;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TCConnectDialogFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.TCListCollectionFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.*;
import com.github.appreciated.turbo_crud.ui.factories.item.TCCardFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.form.TCFormRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.TCGridRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.TCKanbanDetailFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.list.TCListRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.master_detail.TCMasterDetailRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.submenu.TCSubmenuRouteFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.*;

/**
 * Service for loading and providing access to the Turbo CRUD configuration.
 * Reads configuration from a resource file and offers methods to retrieve route and application settings.
 */

@Service
public class TurboCrudConfigService {

    private final Application configuration;

    public TurboCrudConfigService() {
        Route taskForm = Route.Builder.of(TCFormRouteFactory.class)
                .withRepository("tasks")
                .withConfiguration(RouteConfiguration.Builder.of(TCCardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new FormElement("title", "field", "route.tasks.labels.title"),
                                new FormElement("description", "field", "route.tasks.labels.description"),
                                new FormElement("status", "field", "route.tasks.labels.status"),
                                new FormElement("due_date", "field", "route.tasks.labels.due_date"),
                                new FormElement("assigned_to", "field", "route.tasks.labels.assigned_to"),
                                FormElement.Builder.of("comments", "collection", "route.tasks.labels.comments")
                                        .withConfiguration(Collection.Builder.of(TCListCollectionFactory.class)
                                                .withData(CollectionData.Builder.of("task_comments")
                                                        .withOneToMany(new OneToMany("task_id"))
                                                        .withChildren(
                                                                "comment_text",
                                                                "field",
                                                                "route.tasks.labels.comment"
                                                        )
                                                        .build())
                                                .build())
                                        .build(),
                                FormElement.Builder.of("related-tasks",
                                                "collection",
                                                "route.tasks.labels.related-tasks")
                                        .withConfiguration(Collection.Builder.of(TCListCollectionFactory.class)
                                                .withData(CollectionData.Builder.of("tasks")
                                                        .withManyToMany(new ManyToMany("task_has_task",
                                                                "task_id",
                                                                "related_task_id",
                                                                "id"))
                                                        .withChildren(
                                                                "title",
                                                                "field",
                                                                "route.tasks.labels.title"
                                                        )
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.related-tasks-empty-message")
                                                .withConfiguration(new CollectionConfig("title"))
                                                .withFactory(TCConnectDialogFactory.class)
                                                .build())
                                        .build()
                        )
                        .build())
                .build();
        Route projectForm = Route.Builder.of(TCFormRouteFactory.class)
                .withRepository("projects")
                .withTitle("route.projects.title-cards")
                .withConfiguration(RouteConfiguration.Builder.of(TCCardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new FormElement("name", "field", "route.projects.labels.name"),
                                new FormElement("description", "field", "route.projects.labels.description"),
                                new FormElement("start_date", "field", "route.projects.labels.start_date"),
                                new FormElement("end_date", "field", "route.projects.labels.end_date")
                        )
                        .build())
                .build();
        Route imageForm = Route.Builder.of(TCFormRouteFactory.class)
                .withRepository("images")
                .withTitle("route.projects.title-cards")
                .withConfiguration(RouteConfiguration.Builder.of(TCCardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new FormElement("title", "field", "route.images.labels.title"),
                                new FormElement("url", "field", "route.images.labels.image")
                        )
                        .build())
                .build();

        Map<String, Repository> repositories = Map.of("projects",
                Repository.Builder.of(TCJpaEntityManager.class)
                        .withFields(Map.of(
                                "id", new Field(TCIdFieldFactory.class, true),
                                "name", new Field(TCTextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                "description", new Field(TCTextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(500).build()),
                                "start_date", new Field(TCDateFieldFactory.class),
                                "end_date", new Field(TCDateFieldFactory.class),
                                "created_at", new Field(TCDateTimePickerFactory.class),
                                "updated_at", new Field(TCDateTimePickerFactory.class)))
                        .build(),
                "tasks",
                Repository.Builder.of(TCJpaEntityManager.class)
                        .withFields(Map.of(
                                "id", new Field(TCIdFieldFactory.class, true),
                                "title", new Field(TCTextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                "description", new Field(TCTextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                "assigned_to", new Field(TCReferenceFieldFactory.class, "users", "id", "username", List.of("username")) /* 1:1 Relation */,
                                "status", new Field(TCSelectFieldFactory.class, "task-status"),
                                "due_date", Field.Builder.of(TCDateFieldFactory.class).withReadOnlyForRoles("developer").build(),
                                "created_at", new Field(TCDateTimePickerFactory.class),
                                "updated_at", new Field(TCDateTimePickerFactory.class)))
                        .build(),
                "task_has_task",
                Repository.Builder.of(TCJpaEntityManager.class)
                        .withFields(Map.of(
                                "task_id", new Field(TCIdFieldFactory.class),
                                "related_task_id", new Field(TCIdFieldFactory.class)))
                        .build(),
                "task_comments",
                Repository.Builder.of(TCJpaEntityManager.class)
                        .withFields(Map.of(
                                "id", new Field(TCIdFieldFactory.class, true),
                                "comment_text", new Field(TCTextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                "user_id", new Field(TCNumberFieldFactory.class),
                                "created_at", Field.Builder.of(TCDateTimePickerFactory.class).build()))
                        .build(),
                "images",
                Repository.Builder.of(TCJpaEntityManager.class)
                        .withFields(Map.of(
                                "id", new Field(TCIdFieldFactory.class, true),
                                "title", Field.Builder.of(TCTextFieldFactory.class)
                                        .withRequired(true)
                                        .withValidation(Validation.Builder.of().withMaxLength(255).build())
                                        .build(),
                                "url", Field.Builder.of(TCImageFieldFactory.class)
                                        .withConfiguration(new ImageFieldConfiguration(TCFileProviderRegistry.class))
                                        .build()))
                        .build());

        Map<String, Route> routes = Map.of("projects-cards",
                Route.Builder.of(TCGridRouteFactory.class)
                        .withDefaultRoute(true)
                        .withRepository("projects")
                        .withIconFactory(FACTORY::create)
                        .withTitle("route.projects.title-cards")
                        .withConfiguration(GridOrListConfiguration.Builder.of(TCCardFactory.class)
                                .withTitleField("name")
                                .withDescriptionField("description")
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(projectForm)
                        .build(),
                "projects-list",
                Route.Builder.of(TCListRouteFactory.class)
                        .withRepository("projects")
                        .withIconFactory(FACTORY::create)
                        .withTitle("route.projects.title-list")
                        .withConfiguration(GridOrListConfiguration.Builder.of(TCCardFactory.class)
                                .withInlineEdit(true)
                                .withFilterField("name")
                                .withChildren(
                                        new FormElement("name", "field", "route.projects.labels.name"),
                                        new FormElement("description", "field", "route.projects.labels.description"),
                                        new FormElement("start_date", "field", "route.projects.labels.start_date"),
                                        new FormElement("end_date", "field", "route.projects.labels.end_date")
                                )
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(projectForm)
                        .build(),
                "tasks",
                Route.Builder.of(TCSubmenuRouteFactory.class)
                        .withIconFactory(TASKS::create)
                        .withRepository("tasks")
                        .withTitle("route.tasks.title")
                        .withChildrenMap(Map.of("open",
                                Route.Builder.of(TCKanbanDetailFactory.class)
                                        .withIconFactory(TASKS::create)
                                        .withRepository("tasks")
                                        .withTitle("route.open-tasks.title")
                                        .withConfiguration(Kanban.Builder.of(TCCardFactory.class)
                                                .withTitleField("title")
                                                .withDescriptionField("description")
                                                .withColumnField("status")
                                                .build())
                                        .withChild(taskForm)
                                        .build(),
                                "done",
                                Route.Builder.of(TCMasterDetailRouteFactory.class)
                                        .withIconFactory(CHECK_CIRCLE::create)
                                        .withRepository("tasks")
                                        .withTitle("route.done-tasks.title")
                                        .withConfiguration(GridOrListConfiguration.Builder.of(TCCardFactory.class)
                                                .withTitleField("title")
                                                .withDescriptionField("description")
                                                .build())
                                        .withChild(taskForm)
                                        .build()))
                        .build(),
                "images-grid",
                Route.Builder.of(TCGridRouteFactory.class)
                        .withRepository("images")
                        .withIconFactory(CAMERA::create)
                        .withTitle("route.images-cards")
                        .withConfiguration(GridOrListConfiguration.Builder.of(TCCardFactory.class)
                                .withTitleField("title")
                                .withImageField("url")
                                .withImageFactory(TCFileProvider.class)
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(imageForm)
                        .build(),
                "images-list",
                Route.Builder.of(TCListRouteFactory.class)
                        .withRepository("images")
                        .withIconFactory(CAMERA::create)
                        .withTitle("route.images-list")
                        .withConfiguration(GridOrListConfiguration.Builder.of(TCCardFactory.class)
                                .withInlineEdit(true)
                                .withFilterField("title")
                                .withChildren(
                                        new FormElement("url", "field", "route.projects.labels.description"),
                                        new FormElement("title", "field", "route.projects.labels.name")
                                )
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(imageForm)
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
                .withRoutes(routes)
                .withSelects(Selects.Builder.of().withConfigs(
                        Map.of("task-status",
                                Map.of(
                                        "open", "selects.task-status.open",
                                        "todo", "selects.task-status.todo",
                                        "work-in-progress", "selects.task-status.progress",
                                        "closed", "selects.task-status.closed"
                                )
                        )).build())
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
