package com.github.appreciated.vortex_crud.example.jpa;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.GlobalRouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormSlideFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.example.jpa.custom.SimpleMapDataStore;
import com.github.appreciated.vortex_crud.example.jpa.entity.Status;
import com.github.appreciated.vortex_crud.example.jpa.repository.*;
import com.github.appreciated.vortex_crud.example.jpa.view.CustomView;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.JpaManyToMany;
import com.github.appreciated.vortex_crud.jpa.service.JpaOneToMany;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.security.core.strategy.FieldBasedRoleResolutionStrategy;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.core.config.model.AuditingAction.*;
import static com.github.appreciated.vortex_crud.example.jpa.entity.Status.*;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ExampleJpaConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final ImageRepository imageRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTagRepository projectTagRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final DocumentRepository documentRepository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;
    private final JpaFieldService fieldService;

    // Marker key for custom data store - cast required due to type erasure
    @SuppressWarnings("unchecked")
    private static final JpaRepository<?, ?> NOTES_KEY = (JpaRepository<?, ?>) java.lang.reflect.Proxy.newProxyInstance(
            ExampleJpaConfiguration.class.getClassLoader(),
            new Class[]{JpaRepository.class},
            (proxy, method, args) -> {
                if (method.getName().equals("toString")) return "NotesDataStore";
                if (method.getName().equals("hashCode")) return 42;
                if (method.getName().equals("equals")) return proxy == args[0];
                return null;
            }
    );

    public ExampleJpaConfiguration(
            ImageRepository imageRepository,
            ProjectRepository projectRepository,
            ProjectTagRepository projectTagRepository,
            TaskCommentRepository taskCommentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository,
            VideoRepository videoRepository,
            DocumentRepository documentRepository,
            JpaFieldAnnotationRegistryService annotationRegistryService,
            JpaFieldService fieldService
    ) {
        this.imageRepository = imageRepository;
        this.projectRepository = projectRepository;
        this.projectTagRepository = projectTagRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.documentRepository = documentRepository;
        this.annotationRegistryService = annotationRegistryService;
        this.fieldService = fieldService;
    }

    private <T> JpaRepositoryDataStore<T> createStore(JpaRepository<T, ?> repo) {
        return new JpaRepositoryDataStore<>(repo, annotationRegistryService);
    }

    @Override
    public com.github.appreciated.vortex_crud.core.config.model.Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // 1. Create DataStores
        var projectStore = createStore(projectRepository);
        var projectTagStore = createStore(projectTagRepository);
        var taskStore = createStore(taskRepository);
        var commentStore = createStore(taskCommentRepository);
        var imageStore = createStore(imageRepository);
        var videoStore = createStore(videoRepository);
        var userStore = createStore(userRepository);
        var documentStore = createStore(documentRepository);
        SimpleMapDataStore notesStore = new SimpleMapDataStore();

        // 2. Build map of Class -> DataStore
        Map<Class<?>, VortexCrudDataStore> storeMap = new HashMap<>();
        storeMap.put(projectStore.getModelClass(), projectStore);
        storeMap.put(projectTagStore.getModelClass(), projectTagStore);
        storeMap.put(taskStore.getModelClass(), taskStore);
        storeMap.put(commentStore.getModelClass(), commentStore);
        storeMap.put(imageStore.getModelClass(), imageStore);
        storeMap.put(videoStore.getModelClass(), videoStore);
        storeMap.put(userStore.getModelClass(), userStore);
        storeMap.put(documentStore.getModelClass(), documentStore);

        // 3. Build DataStoreConfigs
        var projectConfig = JpaDataStoreConfig.builder(projectRepository, projectStore).withServices(fieldService, storeMap).build();
        var projectTagConfig = JpaDataStoreConfig.builder(projectTagRepository, projectTagStore).withServices(fieldService, storeMap).build();
        var taskConfig = JpaDataStoreConfig.builder(taskRepository, taskStore).withServices(fieldService, storeMap).build();
        var commentConfig = JpaDataStoreConfig.builder(taskCommentRepository, commentStore).withServices(fieldService, storeMap).build();
        var imageConfig = JpaDataStoreConfig.builder(imageRepository, imageStore).withServices(fieldService, storeMap).build();
        var videoConfig = JpaDataStoreConfig.builder(videoRepository, videoStore).withServices(fieldService, storeMap).build();
        var userConfig = JpaDataStoreConfig.builder(userRepository, userStore).withServices(fieldService, storeMap).build();
        var documentConfig = JpaDataStoreConfig.builder(documentRepository, documentStore).withServices(fieldService, storeMap).build();

        var notesConfig = DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory(NOTES_KEY)
                .dataStoreInstance((VortexCrudDataStore) notesStore)
                .fields(Map.of(
                        "title", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                        "content", TextAreaField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()
                ))
                .build();

        InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> build = JpaCollectionElement.builder("route.tasks.labels.comments")
                .field("comments")
                .listFactory(new ListCollectionFactory<>())
                .dialogFactory(new FormDialogFactory<>())
                .dataStoreConfig(commentConfig)
                .oneToMany(new JpaOneToMany("task"))
                .children(List.of("commentText"))
                .emptyMessage("route.tasks.labels.comments-empty-message")
                .form(JpaFormRoute.builder()
                        .dataStoreConfig(commentConfig)
                        .titleField("name")
                        .fields(List.of(
                                JpaFieldElement.builder("commentText", "route.tasks.labels.comment").build()
                        ))
                        .build())
                .build();

        InternalFormElement build1 = JpaCollectionElement.builder("route.tasks.labels.related-tasks")
                .field("relatedTasks")
                .listFactory(new ListCollectionFactory<>())
                .dialogFactory(new ConnectDialogFactory<>())
                .dataStoreConfig(taskConfig)
                .manyToMany(new JpaManyToMany(taskRepository, "relatedTasks"))
                .children(List.of("title"))
                .emptyMessage("route.tasks.labels.related-tasks-empty-message")
                .titleField("title")
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> taskForm = JpaFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .titleField("title")
                .fields(List.of(
                        JpaFieldElement.builder("title", "route.tasks.labels.title").build(),
                        JpaFieldElement.builder("description", "route.tasks.labels.description").build(),
                        JpaFieldElement.builder("status", "route.tasks.labels.status").build(),
                        JpaFieldElement.builder("dueDate", "route.tasks.labels.due_date").build(),
                        JpaFieldElement.builder("assignedTo", "route.tasks.labels.assigned_to").build(),
                        build,
                        build1
                ))
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> projectForm = JpaFormRoute.builder()
                .dataStoreConfig(projectConfig)
                .title("route.projects.title-cards")
                .titleField("name")
                .fields(List.of(
                        JpaFieldElement.builder("name", "route.projects.labels.name").build(),
                        JpaFieldElement.builder("description", "route.projects.labels.description").build(),
                        JpaFieldElement.builder("budget", "route.projects.labels.budget").build(),
                        JpaFieldElement.builder("tagsMulti", "route.projects.labels.tags_multi").build(),
                        JpaCollectionElement.builder("route.projects.labels.tags_collection")
                                .field("projectTags")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(projectTagConfig)
                                .oneToMany(new JpaOneToMany("project"))
                                .children(List.of("tag"))
                                .emptyMessage("route.projects.labels.no_tags")
                                .form(JpaFormRoute.builder()
                                        .dataStoreConfig(projectTagConfig)
                                        .titleField("tag")
                                        .fields(List.of(
                                                JpaFieldElement.builder("tag", "route.projects.labels.tag").build()
                                        ))
                                        .build())
                                .build(),
                        JpaFieldElement.builder("active", "route.projects.labels.active").build(),
                        JpaFieldElement.builder("startDate", "route.projects.labels.start_date").build(),
                        JpaFieldElement.builder("endDate", "route.projects.labels.end_date").build()
                ))
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaFormRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.projects.title-cards")
                .titleField("title")
                .fields(List.of(
                        JpaFieldElement.builder("title", "route.images.labels.title").build(),
                        JpaFieldElement.builder("url", "route.images.labels.image").build()
                ))
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageSlideForm = JpaFormRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.projects.title-cards")
                .titleField("title")
                .dialogFactory(new FormSlideFactory<>())
                .fields(List.of(
                        JpaFieldElement.builder("title", "route.images.labels.title").build(),
                        JpaFieldElement.builder("url", "route.images.labels.image").build()
                ))
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> videoForm = JpaFormRoute.builder()
                .dataStoreConfig(videoConfig)
                .title("route.videos.title-cards")
                .titleField("title")
                .fields(List.of(
                        JpaFieldElement.builder("title", "route.videos.labels.title").build(),
                        JpaFieldElement.builder("url", "route.videos.labels.video").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("search", SearchRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .title("route.search.title")
                .iconFactory(SEARCH::create)
                .build());
        routes.put("projects-cards", JpaGridRoute.builder()
                .defaultRoute(true)
                .dataStoreConfig(projectConfig)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-cards")
                .titleField("name")
                .descriptionField("description")
                .writeRoles(List.of("admin", "manager"))
                .routeActions(List.of(
                        GlobalRouteAction.<String, JpaRepository<?, ?>>builder()
                                .componentFactory(() -> new Button("Global Action"))
                                .handler((context) -> Notification.show("Global Action Clicked"))
                                .build()
                ))
                .form(projectForm)
                .build());
        routes.put("projects-list", JpaListRoute.builder()
                .dataStoreConfig(projectConfig)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .inlineEdit(true)
                .filterField("name")
                .columns(List.of(
                        JpaFieldElement.builder("name", "route.projects.labels.name").build(),
                        JpaFieldElement.builder("description", "route.projects.labels.description").build(),
                        JpaFieldElement.builder("startDate", "route.projects.labels.start_date").build(),
                        JpaFieldElement.builder("endDate", "route.projects.labels.end_date").build()
                ))
                .writeRoles(List.of("admin", "manager", "editor"))
                .form(projectForm)
                .build());
        routes.put("open-tasks", JpaKanbanRoute.builder()
                .iconFactory(TASKS::create)
                .dataStoreConfig(taskConfig)
                .title("route.open-tasks.title")
                .titleField("title")
                .descriptionField("description")
                .columnField("status")
                .rowIndexField("rowIndex")
                .titleField("title")
                .filterField("title")
                .writeRoles(List.of("admin", "manager", "editor", "viewer"))
                .form(taskForm)
                .build());
        routes.put("done-tasks", JpaMasterDetailRoute.builder()
                .iconFactory(CHECK_CIRCLE::create)
                .dataStoreConfig(taskConfig)
                .title("route.done-tasks.title")
                .titleField("title")
                .descriptionField("description")
                .writeRoles(List.of("admin", "manager"))
                .form(taskForm)
                .build());
        routes.put("images-grid", JpaGridRoute.builder()
                .dataStoreConfig(imageConfig)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .titleField("title")
                .imageField("url")
                .resourceProvider(new LocalImageResourceProvider())
                .writeRoles(List.of("admin"))
                .form(imageForm)
                .build());
        routes.put("images-list", JpaListRoute.builder()
                .dataStoreConfig(imageConfig)
                .iconFactory(CAMERA::create)
                .title("route.images-list")
                .inlineEdit(true)
                .filterField("title")
                .columns(List.of(
                        JpaFieldElement.builder("url", "route.images.labels.image").build(),
                        JpaFieldElement.builder("title", "route.images.labels.title").build()
                ))
                .writeRoles(List.of("admin"))
                .form(imageForm)
                .build());

        routes.put("images-slide", JpaGridRoute.builder()
                .dataStoreConfig(imageConfig)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .titleField("title")
                .imageField("url")
                .resourceProvider(new LocalImageResourceProvider())
                .writeRoles(List.of("admin"))
                .form(imageSlideForm)
                .build());

        routes.put("videos-grid", JpaGridRoute.builder()
                .dataStoreConfig(videoConfig)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-cards")
                .titleField("title")
                .writeRoles(List.of("admin"))
                .form(videoForm)
                .build());

        routes.put("videos-list", JpaListRoute.builder()
                .dataStoreConfig(videoConfig)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-list")
                .inlineEdit(true)
                .filterField("title")
                .columns(List.of(
                        JpaFieldElement.builder("title", "route.videos.labels.title").build(),
                        JpaFieldElement.builder("url", "route.videos.labels.video").build()
                ))
                .writeRoles(List.of("admin"))
                .form(videoForm)
                .build());

        routes.put("submenu", JpaSubmenuRoute.builder()
                .iconFactory(MENU::create)
                .dataStoreConfig(projectConfig)
                .title("route.submenu.title")
                .childrenMap(Map.of(
                        "project-form", projectForm,
                        "image-form", imageForm))
                .build());

        routes.put("profile", JpaSingleFormRoute.builder()
                .iconFactory(USER::create)
                .dataStoreConfig(userConfig)
                .title("route.profile.title")
                .entityFilterField("username")
                .entityFilterValueProvider(() -> {
                    // Get current user from security context
                    VaadinServletRequest request = VaadinServletRequest.getCurrent();
                    return request != null && request.getUserPrincipal() != null
                            ? request.getUserPrincipal().getName()
                            : null;
                })
                .titleField("username")
                .fields(List.of(
                        JpaFieldElement.builder("username", "route.profile.labels.username").build(),
                        JpaFieldElement.builder("firstName", "route.profile.labels.first_name").build(),
                        JpaFieldElement.builder("lastName", "route.profile.labels.last_name").build(),
                        JpaFieldElement.builder("createdAt", "route.profile.labels.created_at").build()
                ))
                .build());

        // Custom in-memory data store example
        routes.put("notes", JpaGridRoute.builder()
                .dataStoreConfig(notesConfig)
                .iconFactory(NOTEBOOK::create)
                .title("route.notes.title")
                .titleField("title")
                .descriptionField("content")
                .form(JpaFormRoute.builder()
                        .dataStoreConfig(notesConfig)
                        .titleField("title")
                        .fields(List.of(
                                JpaFieldElement.builder("title", "route.notes.labels.title").build(),
                                JpaFieldElement.builder("content", "route.notes.labels.content").build()
                        ))
                        .build())
                .build());

        routes.put("calendar", JpaCalendarRoute.builder()
                .title("route.calendar.title")
                .iconFactory(CALENDAR::create)
                .dataStoreConfig(projectConfig)
                .titleField("name")
                .startDateField("startDate")
                .endDateField("endDate")
                .descriptionField("description")
                .build());

        routes.put("custom", CustomRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .title("route.custom.title")
                .iconFactory(CODE::create)
                .componentClass(CustomView.class)
                .build());

        routes.put("documents", JpaGridRoute.builder()
                .dataStoreConfig(documentConfig)
                .iconFactory(FILE::create)
                .title("route.documents.title")
                .titleField("title")
                .form(JpaFormRoute.builder()
                        .dataStoreConfig(documentConfig)
                        .titleField("title")
                        .fields(List.of(
                                JpaFieldElement.builder("title", "route.documents.labels.title").build(),
                                JpaFieldElement.builder("pdf", "route.documents.labels.pdf").build()
                        ))
                        .build())
                .build());

        LinkedHashMap<Status, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put(TODO, "selects.task-status.todo");
        taskStatuses.put(OPEN, "selects.task-status.open");
        taskStatuses.put(WORK_IN_PROGRESS, "selects.task-status.progress");
        taskStatuses.put(CLOSED, "selects.task-status.closed");

        LinkedHashMap<String, String> projectTags = new LinkedHashMap<>();
        projectTags.put("tag1", "selects.project-tags.tag1");
        projectTags.put("tag2", "selects.project-tags.tag2");
        projectTags.put("tag3", "selects.project-tags.tag3");

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("some_i18n")
                .identityAndAccessManagement(
                        LocalIdentityAndAccessManagement.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .dataStoreConfig(userConfig)
                                .roleResolutionStrategy(new FieldBasedRoleResolutionStrategy<>("roles"))
                                .availableRoles(Roles.builder().roles(List.of("admin", "viewer", "guest")).build())
                                .defaultReadRoles(List.of("viewer"))
                                .defaultWriteRoles(List.of("admin"))
                                .signUpEnabled(true)
                                .loginView(LoginView.class)
                                .signUpView(SignUpView.class)
                                .username(JpaFieldElement.builder("username", "route.projects.labels.name").build())
                                .password(JpaFieldElement.builder("passwordHash", "route.projects.labels.password").build())
                                .signUpFields(List.of(
                                        JpaFieldElement.builder("firstName", "route.projects.labels.end_date").build(),
                                        JpaFieldElement.builder("lastName", "route.projects.labels.end_date").build()
                                ))
                                .build()
                )
                .routes(routes)
                .versioning(JpaVersioning.builder().dataStores(List.of(projectRepository, taskRepository, taskCommentRepository)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(new Selects(Map.of("task-status", taskStatuses, "project-tags", projectTags)))
                .build();
    }
}
