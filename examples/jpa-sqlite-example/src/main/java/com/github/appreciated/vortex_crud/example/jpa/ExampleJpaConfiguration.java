package com.github.appreciated.vortex_crud.example.jpa;


import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.file_provider.ImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.*;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ExampleJpaConfiguration implements VortexCrudConfigurationProvider<String, String> {

    @Override
    public Application<String, String> get() {
        RouteRenderer<String, String> taskForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore("tasks")
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JpaFormElement("title", "field", "route.tasks.labels.title"),
                                new JpaFormElement("description", "field", "route.tasks.labels.description"),
                                new JpaFormElement("status", "field", "route.tasks.labels.status"),
                                new JpaFormElement("due_date", "field", "route.tasks.labels.due_date"),
                                new JpaFormElement("assigned_to", "field", "route.tasks.labels.assigned_to"),
                                JpaFormElement.of(null, "collection", "route.tasks.labels.comments")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.<String, String>of(FormDialogFactory.class)
                                                .withData(CollectionData.Builder.<String, String>of("task_comments")
                                                        .withOneToMany(new OneToMany<>("task_id"))
                                                        .withChildren("comment_text")
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.comments-empty-message")
                                                .withChild(JpaRouteRenderer.of(FormRouteFactory.class)
                                                        .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                                                                .withTitleField("name")
                                                                .withChildren(
                                                                        new JpaFormElement("comment_text", "field", "route.tasks.labels.comment")
                                                                )
                                                                .build())
                                                        .build())
                                                .build())
                                        .build(),
                                JpaFormElement.of(null, "collection", "route.tasks.labels.related-tasks")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.<String, String>of(ConnectDialogFactory.class)
                                                .withData(CollectionData.Builder.<String, String>of("tasks")
                                                        .withManyToMany(new ManyToMany<>("task_has_task",
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
        RouteRenderer<String, String> projectForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore("projects")
                .withTitle("route.projects.title-cards")
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new JpaFormElement("name", "field", "route.projects.labels.name"),
                                new JpaFormElement("description", "field", "route.projects.labels.description"),
                                new JpaFormElement("start_date", "field", "route.projects.labels.start_date"),
                                new JpaFormElement("end_date", "field", "route.projects.labels.end_date")
                        )
                        .build())
                .build();
        RouteRenderer<String, String> imageForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore("images")
                .withTitle("route.projects.title-cards")
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JpaFormElement("title", "field", "route.images.labels.title"),
                                new JpaFormElement("url", "field", "route.images.labels.image")
                        )
                        .build())
                .build();

        Map<String, DataStoreConfig<String, String>> dataStores = Map.of(
                "projects", JpaDataStoreConfig.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "id", new JpaField(IdFieldFactory.class, true),
                                "name", new JpaField(TextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                "description", new JpaField(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(500).build()),
                                "start_date", new JpaField(DateFieldFactory.class),
                                "end_date", new JpaField(DateFieldFactory.class),
                                "created_at", new JpaField(DateTimePickerFactory.class),
                                "updated_at", new JpaField(DateTimePickerFactory.class)))
                        .build(),
                "tasks", JpaDataStoreConfig.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "id", new JpaField(IdFieldFactory.class, true),
                                "title", new JpaField(TextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                "description", new JpaField(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                "assigned_to", new JpaField(ReferenceFieldFactory.class, "id", "username", "users", List.of("username")) /* 1:1 Relation */,
                                "status", new JpaField(SelectFieldFactory.class, "task-status"),
                                "due_date", JpaField.of(DateFieldFactory.class).withReadOnlyForRoles("developer").build(),
                                "created_at", new JpaField(DateTimePickerFactory.class),
                                "updated_at", new JpaField(DateTimePickerFactory.class)))
                        .build(),
                "task_has_task", JpaDataStoreConfig.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "task_id", new JpaField(IdFieldFactory.class),
                                "related_task_id", new JpaField(IdFieldFactory.class)))
                        .build(),
                "task_comments", JpaDataStoreConfig.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "id", new JpaField(IdFieldFactory.class, true),
                                "comment_text", new JpaField(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                "user_id", new JpaField(NumberFieldFactory.class),
                                "created_at", JpaField.of(DateTimePickerFactory.class).build()))
                        .build(),
                "images", JpaDataStoreConfig.of(JpaDataStore.class)
                        .withFields(Map.of(
                                "id", new JpaField(IdFieldFactory.class, true),
                                "title", JpaField.of(TextFieldFactory.class)
                                        .withRequired(true)
                                        .withValidation(Validation.Builder.of().withMaxLength(255).build())
                                        .build(),
                                "url", JpaField.of(ImageFieldFactory.class)
                                        .withConfiguration(new ImageFieldRendererConfiguration<>(ImageResourceProvider.class))
                                        .build()))
                        .build());

        LinkedHashMap<String, RouteRenderer<String, String>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JpaRouteRenderer.of(GridRouteFactory.class)
                .withDefaultRoute(true)
                .withDataStore("projects")
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-cards")
                .withConfiguration(GridOrListRendererConfiguration.Builder.<String, String>of(CardFactory.class)
                        .withTitleField("name")
                        .withDescriptionField("description")
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(projectForm)
                .build());
        routes.put("projects-list", JpaRouteRenderer.of(ListRouteFactory.class)
                .withDataStore("projects")
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-list")
                .withConfiguration(GridOrListRendererConfiguration.Builder.<String, String>of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField("name")
                        .withChildren(
                                new JpaFormElement("name", "field", "route.projects.labels.name"),
                                new JpaFormElement("description", "field", "route.projects.labels.description"),
                                new JpaFormElement("start_date", "field", "route.projects.labels.start_date"),
                                new JpaFormElement("end_date", "field", "route.projects.labels.end_date")
                        )
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(projectForm)
                .build());
        routes.put("tasks", JpaRouteRenderer.of(SubmenuRouteFactory.class)
                .withIconFactory(TASKS::create)
                .withDataStore("tasks")
                .withTitle("route.tasks.title")
                .withChildrenMap(Map.of("open",
                        JpaRouteRenderer.of(KanbanDetailFactory.class)
                                .withIconFactory(TASKS::create)
                                .withDataStore("tasks")
                                .withTitle("route.open-tasks.title")
                                .withConfiguration(Kanban.Builder.<String, String>of(CardFactory.class)
                                        .withTitleField("title")
                                        .withDescriptionField("description")
                                        .withColumnField("status")
                                        .build())
                                .withChild(taskForm)
                                .build(),
                        "done",
                        JpaRouteRenderer.of(MasterDetailRouteFactory.class)
                                .withIconFactory(CHECK_CIRCLE::create)
                                .withDataStore("tasks")
                                .withTitle("route.done-tasks.title")
                                .withConfiguration(GridOrListRendererConfiguration.Builder.<String, String>of(CardFactory.class)
                                        .withTitleField("title")
                                        .withDescriptionField("description")
                                        .build())
                                .withChild(taskForm)
                                .build()))
                .build());
        routes.put("images-grid", JpaRouteRenderer.of(GridRouteFactory.class)
                .withDataStore("images")
                .withIconFactory(CAMERA::create)
                .withTitle("route.images-cards")
                .withConfiguration(GridOrListRendererConfiguration.Builder.<String, String>of(CardFactory.class)
                        .withTitleField("title")
                        .withImageField("url")
                        .withImageFactory(ImageResourceProvider.class)
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(imageForm)
                .build());
        routes.put("images-list", JpaRouteRenderer.of(ListRouteFactory.class)
                .withDataStore("images")
                .withIconFactory(CAMERA::create)
                .withTitle("route.images-list")
                .withConfiguration(GridOrListRendererConfiguration.Builder.<String, String>of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField("title")
                        .withChildren(
                                new JpaFormElement("url", "field", "route.projects.labels.description"),
                                new JpaFormElement("title", "field", "route.projects.labels.name")
                        )
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(imageForm)
                .build());

        return JpaApplication.of()
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
                .withVersioning(Versioning.Builder.<String>of().withDataStores("projects", "tasks", "task_comments").build())
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
}
