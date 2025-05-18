package com.github.appreciated.vortex_crud.example.jpa;


import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.file_provider.ImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.vortex_crud.example.jpa.repository.*;
import com.github.appreciated.vortex_crud.jpa.service.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ExampleJpaConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String> {

    private final ImageRepository imageRepository;
    private final ProjectRepository projectRepository;
    private final TaskCommentRepository taskCommentRepository;
    private final TaskHasTaskRepository taskHasTaskRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public ExampleJpaConfiguration(
            ImageRepository imageRepository,
            ProjectRepository projectRepository,
            TaskCommentRepository taskCommentRepository,
            TaskHasTaskRepository taskHasTaskRepository,
            TaskRepository taskRepository,
            UserRepository userRepository
    ) {
        this.imageRepository = imageRepository;
        this.projectRepository = projectRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.taskHasTaskRepository = taskHasTaskRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String> get() {
        RouteRenderer<JpaRepository<?, ?>, String> taskForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(taskRepository)
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JpaFieldElement("title", "route.tasks.labels.title"),
                                new JpaFieldElement("description", "route.tasks.labels.description"),
                                new JpaFieldElement("status", "route.tasks.labels.status"),
                                new JpaFieldElement("due_date", "route.tasks.labels.due_date"),
                                new JpaFieldElement("assigned_to", "route.tasks.labels.assigned_to"),
                                JpaCollectionElement.of("route.tasks.labels.comments")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.<JpaRepository<?, ?>, String>of(FormDialogFactory.class)
                                                .withData(CollectionData.Builder.<JpaRepository<?, ?>, String>of(taskCommentRepository)
                                                        .withOneToMany(new OneToMany<>("task_id"))
                                                        .withChildren("comment_text")
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.comments-empty-message")
                                                .withChild(JpaRouteRenderer.of(FormRouteFactory.class)
                                                        .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                                                                .withTitleField("name")
                                                                .withChildren(
                                                                        new JpaFieldElement("comment_text", "route.tasks.labels.comment")
                                                                )
                                                                .build())
                                                        .build())
                                                .build())
                                        .build(),
                                JpaCollectionElement.of("route.tasks.labels.related-tasks")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(Collection.Builder.<JpaRepository<?, ?>, String>of(ConnectDialogFactory.class)
                                                .withData(CollectionData.Builder.<JpaRepository<?, ?>, String>of(taskRepository)
                                                        .withManyToMany(new ManyToMany<>(taskHasTaskRepository,
                                                                "task_id",
                                                                "related_task_id",
                                                                "id"))
                                                        .withChildren("title")
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.related-tasks-empty-message")
                                                .withConfiguration(new CollectionConfig("title"))
                                                .build())
                                        .build()
                        )
                        .build())
                .build();
        RouteRenderer<JpaRepository<?, ?>, String> projectForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(projectRepository)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new JpaFieldElement("name", "route.projects.labels.name"),
                                new JpaFieldElement("description", "route.projects.labels.description"),
                                new JpaFieldElement("start_date", "route.projects.labels.start_date"),
                                new JpaFieldElement("end_date", "route.projects.labels.end_date")
                        )
                        .build())
                .build();
        RouteRenderer<JpaRepository<?, ?>, String> imageForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(imageRepository)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JpaFieldElement("title", "route.images.labels.title"),
                                new JpaFieldElement("url", "route.images.labels.image")
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JpaRouteRenderer.of(GridRouteFactory.class)
                .withDefaultRoute(true)
                .withDataStore(projectRepository)
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-cards")
                .withConfiguration(GridOrListRendererConfiguration.Builder.<JpaRepository<?, ?>, String>of(CardFactory.class)
                        .withTitleField("name")
                        .withDescriptionField("description")
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(projectForm)
                .build());
        routes.put("projects-list", JpaRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(projectRepository)
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-list")
                .withConfiguration(GridOrListRendererConfiguration.Builder.<JpaRepository<?, ?>, String>of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField("name")
                        .withChildren(
                                new JpaFieldElement("name", "route.projects.labels.name"),
                                new JpaFieldElement("description", "route.projects.labels.description"),
                                new JpaFieldElement("start_date", "route.projects.labels.start_date"),
                                new JpaFieldElement("end_date", "route.projects.labels.end_date")
                        )
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(projectForm)
                .build());
        routes.put("tasks", JpaRouteRenderer.of(SubmenuRouteFactory.class)
                .withIconFactory(TASKS::create)
                .withDataStore(taskCommentRepository)
                .withTitle("route.tasks.title")
                .withChildrenMap(Map.of("open",
                        JpaRouteRenderer.of(KanbanDetailFactory.class)
                                .withIconFactory(TASKS::create)
                                .withDataStore(taskCommentRepository)
                                .withTitle("route.open-tasks.title")
                                .withConfiguration(Kanban.Builder.<JpaRepository<?, ?>, String>of(CardFactory.class)
                                        .withTitleField("title")
                                        .withDescriptionField("description")
                                        .withColumnField("status")
                                        .build())
                                .withChild(taskForm)
                                .build(),
                        "done",
                        JpaRouteRenderer.of(MasterDetailRouteFactory.class)
                                .withIconFactory(CHECK_CIRCLE::create)
                                .withDataStore(taskCommentRepository)
                                .withTitle("route.done-tasks.title")
                                .withConfiguration(GridOrListRendererConfiguration.Builder.<JpaRepository<?, ?>, String>of(CardFactory.class)
                                        .withTitleField("title")
                                        .withDescriptionField("description")
                                        .build())
                                .withChild(taskForm)
                                .build()))
                .build());
        routes.put("images-grid", JpaRouteRenderer.of(GridRouteFactory.class)
                .withDataStore(imageRepository)
                .withIconFactory(CAMERA::create)
                .withTitle("route.images-cards")
                .withConfiguration(GridOrListRendererConfiguration.Builder.<JpaRepository<?, ?>, String>of(CardFactory.class)
                        .withTitleField("title")
                        .withImageField("url")
                        .withImageFactory(ImageResourceProvider.class)
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(imageForm)
                .build());
        routes.put("images-list", JpaRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(imageRepository)
                .withIconFactory(CAMERA::create)
                .withTitle("route.images-list")
                .withConfiguration(GridOrListRendererConfiguration.Builder.<JpaRepository<?, ?>, String>of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField("title")
                        .withChildren(
                                new JpaFieldElement("url", "route.projects.labels.description"),
                                new JpaFieldElement("title", "route.projects.labels.name")
                        )
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(imageForm)
                .build());

        LinkedHashMap<String, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put("todo", "selects.task-status.todo");
        taskStatuses.put("open", "selects.task-status.open");
        taskStatuses.put("work-in-progress", "selects.task-status.progress");
        taskStatuses.put("closed", "selects.task-status.closed");

        return JpaApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("some_i18n")
                .withUserManagement(UserManagement.Builder.of()
                        .withEnabled(true)
                        .withAccessControl(AccessControl.Builder.of().withRoles(List.of("manager", "admin")).build())
                        .withSignUp(true)
                        .withAdditionalFields(List.of(AdditionalField.Builder.of()
                                .withName("start_date")
                                .withType("date")
                                .build()))
                        .build())
                .withRoutes(routes)
                .withVersioning(Versioning.Builder.<JpaRepository<?, ?>>of().withDataStores(projectRepository, taskRepository, taskCommentRepository).build())
                .withAuditing(Auditing.Builder.of().withActions("create", "update", "delete", "login", "logout").build())
                .withSelects(Selects.Builder.of().withConfigs(
                        Map.of("task-status", taskStatuses)).build())
                .build();
    }
}
