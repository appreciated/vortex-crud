package com.github.appreciated.turbo_crud.service;

import com.github.appreciated.turbo_crud.config.model.*;
import com.github.appreciated.turbo_crud.entity.data_store.JpaDataStore;
import com.github.appreciated.turbo_crud.file_provider.FileProvider;
import com.github.appreciated.turbo_crud.file_provider.FileProviderRegistry;
import com.github.appreciated.turbo_crud.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.functions.*;
import com.github.appreciated.turbo_crud.ui.factories.item.CardFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.submenu.SubmenuRouteFactory;
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
        Route taskForm = Route.Builder.of(FormRouteFactory.class)
                .withDataStore("tasks")
                .withConfiguration(RouteConfiguration.Builder.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new FormElement("title", "field", "route.tasks.labels.title"),
                                new FormElement("description", "field", "route.tasks.labels.description"),
                                new FormElement("status", "field", "route.tasks.labels.status"),
                                new FormElement("due_date", "field", "route.tasks.labels.due_date"),
                                new FormElement("assigned_to", "field", "route.tasks.labels.assigned_to"),
                                FormElement.Builder.of(null, "collection", "route.tasks.labels.comments")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.of(FormRouteFactory.class)
                                                .withData(CollectionData.Builder.of("task_comments")
                                                        .withOneToMany(new OneToMany("task_id"))
                                                        .withChildren("comment_text")
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.comments-empty-message")
                                                .withChild(Route.Builder.of(FormRouteFactory.class)
                                                        .withConfiguration(RouteConfiguration.Builder.of(CardFactory.class)
                                                                .withTitleField("name")
                                                                .withChildren(
                                                                        new FormElement("comment_text", "field", "route.tasks.labels.comment")
                                                                )
                                                                .build())
                                                        .build())
                                                .build())
                                        .build(),
                                FormElement.Builder.of(null, "collection", "route.tasks.labels.related-tasks")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.of(ConnectDialogFactory.class)
                                                .withData(CollectionData.Builder.of("tasks")
                                                        .withManyToMany(new ManyToMany("task_has_task",
                                                                "task_id",
                                                                "related_task_id",
                                                                "id"))
                                                        .withChildren("title")
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.related-tasks-empty-message")
                                                .withConfiguration(new CollectionConfig("title"))
                                                .build())
                                        .build()
                        )
                        .build())
                .build();
        Route projectForm = Route.Builder.of(FormRouteFactory.class)
                .withDataStore("projects")
                .withTitle("route.projects.title-cards")
                .withConfiguration(RouteConfiguration.Builder.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new FormElement("name", "field", "route.projects.labels.name"),
                                new FormElement("description", "field", "route.projects.labels.description"),
                                new FormElement("start_date", "field", "route.projects.labels.start_date"),
                                new FormElement("end_date", "field", "route.projects.labels.end_date")
                        )
                        .build())
                .build();
        Route imageForm = Route.Builder.of(FormRouteFactory.class)
                .withDataStore("images")
                .withTitle("route.projects.title-cards")
                .withConfiguration(RouteConfiguration.Builder.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new FormElement("title", "field", "route.images.labels.title"),
                                new FormElement("url", "field", "route.images.labels.image")
                        )
                        .build())
                .build();

        Map<String, DataStore> dataStores = Map.of(
                "projects", DataStore.Builder.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "id", new Field(IdFieldFactory.class, true),
                                "name", new Field(TextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                "description", new Field(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(500).build()),
                                "start_date", new Field(DateFieldFactory.class),
                                "end_date", new Field(DateFieldFactory.class),
                                "created_at", new Field(DateTimePickerFactory.class),
                                "updated_at", new Field(DateTimePickerFactory.class)))
                        .build(),
                "tasks", DataStore.Builder.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "id", new Field(IdFieldFactory.class, true),
                                "title", new Field(TextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                "description", new Field(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                "assigned_to", new Field(ReferenceFieldFactory.class, "id", "username", "users", List.of("username")) /* 1:1 Relation */,
                                "status", new Field(SelectFieldFactory.class, "task-status"),
                                "due_date", Field.Builder.of(DateFieldFactory.class).withReadOnlyForRoles("developer").build(),
                                "created_at", new Field(DateTimePickerFactory.class),
                                "updated_at", new Field(DateTimePickerFactory.class)))
                        .build(),
                "task_has_task", DataStore.Builder.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "task_id", new Field(IdFieldFactory.class),
                                "related_task_id", new Field(IdFieldFactory.class)))
                        .build(),
                "task_comments", DataStore.Builder.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "id", new Field(IdFieldFactory.class, true),
                                "comment_text", new Field(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                "user_id", new Field(NumberFieldFactory.class),
                                "created_at", Field.Builder.of(DateTimePickerFactory.class).build()))
                        .build(),
                "images", DataStore.Builder.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "id", new Field(IdFieldFactory.class, true),
                                "title", Field.Builder.of(TextFieldFactory.class)
                                        .withRequired(true)
                                        .withValidation(Validation.Builder.of().withMaxLength(255).build())
                                        .build(),
                                "url", Field.Builder.of(ImageFieldFactory.class)
                                        .withConfiguration(new ImageFieldConfiguration(FileProvider.class))
                                        .build()))
                        .build());

        Map<String, Route> routes = Map.of(
                "projects-cards", Route.Builder.of(GridRouteFactory.class)
                        .withDefaultRoute(true)
                        .withDataStore("projects")
                        .withIconFactory(FACTORY::create)
                        .withTitle("route.projects.title-cards")
                        .withConfiguration(GridOrListConfiguration.Builder.of(CardFactory.class)
                                .withTitleField("name")
                                .withDescriptionField("description")
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(projectForm)
                        .build(),
                "projects-list", Route.Builder.of(ListRouteFactory.class)
                        .withDataStore("projects")
                        .withIconFactory(FACTORY::create)
                        .withTitle("route.projects.title-list")
                        .withConfiguration(GridOrListConfiguration.Builder.of(CardFactory.class)
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
                "tasks", Route.Builder.of(SubmenuRouteFactory.class)
                        .withIconFactory(TASKS::create)
                        .withDataStore("tasks")
                        .withTitle("route.tasks.title")
                        .withChildrenMap(Map.of("open",
                                Route.Builder.of(KanbanDetailFactory.class)
                                        .withIconFactory(TASKS::create)
                                        .withDataStore("tasks")
                                        .withTitle("route.open-tasks.title")
                                        .withConfiguration(Kanban.Builder.of(CardFactory.class)
                                                .withTitleField("title")
                                                .withDescriptionField("description")
                                                .withColumnField("status")
                                                .build())
                                        .withChild(taskForm)
                                        .build(),
                                "done",
                                Route.Builder.of(MasterDetailRouteFactory.class)
                                        .withIconFactory(CHECK_CIRCLE::create)
                                        .withDataStore("tasks")
                                        .withTitle("route.done-tasks.title")
                                        .withConfiguration(GridOrListConfiguration.Builder.of(CardFactory.class)
                                                .withTitleField("title")
                                                .withDescriptionField("description")
                                                .build())
                                        .withChild(taskForm)
                                        .build()))
                        .build(),
                "images-grid", Route.Builder.of(GridRouteFactory.class)
                        .withDataStore("images")
                        .withIconFactory(CAMERA::create)
                        .withTitle("route.images-cards")
                        .withConfiguration(GridOrListConfiguration.Builder.of(CardFactory.class)
                                .withTitleField("title")
                                .withImageField("url")
                                .withImageFactory(FileProvider.class)
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(imageForm)
                        .build(),
                "images-list", Route.Builder.of(ListRouteFactory.class)
                        .withDataStore("images")
                        .withIconFactory(CAMERA::create)
                        .withTitle("route.images-list")
                        .withConfiguration(GridOrListConfiguration.Builder.of(CardFactory.class)
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
                .withVersioning(Versioning.Builder.of().withDataStores("projects", "tasks", "task_comments").build())
                .withAuditing(Auditing.Builder.of().withActions("create", "update", "delete", "login", "logout").build())
                .withSelects(Selects.Builder.of().withConfigs(
                        Map.of("task-status",
                                Map.of(
                                         "open", "selects.task-status.open",
                                        "todo", "selects.task-status.todo",
                                        "work-in-progress", "selects.task-status.progress",
                                        "closed", "selects.task-status.closed"
                                )
                        )).build())
                .withDataStores(dataStores)
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
