package com.github.appreciated.vortex_crud.example.jpa;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.example.jpa.entity.Status;
import com.github.appreciated.vortex_crud.example.jpa.repository.*;
import com.github.appreciated.vortex_crud.jpa.service.JpaManyToMany;
import com.github.appreciated.vortex_crud.jpa.service.JpaOneToMany;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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

    public ExampleJpaConfiguration(
            ImageRepository imageRepository,
            ProjectRepository projectRepository,
            TaskCommentRepository taskCommentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository,
            VideoRepository videoRepository
    ) {
        this.imageRepository = imageRepository;
        this.projectRepository = projectRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        InternalFormElement build = JpaCollectionElement.of("route.tasks.labels.comments")
                .configuration(JpaCollection.of(FormDialogFactory.class)
                        .data(JpaCollectionConfiguration.of(taskCommentRepository)
                                .oneToMany(new JpaOneToMany("task"))
                                .children(List.of("commentText"))
                                .build())
                        .emptyMessage("route.tasks.labels.comments-empty-message")
                        .child(JpaFormRoute.builder()
                                .configuration(JpaFormRendererConfiguration.builder()
                                        .titleField("name")
                                        .children(List.of(
                                                JpaFieldElement.of("commentText", "route.tasks.labels.comment").build()
                                        ))
                                        .build())
                                .build())
                        .build())
                .build();
        CollectionConfiguration build2 = JpaCollectionConfiguration.of(taskRepository)
                .manyToMany(new JpaManyToMany(taskRepository, "relatedTasks"))
                .children(List.of("title"))
                .build();
        InternalFormElement build1 = JpaCollectionElement.of("route.tasks.labels.related-tasks")
                .configuration(JpaCollection.of(ConnectDialogFactory.class)
                        .data(build2)
                        .emptyMessage("route.tasks.labels.related-tasks-empty-message")
                        .config(new CollectionConfig("title"))
                        .build())
                .build();
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> taskForm = JpaFormRoute.builder()
                .dataStoreKey(taskRepository)
                .configuration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.of("title", "route.tasks.labels.title").build(),
                                JpaFieldElement.of("description", "route.tasks.labels.description").build(),
                                JpaFieldElement.of("status", "route.tasks.labels.status").build(),
                                JpaFieldElement.of("dueDate", "route.tasks.labels.due_date").build(),
                                JpaFieldElement.of("assignedTo", "route.tasks.labels.assigned_to").build(),
                                build,
                                build1
                        ))
                        .build())
                .build();
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> projectForm = JpaFormRoute.builder()
                .dataStoreKey(projectRepository)
                .title("route.projects.title-cards")
                .configuration(JpaFormRendererConfiguration.builder()
                        .titleField("name")
                        .children(List.of(
                                JpaFieldElement.of("name", "route.projects.labels.name").build(),
                                JpaFieldElement.of("description", "route.projects.labels.description").build(),
                                JpaFieldElement.of("startDate", "route.projects.labels.start_date").build(),
                                JpaFieldElement.of("endDate", "route.projects.labels.end_date").build()
                        ))
                        .build())
                .build();
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaFormRoute.builder()
                .dataStoreKey(imageRepository)
                .title("route.projects.title-cards")
                .configuration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.of("title", "route.images.labels.title").build(),
                                JpaFieldElement.of("url", "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        FormSlideRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageSlideForm = JpaFormSlideRoute.builder()
                .dataStoreKey(imageRepository)
                .title("route.projects.title-cards")
                .configuration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.of("title", "route.images.labels.title").build(),
                                JpaFieldElement.of("url", "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> videoForm = JpaFormRoute.builder()
                .dataStoreKey(videoRepository)
                .title("route.videos.title-cards")
                .configuration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.of("title", "route.videos.labels.title").build(),
                                JpaFieldElement.of("url", "route.videos.labels.video").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JpaGridRoute.builder()
                .defaultRoute(true)
                .dataStoreKey(projectRepository)
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
                .iconFactory(FACTORY::create)
                .title("route.projects.title-list")
                .configuration(JpaListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField("name")
                        .children(List.of(
                                JpaFieldElement.of("name", "route.projects.labels.name").build(),
                                JpaFieldElement.of("description", "route.projects.labels.description").build(),
                                JpaFieldElement.of("startDate", "route.projects.labels.start_date").build(),
                                JpaFieldElement.of("endDate", "route.projects.labels.end_date").build()
                        ))
                        .build())
                .writeRoles(List.of("admin", "manager", "editor"))
                .child(projectForm)
                .build());
        routes.put("open-tasks", JpaKanbanRoute.builder()
                .iconFactory(TASKS::create)
                .dataStoreKey(taskRepository)
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
                .iconFactory(CAMERA::create)
                .title("route.images-list")
                .configuration(JpaListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField("title")
                        .children(List.of(
                                JpaFieldElement.of("url", "route.projects.labels.description").build(),
                                JpaFieldElement.of("title", "route.projects.labels.name").build()
                        ))
                        .build())
                .writeRoles(List.of("admin"))
                .child(imageForm)
                .build());

        routes.put("images-slide", JpaGridRoute.builder()
                .dataStoreKey(imageRepository)
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
                .iconFactory(MOVIE::create)
                .title("route.videos.title-list")
                .configuration(JpaListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField("title")
                        .children(List.of(
                                JpaFieldElement.of("title", "route.videos.labels.title").build(),
                                JpaFieldElement.of("url", "route.videos.labels.video").build()
                        ))
                        .build())
                .writeRoles(List.of("admin"))
                .child(videoForm)
                .build());

        routes.put("submenu", JpaSubmenuRoute.builder()
                .iconFactory(MENU::create)
                .dataStoreKey(projectRepository)
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

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("some_i18n")
                .identityAndAccessManagement(
                        LocalIdentityAndAccessManagement.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                                .repositoryKey(userRepository)
                                .roles(Roles.builder().roles(List.of("admin", "manager", "editor", "viewer", "guest")).build())
                                .signUpEnabled(true)
                                .loginView(LoginView.class)
                                .signUpView(SignUpView.class)
                                .username(JpaFieldElement.of("username", "route.projects.labels.name").build())
                                .password(JpaFieldElement.of("passwordHash", "route.projects.labels.password").build())
                                .signUpFields(List.of(
                                        JpaFieldElement.of("firstName", "route.projects.labels.end_date").build(),
                                        JpaFieldElement.of("lastName", "route.projects.labels.end_date").build()
                                ))
                                .rolesField("roles")
                                .build()
                )
                .routes(routes)
                .versioning(JpaVersioning.of().dataStores(List.of(projectRepository, taskRepository, taskCommentRepository)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(Selects.builder().configs(
                        Map.of("task-status", taskStatuses)).build())
                .build();
    }
}
