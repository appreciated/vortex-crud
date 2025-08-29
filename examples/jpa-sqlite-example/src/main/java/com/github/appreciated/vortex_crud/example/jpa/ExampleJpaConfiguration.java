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
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormSlideRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.vortex_crud.example.jpa.entity.Status;
import com.github.appreciated.vortex_crud.example.jpa.repository.ImageRepository;
import com.github.appreciated.vortex_crud.example.jpa.repository.ProjectRepository;
import com.github.appreciated.vortex_crud.example.jpa.repository.TaskCommentRepository;
import com.github.appreciated.vortex_crud.example.jpa.repository.TaskRepository;
import com.github.appreciated.vortex_crud.jpa.service.JpaManyToMany;
import com.github.appreciated.vortex_crud.jpa.service.JpaOneToMany;
import com.github.appreciated.vortex_crud.jpa.service.JpaRouteRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
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

    public ExampleJpaConfiguration(
            ImageRepository imageRepository,
            ProjectRepository projectRepository,
            TaskCommentRepository taskCommentRepository,
            TaskRepository taskRepository
    ) {
        this.imageRepository = imageRepository;
        this.projectRepository = projectRepository;
        this.taskCommentRepository = taskCommentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> taskForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(taskRepository)
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JpaFieldElement("title", "route.tasks.labels.title"),
                                new JpaFieldElement("description", "route.tasks.labels.description"),
                                new JpaFieldElement("status", "route.tasks.labels.status"),
                                new JpaFieldElement("dueDate", "route.tasks.labels.due_date"),
                                new JpaFieldElement("assignedTo", "route.tasks.labels.assigned_to"),
                                JpaCollectionElement.of("route.tasks.labels.comments")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(JpaCollection.of(FormDialogFactory.class)
                                                .withData(JpaCollectionConfiguration.of(taskCommentRepository)
                                                        .withOneToMany(new JpaOneToMany("task"))
                                                        .withChildren("commentText")
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.comments-empty-message")
                                                .withChild(JpaRouteRenderer.of(FormRouteFactory.class)
                                                        .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                                                                .withTitleField("name")
                                                                .withChildren(
                                                                        new JpaFieldElement("commentText", "route.tasks.labels.comment")
                                                                )
                                                                .build())
                                                        .build())
                                                .build())
                                        .build(),
                                JpaCollectionElement.of("route.tasks.labels.related-tasks")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(JpaCollection.of(ConnectDialogFactory.class)
                                                .withData(JpaCollectionConfiguration.of(taskRepository)
                                                        .withManyToMany(new JpaManyToMany(taskRepository, "relatedTasks"))
                                                        .withChildren("title")
                                                        .build())
                                                .withEmptyMessage("route.tasks.labels.related-tasks-empty-message")
                                                .withConfiguration(new CollectionConfig("title"))
                                                .build())
                                        .build()
                        )
                        .build())
                .build();
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> projectForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(projectRepository)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new JpaFieldElement("name", "route.projects.labels.name"),
                                new JpaFieldElement("description", "route.projects.labels.description"),
                                new JpaFieldElement("startDate", "route.projects.labels.start_date"),
                                new JpaFieldElement("endDate", "route.projects.labels.end_date")
                        )
                        .build())
                .build();
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaRouteRenderer.of(FormRouteFactory.class)
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

        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageSlideForm = JpaRouteRenderer.of(FormSlideRouteFactory.class)
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

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JpaRouteRenderer.of(GridRouteFactory.class)
                .withDefaultRoute(true)
                .withDataStore(projectRepository)
                .withIconFactory(FACTORY::create)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
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
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField("name")
                        .withChildren(
                                new JpaFieldElement("name", "route.projects.labels.name"),
                                new JpaFieldElement("description", "route.projects.labels.description"),
                                new JpaFieldElement("startDate", "route.projects.labels.start_date"),
                                new JpaFieldElement("endDate", "route.projects.labels.end_date")
                        )
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(projectForm)
                .build());
        routes.put("tasks", JpaRouteRenderer.of(SubmenuRouteFactory.class)
                .withIconFactory(TASKS::create)
                .withDataStore(taskRepository)
                .withTitle("route.tasks.title")
                .withChildrenMap(Map.of("open",
                        JpaRouteRenderer.of(KanbanDetailFactory.class)
                                .withIconFactory(TASKS::create)
                                .withDataStore(taskRepository)
                                .withTitle("route.open-tasks.title")
                                .withConfiguration(JpaKanban.of(CardFactory.class)
                                        .withTitleField("title")
                                        .withDescriptionField("description")
                                        .withColumnField("status")
                                        .withRowIndexField("rowIndex")
                                        .build())
                                .withChild(taskForm)
                                .build(),
                        "done",
                        JpaRouteRenderer.of(MasterDetailRouteFactory.class)
                                .withIconFactory(CHECK_CIRCLE::create)
                                .withDataStore(taskRepository)
                                .withTitle("route.done-tasks.title")
                                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
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
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
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
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
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

        routes.put("images-slide", JpaRouteRenderer.of(GridRouteFactory.class)
                .withDataStore(imageRepository)
                .withIconFactory(CAMERA::create)
                .withTitle("route.images-cards")
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withImageField("url")
                        .withImageFactory(ImageResourceProvider.class)
                        .build())
                .withRoles(List.of("manager", "admin"))
                .withChild(imageSlideForm)
                .build());

        LinkedHashMap<Status, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put(TODO, "selects.task-status.todo");
        taskStatuses.put(OPEN, "selects.task-status.open");
        taskStatuses.put(WORK_IN_PROGRESS, "selects.task-status.progress");
        taskStatuses.put(CLOSED, "selects.task-status.closed");

        return JpaApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("some_i18n")
                .withUserManagement(UserManagement.of()
                        .withEnabled(true)
                        .withAccessControl(AccessControl.of().withRoles(List.of("manager", "admin")).build())
                        .withSignUp(true)
                        .withAdditionalFields(List.of(AdditionalField.of()
                                .withName("startDate")
                                .withType("date")
                                .build()))
                        .build())
                .withRoutes(routes)
                .withVersioning(JpaVersioning.of().withDataStores(projectRepository, taskRepository, taskCommentRepository).build())
                .withAuditing(Auditing.of().withActions(CREATE, UPDATE, DELETE, LOGIN, LOGOUT).build())
                .withSelects(Selects.of().withConfigs(
                        Map.of("task-status", taskStatuses)).build())
                .build();
    }
}
