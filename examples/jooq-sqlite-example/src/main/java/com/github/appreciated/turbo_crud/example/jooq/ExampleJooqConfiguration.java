package com.github.appreciated.turbo_crud.example.jooq;

import com.github.appreciated.turbo_crud.core.config.model.Application;
import com.github.appreciated.turbo_crud.core.config.model.*;
import com.github.appreciated.turbo_crud.core.file_provider.FileProvider;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigurationProvider;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.functions.*;
import com.github.appreciated.turbo_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.turbo_crud.jooq.service.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.github.appreciated.turbo_crud.jooq.models.tables.Images.IMAGES;
import static com.github.appreciated.turbo_crud.jooq.models.tables.Projects.PROJECTS;
import static com.github.appreciated.turbo_crud.jooq.models.tables.TaskComments.TASK_COMMENTS;
import static com.github.appreciated.turbo_crud.jooq.models.tables.TaskHasTask.TASK_HAS_TASK;
import static com.github.appreciated.turbo_crud.jooq.models.tables.Tasks.TASKS;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ExampleJooqConfiguration implements TurboCrudConfigurationProvider<Table<?>, TableField<?,?>> {
    @Override
    public Application<Table<?>, TableField<?,?>> get() {
        Route<Table<?>> taskForm = JooqRoute.of(FormRouteFactory.class)
                .withDataStore(TASKS)
                .withConfiguration(JooqRouteConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JooqFormElement("title", "field", "route.tasks.labels.title"),
                                new JooqFormElement("description", "field", "route.tasks.labels.description"),
                                new JooqFormElement("status", "field", "route.tasks.labels.status"),
                                new JooqFormElement("due_date", "field", "route.tasks.labels.due_date"),
                                new JooqFormElement("assigned_to", "field", "route.tasks.labels.assigned_to"),
                                JooqFormElement.of(null, "collection", "route.tasks.labels.comments")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.<Table<?>>of(FormDialogFactory.class)
                                                .withData(CollectionData.Builder.<Table<?>>of(TASK_COMMENTS)
                                                        .withOneToMany(new OneToMany("task_id"))
                                                        .withChildren("comment_text")
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.comments-empty-message")
                                                .withChild(JooqRoute.of(FormRouteFactory.class)
                                                        .withConfiguration(JooqRouteConfiguration.of(CardFactory.class)
                                                                .withTitleField("name")
                                                                .withChildren(
                                                                        new JooqFormElement("comment_text", "field", "route.tasks.labels.comment")
                                                                )
                                                                .build())
                                                        .build())
                                                .build())
                                        .build(),
                                JooqFormElement.of(null, "collection", "route.tasks.labels.related-tasks")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.<Table<?>>of(ConnectDialogFactory.class)
                                                .withData(CollectionData.Builder.<Table<?>>of(TASKS)
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
        Route<Table<?>> projectForm = JooqRoute.of(FormRouteFactory.class)
                .withDataStore(PROJECTS)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteConfiguration.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new JooqFormElement("name", "field", "route.projects.labels.name"),
                                new JooqFormElement("description", "field", "route.projects.labels.description"),
                                new JooqFormElement("start_date", "field", "route.projects.labels.start_date"),
                                new JooqFormElement("end_date", "field", "route.projects.labels.end_date")
                        )
                        .build())
                .build();
        Route<Table<?>> imageForm = JooqRoute.of(FormRouteFactory.class)
                .withDataStore(IMAGES)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JooqFormElement("title", "field", "route.images.labels.title"),
                                new JooqFormElement("url", "field", "route.images.labels.image")
                        )
                        .build())
                .build();
        Map<Table<?>, DataStoreConfig<TableField<?,?>>> dataStores = Map.of(
                PROJECTS, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                PROJECTS.ID, new Field(IdFieldFactory.class, true),
                                PROJECTS.NAME, new Field(TextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                PROJECTS.DESCRIPTION, new Field(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(500).build()),
                                PROJECTS.START_DATE, new Field(DateFieldFactory.class),
                                PROJECTS.END_DATE, new Field(DateFieldFactory.class),
                                PROJECTS.CREATED_AT, new Field(DateTimePickerFactory.class),
                                PROJECTS.UPDATED_AT, new Field(DateTimePickerFactory.class)))
                        .build(),
                TASKS, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                TASKS.ID, new Field(IdFieldFactory.class, true),
                                TASKS.TITLE, new Field(TextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                TASKS.DESCRIPTION, new Field(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                TASKS.ASSIGNED_TO, new Field(ReferenceFieldFactory.class, "id", "username", "users", List.of("username")) /* 1:1 Relation */,
                                TASKS.STATUS, new Field(SelectFieldFactory.class, "task-status"),
                                TASKS.DUE_DATE, Field.Builder.of(DateFieldFactory.class).withReadOnlyForRoles("developer").build(),
                                TASKS.CREATED_AT, new Field(DateTimePickerFactory.class),
                                TASKS.UPDATED_AT, new Field(DateTimePickerFactory.class)))
                        .build(),
                TASK_HAS_TASK, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                TASK_HAS_TASK.TASK_ID, new Field(IdFieldFactory.class),
                                TASK_HAS_TASK.RELATED_TASK_ID, new Field(IdFieldFactory.class)))
                        .build(),
                TASK_COMMENTS, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                TASK_COMMENTS.ID, new Field(IdFieldFactory.class, true),
                                TASK_COMMENTS.COMMENT_TEXT, new Field(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                TASK_COMMENTS.USER_ID, new Field(NumberFieldFactory.class),
                                TASK_COMMENTS.CREATED_AT, Field.Builder.of(DateTimePickerFactory.class).build()))
                        .build(),
                IMAGES, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                IMAGES.ID, new Field(IdFieldFactory.class, true),
                                IMAGES.TITLE, Field.Builder.of(TextFieldFactory.class)
                                        .withRequired(true)
                                        .withValidation(Validation.Builder.of().withMaxLength(255).build())
                                        .build(),
                                IMAGES.URL, Field.Builder.of(ImageFieldFactory.class)
                                        .withConfiguration(new ImageFieldConfiguration(FileProvider.class))
                                        .build()))
                        .build());

        Map<String, Route<Table<?>>> routes = Map.of(
                "projects-cards", JooqRoute.of(GridRouteFactory.class)
                        .withDefaultRoute(true)
                        .withDataStore(PROJECTS)
                        .withIconFactory(FACTORY::create)
                        .withTitle("route.projects.title-cards")
                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>>of(CardFactory.class)
                                .withTitleField("name")
                                .withDescriptionField("description")
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(projectForm)
                        .build(),
                "projects-list", JooqRoute.of(ListRouteFactory.class)
                        .withDataStore(PROJECTS)
                        .withIconFactory(FACTORY::create)
                        .withTitle("route.projects.title-list")
                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>>of(CardFactory.class)
                                .withInlineEdit(true)
                                .withFilterField("name")
                                .withChildren(
                                        new JooqFormElement("name", "field", "route.projects.labels.name"),
                                        new JooqFormElement("description", "field", "route.projects.labels.description"),
                                        new JooqFormElement("start_date", "field", "route.projects.labels.start_date"),
                                        new JooqFormElement("end_date", "field", "route.projects.labels.end_date")
                                )
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(projectForm)
                        .build(),
                "tasks", JooqRoute.of(SubmenuRouteFactory.class)
                        .withIconFactory(VaadinIcon.TASKS::create)
                        .withDataStore(TASKS)
                        .withTitle("route.tasks.title")
                        .withChildrenMap(Map.of("open",
                                JooqRoute.of(KanbanDetailFactory.class)
                                        .withIconFactory(VaadinIcon.TASKS::create)
                                        .withDataStore(TASKS)
                                        .withTitle("route.open-tasks.title")
                                        .withConfiguration(Kanban.Builder.of(CardFactory.class)
                                                .withTitleField("title")
                                                .withDescriptionField("description")
                                                .withColumnField("status")
                                                .build())
                                        .withChild(taskForm)
                                        .build(),
                                "done",
                                JooqRoute.of(MasterDetailRouteFactory.class)
                                        .withIconFactory(CHECK_CIRCLE::create)
                                        .withDataStore(TASKS)
                                        .withTitle("route.done-tasks.title")
                                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>>of(CardFactory.class)
                                                .withTitleField("title")
                                                .withDescriptionField("description")
                                                .build())
                                        .withChild(taskForm)
                                        .build()))
                        .build(),
                "images-grid", JooqRoute.of(GridRouteFactory.class)
                        .withDataStore(IMAGES)
                        .withIconFactory(CAMERA::create)
                        .withTitle("route.images-cards")
                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>>of(CardFactory.class)
                                .withTitleField("title")
                                .withImageField("url")
                                .withImageFactory(FileProvider.class)
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(imageForm)
                        .build(),
                "images-list", JooqRoute.of(ListRouteFactory.class)
                        .withDataStore(IMAGES)
                        .withIconFactory(CAMERA::create)
                        .withTitle("route.images-list")
                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>>of(CardFactory.class)
                                .withInlineEdit(true)
                                .withFilterField("title")
                                .withChildren(
                                        new JooqFormElement("url", "field", "route.projects.labels.description"),
                                        new JooqFormElement("title", "field", "route.projects.labels.name")
                                )
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(imageForm)
                        .build());

        return JooqApplication.of()
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
}
