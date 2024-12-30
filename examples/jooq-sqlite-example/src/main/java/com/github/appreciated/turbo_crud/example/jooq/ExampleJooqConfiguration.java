package com.github.appreciated.turbo_crud.example.jooq;

import com.github.appreciated.turbo_crud.core.config.model.Application;
import com.github.appreciated.turbo_crud.core.config.model.*;
import com.github.appreciated.turbo_crud.core.file_provider.FileProvider;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigurationProvider;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.functions.*;
import com.github.appreciated.turbo_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.turbo_crud.jooq.models.tables.Users;
import com.github.appreciated.turbo_crud.jooq.service.*;
import com.vaadin.flow.component.icon.VaadinIcon;
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
public class ExampleJooqConfiguration implements TurboCrudConfigurationProvider<Table<?>, TableField<?, ?>> {
    @Override
    public Application<Table<?>, TableField<?, ?>> get() {
        Route<Table<?>, TableField<?, ?>> taskForm = JooqRoute.of(FormRouteFactory.class)
                .withDataStore(TASKS)
                .withConfiguration(JooqRouteConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JooqFormElement(TASKS.TITLE, "field", "route.tasks.labels.title"),
                                new JooqFormElement(TASKS.DESCRIPTION, "field", "route.tasks.labels.description"),
                                new JooqFormElement(TASKS.STATUS, "field", "route.tasks.labels.status"),
                                new JooqFormElement(TASKS.DUE_DATE, "field", "route.tasks.labels.due_date"),
                                new JooqFormElement(TASKS.ASSIGNED_TO, "field", "route.tasks.labels.assigned_to"),
                                JooqFormElement.of(null, "collection", "route.tasks.labels.comments")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.<Table<?>, TableField<?, ?>>of(FormDialogFactory.class)
                                                .withData(CollectionData.Builder.<Table<?>>of(TASK_COMMENTS)
                                                        .withOneToMany(new OneToMany("task_id"))
                                                        .withChildren("comment_text")
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.comments-empty-message")
                                                .withChild(JooqRoute.of(FormRouteFactory.class)
                                                        .withConfiguration(JooqRouteConfiguration.of(CardFactory.class)
                                                                .withTitleField("name")
                                                                .withChildren(
                                                                        new JooqFormElement(TASK_COMMENTS.COMMENT_TEXT, "field", "route.tasks.labels.comment")
                                                                )
                                                                .build())
                                                        .build())
                                                .build())
                                        .build(),
                                JooqFormElement.of(null, "collection", "route.tasks.labels.related-tasks")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.<Table<?>, TableField<?, ?>>of(ConnectDialogFactory.class)
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

        Route<Table<?>, TableField<?, ?>> projectForm = JooqRoute.of(FormRouteFactory.class)
                .withDataStore(PROJECTS)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteConfiguration.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new JooqFormElement(PROJECTS.NAME, "field", "route.projects.labels.name"),
                                new JooqFormElement(PROJECTS.DESCRIPTION, "field", "route.projects.labels.description"),
                                new JooqFormElement(PROJECTS.START_DATE, "field", "route.projects.labels.start_date"),
                                new JooqFormElement(PROJECTS.END_DATE, "field", "route.projects.labels.end_date")
                        )
                        .build())
                .build();

