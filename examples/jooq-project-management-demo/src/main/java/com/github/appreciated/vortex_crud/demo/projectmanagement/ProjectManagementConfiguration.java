package com.github.appreciated.vortex_crud.demo.projectmanagement;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.Priority;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.ProjectStatus;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.TaskStatus;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.TaskType;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.core.config.model.AuditingAction.*;
import static com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.*;
import static com.vaadin.flow.component.icon.VaadinIcon.*;

@Service
public class ProjectManagementConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Data Store Configurations
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.ofEntries(
                Map.entry(PROJECT, JooqDataStoreConfig.of(PROJECT)
                        .fields(Map.of(
                                PROJECT.ID, JooqIdField.builder().build(),
                                PROJECT.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build(),
                                PROJECT.CODE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                                PROJECT.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                                PROJECT.STATUS, JooqSelectField.builder().values("project-status").build(),
                                PROJECT.PRIORITY, JooqSelectField.builder().values("priority").build(),
                                PROJECT.START_DATE, JooqDateField.builder().build(),
                                PROJECT.END_DATE, JooqDateField.builder().build(),
                                PROJECT.OWNER_ID, JooqReferenceField.builder().dataStore(USERS).field(PROJECT.OWNER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                                PROJECT.PROGRESS_PERCENTAGE, JooqIntegerField.builder().build(),
                                PROJECT.COLOR, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                                PROJECT.IS_ARCHIVED, JooqCheckboxField.builder().build(),
                                PROJECT.CREATED_AT, JooqDateTimePickerField.builder().build(),
                                PROJECT.UPDATED_AT, JooqDateTimePickerField.builder().build(),
                                PROJECT.CUSTOM_FIELDS, JooqTextAreaField.builder().build()))
                        .build()),

                Map.entry(TASK, JooqDataStoreConfig.of(TASK)
                        .fields(Map.of(
                                TASK.ID, JooqIdField.builder().build(),
                                TASK.PROJECT_ID, JooqReferenceField.builder().dataStore(PROJECT).field(TASK.PROJECT_ID).filterField(PROJECT.NAME).children(List.of(PROJECT.NAME)).build(),
                                TASK.MILESTONE_ID, JooqReferenceField.builder().dataStore(MILESTONE).field(TASK.MILESTONE_ID).filterField(MILESTONE.TITLE).children(List.of(MILESTONE.TITLE)).build(),
                                TASK.PARENT_TASK_ID, JooqReferenceField.builder().dataStore(TASK).field(TASK.PARENT_TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                                TASK.TASK_NUMBER, JooqIntegerField.builder().required(true).build(),
                                TASK.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 300 characters", 0, 300))).build(),
                                TASK.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 2000 characters", 0, 2000))).build(),
                                TASK.TASK_TYPE, JooqSelectField.builder().values("task-type").build(),
                                TASK.STATUS, JooqSelectField.builder().values("task-status").build(),
                                TASK.PRIORITY, JooqSelectField.builder().values("priority").build(),
                                TASK.ASSIGNEE_ID, JooqReferenceField.builder().dataStore(USERS).field(TASK.ASSIGNEE_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                                TASK.REPORTER_ID, JooqReferenceField.builder().dataStore(USERS).field(TASK.REPORTER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                                TASK.ESTIMATED_HOURS, JooqDoubleField.builder().build(),
                                TASK.DUE_DATE, JooqDateField.builder().build(),
                                TASK.CREATED_AT, JooqDateTimePickerField.builder().build(),
                                TASK.UPDATED_AT, JooqDateTimePickerField.builder().build(),
                                TASK.CUSTOM_FIELDS, JooqTextAreaField.builder().build()))
                        .build()),

                Map.entry(MILESTONE, JooqDataStoreConfig.of(MILESTONE)
                        .fields(Map.of(
                                MILESTONE.ID, JooqIdField.builder().build(),
                                MILESTONE.PROJECT_ID, JooqReferenceField.builder().dataStore(PROJECT).field(MILESTONE.PROJECT_ID).filterField(PROJECT.NAME).children(List.of(PROJECT.NAME)).build(),
                                MILESTONE.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build(),
                                MILESTONE.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                                MILESTONE.DUE_DATE, JooqDateField.builder().build(),
                                MILESTONE.STATUS, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build(),
                                MILESTONE.COMPLETION_PERCENTAGE, JooqIntegerField.builder().build(),
                                MILESTONE.CREATED_AT, JooqDateTimePickerField.builder().build(),
                                MILESTONE.COMPLETED_AT, JooqDateTimePickerField.builder().build(),
                                MILESTONE.CUSTOM_FIELDS, JooqTextAreaField.builder().build()))
                        .build()),

                Map.entry(LABEL, JooqDataStoreConfig.of(LABEL)
                        .fields(Map.of(
                                LABEL.ID, JooqIdField.builder().build(),
                                LABEL.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build(),
                                LABEL.COLOR, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                                LABEL.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                                LABEL.CREATED_AT, JooqDateTimePickerField.builder().build()))
                        .build()),

                Map.entry(TASK_COMMENT, JooqDataStoreConfig.of(TASK_COMMENT)
                        .fields(Map.of(
                                TASK_COMMENT.ID, JooqIdField.builder().build(),
                                TASK_COMMENT.TASK_ID, JooqReferenceField.builder().dataStore(TASK).field(TASK_COMMENT.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                                TASK_COMMENT.AUTHOR_ID, JooqReferenceField.builder().dataStore(USERS).field(TASK_COMMENT.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                                TASK_COMMENT.CONTENT, JooqTextAreaField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 2000 characters", 0, 2000))).build(),
                                TASK_COMMENT.CREATED_AT, JooqDateTimePickerField.builder().build()))
                        .build()),

                Map.entry(TASK_LABEL, JooqDataStoreConfig.of(TASK_LABEL)
                        .fields(Map.of(
                                TASK_LABEL.ID, JooqIdField.builder().build(),
                                TASK_LABEL.TASK_ID, JooqReferenceField.builder().dataStore(TASK).field(TASK_LABEL.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                                TASK_LABEL.LABEL_ID, JooqReferenceField.builder().dataStore(LABEL).field(TASK_LABEL.LABEL_ID).filterField(LABEL.NAME).children(List.of(LABEL.NAME)).build()))
                        .build()),

                Map.entry(USERS, JooqDataStoreConfig.of(USERS)
                        .fields(Map.of(
                                USERS.ID, JooqIdField.builder().build(),
                                USERS.USERNAME, JooqEmailField.builder().required(true).build(),
                                USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                                USERS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                        .build())
        );

        // Task Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStoreKey(TASK)
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(TASK.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(TASK.TITLE, "route.tasks.labels.title").build(),
                                JooqFieldElement.of(TASK.DESCRIPTION, "route.tasks.labels.description").build(),
                                JooqFieldElement.of(TASK.TASK_TYPE, "route.tasks.labels.task_type").build(),
                                JooqFieldElement.of(TASK.STATUS, "route.tasks.labels.status").build(),
                                JooqFieldElement.of(TASK.PRIORITY, "route.tasks.labels.priority").build(),
                                JooqFieldElement.of(TASK.ASSIGNEE_ID, "route.tasks.labels.assignee").build(),
                                JooqFieldElement.of(TASK.ESTIMATED_HOURS, "route.tasks.labels.estimated_hours").build(),
                                JooqFieldElement.of(TASK.DUE_DATE, "route.tasks.labels.due_date").build(),
                                JooqCollectionElement.of("route.tasks.labels.comments")
                                        .factory((Class<? extends VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class<?>) ListCollectionFactory.class)
                                        .configuration(JooqCollection.builder((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class) FormDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(TASK_COMMENT)
                                                        .oneToMany(new JooqOneToMany(TASK_COMMENT.TASK_ID))
                                                        .children(List.of(TASK_COMMENT.CONTENT, TASK_COMMENT.CREATED_AT))
                                                        .build())
                                                .emptyMessage("route.tasks.labels.comments-empty-message")
                                                .child(JooqFormRoute.builder()
                                                        .formConfiguration(JooqFormRendererConfiguration.builder()
                                                                .titleField(TASK_COMMENT.CONTENT)
                                                                .children(List.of(
                                                                        JooqFieldElement.of(TASK_COMMENT.CONTENT, "route.tasks.labels.comment").build()))
                                                                .build())
                                                        .build())
                                                .build())
                                        .build(),
                                JooqCollectionElement.of("route.tasks.labels.labels")
                                        .factory((Class<? extends VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class<?>) ListCollectionFactory.class)
                                        .configuration(JooqCollection.builder((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class) ConnectDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(LABEL)
                                                        .manyToMany(new JooqManyToMany(
                                                                TASK_LABEL.TASK_ID,
                                                                TASK_LABEL.LABEL_ID,
                                                                LABEL.ID,
                                                                TASK_LABEL))
                                                        .children(List.of(LABEL.NAME, LABEL.COLOR))
                                                        .build())
                                                .emptyMessage("route.tasks.labels.labels-empty-message")
                                                .configuration(new CollectionConfig<TableField<?, ?>>(LABEL.NAME))
                                                .build())
                                        .build()))
                        .build())
                .build();

        // Project Form Configuration
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> projectForm = JooqFormRoute.builder()
                .dataStoreKey(PROJECT)
                .title("route.projects.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(PROJECT.NAME)
                        .children(List.of(
                                JooqFieldElement.of(PROJECT.NAME, "route.projects.labels.name").build(),
                                JooqFieldElement.of(PROJECT.CODE, "route.projects.labels.code").build(),
                                JooqFieldElement.of(PROJECT.DESCRIPTION, "route.projects.labels.description").build(),
                                JooqFieldElement.of(PROJECT.STATUS, "route.projects.labels.status").build(),
                                JooqFieldElement.of(PROJECT.PRIORITY, "route.projects.labels.priority").build(),
                                JooqFieldElement.of(PROJECT.START_DATE, "route.projects.labels.start_date").build(),
                                JooqFieldElement.of(PROJECT.END_DATE, "route.projects.labels.end_date").build(),
                                JooqFieldElement.of(PROJECT.PROGRESS_PERCENTAGE, "route.projects.labels.progress").build(),
                                JooqFieldElement.of(PROJECT.COLOR, "route.projects.labels.color").build()))
                        .build())
                .build();

        // Milestone Form Configuration
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> milestoneForm = JooqFormRoute.builder()
                .dataStoreKey(MILESTONE)
                .title("route.milestones.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(MILESTONE.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                                JooqFieldElement.of(MILESTONE.DESCRIPTION, "route.milestones.labels.description").build(),
                                JooqFieldElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build(),
                                JooqFieldElement.of(MILESTONE.COMPLETION_PERCENTAGE, "route.milestones.labels.completion").build()))
                        .build())
                .build();

        // Routes Configuration
        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        routes.put("projects", JooqGridRoute.builder()
                .isDefaultRoute(true)
                .dataStoreKey(PROJECT)
                .iconFactory(RECORDS::create)
                .title("route.projects.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(PROJECT.NAME)
                        .descriptionField(PROJECT.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .child(projectForm)
                .build());

        routes.put("tasks-kanban", JooqKanbanRoute.builder()
                .iconFactory(TASKS::create)
                .dataStoreKey(TASK)
                .title("route.tasks.title")
                .configuration(JooqKanbanConfiguration.builder()
                        .titleField(TASK.TITLE)
                        .descriptionField(TASK.DESCRIPTION)
                        .columnField(TASK.STATUS)
                        .filterField(TASK.TITLE)
                        .build())
                .writeRoles(List.of("admin", "manager", "developer"))
                .child(taskForm)
                .build());

        routes.put("milestones", JooqGridRoute.builder()
                .dataStoreKey(MILESTONE)
                .iconFactory(FLAG::create)
                .title("route.milestones.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(MILESTONE.TITLE)
                        .descriptionField(MILESTONE.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin", "manager"))
                .child(milestoneForm)
                .build());

        // Select Options
        LinkedHashMap<ProjectStatus, String> projectStatuses = new LinkedHashMap<>();
        projectStatuses.put(ProjectStatus.PLANNING, "selects.project-status.planning");
        projectStatuses.put(ProjectStatus.ACTIVE, "selects.project-status.active");
        projectStatuses.put(ProjectStatus.ON_HOLD, "selects.project-status.on_hold");
        projectStatuses.put(ProjectStatus.COMPLETED, "selects.project-status.completed");
        projectStatuses.put(ProjectStatus.CANCELLED, "selects.project-status.cancelled");

        LinkedHashMap<TaskStatus, String> taskStatuses = new LinkedHashMap<>();
        taskStatuses.put(TaskStatus.TODO, "selects.task-status.todo");
        taskStatuses.put(TaskStatus.IN_PROGRESS, "selects.task-status.in_progress");
        taskStatuses.put(TaskStatus.IN_REVIEW, "selects.task-status.in_review");
        taskStatuses.put(TaskStatus.DONE, "selects.task-status.done");
        taskStatuses.put(TaskStatus.BLOCKED, "selects.task-status.blocked");

        LinkedHashMap<TaskType, String> taskTypes = new LinkedHashMap<>();
        taskTypes.put(TaskType.TASK, "selects.task-type.task");
        taskTypes.put(TaskType.BUG, "selects.task-type.bug");
        taskTypes.put(TaskType.STORY, "selects.task-type.story");
        taskTypes.put(TaskType.EPIC, "selects.task-type.epic");
        taskTypes.put(TaskType.SUBTASK, "selects.task-type.subtask");

        LinkedHashMap<Priority, String> priorities = new LinkedHashMap<>();
        priorities.put(Priority.LOWEST, "selects.priority.lowest");
        priorities.put(Priority.LOW, "selects.priority.low");
        priorities.put(Priority.MEDIUM, "selects.priority.medium");
        priorities.put(Priority.HIGH, "selects.priority.high");
        priorities.put(Priority.HIGHEST, "selects.priority.highest");
        priorities.put(Priority.CRITICAL, "selects.priority.critical");

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("pm_i18n")
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .repositoryKey(USERS)
                        .availableRoles(Roles.builder().roles(List.of("admin", "manager", "developer", "viewer")).build())
                        .defaultReadRoles(List.of("viewer"))
                        .defaultWriteRoles(List.of("admin", "manager"))
                        .signUpEnabled(true)
                        .loginView(LoginView.class)
                        .signUpView(SignUpView.class)
                        .username(JooqFieldElement.of(USERS.USERNAME, "route.users.labels.username").build())
                        .password(JooqFieldElement.of(USERS.PASSWORD_HASH, "route.users.labels.password").build())
                        .signUpFields(List.of())
                        .rolesField(null)
                        .build())
                .routes(routes)
                .versioning(JooqVersioning.builder().dataStores(List.of(PROJECT, TASK, MILESTONE, TASK_COMMENT)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(Selects.builder()
                        .configs(Map.of(
                                "project-status", projectStatuses,
                                "task-status", taskStatuses,
                                "task-type", taskTypes,
                                "priority", priorities))
                        .build())
                .dataStores(dataStores)
                .build();
    }
}
