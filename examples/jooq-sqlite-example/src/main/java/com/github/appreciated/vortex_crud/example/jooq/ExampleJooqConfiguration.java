package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.GlobalRouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.example.jooq.custom.SimpleMapDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.example.jooq.view.CustomView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinServletRequest;
import org.jooq.DSLContext;
import org.jooq.Record;
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
import static com.github.appreciated.vortex_crud.jooq.models.tables.Documents.DOCUMENTS;
import static com.github.appreciated.vortex_crud.jooq.models.tables.ProjectTags.PROJECT_TAGS;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ExampleJooqConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    // Proxy fields for custom DataStore
    @SuppressWarnings("unchecked")
    private static final TableField<?, ?> NOTE_TITLE = (TableField<?, ?>) java.lang.reflect.Proxy.newProxyInstance(
            ExampleJooqConfiguration.class.getClassLoader(),
            new Class[]{TableField.class},
            (proxy, method, args) -> {
                if (method.getName().equals("getName")) return "title";
                if (method.getName().equals("toString")) return "NoteTitle";
                if (method.getName().equals("hashCode")) return 43;
                if (method.getName().equals("equals")) return proxy == args[0];
                return null;
            }
    );

    @SuppressWarnings("unchecked")
    private static final TableField<?, ?> NOTE_CONTENT = (TableField<?, ?>) java.lang.reflect.Proxy.newProxyInstance(
            ExampleJooqConfiguration.class.getClassLoader(),
            new Class[]{TableField.class},
            (proxy, method, args) -> {
                if (method.getName().equals("getName")) return "content";
                if (method.getName().equals("toString")) return "NoteContent";
                if (method.getName().equals("hashCode")) return 44;
                if (method.getName().equals("equals")) return proxy == args[0];
                return null;
            }
    );

    private static final TableImpl<?> NOTES_KEY = new TableImpl<Record>("NOTES") {
    };

    public ExampleJooqConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore projectsStore = new JooqDataStore(PROJECTS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore tasksStore = new JooqDataStore(TASKS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore taskHasTaskStore = new JooqDataStore(TASK_HAS_TASK.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore commentsStore = new JooqDataStore(TASK_COMMENTS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore imagesStore = new JooqDataStore(IMAGES.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore videosStore = new JooqDataStore(VIDEOS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore usersStore = new JooqDataStore(USERS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore documentsStore = new JooqDataStore(DOCUMENTS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore projectTagsStore = new JooqDataStore(PROJECT_TAGS.getRecordType(), dsl, new DataStoreHooks<>());
        SimpleMapDataStore notesStore = new SimpleMapDataStore();

        var projectsConfig = JooqDataStoreConfig.of(PROJECTS)
                .dataStoreInstance((VortexCrudDataStore) projectsStore)
                .fields(Map.of(
                        PROJECTS.ID, JooqIdField.builder().build(),
                        PROJECTS.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        PROJECTS.DESCRIPTION, JooqMarkDownField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                        PROJECTS.BUDGET, JooqBigDecimalField.builder().build(),
                        PROJECTS.ACTIVE, JooqCheckboxField.builder().build(),
                        PROJECTS.START_DATE, JooqDateField.builder().build(),
                        PROJECTS.END_DATE, JooqDateField.builder().build(),
                        PROJECTS.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        PROJECTS.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var usersConfig = JooqDataStoreConfig.of(USERS)
                        .dataStoreInstance((VortexCrudDataStore) usersStore)
                        .fields(Map.of(
                                USERS.ID, JooqIdField.builder().build(),
                                USERS.USERNAME, JooqEmailField.builder().build(),
                                USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                                USERS.FIRST_NAME, JooqTextField.builder().build(),
                                USERS.LAST_NAME, JooqTextField.builder().build(),
                                USERS.CREATED_AT, JooqDateTimePickerField.builder().build()
                        ))
                        .build();

        var tasksConfig = JooqDataStoreConfig.of(TASKS)
                .dataStoreInstance((VortexCrudDataStore) tasksStore)
                .fields(Map.of(
                        TASKS.ID, JooqIdField.builder().build(),
                        TASKS.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        TASKS.DESCRIPTION, JooqTextAreaField.builder().required(false).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        TASKS.ASSIGNED_TO, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(TASKS.ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        TASKS.STATUS, JooqSelectField.builder().values("task-status").build(),
                        TASKS.DUE_DATE, JooqDateField.builder().build(),
                        TASKS.ROW_INDEX, JooqIntegerField.builder().build(),
                        TASKS.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        TASKS.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var taskHasTaskConfig = JooqDataStoreConfig.of(TASK_HAS_TASK)
                .dataStoreInstance((VortexCrudDataStore) taskHasTaskStore)
                .fields(Map.of(
                        TASK_HAS_TASK.TASK_ID, JooqIdField.builder().build(),
                        TASK_HAS_TASK.RELATED_TASK_ID, JooqIdField.builder().build()))
                .build();

        var commentsConfig = JooqDataStoreConfig.of(TASK_COMMENTS)
                .dataStoreInstance((VortexCrudDataStore) commentsStore)
                .fields(Map.of(
                        TASK_COMMENTS.ID, JooqIdField.builder().build(),
                        TASK_COMMENTS.COMMENT_TEXT, JooqTextAreaField.builder().required(false).validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                        TASK_COMMENTS.USER_ID, JooqDoubleField.builder().build(),
                        TASK_COMMENTS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var imagesConfig = JooqDataStoreConfig.of(IMAGES)
                .dataStoreInstance((VortexCrudDataStore) imagesStore)
                .fields(Map.of(
                        IMAGES.ID, JooqIdField.builder().build(),
                        IMAGES.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        IMAGES.URL, JooqImageField.builder().configuration(JooqImageFieldRendererConfiguration.builder().resourceProvider(new LocalImageResourceProvider()).build()).build()
                ))
                .build();

        var videosConfig = JooqDataStoreConfig.of(VIDEOS)
                .dataStoreInstance((VortexCrudDataStore) videosStore)
                .fields(Map.of(
                        VIDEOS.ID, JooqIdField.builder().build(),
                        VIDEOS.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        VIDEOS.URL, JooqVideoField.builder().configuration(JooqVideoFieldRendererConfiguration.builder().resourceProvider(new LocalVideoResourceProvider()).build()).build()
                ))
                .build();

        var notesConfig = DataStoreConfig.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .factory(NOTES_KEY)
                .dataStoreInstance((VortexCrudDataStore) notesStore)
                .fields(Map.of(
                        NOTE_TITLE, JooqTextField.builder().build(),
                        NOTE_CONTENT, JooqTextAreaField.builder().build()
                ))
                .build();

        var documentsConfig = JooqDataStoreConfig.of(DOCUMENTS)
                .dataStoreInstance((VortexCrudDataStore) documentsStore)
                .fields(Map.of(
                        DOCUMENTS.ID, JooqIdField.builder().build(),
                        DOCUMENTS.TITLE, JooqTextField.builder().required(true).build(),
                        DOCUMENTS.PDF, JooqPdfField.builder().configuration(JooqPdfFieldRendererConfiguration.builder().resourceProvider(new LocalPdfResourceProvider()).build()).build()
                ))
                .build();

        var projectTagsConfig = JooqDataStoreConfig.of(PROJECT_TAGS)
                .dataStoreInstance((VortexCrudDataStore) projectTagsStore)
                .fields(Map.of(
                        PROJECT_TAGS.PROJECT_ID, JooqIdField.builder().build(),
                        PROJECT_TAGS.TAG, JooqTextField.builder().build()
                )).build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStoreConfig(tasksConfig)
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(TASKS.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(TASKS.TITLE, "route.tasks.labels.title").build(),
                                JooqFieldElement.of(TASKS.DESCRIPTION, "route.tasks.labels.description").build(),
                                JooqFieldElement.of(TASKS.STATUS, "route.tasks.labels.status").build(),
                                JooqFieldElement.of(TASKS.DUE_DATE, "route.tasks.labels.due_date").build(),
                                JooqFieldElement.of(TASKS.ASSIGNED_TO, "route.tasks.labels.assigned_to").build(),
                                JooqCollectionElement.of("route.tasks.labels.comments")
                                        .factory(new ListCollectionFactory<>())
                                        .configuration(JooqCollection.builder(new FormDialogFactory<>())
                                                .data(JooqCollectionConfiguration.of(commentsConfig)
                                                        .oneToMany(new JooqOneToMany(TASK_COMMENTS.TASK_ID))
                                                        .children(List.of(TASK_COMMENTS.COMMENT_TEXT))
                                                        .build()
                                                )
                                                .emptyMessage("route.tasks.labels.comments-empty-message")
                                                .child(JooqFormRoute.builder()
                                                        .formConfiguration(JooqFormRendererConfiguration.builder()
                                                                .titleField(TASK_COMMENTS.COMMENT_TEXT)
                                                                .children(List.of(
                                                                        JooqFieldElement.of(TASK_COMMENTS.COMMENT_TEXT, "route.tasks.labels.comment").build()
                                                                ))
                                                                .build())
                                                        .build()
                                                ).build()
                                        ).build(),
                                JooqCollectionElement.of("route.tasks.labels.related-tasks")
                                        .factory(new ListCollectionFactory<>())
                                        .configuration(JooqCollection.builder(new ConnectDialogFactory<>())
                                                .data(JooqCollectionConfiguration.of(tasksConfig)
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
                .dataStoreConfig(projectsConfig)
                .title("route.projects.title-cards").formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(PROJECTS.NAME)
                        .children(List.of(
                                JooqFieldElement.of(PROJECTS.NAME, "route.projects.labels.name").build(),
                                JooqFieldElement.of(PROJECTS.DESCRIPTION, "route.projects.labels.description").build(),
                                JooqFieldElement.of(PROJECTS.BUDGET, "Budget").build(),
                                JooqFieldElement.of(PROJECTS.ACTIVE, "Active").build(),
                                JooqCollectionElement.of("Tags")
                                        .factory(new ListCollectionFactory<>())
                                        .configuration(JooqCollection.builder(new FormDialogFactory<>())
                                                .data(JooqCollectionConfiguration.of(projectTagsConfig)
                                                        .oneToMany(new JooqOneToMany(PROJECT_TAGS.PROJECT_ID))
                                                        .children(List.of(PROJECT_TAGS.TAG))
                                                        .build())
                                                .emptyMessage("No tags")
                                                .child(JooqFormRoute.builder()
                                                        .formConfiguration(JooqFormRendererConfiguration.builder()
                                                                .titleField(PROJECT_TAGS.TAG)
                                                                .children(List.of(JooqFieldElement.of(PROJECT_TAGS.TAG, "Tag").build()))
                                                                .build())
                                                        .build())
                                                .build())
                                        .build(),
                                JooqFieldElement.of(PROJECTS.START_DATE, "route.projects.labels.start_date").build(),
                                JooqFieldElement.of(PROJECTS.END_DATE, "route.projects.labels.end_date").build()
                        )).build()
                )
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqFormRoute.builder()
                .dataStoreConfig(imagesConfig)
                .title("route.projects.title-cards")
                .formConfiguration(
                        JooqFormRendererConfiguration.builder()
                                .titleField(IMAGES.TITLE)
                                .children(List.of(
                                        JooqFieldElement.of(IMAGES.TITLE, "route.images.labels.title").build(),
                                        JooqFieldElement.of(IMAGES.URL, "route.images.labels.image").build()
                                )).build()
                )
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> documentForm = JooqFormRoute.builder()
                .dataStoreConfig(documentsConfig)
                .title("Documents")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(DOCUMENTS.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(DOCUMENTS.TITLE, "Title").build(),
                                JooqFieldElement.of(DOCUMENTS.PDF, "PDF").build()
                        )).build())
                .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageSlideForm = JooqFormSlideRoute.builder()
                .dataStoreConfig(imagesConfig)
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
                .dataStoreConfig(videosConfig)
                .title("route.videos.title-cards").formConfiguration(
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
                .dataStoreConfig(projectsConfig)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(PROJECTS.NAME)
                        .descriptionField(PROJECTS.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .routeActions(List.of(
                        GlobalRouteAction.<TableField<?, ?>, TableRecord<?>>builder()
                                .componentFactory(() -> new Button("Global Action"))
                                .handler((context) -> Notification.show("Global Action Clicked"))
                                .build()
                ))
                .child(projectForm)
                .build());
        routes.put("projects-list", JooqListRoute.builder()
                .dataStoreConfig(projectsConfig)
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
                .dataStoreConfig(tasksConfig)
                .title("route.open-tasks.title")
                .configuration(JooqKanbanConfiguration.builder()
                        .titleField(TASKS.TITLE)
                        .descriptionField(TASKS.DESCRIPTION)
                        .columnField(TASKS.STATUS)
                        .rowIndexField(TASKS.ROW_INDEX)
                        .filterField(TASKS.TITLE)
                        .build())
                .writeRoles(List.of("admin", "manager", "editor", "viewer"))
                .menuActions(List.of(
                    DataStoreDropdownMenuAction.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(usersConfig)
                        .labelField(USERS.USERNAME)
                        .placeholder("Filter by user...")
                        .label("Assigned User")
                        .limit(50)
                        .build()
                ))
                .child(taskForm)
                .build());
        routes.put("done-tasks", JooqMasterDetailRoute.builder()
                .iconFactory(CHECK_CIRCLE::create)
                .dataStoreConfig(tasksConfig)
                .title("route.done-tasks.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(TASKS.TITLE)
                        .descriptionField(TASKS.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .child(taskForm)
                .build());
        routes.put("images-grid", JooqGridRoute.builder()
                .dataStoreConfig(imagesConfig)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(IMAGES.TITLE)
                        .imageField(IMAGES.URL)
                        .resourceProvider(new LocalImageResourceProvider())
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageForm)
                .build());
        routes.put("images-list", JooqListRoute.builder()
                .dataStoreConfig(imagesConfig)
                .iconFactory(CAMERA::create)
                .title("route.images-list")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField(IMAGES.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(IMAGES.URL, "route.images.labels.image").build(),
                                JooqFieldElement.of(IMAGES.TITLE, "route.images.labels.title").build()
                        ))
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageForm)
                .build());

        routes.put("images-slide", JooqGridRoute.builder()
                .dataStoreConfig(imagesConfig)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(IMAGES.TITLE)
                        .imageField(IMAGES.URL)
                        .resourceProvider(new LocalImageResourceProvider())
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageSlideForm)
                .build());

        routes.put("videos-grid", JooqGridRoute.builder()
                .dataStoreConfig(videosConfig)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(VIDEOS.TITLE)
                        .build())
                .writeRoles(List.of("admin"))
                .child(videoForm)
                .build());

        routes.put("videos-list", JooqListRoute.builder()
                .dataStoreConfig(videosConfig)
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
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(USERS.USERNAME)
                        .children(List.of(
                                JooqFieldElement.of(USERS.USERNAME, "route.profile.labels.username").build(),
                                JooqFieldElement.of(USERS.FIRST_NAME, "route.profile.labels.first_name").build(),
                                JooqFieldElement.of(USERS.LAST_NAME, "route.profile.labels.last_name").build(),
                                JooqFieldElement.of(USERS.CREATED_AT, "route.profile.labels.created_at").build()
                        ))
                        .build())
                .build());

        routes.put("calendar", JooqCalendarRoute.builder()
                .title("Calendar")
                .iconFactory(CALENDAR::create)
                .dataStoreConfig(projectsConfig)
                .configuration(JooqCalendarConfiguration.builder()
                        .titleField(PROJECTS.NAME)
                        .startDateField(PROJECTS.START_DATE)
                        .endDateField(PROJECTS.END_DATE)
                        .descriptionField(PROJECTS.DESCRIPTION)
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
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(DOCUMENTS.TITLE)
                        .build())
                .child(documentForm)
                .build());

        // Notes Route (Custom DataStore)
        routes.put("notes", (RouteRenderer) GridRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .dataStoreConfig(notesConfig)
                .iconFactory(NOTEBOOK::create)
                .title("Notes (Custom DataStore)")
                .configuration(GridItemRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .titleField(NOTE_TITLE)
                        .descriptionField(NOTE_CONTENT)
                        .build())
                .child(FormRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(notesConfig)
                        .formConfiguration(FormRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                .titleField(NOTE_TITLE)
                                .children(List.of(
                                        InternalFormElement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().field(NOTE_TITLE).label("Title").type(ViewFieldType.FIELD).build(),
                                        InternalFormElement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().field(NOTE_CONTENT).label("Content").type(ViewFieldType.FIELD).build()
                                ))
                                .build())
                        .build())
                .build());

        LinkedHashMap<Status, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put(TODO, "selects.task-status.todo");
        taskStatuses.put(OPEN, "selects.task-status.open");
        taskStatuses.put(WORK_IN_PROGRESS, "selects.task-status.progress");
        taskStatuses.put(CLOSED, "selects.task-status.closed");

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("some_i18n")
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(usersConfig)
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
                        .rolesField(null) //TODO: Implement roles support for jOOQ
                        .build())
                .routes(routes)
                .versioning(JooqVersioning.builder().dataStores(List.of(PROJECTS, TASKS, TASK_COMMENTS)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(Selects.builder()
                        .configs(Map.of("task-status", taskStatuses))
                        .build())
                .build();
    }
}
