package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.fields.*;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
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
import static com.github.appreciated.vortex_crud.jooq.models.tables.Images.IMAGES;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Projects.PROJECTS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.TaskComments.TASK_COMMENTS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.TaskHasTask.TASK_HAS_TASK;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Videos.VIDEOS;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ExampleJooqConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.ofEntries(
                Map.entry(PROJECTS, JooqDataStoreConfig.of(PROJECTS)
                        .fields(Map.of(
                                PROJECTS.ID, new IdField<>(),
                                PROJECTS.NAME, new TextField<>(true, TextFieldValidation.builder().maxLength(255).build()),
                                PROJECTS.DESCRIPTION, new TextAreaField<>(false, TextFieldValidation.builder().maxLength(500).build()),
                                PROJECTS.START_DATE, new DateField<>(),
                                PROJECTS.END_DATE, new DateField<>(),
                                PROJECTS.CREATED_AT, new DateTimePickerField<>(),
                                PROJECTS.UPDATED_AT, new DateTimePickerField<>()))
                        .build()),
                Map.entry(TASKS, JooqDataStoreConfig.of(TASKS)
                        .fields(Map.of(
                                TASKS.ID, new IdField<>(),
                                TASKS.TITLE, new TextField<>(true, TextFieldValidation.builder().maxLength(255).build()),
                                TASKS.DESCRIPTION, new TextAreaField<>(false, TextFieldValidation.builder().maxLength(1000).build()),
                                TASKS.ASSIGNED_TO, new ReferenceField<>(USERS, TASKS.ID, USERS.USERNAME, List.of(USERS.USERNAME)) /* 1:1 Relation */,
                                TASKS.STATUS, new SelectField<>("task-status"),
                                TASKS.DUE_DATE, new DateField<>(), //.readOnlyForRoles("developer").build(),
                                TASKS.CREATED_AT, new DateTimePickerField<>(),
                                TASKS.UPDATED_AT, new DateTimePickerField<>()))
                        .build()),
                Map.entry(TASK_HAS_TASK, JooqDataStoreConfig.of(TASK_HAS_TASK)
                        .fields(Map.of(
                                TASK_HAS_TASK.TASK_ID, new IdField<>(),
                                TASK_HAS_TASK.RELATED_TASK_ID, new IdField<>()))
                        .build()),
                Map.entry(TASK_COMMENTS, JooqDataStoreConfig.of(TASK_COMMENTS)
                        .fields(Map.of(
                                TASK_COMMENTS.ID, new IdField<>(),
                                TASK_COMMENTS.COMMENT_TEXT, new TextAreaField<>(false, TextFieldValidation.builder().maxLength(1000).build()),
                                TASK_COMMENTS.USER_ID, new DoubleField<>(),
                                TASK_COMMENTS.CREATED_AT, new DateTimePickerField<>()))
                        .build()),
                Map.entry(IMAGES, JooqDataStoreConfig.of(IMAGES)
                        .fields(Map.of(
                                IMAGES.ID, new IdField<>(),
                                IMAGES.TITLE, new TextField<>(true, TextFieldValidation.builder().maxLength(255).build()),
                                IMAGES.URL, new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class))
                        ))
                        .build()),
                Map.entry(VIDEOS, JooqDataStoreConfig.of(VIDEOS)
                        .fields(Map.of(
                                VIDEOS.ID, new IdField<>(),
                                VIDEOS.TITLE, new TextField<>(true, TextFieldValidation.builder().maxLength(255).build()),
                                VIDEOS.URL, new VideoField<>(new VideoFieldRendererConfiguration<>(LocalVideoResourceProvider.class))
                        ))
                        .build()),
                Map.entry(USERS,
                        JooqDataStoreConfig.of(USERS)
                                .fields(Map.of(
                                        USERS.USERNAME, new EmailField<>(),
                                        USERS.PASSWORD_HASH, new PasswordField<>(true, TextFieldValidation.builder().maxLength(255).build())
                                ))
                                .build())
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStore(TASKS)
                .titleField(TASKS.TITLE)
                .children(
                        JooqFieldElement.of(TASKS.TITLE, "route.tasks.labels.title"),
                        JooqFieldElement.of(TASKS.DESCRIPTION, "route.tasks.labels.description"),
                        JooqFieldElement.of(TASKS.STATUS, "route.tasks.labels.status"),
                        JooqFieldElement.of(TASKS.DUE_DATE, "route.tasks.labels.due_date"),
                        JooqFieldElement.of(TASKS.ASSIGNED_TO, "route.tasks.labels.assigned_to"),
                        JooqCollectionElement.of("route.tasks.labels.comments")
                                .factory(ListCollectionFactory.class)
                                .configuration(JooqCollection.of(FormDialogFactory.class)
                                        .data(JooqCollectionConfiguration.of(TASK_COMMENTS)
                                                .oneToMany(new JooqOneToMany(TASK_COMMENTS.TASK_ID))
                                                .children(TASK_COMMENTS.COMMENT_TEXT))
                                        .emptyMessage("route.tasks.labels.comments-empty-message")
                                        .child(JooqFormRoute.builder()
                                                .children(
                                                        JooqFieldElement.of(TASK_COMMENTS.COMMENT_TEXT, "route.tasks.labels.comment")
                                                )))
                                .build(),
                        JooqCollectionElement.of("route.tasks.labels.related-tasks")
                                .factory(ListCollectionFactory.class)
                                .configuration(JooqCollection.of(ConnectDialogFactory.class)
                                        .data(JooqCollectionConfiguration.of(TASKS)
                                                .manyToMany(new JooqManyToMany(
                                                        TASK_HAS_TASK.TASK_ID,
                                                        TASK_HAS_TASK.RELATED_TASK_ID,
                                                        TASKS.ID,
                                                        TASK_HAS_TASK))
                                                .children(TASKS.TITLE))
                                        .emptyMessage("route.tasks.labels.related-tasks-empty-message")
                                        .configuration(new CollectionConfig<TableField<?, ?>>(TASKS.TITLE)))
                                .build()
                )
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> projectForm = JooqFormRoute.builder()
                .dataStore(PROJECTS)
                .title("route.projects.title-cards")
                .titleField(PROJECTS.NAME)
                .children(
                        JooqFieldElement.of(PROJECTS.NAME, "route.projects.labels.name"),
                        JooqFieldElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description"),
                        JooqFieldElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date"),
                        JooqFieldElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date")
                )
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqFormRoute.builder()
                .dataStore(IMAGES)
                .title("route.projects.title-cards")
                .titleField(IMAGES.TITLE)
                .children(
                        JooqFieldElement.of(IMAGES.TITLE, "route.images.labels.title"),
                        JooqFieldElement.of(IMAGES.URL, "route.images.labels.image")
                )
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageSlideForm = JooqRouteRenderer.of(FormSlideRouteFactory.class)
                .dataStore(IMAGES)
                .title("route.projects.title-cards")
                .configuration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .titleField(IMAGES.TITLE)
                        .children(
                                JooqFieldElement.of(IMAGES.TITLE, "route.images.labels.title"),
                                JooqFieldElement.of(IMAGES.URL, "route.images.labels.image")
                        )
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> videoForm = JooqFormRoute.builder()
                .dataStore(VIDEOS)
                .title("route.videos.title-cards")
                .titleField(VIDEOS.TITLE)
                .children(
                        JooqFieldElement.of(VIDEOS.TITLE, "route.videos.labels.title"),
                        JooqFieldElement.of(VIDEOS.URL, "route.videos.labels.video")
                )
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JooqGridRoute.builder()
                .defaultRoute(true)
                .dataStore(PROJECTS)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-cards")
                .titleField(PROJECTS.NAME)
                .descriptionField(PROJECTS.DESCRIPTION)
                .roles(List.of("manager", "admin"))
                .child(projectForm)
                .build());
        routes.put("projects-list", JooqListRoute.builder()
                .dataStore(PROJECTS)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .inlineEdit(true)
                .filterField(PROJECTS.NAME)
                .children(
                        JooqFieldElement.of(PROJECTS.NAME, "route.projects.labels.name"),
                        JooqFieldElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description"),
                        JooqFieldElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date"),
                        JooqFieldElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date")
                        )
                        .build())
                .roles(List.of("manager", "admin"))
                .child(projectForm)
                .build());
        routes.put("open-tasks", JooqRouteRenderer.of(KanbanDetailFactory.class)
                .iconFactory(VaadinIcon.TASKS::create)
                .dataStore(TASKS)
                .title("route.open-tasks.title")
                .configuration(JooqKanban.of(CardFactory.class)
                        .titleField(TASKS.TITLE)
                        .descriptionField(TASKS.DESCRIPTION)
                        .columnField(TASKS.STATUS)
                        .rowIndexField(TASKS.ROW_INDEX)
                        .filterField(TASKS.TITLE)
                        .build())
                .child(taskForm)
                .build());
        routes.put("done-tasks", JooqRouteRenderer.of(MasterDetailRouteFactory.class)
                .iconFactory(CHECK_CIRCLE::create)
                .dataStore(TASKS)
                .title("route.done-tasks.title")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .titleField(TASKS.TITLE)
                        .descriptionField(TASKS.DESCRIPTION)
                        .build())
                .child(taskForm)
                .build());
        routes.put("images-grid", JooqRouteRenderer.of(GridRouteFactory.class)
                .dataStore(IMAGES)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .titleField(IMAGES.TITLE)
                        .imageField(IMAGES.URL)
                        .imageFactory(LocalImageResourceProvider.class)
                        .build())
                .roles(List.of("manager", "admin"))
                .child(imageForm)
                .build());
        routes.put("images-list", JooqRouteRenderer.of(ListRouteFactory.class)
                .dataStore(IMAGES)
                .iconFactory(CAMERA::create)
                .title("route.images-list")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .inlineEdit(true)
                        .filterField(IMAGES.TITLE)
                        .children(
                                JooqFieldElement.of(IMAGES.URL, "route.projects.labels.description"),
                                JooqFieldElement.of(IMAGES.TITLE, "route.projects.labels.name")
                        )
                        .build())
                .roles(List.of("manager", "admin"))
                .child(imageForm)
                .build());

        routes.put("images-slide", JooqRouteRenderer.of(GridRouteFactory.class)
                .dataStore(IMAGES)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .titleField(IMAGES.TITLE)
                        .imageField(IMAGES.URL)
                        .imageFactory(LocalImageResourceProvider.class)
                        .build())
                .roles(List.of("manager", "admin"))
                .child(imageSlideForm)
                .build());

        routes.put("videos-grid", JooqRouteRenderer.of(GridRouteFactory.class)
                .dataStore(VIDEOS)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-cards")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .titleField(VIDEOS.TITLE)
                        .build())
                .roles(List.of("manager", "admin"))
                .child(videoForm)
                .build());

        routes.put("videos-list", JooqRouteRenderer.of(ListRouteFactory.class)
                .dataStore(VIDEOS)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-list")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .inlineEdit(true)
                        .filterField(VIDEOS.TITLE)
                        .children(
                                JooqFieldElement.of(VIDEOS.TITLE, "route.videos.labels.title"),
                                JooqFieldElement.of(VIDEOS.URL, "route.videos.labels.video")
                        )
                        .build())
                .roles(List.of("manager", "admin"))
                .child(videoForm)
                .build());

        routes.put("submenu", JooqRouteRenderer.of(SubmenuRouteFactory.class)
                .iconFactory(MENU::create)
                .dataStore(PROJECTS)
                .title("route.submenu.title")
                .childrenMap(Map.of(
                        "project-form", projectForm,
                        "image-form", imageForm))
                .build());

        LinkedHashMap<Status, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put(TODO, "selects.task-status.todo");
        taskStatuses.put(OPEN, "selects.task-status.open");
        taskStatuses.put(WORK_IN_PROGRESS, "selects.task-status.progress");
        taskStatuses.put(CLOSED, "selects.task-status.closed");

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("some_i18n")
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>of(USERS)
                        .loginView(LoginView.class)
                        .signUpView(SignUpView.class)
                        .roles(Roles.builder().roles(List.of("manager", "admin")).build())
                        .signUp(true)
                        .username(JooqFieldElement.of(USERS.USERNAME, "route.projects.labels.name"))
                        .password(JooqFieldElement.of(USERS.PASSWORD_HASH, "route.projects.labels.name"))
                        .signUpFields(JooqFieldElement.of(USERS.CREATED_AT, "route.projects.labels.description"))
                        .build())
                .routes(routes)
                .versioning(JooqVersioning.builder().dataStores(PROJECTS, TASKS, TASK_COMMENTS).build())
                .auditing(Auditing.builder().actions(CREATE, UPDATE, DELETE, LOGIN, LOGOUT).build())
                .selects(Selects.builder()
                        .configs(Map.of("task-status", taskStatuses))
                        .build())
                .dataStores(dataStores)
                .build();
    }
}
