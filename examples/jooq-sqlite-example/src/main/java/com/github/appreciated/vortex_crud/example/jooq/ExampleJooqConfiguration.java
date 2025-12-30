package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.GlobalRouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.example.jooq.custom.SimpleMapDataStore;
import com.github.appreciated.vortex_crud.example.jooq.view.CustomView;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.*;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
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
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore<ProjectsRecord> projectsStore = new JooqDataStore<>(PROJECTS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<TasksRecord> tasksStore = new JooqDataStore<>(TASKS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<TaskHasTaskRecord> taskHasTaskStore = new JooqDataStore<>(TASK_HAS_TASK.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<TaskCommentsRecord> commentsStore = new JooqDataStore<>(TASK_COMMENTS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<ImagesRecord> imagesStore = new JooqDataStore<>(IMAGES.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<VideosRecord> videosStore = new JooqDataStore<>(VIDEOS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<UsersRecord> usersStore = new JooqDataStore<>(USERS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<DocumentsRecord> documentsStore = new JooqDataStore<>(DOCUMENTS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore<ProjectTagsRecord> projectTagsStore = new JooqDataStore<>(PROJECT_TAGS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore rolesStore = new JooqDataStore(ROLES.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore userRolesStore = new JooqDataStore(USER_ROLES.getRecordType(), dsl, new DataStoreHooks<>());

        // Custom DataStore
        SimpleMapDataStore notesStore = new SimpleMapDataStore();

        var projectsConfig = JooqDataStoreConfig.of(PROJECTS)
                .dataStoreInstance(projectsStore)
                .fields(Map.of(
                        PROJECTS.ID, JooqNumericIdField.builder().build(),
                        PROJECTS.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        PROJECTS.DESCRIPTION, JooqMarkDownField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                        PROJECTS.BUDGET, JooqBigDecimalField.builder().build(),
                        PROJECTS.TAGS_MULTI, JooqMultiSelectValueField.builder().values("project-tags").build(),
                        PROJECTS.ACTIVE, JooqCheckboxField.builder().build(),
                        PROJECTS.START_DATE, JooqDateField.builder().build(),
                        PROJECTS.END_DATE, JooqDateField.builder().build(),
                        PROJECTS.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        PROJECTS.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var usersConfig = JooqDataStoreConfig.of(USERS)
                .dataStoreInstance(usersStore)
                .fields(Map.of(
                        USERS.ID, JooqNumericIdField.builder().build(),
                        USERS.USERNAME, JooqEmailField.builder().build(),
                        USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        USERS.FIRST_NAME, JooqTextField.builder().build(),
                        USERS.LAST_NAME, JooqTextField.builder().build(),
                        USERS.CREATED_AT, JooqDateTimePickerField.builder().build()
                ))
                .build();

        var tasksConfig = JooqDataStoreConfig.of(TASKS)
                .dataStoreInstance(tasksStore)
                .fields(Map.of(
                        TASKS.ID, JooqNumericIdField.builder().build(),
                        TASKS.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        TASKS.DESCRIPTION, JooqTextAreaField.builder().required(false).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        TASKS.ASSIGNED_TO, JooqReferenceField.builder().dataStore(usersStore).field(TASKS.ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        TASKS.STATUS, JooqSelectField.builder().values("task-status").build(),
                        TASKS.DUE_DATE, JooqDateField.builder().build(),
                        TASKS.ROW_INDEX, JooqIntegerField.builder().build(),
                        TASKS.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        TASKS.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var taskHasTaskConfig = JooqDataStoreConfig.of(TASK_HAS_TASK)
                .dataStoreInstance(taskHasTaskStore)
                .fields(Map.of(
                        TASK_HAS_TASK.TASK_ID, JooqNumericIdField.builder().build(),
                        TASK_HAS_TASK.RELATED_TASK_ID, JooqNumericIdField.builder().build()))
                .build();

        var commentsConfig = JooqDataStoreConfig.of(TASK_COMMENTS)
                .dataStoreInstance(commentsStore)
                .fields(Map.of(
                        TASK_COMMENTS.ID, JooqNumericIdField.builder().build(),
                        TASK_COMMENTS.COMMENT_TEXT, JooqTextAreaField.builder().required(false).validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                        TASK_COMMENTS.USER_ID, JooqDoubleField.builder().build(),
                        TASK_COMMENTS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var imagesConfig = JooqDataStoreConfig.of(IMAGES)
                .dataStoreInstance(imagesStore)
                .fields(Map.of(
                        IMAGES.ID, JooqNumericIdField.builder().build(),
                        IMAGES.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        IMAGES.URL, JooqImageField.builder().resourceProvider(new LocalImageResourceProvider()).build()
                ))
                .build();

        var videosConfig = JooqDataStoreConfig.of(VIDEOS)
                .dataStoreInstance(videosStore)
                .fields(Map.of(
                        VIDEOS.ID, JooqNumericIdField.builder().build(),
                        VIDEOS.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        VIDEOS.URL, JooqVideoField.builder().resourceProvider(new LocalVideoResourceProvider()).build()
                ))
                .build();

        var documentsConfig = JooqDataStoreConfig.of(DOCUMENTS)
                .dataStoreInstance(documentsStore)
                .fields(Map.of(
                        DOCUMENTS.ID, JooqNumericIdField.builder().build(),
                        DOCUMENTS.TITLE, JooqTextField.builder().required(true).build(),
                        DOCUMENTS.PDF, JooqPdfField.builder().resourceProvider(new LocalPdfResourceProvider()).build()
                ))
                .build();

        var projectTagsConfig = JooqDataStoreConfig.of(PROJECT_TAGS)
                .dataStoreInstance(projectTagsStore)
                .fields(Map.of(
                        PROJECT_TAGS.ID, JooqNumericIdField.builder().build(),
                        PROJECT_TAGS.PROJECT_ID, JooqIntegerField.builder().build(),
                        PROJECT_TAGS.TAG, JooqTextField.builder().build()
                )).build();

        // Custom in-memory data store configuration
        // Using DataStoreConfig with String field names (same pattern as JPA!)
        var notesConfig = DataStoreConfig.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                .factory(notesStore)  // Use the datastore instance itself as the key!
                .dataStoreInstance(notesStore)
                .fields(Map.of(
                        "title", TextField.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder().build(),
                        "content", TextAreaField.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder().build()
                ))
                .build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStoreConfig(tasksConfig)
                .titleField(TASKS.TITLE)
                .children(List.of(
                        JooqFieldElement.of(TASKS.TITLE, "route.tasks.labels.title").build(),
                        JooqFieldElement.of(TASKS.DESCRIPTION, "route.tasks.labels.description").build(),
                        JooqFieldElement.of(TASKS.STATUS, "route.tasks.labels.status").build(),
                        JooqFieldElement.of(TASKS.DUE_DATE, "route.tasks.labels.due_date").build(),
                        JooqFieldElement.of(TASKS.ASSIGNED_TO, "route.tasks.labels.assigned_to").build(),
                        JooqCollectionElement.of("route.tasks.labels.comments")
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(commentsConfig)
                                .oneToMany(new JooqOneToMany(TASK_COMMENTS.TASK_ID))
                                .children(List.of(TASK_COMMENTS.COMMENT_TEXT))
                                .emptyMessage("route.tasks.labels.comments-empty-message")
                                .form(JooqFormRoute.builder()
                                        .titleField(TASK_COMMENTS.COMMENT_TEXT)
                                        .children(List.of(
                                                JooqFieldElement.of(TASK_COMMENTS.COMMENT_TEXT, "route.tasks.labels.comment").build()
                                        ))
                                        .build()
                                ).build(),
                        JooqCollectionElement.of("route.tasks.labels.related-tasks")
                                .factory(new ListCollectionFactory<>())
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

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> projectForm = JooqFormRoute.builder()
                .dataStoreConfig(projectsConfig)
                .title("route.projects.title-cards")
                .titleField(PROJECTS.NAME)
                .children(List.of(
                        JooqFieldElement.of(PROJECTS.NAME, "route.projects.labels.name").build(),
                        JooqFieldElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description").build(),
                        JooqFieldElement.of(PROJECTS.BUDGET, "Budget").build(),
                        JooqFieldElement.of(PROJECTS.TAGS_MULTI, "Tags (MultiSelect)").build(),
                        JooqFieldElement.of(PROJECTS.ACTIVE, "Active").build(),
                        JooqCollectionElement.of("Tags (Collection)")
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(projectTagsConfig)
                                .oneToMany(new JooqOneToMany(PROJECT_TAGS.PROJECT_ID))
                                .children(List.of(PROJECT_TAGS.TAG))
                                .emptyMessage("No tags")
                                .form(JooqFormRoute.builder()
                                        .titleField(PROJECT_TAGS.TAG)
                                        .children(List.of(JooqFieldElement.of(PROJECT_TAGS.TAG, "Tag").build()))
                                        .build())
                                .build(),
                        JooqFieldElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date").build(),
                        JooqFieldElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date").build()
                ))
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqFormRoute.builder()
                .dataStoreConfig(imagesConfig)
                .title("route.projects.title-cards")
                .titleField(IMAGES.TITLE)
                .children(List.of(
                        JooqFieldElement.of(IMAGES.TITLE, "route.images.labels.title").build(),
                        JooqFieldElement.of(IMAGES.URL, "route.images.labels.image").build()
                ))
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> documentForm = JooqFormRoute.builder()
                .dataStoreConfig(documentsConfig)
                .title("Documents")
                .titleField(DOCUMENTS.TITLE)
                .children(List.of(
                        JooqFieldElement.of(DOCUMENTS.TITLE, "Title").build(),
                        JooqFieldElement.of(DOCUMENTS.PDF, "PDF").build()
                ))
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageSlideForm = JooqFormSlideRoute.builder()
                .dataStoreConfig(imagesConfig)
                .title("route.projects.title-cards")
                .titleField(IMAGES.TITLE)
                .children(List.of(
                                JooqFieldElement.of(IMAGES.TITLE, "route.images.labels.title").build(),
                                JooqFieldElement.of(IMAGES.URL, "route.images.labels.image").build()
                        )
                )
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> videoForm = JooqFormRoute.builder()
                .dataStoreConfig(videosConfig)
                .title("route.videos.title-cards")
                .titleField(VIDEOS.TITLE)
                .children(List.of(
                        JooqFieldElement.of(VIDEOS.TITLE, "route.videos.labels.title").build(),
                        JooqFieldElement.of(VIDEOS.URL, "route.videos.labels.video").build()
                ))
                .build();

        // Routes map can accept any RouteRenderer type (jOOQ, JPA, or custom)
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JooqGridRoute.builder()
                .isDefaultRoute(true)
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
                .build());
        routes.put("projects-list", JooqListRoute.builder()
                .dataStoreConfig(projectsConfig)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .filterField(PROJECTS.NAME)
                .children(List.of(
                        JooqFieldElement.of(PROJECTS.NAME, "route.projects.labels.name").build(),
                        JooqFieldElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description").build(),
                        JooqFieldElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date").build(),
                        JooqFieldElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date").build()
                ))
                .writeRoles(List.of("admin", "manager", "editor"))
                .form(projectForm)
                .build());
        routes.put("open-tasks", JooqKanbanRoute.builder()
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
                .build());
        routes.put("done-tasks", JooqMasterDetailRoute.builder()
                .iconFactory(CHECK_CIRCLE::create)
                .dataStoreConfig(tasksConfig)
                .title("route.done-tasks.title")
                .titleField(TASKS.TITLE)
                .descriptionField(TASKS.DESCRIPTION)
                .writeRoles(List.of("admin", "manager"))
                .form(taskForm)
                .build());
        routes.put("images-grid", JooqGridRoute.builder()
                .dataStoreConfig(imagesConfig)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .titleField(IMAGES.TITLE)
                .imageField(IMAGES.URL)
                .resourceProvider(new LocalImageResourceProvider())
                .writeRoles(List.of("admin"))
                .form(imageForm)
                .build());
        routes.put("images-list", JooqListRoute.builder()
                .dataStoreConfig(imagesConfig)
                .iconFactory(CAMERA::create)
                .title("route.images-list")
                .inlineEdit(true)
                .filterField(IMAGES.TITLE)
                .children(List.of(
                        JooqFieldElement.of(IMAGES.URL, "route.images.labels.image").build(),
                        JooqFieldElement.of(IMAGES.TITLE, "route.images.labels.title").build()
                ))
                .writeRoles(List.of("admin"))
                .form(imageForm)
                .build());

        routes.put("images-slide", JooqGridRoute.builder()
                .dataStoreConfig(imagesConfig)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .titleField(IMAGES.TITLE)
                .imageField(IMAGES.URL)
                .resourceProvider(new LocalImageResourceProvider())
                .writeRoles(List.of("admin"))
                .form(imageSlideForm)
                .build());

        routes.put("videos-grid", JooqGridRoute.builder()
                .dataStoreConfig(videosConfig)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-cards")
                .titleField(VIDEOS.TITLE)
                .writeRoles(List.of("admin"))
                .form(videoForm)
                .build());

        routes.put("videos-list", JooqListRoute.builder()
                .dataStoreConfig(videosConfig)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-list")
                .inlineEdit(true)
                .filterField(VIDEOS.TITLE)
                .children(List.of(
                        JooqFieldElement.of(VIDEOS.TITLE, "route.videos.labels.title").build(),
                        JooqFieldElement.of(VIDEOS.URL, "route.videos.labels.video").build()
                ))
                .writeRoles(List.of("admin"))
                .form(videoForm)
                .build());

        routes.put("submenu", JooqSubmenuRoute.builder()
                .iconFactory(MENU::create)
                .dataStoreConfig(projectsConfig)
                .title("route.submenu.title")
                .childrenMap(Map.of(
                        "project-form", projectForm,
                        "image-form", imageForm))
                .build());

        routes.put("profile", JooqSingleFormRoute.builder()
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
                .children(List.of(
                        JooqFieldElement.of(USERS.USERNAME, "route.profile.labels.username").build(),
                        JooqFieldElement.of(USERS.FIRST_NAME, "route.profile.labels.first_name").build(),
                        JooqFieldElement.of(USERS.LAST_NAME, "route.profile.labels.last_name").build(),
                        JooqFieldElement.of(USERS.CREATED_AT, "route.profile.labels.created_at").build()
                ))
                .build());

        routes.put("calendar", JooqCalendarRoute.builder()
                .title("Calendar")
                .iconFactory(CALENDAR::create)
                .dataStoreConfig(projectsConfig)
                .titleField(PROJECTS.NAME)
                .startDateField(PROJECTS.START_DATE)
                .endDateField(PROJECTS.END_DATE)
                .descriptionField(PROJECTS.DESCRIPTION)
                .build());

        // Custom in-memory data store route
        // Uses String field names (simple and framework-agnostic!)
        routes.put("notes", GridRoute.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                .dataStoreConfig(notesConfig)
                .iconFactory(NOTEBOOK::create)
                .title("Notes (Custom DataStore)")
                .titleField("title")
                .descriptionField("content")
                .form(FormRoute.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                        .dataStoreConfig(notesConfig)
                        .titleField("title")
                        .children(List.of(
                                InternalFormElement.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                                        .field("title")
                                        .label("Title")
                                        .build(),
                                InternalFormElement.<SimpleMapDataStore.Note, String, SimpleMapDataStore>builder()
                                        .field("content")
                                        .label("Content")
                                        .build()
                        ))
                        .build())
                .build());

        routes.put("custom", CustomRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .title("Custom Route")
                .iconFactory(CODE::create)
                .componentClass(CustomView.class)
                .build());

        routes.put("documents", JooqGridRoute.builder()
                .dataStoreConfig(documentsConfig)
                .iconFactory(FILE::create)
                .title("Documents")
                .titleField(DOCUMENTS.TITLE)
                .form(documentForm)
                .build());

        routes.put("filtered-grid", JooqGridRoute.builder()
                .dataStoreConfig(imagesConfig)
                .iconFactory(FILTER::create)
                .title("Filtered Grid")
                .titleField(IMAGES.TITLE)
                .imageField(IMAGES.URL)
                .resourceProvider(new LocalImageResourceProvider())
                .filter(RouteFilter.<TableField<?, ?>>builder()
                        .field(IMAGES.TITLE)
                        .value("Red")
                        .build())
                .writeRoles(List.of("admin"))
                .form(imageForm)
                .build());

        LinkedHashMap<Status, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put(TODO, "selects.task-status.todo");
        taskStatuses.put(OPEN, "selects.task-status.open");
        taskStatuses.put(WORK_IN_PROGRESS, "selects.task-status.progress");
        taskStatuses.put(CLOSED, "selects.task-status.closed");

        LinkedHashMap<String, String> projectTags = new LinkedHashMap<>();
        projectTags.put("tag1", "Tag 1");
        projectTags.put("tag2", "Tag 2");
        projectTags.put("tag3", "Tag 3");


        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("some_i18n")
                .identityAndAccessManagement(
                        LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                .dataStoreConfig(usersConfig)
                                .roleResolutionStrategy(new JoinTableRoleResolutionStrategy<TableField<?, ?>>(
                                        userRolesStore,
                                        rolesStore,
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
                                .username(JooqFieldElement.of(USERS.USERNAME, "route.projects.labels.name").build())
                                .password(JooqFieldElement.of(USERS.PASSWORD_HASH, "route.projects.labels.password").build())
                                .signUpFields(List.of(
                                        JooqFieldElement.of(USERS.FIRST_NAME, "route.profile.labels.first_name").build(),
                                        JooqFieldElement.of(USERS.LAST_NAME, "route.profile.labels.last_name").build()
                                ))
                                .build()
                )
                .menuActions(List.of(
                        DataStoreDropdownMenuAction.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                .dataStoreConfig(usersConfig)
                                .labelField(USERS.USERNAME)
                                .placeholder("Filter by user...")
                                .label("Assigned User")
                                .limit(50)
                                .build()
                ))
                .routes(routes)
                .versioning(JooqVersioning.builder().dataStores(List.of(PROJECTS, TASKS, TASK_COMMENTS)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(Selects.builder()
                        .configs(Map.of("task-status", taskStatuses, "project-tags", projectTags))
                        .build())
                .build();
    }
}
