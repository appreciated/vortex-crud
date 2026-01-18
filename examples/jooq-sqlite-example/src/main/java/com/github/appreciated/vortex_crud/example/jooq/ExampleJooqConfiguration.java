package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.GlobalRouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormSlideFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.example.jooq.custom.SimpleMapDataStore;
import com.github.appreciated.vortex_crud.example.jooq.view.CustomView;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.*;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.github.appreciated.vortex_crud.security.core.strategy.JoinTableRoleResolutionStrategy;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinServletRequest;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.core.config.model.AuditingAction.*;
import static com.github.appreciated.vortex_crud.example.jooq.Status.*;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Documents.DOCUMENTS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Images.IMAGES;
import static com.github.appreciated.vortex_crud.jooq.models.tables.ProjectTags.PROJECT_TAGS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Projects.PROJECTS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Roles.ROLES;
import static com.github.appreciated.vortex_crud.jooq.models.tables.TaskComments.TASK_COMMENTS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.TaskHasTask.TASK_HAS_TASK;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Tasks.TASKS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.UserRoles.USER_ROLES;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Users.USERS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.Videos.VIDEOS;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ExampleJooqConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public ExampleJooqConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public com.github.appreciated.vortex_crud.core.config.model.Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Data Stores & Configs
        var usersConfig = JooqDataStoreConfig.builder(USERS, dsl)
                .autoMapFields()
                .withField(USERS.USERNAME, JooqEmailField.builder().build())
                .withField(USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build())
                .build();
        var usersStore = usersConfig.dataStoreInstance();

        var projectsConfig = JooqDataStoreConfig.builder(PROJECTS, dsl)
                .autoMapFields()
                .withField(PROJECTS.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build())
                .withField(PROJECTS.DESCRIPTION, JooqMarkDownField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build())
                .withField(PROJECTS.TAGS_MULTI, JooqMultiSelectValueField.builder().values("project-tags").build())
                .build();

        var tasksConfig = JooqDataStoreConfig.builder(TASKS, dsl)
                .autoMapFields()
                .withField(TASKS.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build())
                .withField(TASKS.DESCRIPTION, JooqTextAreaField.builder().required(false).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build())
                .withField(TASKS.ASSIGNED_TO, JooqReferenceField.builder().dataStore(usersStore).field(TASKS.ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build())
                .withField(TASKS.STATUS, JooqSelectField.builder().values("task-status").build())
                .build();

        var taskHasTaskConfig = JooqDataStoreConfig.builder(TASK_HAS_TASK, dsl)
                .autoMapFields()
                .build();

        var commentsConfig = JooqDataStoreConfig.builder(TASK_COMMENTS, dsl)
                .autoMapFields()
                .withField(TASK_COMMENTS.COMMENT_TEXT, JooqTextAreaField.builder().required(false).validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build())
                .build();

        var imagesConfig = JooqDataStoreConfig.builder(IMAGES, dsl)
                .autoMapFields()
                .withField(IMAGES.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build())
                .withField(IMAGES.URL, JooqImageField.builder().resourceProvider(new LocalImageResourceProvider()).build())
                .build();

        var videosConfig = JooqDataStoreConfig.builder(VIDEOS, dsl)
                .autoMapFields()
                .withField(VIDEOS.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build())
                .withField(VIDEOS.URL, JooqVideoField.builder().resourceProvider(new LocalVideoResourceProvider()).build())
                .build();

        var documentsConfig = JooqDataStoreConfig.builder(DOCUMENTS, dsl)
                .autoMapFields()
                .withField(DOCUMENTS.TITLE, JooqTextField.builder().required(true).build())
                .withField(DOCUMENTS.PDF, JooqPdfField.builder().resourceProvider(new LocalPdfResourceProvider()).build())
                .build();

        var projectTagsConfig = JooqDataStoreConfig.builder(PROJECT_TAGS, dsl)
                .autoMapFields()
                .build();

        var rolesConfig = JooqDataStoreConfig.builder(ROLES, dsl).autoMapFields().build();
        var userRolesConfig = JooqDataStoreConfig.builder(USER_ROLES, dsl).autoMapFields().build();

        // Custom DataStore
        SimpleMapDataStore notesStore = new SimpleMapDataStore();

        // Custom in-memory data store configuration
        var notesConfig = DataStoreConfig.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                .factory(notesStore)
                .dataStoreInstance(notesStore)
                .fields(Map.of(
                        "title", TextField.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder().build(),
                        "content", TextAreaField.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder().build()
                ))
                .build();

        var taskForm = JooqFormRoute.builder()
                .titleField(TASKS.TITLE)
                .fields(List.of(
                        JooqFormElement.of(TASKS.TITLE, "route.tasks.labels.title").build(),
                        JooqFormElement.of(TASKS.DESCRIPTION, "route.tasks.labels.description").build(),
                        JooqFormElement.of(TASKS.STATUS, "route.tasks.labels.status").build(),
                        JooqFormElement.of(TASKS.DUE_DATE, "route.tasks.labels.due_date").build(),
                        JooqFormElement.of(TASKS.ASSIGNED_TO, "route.tasks.labels.assigned_to").build(),
                        JooqCollection.builder()
                                .label("route.tasks.labels.comments")
                                .field(TASK_COMMENTS.COMMENT_TEXT)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(commentsConfig)
                                .oneToMany(new JooqOneToMany(TASK_COMMENTS.TASK_ID))
                                .children(List.of(TASK_COMMENTS.COMMENT_TEXT))
                                .emptyMessage("route.tasks.labels.comments-empty-message")
                                .form(JooqFormRoute.builder()
                                        .titleField(TASK_COMMENTS.COMMENT_TEXT)
                                        .fields(List.of(
                                                JooqFormElement.of(TASK_COMMENTS.COMMENT_TEXT, "route.tasks.labels.comment").build()
                                        ))
                                        .build()
                                ).build(),
                        JooqCollection.builder()
                                .label("route.tasks.labels.related-tasks")
                                .field(TASKS.TITLE)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new ConnectDialogFactory<>())
                                .dataStoreConfig(tasksConfig)
                                .manyToMany(new JooqManyToMany(
                                        TASK_HAS_TASK.TASK_ID,
                                        TASK_HAS_TASK.RELATED_TASK_ID,
                                        TASKS.ID,
                                        TASK_HAS_TASK))
                                .children(List.of(TASKS.TITLE))
                                .emptyMessage("route.tasks.labels.related-tasks-empty-message")
                                .titleField(TASKS.TITLE)
                                .build())
                )
                .build();

        var projectForm = JooqFormRoute.builder()
                .titleField(PROJECTS.NAME)
                .fields(List.of(
                        JooqFormElement.of(PROJECTS.NAME, "route.projects.labels.name").build(),
                        JooqFormElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description").build(),
                        JooqFormElement.of(PROJECTS.BUDGET, "Budget").build(),
                        JooqFormElement.of(PROJECTS.TAGS_MULTI, "Tags (MultiSelect)").build(),
                        JooqFormElement.of(PROJECTS.ACTIVE, "Active").build(),
                        JooqCollection.builder()
                                .label("Tags (Collection)")
                                .field(PROJECT_TAGS.TAG)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(projectTagsConfig)
                                .oneToMany(new JooqOneToMany(PROJECT_TAGS.PROJECT_ID))
                                .children(List.of(PROJECT_TAGS.TAG))
                                .emptyMessage("No tags")
                                .form(JooqFormRoute.builder()
                                        .titleField(PROJECT_TAGS.TAG)
                                        .fields(List.of(JooqFormElement.of(PROJECT_TAGS.TAG, "Tag").build()))
                                        .build())
                                .build(),
                        JooqFormElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date").build(),
                        JooqFormElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date").build()
                ))
                .build();

        var imageForm = JooqFormRoute.builder()
                .titleField(IMAGES.TITLE)
                .fields(List.of(
                        JooqFormElement.of(IMAGES.TITLE, "route.images.labels.title").build(),
                        JooqFormElement.of(IMAGES.URL, "route.images.labels.image").build()
                ))
                .build();

        var documentForm = JooqFormRoute.builder()
                .titleField(DOCUMENTS.TITLE)
                .fields(List.of(
                        JooqFormElement.of(DOCUMENTS.TITLE, "Title").build(),
                        JooqFormElement.of(DOCUMENTS.PDF, "PDF").build()
                ))
                .build();

        var imageSlideForm = JooqFormRoute.builder()
                .dialogFactory(new FormSlideFactory<>())
                .titleField(IMAGES.TITLE)
                .fields(List.of(
                                JooqFormElement.of(IMAGES.TITLE, "route.images.labels.title").build(),
                                JooqFormElement.of(IMAGES.URL, "route.images.labels.image").build()
                        )
                )
                .build();

        var videoForm = JooqFormRoute.builder()
                .titleField(VIDEOS.TITLE)
                .fields(List.of(
                        JooqFormElement.of(VIDEOS.TITLE, "route.videos.labels.title").build(),
                        JooqFormElement.of(VIDEOS.URL, "route.videos.labels.video").build()
                ))
                .build();

        LinkedHashMap<Status, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put(TODO, "selects.task-status.todo");
        taskStatuses.put(OPEN, "selects.task-status.open");
        taskStatuses.put(WORK_IN_PROGRESS, "selects.task-status.progress");
        taskStatuses.put(CLOSED, "selects.task-status.closed");

        LinkedHashMap<String, String> projectTags = new LinkedHashMap<>();
        projectTags.put("tag1", "Tag 1");
        projectTags.put("tag2", "Tag 2");
        projectTags.put("tag3", "Tag 3");

        return JooqApplication.jooqBuilder()
                .applicationName("application.name")
                .i18nBundlePrefix("some_i18n")
                .identityAndAccessManagement(
                        LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                .dataStoreConfig(usersConfig)
                                .roleResolutionStrategy(new JoinTableRoleResolutionStrategy<>(
                                        userRolesConfig.dataStoreInstance(),
                                        rolesConfig.dataStoreInstance(),
                                        USER_ROLES.USER_ID,
                                        USER_ROLES.ROLE_ID,
                                        ROLES.NAME,
                                        USERS.ID
                                ))
                                .availableRoles(Roles.builder().roles(List.of("admin", "viewer", "guest")).build())
                                .defaultReadRoles(List.of("viewer"))
                                .defaultWriteRoles(List.of("admin"))
                                .signUpEnabled(true)
                                .loginView(LoginView.class)
                                .signUpView(SignUpView.class)
                                .username(JooqFormElement.of(USERS.USERNAME, "route.projects.labels.name").build())
                                .password(JooqFormElement.of(USERS.PASSWORD_HASH, "route.projects.labels.password").build())
                                .signUpFields(List.of(
                                        JooqFormElement.of(USERS.FIRST_NAME, "route.profile.labels.first_name").build(),
                                        JooqFormElement.of(USERS.LAST_NAME, "route.profile.labels.last_name").build()
                                ))
                                .build()
                )
                .route("search", SearchRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .title("route.search.title")
                        .iconFactory(SEARCH::create)
                        .build())
                .route("projects-cards", JooqGridRoute.builder()
                        .defaultRoute(true)
                        .dataStoreConfig(projectsConfig)
                        .iconFactory(FACTORY::create)
                        .title("route.projects.title-cards")
                        .titleField(PROJECTS.NAME)
                        .descriptionField(PROJECTS.DESCRIPTION)
                        .writeRoles(List.of("admin", "manager"))
                        .routeActions(List.of(
                                GlobalRouteAction.<TableField<?, ?>, TableRecord<?>>builder()
                                        .componentFactory(() -> new Button("Global Action"))
                                        .handler((context) -> Notification.show("Global Action Clicked"))
                                        .build()
                        ))
                        .form(projectForm)
                        .build())
                .route("projects-list", JooqListRoute.builder()
                        .dataStoreConfig(projectsConfig)
                        .iconFactory(FACTORY::create)
                        .title("route.projects.title-list")
                        .filterField(PROJECTS.NAME)
                        .columns(List.of(
                                JooqFormElement.of(PROJECTS.NAME, "route.projects.labels.name").build(),
                                JooqFormElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description").build(),
                                JooqFormElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date").build(),
                                JooqFormElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date").build()
                        ))
                        .writeRoles(List.of("admin", "manager", "editor"))
                        .form(projectForm)
                        .build())
                .route("open-tasks", JooqKanbanRoute.builder()
                        .iconFactory(VaadinIcon.TASKS::create)
                        .dataStoreConfig(tasksConfig)
                        .title("route.open-tasks.title")
                        .titleField(TASKS.TITLE)
                        .descriptionField(TASKS.DESCRIPTION)
                        .columnField(TASKS.STATUS)
                        .rowIndexField(TASKS.ROW_INDEX)
                        .filterField(TASKS.TITLE)
                        .writeRoles(List.of("admin", "manager", "editor", "viewer"))
                        .form(taskForm)
                        .build())
                .route("done-tasks", JooqMasterDetailRoute.builder()
                        .iconFactory(CHECK_CIRCLE::create)
                        .dataStoreConfig(tasksConfig)
                        .title("route.done-tasks.title")
                        .titleField(TASKS.TITLE)
                        .descriptionField(TASKS.DESCRIPTION)
                        .writeRoles(List.of("admin", "manager"))
                        .form(taskForm)
                        .build())
                .route("images-grid", JooqGridRoute.builder()
                        .dataStoreConfig(imagesConfig)
                        .iconFactory(CAMERA::create)
                        .title("route.images-cards")
                        .titleField(IMAGES.TITLE)
                        .imageField(IMAGES.URL)
                        .resourceProvider(new LocalImageResourceProvider())
                        .writeRoles(List.of("admin"))
                        .form(imageForm)
                        .build())
                .route("images-list", JooqListRoute.builder()
                        .dataStoreConfig(imagesConfig)
                        .iconFactory(CAMERA::create)
                        .title("route.images-list")
                        .inlineEdit(true)
                        .filterField(IMAGES.TITLE)
                        .columns(List.of(
                                JooqFormElement.of(IMAGES.URL, "route.images.labels.image").build(),
                                JooqFormElement.of(IMAGES.TITLE, "route.images.labels.title").build()
                        ))
                        .writeRoles(List.of("admin"))
                        .form(imageForm)
                        .build())
                .route("images-slide", JooqGridRoute.builder()
                        .dataStoreConfig(imagesConfig)
                        .iconFactory(CAMERA::create)
                        .title("route.images-cards")
                        .titleField(IMAGES.TITLE)
                        .imageField(IMAGES.URL)
                        .resourceProvider(new LocalImageResourceProvider())
                        .writeRoles(List.of("admin"))
                        .form(imageSlideForm)
                        .build())
                .route("videos-grid", JooqGridRoute.builder()
                        .dataStoreConfig(videosConfig)
                        .iconFactory(MOVIE::create)
                        .title("route.videos.title-cards")
                        .titleField(VIDEOS.TITLE)
                        .writeRoles(List.of("admin"))
                        .form(videoForm)
                        .build())
                .route("videos-list", JooqListRoute.builder()
                        .dataStoreConfig(videosConfig)
                        .iconFactory(MOVIE::create)
                        .title("route.videos.title-list")
                        .inlineEdit(true)
                        .filterField(VIDEOS.TITLE)
                        .columns(List.of(
                                JooqFormElement.of(VIDEOS.TITLE, "route.videos.labels.title").build(),
                                JooqFormElement.of(VIDEOS.URL, "route.videos.labels.video").build()
                        ))
                        .writeRoles(List.of("admin"))
                        .form(videoForm)
                        .build())
                .route("submenu", JooqSubmenuRoute.builder()
                        .iconFactory(MENU::create)
                        .dataStoreConfig(projectsConfig)
                        .title("route.submenu.title")
                        .routes(Map.of(
                                "project-form", projectForm,
                                "image-form", imageForm))
                        .build())
                .route("profile", JooqSingleFormRoute.builder()
                        .iconFactory(USER::create)
                        .dataStoreConfig(usersConfig)
                        .title("route.profile.title")
                        .entityFilterField(USERS.USERNAME)
                        .entityFilterValueProvider(() -> {
                            // Get current user from security context
                            VaadinServletRequest request = VaadinServletRequest.getCurrent();
                            return request != null && request.getUserPrincipal() != null
                                    ? request.getUserPrincipal().getName()
                                    : null;
                        })
                        .titleField(USERS.USERNAME)
                        .fields(List.of(
                                JooqFormElement.of(USERS.USERNAME, "route.profile.labels.username").build(),
                                JooqFormElement.of(USERS.FIRST_NAME, "route.profile.labels.first_name").build(),
                                JooqFormElement.of(USERS.LAST_NAME, "route.profile.labels.last_name").build(),
                                JooqFormElement.of(USERS.CREATED_AT, "route.profile.labels.created_at").build()
                        ))
                        .build())
                .route("calendar", JooqCalendarRoute.builder()
                        .title("route.calendar.title")
                        .iconFactory(CALENDAR::create)
                        .dataStoreConfig(projectsConfig)
                        .titleField(PROJECTS.NAME)
                        .startDateField(PROJECTS.START_DATE)
                        .endDateField(PROJECTS.END_DATE)
                        .descriptionField(PROJECTS.DESCRIPTION)
                        .build())
                .route("notes", GridRoute.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                        .dataStoreConfig(notesConfig)
                        .iconFactory(NOTEBOOK::create)
                        .title("route.notes.title")
                        .titleField("title")
                        .descriptionField("content")
                        .form(FormRoute.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                                .titleField("title")
                                .fields(List.of(
                                        FormElement.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                                                .field("title")
                                                .label("Title")
                                                .build(),
                                        FormElement.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                                                .field("content")
                                                .label("Content")
                                                .build()
                                ))
                                .build())
                        .build())
                .route("custom", CustomRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .title("route.custom.title")
                        .iconFactory(CODE::create)
                        .componentClass(CustomView.class)
                        .build())
                .route("documents", JooqGridRoute.builder()
                        .dataStoreConfig(documentsConfig)
                        .iconFactory(FILE::create)
                        .title("route.documents.title")
                        .titleField(DOCUMENTS.TITLE)
                        .form(documentForm)
                        .build())
                .route("filtered-grid", JooqGridRoute.builder()
                        .dataStoreConfig(imagesConfig)
                        .iconFactory(FILTER::create)
                        .title("route.filtered-grid.title")
                        .titleField(IMAGES.TITLE)
                        .imageField(IMAGES.URL)
                        .resourceProvider(new LocalImageResourceProvider())
                        .filter(StaticRouteFilter.<TableField<?, ?>>builder()
                                .field(IMAGES.TITLE)
                                .filterValue("Red")
                                .build())
                        .writeRoles(List.of("admin"))
                        .form(imageForm)
                        .build())
                .versioning(JooqVersioning.builder().dataStores(List.of(PROJECTS, TASKS, TASK_COMMENTS)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(Selects.builder()
                        .configs(Map.of("task-status", taskStatuses, "project-tags", projectTags))
                        .build())
                .build();
    }
}
