package com.github.appreciated.vortex_crud.example.jpa;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.fields.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.file_provider.LocalVideoResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.example.jpa.custom.SimpleMapDataStore;
import com.github.appreciated.vortex_crud.example.jpa.entity.Status;
import com.github.appreciated.vortex_crud.example.jpa.repository.*;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.JpaManyToMany;
import com.github.appreciated.vortex_crud.jpa.service.JpaOneToMany;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
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
import com.github.appreciated.vortex_crud.example.jpa.entity.*;

@Service
public class ExampleJpaConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final ImageRepository imageRepository;
    private final ProjectRepository projectRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
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
            TaskCommentRepository taskCommentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository,
            VideoRepository videoRepository,
            JpaFieldAnnotationRegistryService annotationRegistryService,
            JpaFieldService fieldService
    ) {
        this.imageRepository = imageRepository;
        this.projectRepository = projectRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.annotationRegistryService = annotationRegistryService;
        this.fieldService = fieldService;
    }

    private <T> JpaRepositoryDataStore<T> createStore(JpaRepository<T, ?> repo) {
        return new JpaRepositoryDataStore<>(repo, annotationRegistryService, new DataStoreHooks<>());
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // 1. Create DataStores
        var projectStore = createStore(projectRepository);
        var taskStore = createStore(taskRepository);
        var commentStore = createStore(taskCommentRepository);
        var imageStore = createStore(imageRepository);
        var videoStore = createStore(videoRepository);
        var userStore = createStore(userRepository);

        SimpleMapDataStore<String> notesStore = new SimpleMapDataStore<>(s -> s);

        // 2. Build map of Class -> DataStore
        Map<Class<?>, VortexCrudDataStore> storeMap = new HashMap<>();
        storeMap.put(projectStore.getModelClass(), projectStore);
        storeMap.put(taskStore.getModelClass(), taskStore);
        storeMap.put(commentStore.getModelClass(), commentStore);
        storeMap.put(imageStore.getModelClass(), imageStore);
        storeMap.put(videoStore.getModelClass(), videoStore);
        storeMap.put(userStore.getModelClass(), userStore);

        // 3. Build DataStoreConfigs
        var projectConfig = JpaDataStoreConfig.builder(projectRepository, projectStore).withServices(fieldService, storeMap).build();
        var taskConfig = JpaDataStoreConfig.builder(taskRepository, taskStore).withServices(fieldService, storeMap).build();
        var commentConfig = JpaDataStoreConfig.builder(taskCommentRepository, commentStore).withServices(fieldService, storeMap).build();
        var imageConfig = JpaDataStoreConfig.builder(imageRepository, imageStore).withServices(fieldService, storeMap).build();
        var videoConfig = JpaDataStoreConfig.builder(videoRepository, videoStore).withServices(fieldService, storeMap).build();
        var userConfig = JpaDataStoreConfig.builder(userRepository, userStore).withServices(fieldService, storeMap).build();

        var notesConfig = DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory(NOTES_KEY)
                .dataStoreInstance((VortexCrudDataStore) notesStore)
                .fields(Map.ofEntries(
                        Map.entry("id", IdField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("title", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("content", TextAreaField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("price", BigDecimalField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("active", CheckboxField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("projectDuration", DateRangeField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("eventDuration", DateTimeRangeField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("attachment", FileField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("markdownContent", MarkDownField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("tags", MultiSelectValueField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build()),
                        Map.entry("document", PdfField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build())
                ))
                .build();

        InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> build = JpaCollectionElement.builder("route.tasks.labels.comments")
                .factory((Class<? extends VortexCrudCollectionFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) (Class<?>) ListCollectionFactory.class)
                .configuration(JpaCollection.builder((Class<? extends VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) (Class) FormDialogFactory.class)
                        .data(JpaCollectionConfiguration.builder(commentConfig)
                                .oneToMany(new JpaOneToMany("task"))
                                .children(List.of("commentText"))
                                .build())
                        .emptyMessage("route.tasks.labels.comments-empty-message")
                        .child(JpaFormRoute.builder()
                                .formConfiguration(JpaFormRendererConfiguration.builder()
                                        .titleField("name")
                                        .children(List.of(
                                                JpaFieldElement.builder("commentText", "route.tasks.labels.comment").build()
                                        ))
                                        .build())
                                .build())
                        .build())
                .build();
        CollectionConfiguration build2 = JpaCollectionConfiguration.builder(taskConfig)
                .manyToMany(new JpaManyToMany(taskRepository, "relatedTasks"))
                .children(List.of("title"))
                .build();
        InternalFormElement build1 = JpaCollectionElement.builder("route.tasks.labels.related-tasks")
                .factory((Class<? extends VortexCrudCollectionFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) (Class<?>) ListCollectionFactory.class)
                .configuration(JpaCollection.builder((Class<? extends VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) (Class) ConnectDialogFactory.class)
                        .data(build2)
                        .emptyMessage("route.tasks.labels.related-tasks-empty-message")
                        .configuration(new CollectionConfig("title"))
                        .build())
                .build();
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> taskForm = JpaFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.builder("title", "route.tasks.labels.title").build(),
                                JpaFieldElement.builder("description", "route.tasks.labels.description").build(),
                                JpaFieldElement.builder("status", "route.tasks.labels.status").build(),
                                JpaFieldElement.builder("dueDate", "route.tasks.labels.due_date").build(),
                                JpaFieldElement.builder("assignedTo", "route.tasks.labels.assigned_to").build(),
                                build,
                                build1
                        ))
                        .build())
                .build();
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> projectForm = JpaFormRoute.builder()
                .dataStoreConfig(projectConfig)
                .title("route.projects.title-cards")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("name")
                        .children(List.of(
                                JpaFieldElement.builder("name", "route.projects.labels.name").build(),
                                JpaFieldElement.builder("description", "route.projects.labels.description").build(),
                                JpaFieldElement.builder("startDate", "route.projects.labels.start_date").build(),
                                JpaFieldElement.builder("endDate", "route.projects.labels.end_date").build()
                        ))
                        .build())
                .build();
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaFormRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.projects.title-cards")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.builder("title", "route.images.labels.title").build(),
                                JpaFieldElement.builder("url", "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        FormSlideRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageSlideForm = JpaFormSlideRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.projects.title-cards")
                .configuration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.builder("title", "route.images.labels.title").build(),
                                JpaFieldElement.builder("url", "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> videoForm = JpaFormRoute.builder()
                .dataStoreConfig(videoConfig)
                .title("route.videos.title-cards")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.builder("title", "route.videos.labels.title").build(),
                                JpaFieldElement.builder("url", "route.videos.labels.video").build()
                        ))
                        .build())
                .build();

        // Projects Routes
        var projectCards = JpaGridRoute.builder()
                .isDefaultRoute(true)
                .dataStoreConfig(projectConfig)
                .title("route.projects.title-cards")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("name")
                        .descriptionField("description")
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .child(projectForm)
                .build();

        var projectList = JpaListRoute.builder()
                .dataStoreConfig(projectConfig)
                .title("route.projects.title-list")
                .configuration(JpaListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField("name")
                        .children(List.of(
                                JpaFieldElement.builder("name", "route.projects.labels.name").build(),
                                JpaFieldElement.builder("description", "route.projects.labels.description").build(),
                                JpaFieldElement.builder("startDate", "route.projects.labels.start_date").build(),
                                JpaFieldElement.builder("endDate", "route.projects.labels.end_date").build()
                        ))
                        .build())
                .writeRoles(List.of("admin", "manager", "editor"))
                .child(projectForm)
                .build();

        // Tasks Routes
        var openTasks = JpaKanbanRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("route.open-tasks.title")
                .configuration(JpaKanbanConfiguration.builder()
                        .titleField("title")
                        .descriptionField("description")
                        .columnField("status")
                        .rowIndexField("rowIndex")
                        .filterField("title")
                        .build())
                .writeRoles(List.of("admin", "manager", "editor", "viewer"))
                .menuActions(List.of(
                        DataStoreDropdownMenuAction.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .dataStoreConfig(userConfig)
                                .labelField("username")
                                .placeholder("Filter by user...")
                                .label("Assigned User")
                                .limit(50)
                                .build()
                ))
                .child(taskForm)
                .build();

        var doneTasks = JpaMasterDetailRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("route.done-tasks.title")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .descriptionField("description")
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .child(taskForm)
                .build();

        // Images Routes
        var imagesGrid = JpaGridRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.images-cards")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .imageField("url")
                        .resourceProvider(LocalImageResourceProvider.class)
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageForm)
                .build();

        var imagesList = JpaListRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.images-list")
                .configuration(JpaListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField("title")
                        .children(List.of(
                                JpaFieldElement.builder("url", "route.images.labels.image").build(),
                                JpaFieldElement.builder("title", "route.images.labels.title").build()
                        ))
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageForm)
                .build();

        var imagesSlide = JpaGridRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.images-cards")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .imageField("url")
                        .resourceProvider(LocalImageResourceProvider.class)
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageSlideForm)
                .build();

        // Videos Routes
        var videosGrid = JpaGridRoute.builder()
                .dataStoreConfig(videoConfig)
                .title("route.videos.title-cards")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .build())
                .writeRoles(List.of("admin"))
                .child(videoForm)
                .build();

        var videosList = JpaListRoute.builder()
                .dataStoreConfig(videoConfig)
                .title("route.videos.title-list")
                .configuration(JpaListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField("title")
                        .children(List.of(
                                JpaFieldElement.builder("title", "route.videos.labels.title").build(),
                                JpaFieldElement.builder("url", "route.videos.labels.video").build()
                        ))
                        .build())
                .writeRoles(List.of("admin"))
                .child(videoForm)
                .build();

        // Profile Route
        var profileRoute = JpaSingleFormRoute.builder()
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
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("username")
                        .children(List.of(
                                JpaFieldElement.builder("username", "route.profile.labels.username").build(),
                                JpaFieldElement.builder("firstName", "route.profile.labels.first_name").build(),
                                JpaFieldElement.builder("lastName", "route.profile.labels.last_name").build()
                        ))
                        .build())
                .build();

        // Notes Route
        var notesRoute = JpaGridRoute.builder()
                .dataStoreConfig(notesConfig)
                .iconFactory(NOTEBOOK::create)
                .title("Notes (Custom DataStore)")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .descriptionField("content")
                        .build())
                .child(JpaFormRoute.builder()
                        .dataStoreConfig(notesConfig)
                        .formConfiguration(JpaFormRendererConfiguration.builder()
                                .titleField("title")
                                .children(List.of(
                                        JpaFieldElement.builder("title", "Title").build(),
                                        JpaFieldElement.builder("content", "Content").build(),
                                        JpaFieldElement.builder("price", "Price").build(),
                                        JpaFieldElement.builder("active", "Active").build(),
                                        JpaFieldElement.builder("projectDuration", "Project Duration").build(),
                                        JpaFieldElement.builder("eventDuration", "Event Duration").build(),
                                        JpaFieldElement.builder("attachment", "Attachment").build(),
                                        JpaFieldElement.builder("markdownContent", "Markdown").build(),
                                        JpaFieldElement.builder("tags", "Tags").build(),
                                        JpaFieldElement.builder("document", "Document").build()
                                ))
                                .build())
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();

        routes.put("projects", JpaSubmenuRoute.builder()
                .title("Projects")
                .iconFactory(FACTORY::create)
                .childrenMap(Map.of("cards", projectCards, "list", projectList))
                .build());

        routes.put("tasks", JpaSubmenuRoute.builder()
                .title("Tasks")
                .iconFactory(TASKS::create)
                .childrenMap(Map.of("open", openTasks, "done", doneTasks))
                .build());

        routes.put("media", JpaSubmenuRoute.builder()
                .title("Media")
                .iconFactory(CAMERA::create)
                .childrenMap(Map.of(
                        "images", JpaSubmenuRoute.builder()
                                .title("Images")
                                .childrenMap(Map.of(
                                        "grid", imagesGrid,
                                        "list", imagesList,
                                        "slide", imagesSlide
                                )).build(),
                        "videos", JpaSubmenuRoute.builder()
                                .title("Videos")
                                .childrenMap(Map.of(
                                        "grid", videosGrid,
                                        "list", videosList
                                )).build()
                )).build());

        routes.put("profile", profileRoute);

        routes.put("notes", notesRoute);

        LinkedHashMap<Status, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put(TODO, "selects.task-status.todo");
        taskStatuses.put(OPEN, "selects.task-status.open");
        taskStatuses.put(WORK_IN_PROGRESS, "selects.task-status.progress");
        taskStatuses.put(CLOSED, "selects.task-status.closed");

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("some_i18n")
                .identityAndAccessManagement(
                        LocalIdentityAndAccessManagement.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .dataStoreConfig(userConfig)
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
                                .rolesField("roles")
                                .build()
                )
                .menuActions(List.of(
                        DataStoreDropdownMenuAction.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .dataStoreConfig(userConfig)
                                .labelField("username")
                                .placeholder("Filter by user...")
                                .label("Assigned User")
                                .limit(50)
                                .build()
                ))
                .routes(routes)
                .versioning(JpaVersioning.builder().dataStores(List.of(projectRepository, taskRepository, taskCommentRepository)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(new Selects(Map.of("task-status", taskStatuses)))
                .build();
    }
}
