package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
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
import static com.github.appreciated.vortex_crud.jooq.models.tables.Tasks.TASKS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Users.USERS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Videos.VIDEOS;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ExampleJooqConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.ofEntries(
                Map.entry(PROJECTS, JooqDataStoreConfig.of(PROJECTS)
                        .fields(Map.of(
                                PROJECTS.ID, JooqIdField.builder().build(),
                                PROJECTS.NAME, JooqTextField.builder().required(true).validation(new MaxLengthTextFieldValidation(255)).build(),
                                PROJECTS.DESCRIPTION, JooqTextAreaField.builder().validation(new MaxLengthTextFieldValidation(500)).build(),
                                PROJECTS.START_DATE, JooqDateField.builder().build(),
                                PROJECTS.END_DATE, JooqDateField.builder().build(),
                                PROJECTS.CREATED_AT, JooqDateTimePickerField.builder().build(),
                                PROJECTS.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                        .build()),
                Map.entry(TASKS, JooqDataStoreConfig.of(TASKS)
                        .fields(Map.of(
                                TASKS.ID, JooqIdField.builder().build(),
                                TASKS.TITLE, JooqTextField.builder().required(true).validation(new MaxLengthTextFieldValidation(255)).build(),
                                TASKS.DESCRIPTION, JooqTextAreaField.builder().required(false).validation(new MaxLengthTextFieldValidation(255)).build(),
                                TASKS.ASSIGNED_TO, JooqReferenceField.builder().dataStore(USERS).field(TASKS.ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build() /* 1:1 Relation */,
                                TASKS.STATUS, JooqSelectField.builder().values("task-status").build(),
                                TASKS.DUE_DATE, JooqDateField.builder().build(), //.readOnlyForRoles("developer").build(),
                                TASKS.ROW_INDEX, JooqIntegerField.builder().build(),
                                TASKS.CREATED_AT, JooqDateTimePickerField.builder().build(),
                                TASKS.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                        .build()),
                Map.entry(TASK_HAS_TASK, JooqDataStoreConfig.of(TASK_HAS_TASK)
                        .fields(Map.of(
                                TASK_HAS_TASK.TASK_ID, JooqIdField.builder().build(),
                                TASK_HAS_TASK.RELATED_TASK_ID, JooqIdField.builder().build()))
                        .build()),
                Map.entry(TASK_COMMENTS, JooqDataStoreConfig.of(TASK_COMMENTS)
                        .fields(Map.of(
                                TASK_COMMENTS.ID, JooqIdField.builder().build(),
                                TASK_COMMENTS.COMMENT_TEXT, JooqTextAreaField.builder().required(false).validation(new MaxLengthTextFieldValidation(1000)).build(),
                                TASK_COMMENTS.USER_ID, JooqDoubleField.builder().build(),
                                TASK_COMMENTS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                        .build()),
                Map.entry(IMAGES, JooqDataStoreConfig.of(IMAGES)
                        .fields(Map.of(
                                IMAGES.ID, JooqIdField.builder().build(),
                                IMAGES.TITLE, JooqTextField.builder().required(true).validation(new MaxLengthTextFieldValidation(255)).build(),
                                IMAGES.URL, JooqImageField.builder().configuration(JooqImageFieldRendererConfiguration.builder().resourceProvider(LocalImageResourceProvider.class).build()).build()
                        ))
                        .build()),
                Map.entry(VIDEOS, JooqDataStoreConfig.of(VIDEOS)
                        .fields(Map.of(
                                VIDEOS.ID, JooqIdField.builder().build(),
                                VIDEOS.TITLE, JooqTextField.builder().required(true).validation(new MaxLengthTextFieldValidation(255)).build(),
                                VIDEOS.URL, JooqVideoField.builder().configuration(JooqVideoFieldRendererConfiguration.builder().resourceProvider(LocalVideoResourceProvider.class).build()).build()
                        ))
                        .build()),
                Map.entry(USERS,
                        JooqDataStoreConfig.of(USERS)
                                .fields(Map.of(
                                        USERS.ID, JooqIdField.builder().build(),
                                        USERS.USERNAME, JooqEmailField.builder().build(),
                                        USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validation(new MaxLengthTextFieldValidation(255)).build(),
                                        USERS.CREATED_AT, JooqDateTimePickerField.builder().build()
                                ))
                                .build())
        );

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStoreKey(TASKS)
                .configuration(JooqFormRendererConfiguration.builder()
                        .titleField(TASKS.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(TASKS.TITLE, "route.tasks.labels.title").build(),
                                JooqFieldElement.of(TASKS.DESCRIPTION, "route.tasks.labels.description").build(),
                                JooqFieldElement.of(TASKS.STATUS, "route.tasks.labels.status").build(),
                                JooqFieldElement.of(TASKS.DUE_DATE, "route.tasks.labels.due_date").build(),
                                JooqFieldElement.of(TASKS.ASSIGNED_TO, "route.tasks.labels.assigned_to").build(),
                                JooqCollectionElement.of("route.tasks.labels.comments")
                                        .factory((Class<? extends VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class<?>) ListCollectionFactory.class)
                                        .configuration(JooqCollection.builder((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class)FormDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(TASK_COMMENTS)
                                                        .oneToMany(new JooqOneToMany(TASK_COMMENTS.TASK_ID))
                                                        .children(List.of(TASK_COMMENTS.COMMENT_TEXT))
                                                        .build()
                                                )
                                                .emptyMessage("route.tasks.labels.comments-empty-message")
                                                .child(JooqFormRoute.builder()
                                                        .configuration(JooqFormRendererConfiguration.builder()
                                                                .titleField(TASK_COMMENTS.COMMENT_TEXT)
                                                                .children(List.of(
                                                                        JooqFieldElement.of(TASK_COMMENTS.COMMENT_TEXT, "route.tasks.labels.comment").build()
                                                                ))
                                                                .build())
                                                        .build()
                                                ).build()
                                        ).build(),
                                JooqCollectionElement.of("route.tasks.labels.related-tasks")
                                        .factory((Class<? extends VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class<?>) ListCollectionFactory.class)
                                        .configuration(JooqCollection.builder((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>)(Class) ConnectDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(TASKS)
                                                        .manyToMany(new JooqManyToMany(
                                                                TASK_HAS_TASK.TASK_ID,
                                                                TASK_HAS_TASK.RELATED_TASK_ID,
                                                                TASKS.ID,
                                                                TASK_HAS_TASK))
                                                        .children(List.of(TASKS.TITLE)).build()
                                                )
                                                .emptyMessage("route.tasks.labels.related-tasks-empty-message")
                                                .configuration(new CollectionConfig<TableField<?, ?>>(TASKS.TITLE)).build())
                                        .build())
                        )
                        .build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> projectForm = JooqFormRoute.builder()
                .dataStoreKey(PROJECTS)
                .title("route.projects.title-cards").configuration(JooqFormRendererConfiguration.builder()
                        .titleField(PROJECTS.NAME)
                        .children(List.of(
                                JooqFieldElement.of(PROJECTS.NAME, "route.projects.labels.name").build(),
                                JooqFieldElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description").build(),
                                JooqFieldElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date").build(),
                                JooqFieldElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date").build()
                        )).build()
                )
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqFormRoute.builder()
                .dataStoreKey(IMAGES)
                .title("route.projects.title-cards")
                .configuration(
                        JooqFormRendererConfiguration.builder()
                                .titleField(IMAGES.TITLE)
                                .children(List.of(
                                        JooqFieldElement.of(IMAGES.TITLE, "route.images.labels.title").build(),
                                        JooqFieldElement.of(IMAGES.URL, "route.images.labels.image").build()
                                )).build()
                )
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageSlideForm = JooqFormSlideRoute.builder()
                .dataStoreKey(IMAGES)
                .title("route.projects.title-cards")
                .configuration(
                        JooqFormRendererConfiguration.builder()
                                .titleField(IMAGES.TITLE)
                                .children(List.of(
                                                JooqFieldElement.of(IMAGES.TITLE, "route.images.labels.title").build(),
                                                JooqFieldElement.of(IMAGES.URL, "route.images.labels.image").build()
                                        )
                                )
                                .build()
                ).build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> videoForm = JooqFormRoute.builder()
                .dataStoreKey(VIDEOS)
                .title("route.videos.title-cards").configuration(
                        JooqFormRendererConfiguration.builder()
                                .titleField(VIDEOS.TITLE)
                                .children(List.of(
                                        JooqFieldElement.of(VIDEOS.TITLE, "route.videos.labels.title").build(),
                                        JooqFieldElement.of(VIDEOS.URL, "route.videos.labels.video").build()
                                ))
                                .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JooqGridRoute.builder()
                .isDefaultRoute(true)
                .dataStoreKey(PROJECTS)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(PROJECTS.NAME)
                        .descriptionField(PROJECTS.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .child(projectForm)
                .build());
        routes.put("projects-list", JooqListRoute.builder()
                .dataStoreKey(PROJECTS)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField(PROJECTS.NAME)
                        .children(List.of(
                                JooqFieldElement.of(PROJECTS.NAME, "route.projects.labels.name").build(),
                                JooqFieldElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description").build(),
                                JooqFieldElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date").build(),
                                JooqFieldElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date").build()
                        ))
                        .build())
                .writeRoles(List.of("admin", "manager", "editor"))
                .child(projectForm)
                .build());
        routes.put("open-tasks", JooqKanbanRoute.builder()
                .iconFactory(VaadinIcon.TASKS::create)
                .dataStoreKey(TASKS)
                .title("route.open-tasks.title")
                .configuration(JooqKanbanConfiguration.builder()
                        .titleField(TASKS.TITLE)
                        .descriptionField(TASKS.DESCRIPTION)
                        .columnField(TASKS.STATUS)
                        .rowIndexField(TASKS.ROW_INDEX)
                        .filterField(TASKS.TITLE)
                        .build())
                .writeRoles(List.of("admin", "manager", "editor", "viewer"))
                .child(taskForm)
                .build());
        routes.put("done-tasks", JooqMasterDetailRoute.builder()
                .iconFactory(CHECK_CIRCLE::create)
                .dataStoreKey(TASKS)
                .title("route.done-tasks.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(TASKS.TITLE)
                        .descriptionField(TASKS.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .child(taskForm)
                .build());
        routes.put("images-grid", JooqGridRoute.builder()
                .dataStoreKey(IMAGES)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(IMAGES.TITLE)
                        .imageField(IMAGES.URL)
                        .resourceProvider(LocalImageResourceProvider.class)
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageForm)
                .build());
        routes.put("images-list", JooqListRoute.builder()
                .dataStoreKey(IMAGES)
                .iconFactory(CAMERA::create)
                .title("route.images-list")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField(IMAGES.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(IMAGES.URL, "route.projects.labels.description").build(),
                                JooqFieldElement.of(IMAGES.TITLE, "route.projects.labels.name").build()
                        ))
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageForm)
                .build());

        routes.put("images-slide", JooqGridRoute.builder()
                .dataStoreKey(IMAGES)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(IMAGES.TITLE)
                        .imageField(IMAGES.URL)
                        .resourceProvider(LocalImageResourceProvider.class)
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageSlideForm)
                .build());

        routes.put("videos-grid", JooqGridRoute.builder()
                .dataStoreKey(VIDEOS)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(VIDEOS.TITLE)
                        .build())
                .writeRoles(List.of("admin"))
                .child(videoForm)
                .build());

        routes.put("videos-list", JooqListRoute.builder()
                .dataStoreKey(VIDEOS)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-list")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField(VIDEOS.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(VIDEOS.TITLE, "route.videos.labels.title").build(),
                                JooqFieldElement.of(VIDEOS.URL, "route.videos.labels.video").build()
                        ))
                        .build())
                .writeRoles(List.of("admin"))
                .child(videoForm)
                .build());

        routes.put("submenu", JooqSubmenuRoute.builder()
                .iconFactory(MENU::create)
                .dataStoreKey(PROJECTS)
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
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .repositoryKey(USERS)
                        .roles(Roles.builder().roles(List.of("admin", "manager", "editor", "viewer", "guest")).build())
                        .signUpEnabled(true)
                        .loginView(LoginView.class)
                        .signUpView(SignUpView.class)
                        .username(JooqFieldElement.of(USERS.USERNAME, "route.projects.labels.name").build())
                        .password(JooqFieldElement.of(USERS.PASSWORD_HASH, "route.projects.labels.password").build())
                        .signUpFields(List.of())
                        .rolesField(null) //TODO
                        .build())
                .routes(routes)
                .versioning(JooqVersioning.builder().dataStores(List.of(PROJECTS, TASKS, TASK_COMMENTS)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(Selects.builder()
                        .configs(Map.of("task-status", taskStatuses))
                        .build())
                .dataStores(dataStores)
                .build();
    }
}
