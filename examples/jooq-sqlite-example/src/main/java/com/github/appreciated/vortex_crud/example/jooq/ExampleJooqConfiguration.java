package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.fields.*;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormSlideRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.core.config.model.AuditingAction.*;
import static com.github.appreciated.vortex_crud.example.jooq.Status.*;
import static com.github.appreciated.vortex_crud.jooq.models.Tables.USERS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Images.IMAGES;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Projects.PROJECTS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.TaskComments.TASK_COMMENTS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.TaskHasTask.TASK_HAS_TASK;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Tasks.TASKS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Videos.VIDEOS;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ExampleJooqConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.ofEntries(
                Map.entry(PROJECTS, JooqDataStoreConfig.of(PROJECTS)
                        .withFields(Map.of(
                                PROJECTS.ID, new IdField<>(),
                                PROJECTS.NAME, new TextField<>(true, TextFieldValidation.of().withMaxLength(255).build()),
                                PROJECTS.DESCRIPTION, new TextAreaField<>(false, TextFieldValidation.of().withMaxLength(500).build()),
                                PROJECTS.START_DATE, new DateField<>(),
                                PROJECTS.END_DATE, new DateField<>(),
                                PROJECTS.CREATED_AT, new DateTimePickerField<>(),
                                PROJECTS.UPDATED_AT, new DateTimePickerField<>()))
                        .build()),
                Map.entry(TASKS, JooqDataStoreConfig.of(TASKS)
                        .withFields(Map.of(
                                TASKS.ID, new IdField<>(),
                                TASKS.TITLE, new TextField<>(true, TextFieldValidation.of().withMaxLength(255).build()),
                                TASKS.DESCRIPTION, new TextAreaField<>(false, TextFieldValidation.of().withMaxLength(1000).build()),
                                TASKS.ASSIGNED_TO, new ReferenceField<>(USERS, TASKS.ID, USERS.USERNAME, List.of(USERS.USERNAME)) /* 1:1 Relation */,
                                TASKS.STATUS, new SelectField<>("task-status"),
                                TASKS.DUE_DATE, new DateField<>(), //.withReadOnlyForRoles("developer").build(),
                                TASKS.CREATED_AT, new DateTimePickerField<>(),
                                TASKS.UPDATED_AT, new DateTimePickerField<>()))
                        .build()),
                Map.entry(TASK_HAS_TASK, JooqDataStoreConfig.of(TASK_HAS_TASK)
                        .withFields(Map.of(
                                TASK_HAS_TASK.TASK_ID, new IdField<>(),
                                TASK_HAS_TASK.RELATED_TASK_ID, new IdField<>()))
                        .build()),
                Map.entry(TASK_COMMENTS, JooqDataStoreConfig.of(TASK_COMMENTS)
                        .withFields(Map.of(
                                TASK_COMMENTS.ID, new IdField<>(),
                                TASK_COMMENTS.COMMENT_TEXT, new TextAreaField<>(false, TextFieldValidation.of().withMaxLength(1000).build()),
                                TASK_COMMENTS.USER_ID, new DoubleField<>(),
                                TASK_COMMENTS.CREATED_AT, new DateTimePickerField<>()))
                        .build()),
                Map.entry(IMAGES, JooqDataStoreConfig.of(IMAGES)
                        .withFields(Map.of(
                                IMAGES.ID, new IdField<>(),
                                IMAGES.TITLE, new TextField<>(true, TextFieldValidation.of().withMaxLength(255).build()),
                                IMAGES.URL, new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class))
                        ))
                        .build()),
                Map.entry(VIDEOS, JooqDataStoreConfig.of(VIDEOS)
                        .withFields(Map.of(
                                VIDEOS.ID, new IdField<>(),
                                VIDEOS.TITLE, new TextField<>(true, TextFieldValidation.of().withMaxLength(255).build()),
                                VIDEOS.URL, new VideoField<>(new VideoFieldRendererConfiguration<>(LocalImageResourceProvider.class))
                        ))
                        .build()),
                Map.entry(USERS,
                        JooqDataStoreConfig.of(USERS)
                                .withFields(Map.of(
                                        USERS.USERNAME, new EmailField<>(),
                                        USERS.PASSWORD_HASH, new PasswordField<>(true, TextFieldValidation.of().withMaxLength(255).build())
                                ))
                                .build())
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(TASKS)
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(TASKS.TITLE)
                        .withChildren(
                                new JooqFieldElement(TASKS.TITLE, "route.tasks.labels.title"),
                                new JooqFieldElement(TASKS.DESCRIPTION, "route.tasks.labels.description"),
                                new JooqFieldElement(TASKS.STATUS, "route.tasks.labels.status"),
                                new JooqFieldElement(TASKS.DUE_DATE, "route.tasks.labels.due_date"),
                                new JooqFieldElement(TASKS.ASSIGNED_TO, "route.tasks.labels.assigned_to"),
                                JooqCollectionElement.of("route.tasks.labels.comments")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(JooqCollection.of(FormDialogFactory.class)
                                                .withData(JooqCollectionConfiguration.of(TASK_COMMENTS)
                                                        .withOneToMany(new JooqOneToMany(TASK_COMMENTS.TASK_ID))
                                                        .withChildren(TASK_COMMENTS.COMMENT_TEXT)
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.comments-empty-message")
                                                .withChild(JooqRouteRenderer.of(FormRouteFactory.class)
                                                        .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                                                                .withChildren(
                                                                        new JooqFieldElement(TASK_COMMENTS.COMMENT_TEXT, "route.tasks.labels.comment")
                                                                )
                                                                .build())
                                                        .build())
                                                .build())
                                        .build(),
                                JooqCollectionElement.of("route.tasks.labels.related-tasks")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(JooqCollection.of(ConnectDialogFactory.class)
                                                .withData(JooqCollectionConfiguration.of(TASKS)
                                                        .withManyToMany(new JooqManyToMany(
                                                                TASK_HAS_TASK.TASK_ID,
                                                                TASK_HAS_TASK.RELATED_TASK_ID,
                                                                TASKS.ID,
                                                                TASK_HAS_TASK))
                                                        .withChildren(TASKS.TITLE)
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.related-tasks-empty-message")
                                                .withConfiguration(new CollectionConfig<TableField<?, ?>>(TASKS.TITLE))
                                                .build())
                                        .build()
                        )
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> projectForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(PROJECTS)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(PROJECTS.NAME)
                        .withChildren(
                                new JooqFieldElement(PROJECTS.NAME, "route.projects.labels.name"),
                                new JooqFieldElement(PROJECTS.DESCRIPTION, "route.projects.labels.description"),
                                new JooqFieldElement(PROJECTS.START_DATE, "route.projects.labels.start_date"),
                                new JooqFieldElement(PROJECTS.END_DATE, "route.projects.labels.end_date")
                        )
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(IMAGES)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(IMAGES.TITLE)
                        .withChildren(
                                new JooqFieldElement(IMAGES.TITLE, "route.images.labels.title"),
                                new JooqFieldElement(IMAGES.URL, "route.images.labels.image")
                        )
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageSlideForm = JooqRouteRenderer.of(FormSlideRouteFactory.class)
                .withDataStore(IMAGES)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(IMAGES.TITLE)
                        .withChildren(
                                new JooqFieldElement(IMAGES.TITLE, "route.images.labels.title"),
                                new JooqFieldElement(IMAGES.URL, "route.images.labels.image")
                        )
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> videoForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(VIDEOS)
                .withTitle("route.videos.title-cards")
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(VIDEOS.TITLE)
                        .withChildren(
                                new JooqFieldElement(VIDEOS.TITLE, "route.videos.labels.title"),
                                new JooqFieldElement(VIDEOS.URL, "route.videos.labels.video")
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JooqRouteRenderer.of(GridRouteFactory.class)
                .withDefaultRoute(true)
                .withDataStore(PROJECTS)
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField(PROJECTS.NAME)
                        .withDescriptionField(PROJECTS.DESCRIPTION)
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(projectForm)
                .build());
        routes.put("projects-list", JooqRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(PROJECTS)
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-list")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField(PROJECTS.NAME)
                        .withChildren(
                                new JooqFieldElement(PROJECTS.NAME, "route.projects.labels.name"),
                                new JooqFieldElement(PROJECTS.DESCRIPTION, "route.projects.labels.description"),
                                new JooqFieldElement(PROJECTS.START_DATE, "route.projects.labels.start_date"),
                                new JooqFieldElement(PROJECTS.END_DATE, "route.projects.labels.end_date")
                        )
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(projectForm)
                .build());
        routes.put("open-tasks", JooqRouteRenderer.of(KanbanDetailFactory.class)
                .withIconFactory(VaadinIcon.TASKS::create)
                .withDataStore(TASKS)
                .withTitle("route.open-tasks.title")
                .withConfiguration(JooqKanban.of(CardFactory.class)
                        .withTitleField(TASKS.TITLE)
                        .withDescriptionField(TASKS.DESCRIPTION)
                        .withColumnField(TASKS.STATUS)
                        .withRowIndexField(TASKS.ROW_INDEX)
                        .withFilterField(TASKS.TITLE)
                        .build())
                .withChild(taskForm)
                .build());
        routes.put("done-tasks", JooqRouteRenderer.of(MasterDetailRouteFactory.class)
                .withIconFactory(CHECK_CIRCLE::create)
                .withDataStore(TASKS)
                .withTitle("route.done-tasks.title")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField(TASKS.TITLE)
                        .withDescriptionField(TASKS.DESCRIPTION)
                        .build())
                .withChild(taskForm)
                .build());
        routes.put("images-grid", JooqRouteRenderer.of(GridRouteFactory.class)
                .withDataStore(IMAGES)
                .withIconFactory(CAMERA::create)
                .withTitle("route.images-cards")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField(IMAGES.TITLE)
                        .withImageField(IMAGES.URL)
                        .withImageFactory(LocalImageResourceProvider.class)
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(imageForm)
                .build());
        routes.put("images-list", JooqRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(IMAGES)
                .withIconFactory(CAMERA::create)
                .withTitle("route.images-list")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField(IMAGES.TITLE)
                        .withChildren(
                                new JooqFieldElement(IMAGES.URL, "route.projects.labels.description"),
                                new JooqFieldElement(IMAGES.TITLE, "route.projects.labels.name")
                        )
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(imageForm)
                .build());

        routes.put("images-slide", JooqRouteRenderer.of(GridRouteFactory.class)
                .withDataStore(IMAGES)
                .withIconFactory(CAMERA::create)
                .withTitle("route.images-cards")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField(IMAGES.TITLE)
                        .withImageField(IMAGES.URL)
                        .withImageFactory(LocalImageResourceProvider.class)
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(imageSlideForm)
                .build());

        routes.put("videos-grid", JooqRouteRenderer.of(GridRouteFactory.class)
                .withDataStore(VIDEOS)
                .withIconFactory(MOVIE::create)
                .withTitle("route.videos.title-cards")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField(VIDEOS.TITLE)
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(videoForm)
                .build());

        routes.put("videos-list", JooqRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(VIDEOS)
                .withIconFactory(MOVIE::create)
                .withTitle("route.videos.title-list")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField(VIDEOS.TITLE)
                        .withChildren(
                                new JooqFieldElement(VIDEOS.TITLE, "route.videos.labels.title"),
                                new JooqFieldElement(VIDEOS.URL, "route.videos.labels.video")
                        )
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(videoForm)
                .build());

        routes.put("submenu", JooqRouteRenderer.of(SubmenuRouteFactory.class)
                .withIconFactory(MENU::create)
                .withDataStore(PROJECTS)
                .withTitle("route.submenu.title")
                .withChildrenMap(Map.of(
                        "project-form", projectForm,
                        "image-form", imageForm))
                .build());

        LinkedHashMap<Status, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put(TODO, "selects.task-status.todo");
        taskStatuses.put(OPEN, "selects.task-status.open");
        taskStatuses.put(WORK_IN_PROGRESS, "selects.task-status.progress");
        taskStatuses.put(CLOSED, "selects.task-status.closed");

        return JooqApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("some_i18n")
                .withIdentityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>of(USERS)
                        .withLoginView(LoginView.class)
                        .withSignUpView(SignUpView.class)
                        .withRoles(Roles.of().withRoles(List.of("manager", "admin")).build())
                        .withSignUp(true)
                        .withUsername(new JooqFieldElement(USERS.USERNAME, "route.projects.labels.name"))
                        .withPassword(new JooqFieldElement(USERS.PASSWORD_HASH, "route.projects.labels.name"))
                        .withSignUpFields(new JooqFieldElement(USERS.CREATED_AT, "route.projects.labels.description"))
                        .build())
                .withRoutes(routes)
                .withVersioning(JooqVersioning.of().withDataStores(PROJECTS, TASKS, TASK_COMMENTS).build())
                .withAuditing(Auditing.of().withActions(CREATE, UPDATE, DELETE, LOGIN, LOGOUT).build())
                .withSelects(Selects.of()
                        .withConfigs(Map.of("task-status", taskStatuses))
                        .build())
                .withDataStores(dataStores)
                .build();
    }
}
