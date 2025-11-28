package com.github.appreciated.vortex_crud.example.jpa;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
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

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        // 1. Create DataStores
        JpaRepositoryDataStore<?> projectStore = new JpaRepositoryDataStore<>(projectRepository, annotationRegistryService, new DataStoreHooks<>());
        JpaRepositoryDataStore<?> taskStore = new JpaRepositoryDataStore<>(taskRepository, annotationRegistryService, new DataStoreHooks<>());
        JpaRepositoryDataStore<?> commentStore = new JpaRepositoryDataStore<>(taskCommentRepository, annotationRegistryService, new DataStoreHooks<>());
        JpaRepositoryDataStore<?> imageStore = new JpaRepositoryDataStore<>(imageRepository, annotationRegistryService, new DataStoreHooks<>());
        JpaRepositoryDataStore<?> videoStore = new JpaRepositoryDataStore<>(videoRepository, annotationRegistryService, new DataStoreHooks<>());
        JpaRepositoryDataStore<?> userStore = new JpaRepositoryDataStore<>(userRepository, annotationRegistryService, new DataStoreHooks<>());
        SimpleMapDataStore notesStore = new SimpleMapDataStore();

        // 2. Build map of Class -> Repository for FieldService
        Map<Class<?>, JpaRepository<?, ?>> repoMap = new HashMap<>();
        repoMap.put(projectStore.getModelClass(), projectRepository);
        repoMap.put(taskStore.getModelClass(), taskRepository);
        repoMap.put(commentStore.getModelClass(), taskCommentRepository);
        repoMap.put(imageStore.getModelClass(), imageRepository);
        repoMap.put(videoStore.getModelClass(), videoRepository);
        repoMap.put(userStore.getModelClass(), userRepository);

        // 3. Build DataStoreConfig map
        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = new HashMap<>();
        dataStores.put(projectRepository, createConfig(projectRepository, projectStore, repoMap));
        dataStores.put(taskRepository, createConfig(taskRepository, taskStore, repoMap));
        dataStores.put(taskCommentRepository, createConfig(taskCommentRepository, commentStore, repoMap));
        dataStores.put(imageRepository, createConfig(imageRepository, imageStore, repoMap));
        dataStores.put(videoRepository, createConfig(videoRepository, videoStore, repoMap));
        dataStores.put(userRepository, createConfig(userRepository, userStore, repoMap));
        dataStores.put(NOTES_KEY, DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory(NOTES_KEY)
                .dataStoreInstance((VortexCrudDataStore) notesStore)
                .fields(Map.of())
                .build());

        InternalFormElement<JpaRepository<?, ?>, String, JpaRepository<?, ?>> build = JpaCollectionElement.builder("route.tasks.labels.comments")
                .factory((Class<? extends VortexCrudCollectionFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) (Class<?>) ListCollectionFactory.class)
                .configuration(JpaCollection.builder((Class<? extends VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) (Class) FormDialogFactory.class)
                        .data(JpaCollectionConfiguration.builder(taskCommentRepository)
                                .dataStoreInstance((VortexCrudDataStore) commentStore)
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
        CollectionConfiguration build2 = JpaCollectionConfiguration.builder(taskRepository)
                .dataStoreInstance((VortexCrudDataStore) taskStore)
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
                .dataStoreKey(taskRepository)
                .dataStoreInstance((VortexCrudDataStore) taskStore)
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
                .dataStoreKey(projectRepository)
                .dataStoreInstance((VortexCrudDataStore) projectStore)
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
                .dataStoreKey(imageRepository)
                .dataStoreInstance((VortexCrudDataStore) imageStore)
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
                .dataStoreKey(imageRepository)
                .dataStoreInstance((VortexCrudDataStore) imageStore)
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
                .dataStoreKey(videoRepository)
                .dataStoreInstance((VortexCrudDataStore) videoStore)
                .title("route.videos.title-cards")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.builder("title", "route.videos.labels.title").build(),
                                JpaFieldElement.builder("url", "route.videos.labels.video").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JpaGridRoute.builder()
                .isDefaultRoute(true)
                .dataStoreKey(projectRepository)
                .dataStoreInstance((VortexCrudDataStore) projectStore)
                .iconFactory(FACTORY::create)
                .title("route.projects.title-cards")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("name")
                        .descriptionField("description")
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .child(projectForm)
                .build());
        routes.put("projects-list", JpaListRoute.builder()
                .dataStoreKey(projectRepository)
                .dataStoreInstance((VortexCrudDataStore) projectStore)
                .iconFactory(FACTORY::create)
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
                .build());
        routes.put("open-tasks", JpaKanbanRoute.builder()
                .iconFactory(TASKS::create)
                .dataStoreKey(taskRepository)
                .dataStoreInstance((VortexCrudDataStore) taskStore)
                .title("route.open-tasks.title")
                .configuration(JpaKanbanConfiguration.builder()
                        .titleField("title")
                        .descriptionField("description")
                        .columnField("status")
                        .rowIndexField("rowIndex")
                        .filterField("title")
                        .build())
                .writeRoles(List.of("admin", "manager", "editor", "viewer"))
                .child(taskForm)
                .build());
        routes.put("done-tasks", JpaMasterDetailRoute.builder()
                .iconFactory(CHECK_CIRCLE::create)
                .dataStoreKey(taskRepository)
                .dataStoreInstance((VortexCrudDataStore) taskStore)
                .title("route.done-tasks.title")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .descriptionField("description")
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .child(taskForm)
                .build());
        routes.put("images-grid", JpaGridRoute.builder()
                .dataStoreKey(imageRepository)
                .dataStoreInstance((VortexCrudDataStore) imageStore)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .imageField("url")
                        .resourceProvider(LocalImageResourceProvider.class)
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageForm)
                .build());
        routes.put("images-list", JpaListRoute.builder()
                .dataStoreKey(imageRepository)
                .dataStoreInstance((VortexCrudDataStore) imageStore)
                .iconFactory(CAMERA::create)
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
                .build());

        routes.put("images-slide", JpaGridRoute.builder()
                .dataStoreKey(imageRepository)
                .dataStoreInstance((VortexCrudDataStore) imageStore)
                .iconFactory(CAMERA::create)
                .title("route.images-cards")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .imageField("url")
                        .resourceProvider(LocalImageResourceProvider.class)
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageSlideForm)
                .build());

        routes.put("videos-grid", JpaGridRoute.builder()
                .dataStoreKey(videoRepository)
                .dataStoreInstance((VortexCrudDataStore) videoStore)
                .iconFactory(MOVIE::create)
                .title("route.videos.title-cards")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .build())
                .writeRoles(List.of("admin"))
                .child(videoForm)
                .build());

        routes.put("videos-list", JpaListRoute.builder()
                .dataStoreKey(videoRepository)
                .dataStoreInstance((VortexCrudDataStore) videoStore)
                .iconFactory(MOVIE::create)
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
                .build());

        routes.put("submenu", JpaSubmenuRoute.builder()
                .iconFactory(MENU::create)
                .dataStoreKey(projectRepository)
                .dataStoreInstance((VortexCrudDataStore) projectStore)
                .title("route.submenu.title")
                .childrenMap(Map.of(
                        "project-form", projectForm,
                        "image-form", imageForm))
                .build());

        routes.put("profile", JpaSingleFormRoute.builder()
                .iconFactory(USER::create)
                .dataStoreKey(userRepository)
                .dataStoreInstance((VortexCrudDataStore) userStore)
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
                .build());

        // Custom in-memory data store example
        routes.put("notes", JpaGridRoute.builder()
                .dataStoreKey(NOTES_KEY)
                .dataStoreInstance((VortexCrudDataStore) notesStore)
                .iconFactory(NOTEBOOK::create)
                .title("Notes (Custom DataStore)")
                .configuration(JpaGridItemRendererConfiguration.builder()
                        .titleField("title")
                        .descriptionField("content")
                        .build())
                .child(JpaFormRoute.builder()
                        .dataStoreKey(NOTES_KEY)
                        .dataStoreInstance((VortexCrudDataStore) notesStore)
                        .formConfiguration(JpaFormRendererConfiguration.builder()
                                .titleField("title")
                                .children(List.of(
                                        JpaFieldElement.builder("title", "Title").build(),
                                        JpaFieldElement.builder("content", "Content").build()
                                ))
                                .build())
                        .build())
                .build());

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
                                .repositoryKey(userRepository)
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
                        DataStoreDropdownMenuAction.<String, JpaRepository<?, ?>>builder()
                                .dataStoreKey(userRepository)
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
                .dataStores(dataStores)
                .build();
    }

    private DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>> createConfig(
            JpaRepository<?, ?> repo,
            JpaRepositoryDataStore<?> store,
            Map<Class<?>, JpaRepository<?, ?>> repoMap) {
        return DataStoreConfig.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .factory(repo)
                .dataStoreInstance((VortexCrudDataStore) store)
                .fields(fieldService.getFieldsForDataStore(store, repoMap))
                .build();
    }
}