        Route<Table<?>, TableField<?, ?>> imageForm = JooqRoute.of(FormRouteFactory.class)
                .withDataStore(IMAGES)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JooqFormElement(IMAGES.TITLE, "field", "route.images.labels.title"),
                                new JooqFormElement(IMAGES.URL, "field", "route.images.labels.image")
                        )
                        .build())
                .build();
        Map<Table<?>, DataStoreConfig<TableField<?, ?>>> dataStores = Map.of(
                PROJECTS, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                PROJECTS.ID, new JooqField(IdFieldFactory.class, true),
                                PROJECTS.NAME, new JooqField(TextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                PROJECTS.DESCRIPTION, new JooqField(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(500).build()),
                                PROJECTS.START_DATE, new JooqField(DateFieldFactory.class),
                                PROJECTS.END_DATE, new JooqField(DateFieldFactory.class),
                                PROJECTS.CREATED_AT, new JooqField(DateTimePickerFactory.class),
                                PROJECTS.UPDATED_AT, new JooqField(DateTimePickerFactory.class)))
                        .build(),
                TASKS, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                TASKS.ID, new JooqField(IdFieldFactory.class, true),
                                TASKS.TITLE, new JooqField(TextFieldFactory.class, true, true, Validation.Builder.of().withMaxLength(255).build()),
                                TASKS.DESCRIPTION, new JooqField(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                TASKS.ASSIGNED_TO, new JooqField(ReferenceFieldFactory.class, TASKS.ID, Users.USERS.USERNAME, Users.USERS, List.of("username")) /* 1:1 Relation */,
                                TASKS.STATUS, new JooqField(SelectFieldFactory.class, "task-status"),
                                TASKS.DUE_DATE, JooqField.of(DateFieldFactory.class).withReadOnlyForRoles("developer").build(),
                                TASKS.CREATED_AT, new JooqField(DateTimePickerFactory.class),
                                TASKS.UPDATED_AT, new JooqField(DateTimePickerFactory.class)))
                        .build(),
                TASK_HAS_TASK, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                TASK_HAS_TASK.TASK_ID, new JooqField(IdFieldFactory.class),
                                TASK_HAS_TASK.RELATED_TASK_ID, new JooqField(IdFieldFactory.class)))
                        .build(),
                TASK_COMMENTS, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                TASK_COMMENTS.ID, new JooqField(IdFieldFactory.class, true),
                                TASK_COMMENTS.COMMENT_TEXT, new JooqField(TextAreaFieldFactory.class, false, false, Validation.Builder.of().withMaxLength(1000).build()),
                                TASK_COMMENTS.USER_ID, new JooqField(NumberFieldFactory.class),
                                TASK_COMMENTS.CREATED_AT, JooqField.of(DateTimePickerFactory.class).build()))
                        .build(),
                IMAGES, JooqDataStoreConfig.of(JooqDataStore.class)
                        .withFields(Map.of(
                                IMAGES.ID, new JooqField(IdFieldFactory.class, true),
                                IMAGES.TITLE, JooqField.of(TextFieldFactory.class)
                                        .withRequired(true)
                                        .withValidation(Validation.Builder.of().withMaxLength(255).build())
                                        .build(),
                                IMAGES.URL, JooqField.of(ImageFieldFactory.class)
                                        .withConfiguration(new ImageFieldConfiguration(FileProvider.class))
                                        .build()))
                        .build());

        Map<String, Route<Table<?>, TableField<?, ?>>> routes = Map.of(
                "projects-cards", JooqRoute.of(GridRouteFactory.class)
                        .withDefaultRoute(true)
                        .withDataStore(PROJECTS)
                        .withIconFactory(FACTORY::create)
                        .withTitle("route.projects.title-cards")
                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>, TableField<?, ?>>of(CardFactory.class)
                                .withTitleField(PROJECTS.NAME)
                                .withDescriptionField(PROJECTS.DESCRIPTION)
                                .build())
                        .withRoles(List.of("manager", "admin"))
                        .withChild(projectForm)
                        .build(),
                "projects-list", JooqRoute.of(ListRouteFactory.class)
                        .withDataStore(PROJECTS)
                        .withIconFactory(FACTORY::create)
                        .withTitle("route.projects.title-list")
                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>, TableField<?, ?>>of(CardFactory.class)
                                .withInlineEdit(true)
                                .withFilterField(PROJECTS.NAME)
                                .withChildren(
                                        new JooqFormElement(PROJECTS.NAME, "field", "route.projects.labels.name"),
                                        new JooqFormElement(PROJECTS.DESCRIPTION, "field", "route.projects.labels.description"),
                                        new JooqFormElement(PROJECTS.START_DATE, "field", "route.projects.labels.start_date"),
                                        new JooqFormElement(PROJECTS.END_DATE, "field", "route.projects.labels.end_date")
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
                                        .withConfiguration(Kanban.Builder.<Table<?>, TableField<?, ?>>of(CardFactory.class)
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
                                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>, TableField<?, ?>>of(CardFactory.class)
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
                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>, TableField<?, ?>>of(CardFactory.class)
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
                        .withConfiguration(GridOrListConfiguration.Builder.<Table<?>, TableField<?, ?>>of(CardFactory.class)
                                .withInlineEdit(true)
                                .withFilterField("title")
                                .withChildren(
                                        new JooqFormElement(IMAGES.URL, "field", "route.projects.labels.description"),
                                        new JooqFormElement(IMAGES.TITLE, "field", "route.projects.labels.name")
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
