package com.github.appreciated.vortex_crud.demo.projectmanagement;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.file_provider.LocalFileResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.SingleEntityRouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.Priority;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.ProjectStatus;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.SprintStatus;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.TaskStatus;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.TaskType;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.*;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.github.appreciated.vortex_crud.security.core.strategy.ClassBasedRoleResolutionStrategy;
import com.github.appreciated.vortex_crud.security.core.strategy.JoinTableRoleResolutionStrategy;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinServletRequest;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.appreciated.vortex_crud.core.config.model.AuditingAction.*;
import static com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.*;

@Service
public class ProjectManagementConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public ProjectManagementConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Data Stores
        JooqDataStore<ProjectRecord> projectStore = new JooqDataStore<>(PROJECT.getRecordType(), dsl);
        JooqDataStore<SprintRecord> sprintStore = new JooqDataStore<>(SPRINT.getRecordType(), dsl);
        JooqDataStore<UsersRecord> usersStore = new JooqDataStore<>(USERS.getRecordType(), dsl);
        JooqDataStore<NotificationRecord> notificationStore = new JooqDataStore<>(NOTIFICATION.getRecordType(), dsl);

        // Notification Hooks
        DataStoreHooks<TaskRecord> taskHooks = DataStoreHooks.<TaskRecord>builder()
                .afterCreate(record -> {
                    try {
                        String title = record.get(TASK.TITLE);
                        Integer projectId = record.get(TASK.PROJECT_ID);
                        Integer assigneeId = record.get(TASK.ASSIGNEE_ID);
                        var project = dsl.selectFrom(PROJECT).where(PROJECT.ID.eq(projectId)).fetchOne();
                        if (project != null && assigneeId != null) {
                            var notif = dsl.newRecord(NOTIFICATION);
                            notif.setUserId(assigneeId);
                            notif.setTitle("Assigned to Task: " + title);
                            notif.setMessage("In project " + project.getName());
                            notif.setIsRead(0);
                            notif.setCreatedAt(java.time.LocalDateTime.now());
                            notif.store();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .build();

        JooqDataStore<TaskRecord> taskStore = new JooqDataStore<>(TASK.getRecordType(), dsl, taskHooks);
        JooqDataStore<MilestoneRecord> milestoneStore = new JooqDataStore<>(MILESTONE.getRecordType(), dsl);
        JooqDataStore<LabelRecord> labelStore = new JooqDataStore<>(LABEL.getRecordType(), dsl);
        JooqDataStore<TaskCommentRecord> taskCommentStore = new JooqDataStore<>(TASK_COMMENT.getRecordType(), dsl);
        JooqDataStore<TaskLabelRecord> taskLabelStore = new JooqDataStore<>(TASK_LABEL.getRecordType(), dsl);
        JooqDataStore<RolesRecord> rolesStore = new JooqDataStore<>(ROLES.getRecordType(), dsl);
        JooqDataStore<UserRolesRecord> userRolesStore = new JooqDataStore<>(USER_ROLES.getRecordType(), dsl);
        JooqDataStore<ProjectMemberRecord> projectMemberStore = new JooqDataStore<>(PROJECT_MEMBER.getRecordType(), dsl);
        JooqDataStore<TimeEntryRecord> timeEntryStore = new JooqDataStore<>(TIME_ENTRY.getRecordType(), dsl);
        JooqDataStore<AttachmentRecord> attachmentStore = new JooqDataStore<>(ATTACHMENT.getRecordType(), dsl);

        // Configs
        var usersConfig = JooqDataStoreConfig.of(USERS)
                .dataStoreInstance(usersStore)
                .fields(Map.of(
                        USERS.ID, JooqNumericIdField.builder().build(),
                        USERS.USERNAME, JooqEmailField.builder().required(true).build(),
                        USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        USERS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var projectMemberConfig = JooqDataStoreConfig.of(PROJECT_MEMBER)
                .dataStoreInstance(projectMemberStore)
                .fields(Map.of(
                        PROJECT_MEMBER.ID, JooqNumericIdField.builder().build(),
                        PROJECT_MEMBER.PROJECT_ID, JooqReferenceField.builder().dataStore(projectStore).field(PROJECT_MEMBER.PROJECT_ID).filterField(PROJECT.NAME).children(List.of(PROJECT.NAME)).build(),
                        PROJECT_MEMBER.USER_ID, JooqReferenceField.builder().dataStore(usersStore).field(PROJECT_MEMBER.USER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        PROJECT_MEMBER.ROLE, JooqSelectField.builder().values("project-roles").build(),
                        PROJECT_MEMBER.JOINED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var projectConfig = JooqDataStoreConfig.of(PROJECT)
                .dataStoreInstance(projectStore)
                .fields(Map.ofEntries(
                        Map.entry(PROJECT.ID, JooqNumericIdField.builder().build()),
                        Map.entry(PROJECT.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build()),
                        Map.entry(PROJECT.CODE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build()),
                        Map.entry(PROJECT.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build()),
                        Map.entry(PROJECT.STATUS, JooqSelectField.builder().values("project-status").build()),
                        Map.entry(PROJECT.PRIORITY, JooqSelectField.builder().values("priority").build()),
                        Map.entry(PROJECT.START_DATE, JooqDateField.builder().build()),
                        Map.entry(PROJECT.END_DATE, JooqDateField.builder().build()),
                        Map.entry(PROJECT.OWNER_ID, JooqReferenceField.builder().dataStore(usersStore).field(PROJECT.OWNER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(PROJECT.PROGRESS_PERCENTAGE, JooqIntegerField.builder().build()),
                        Map.entry(PROJECT.COLOR, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build()),
                        Map.entry(PROJECT.IS_ARCHIVED, JooqCheckboxField.builder().build()),
                        Map.entry(PROJECT.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PROJECT.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PROJECT.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                .build();

        var sprintConfig = JooqDataStoreConfig.of(SPRINT)
                .dataStoreInstance(sprintStore)
                .fields(Map.of(
                        SPRINT.ID, JooqNumericIdField.builder().build(),
                        SPRINT.PROJECT_ID, JooqReferenceField.builder().dataStore(projectStore).field(SPRINT.PROJECT_ID).filterField(PROJECT.NAME).children(List.of(PROJECT.NAME)).build(),
                        SPRINT.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build(),
                        SPRINT.GOAL, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                        SPRINT.START_DATE, JooqDateField.builder().build(),
                        SPRINT.END_DATE, JooqDateField.builder().build(),
                        SPRINT.STATUS, JooqSelectField.builder().values("sprint-status").build(),
                        SPRINT.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var milestoneConfig = JooqDataStoreConfig.of(MILESTONE)
                .dataStoreInstance(milestoneStore)
                .fields(Map.of(
                        MILESTONE.ID, JooqNumericIdField.builder().build(),
                        MILESTONE.PROJECT_ID, JooqReferenceField.builder().dataStore(projectStore).field(MILESTONE.PROJECT_ID).filterField(PROJECT.NAME).children(List.of(PROJECT.NAME)).build(),
                        MILESTONE.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build(),
                        MILESTONE.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                        MILESTONE.DUE_DATE, JooqDateField.builder().build(),
                        MILESTONE.STATUS, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build(),
                        MILESTONE.COMPLETION_PERCENTAGE, JooqIntegerField.builder().build(),
                        MILESTONE.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        MILESTONE.COMPLETED_AT, JooqDateTimePickerField.builder().build(),
                        MILESTONE.CUSTOM_FIELDS, JooqTextAreaField.builder().build()))
                .build();

        var taskConfig = JooqDataStoreConfig.of(TASK)
                .dataStoreInstance(taskStore)
                .fields(Map.ofEntries(
                        Map.entry(TASK.ID, JooqNumericIdField.builder().build()),
                        Map.entry(TASK.PROJECT_ID, JooqReferenceField.builder().dataStore(projectStore).field(TASK.PROJECT_ID).filterField(PROJECT.NAME).children(List.of(PROJECT.NAME)).build()),
                        Map.entry(TASK.MILESTONE_ID, JooqReferenceField.builder().dataStore(milestoneStore).field(TASK.MILESTONE_ID).filterField(MILESTONE.TITLE).children(List.of(MILESTONE.TITLE)).build()),
                        Map.entry(TASK.SPRINT_ID, JooqReferenceField.builder().dataStore(sprintStore).field(TASK.SPRINT_ID).filterField(SPRINT.NAME).children(List.of(SPRINT.NAME)).build()),
                        Map.entry(TASK.PARENT_TASK_ID, JooqReferenceField.builder().dataStore(taskStore).field(TASK.PARENT_TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build()),
                        Map.entry(TASK.TASK_NUMBER, JooqIntegerField.builder().required(true).build()),
                        Map.entry(TASK.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 300 characters", 0, 300))).build()),
                        Map.entry(TASK.DESCRIPTION, JooqMarkDownField.builder().validators(List.of(new StringLengthValidator("Maximum 2000 characters", 0, 2000))).build()),
                        Map.entry(TASK.TASK_TYPE, JooqSelectField.builder().values("task-type").build()),
                        Map.entry(TASK.STATUS, JooqSelectField.builder().values("task-status").build()),
                        Map.entry(TASK.PRIORITY, JooqSelectField.builder().values("priority").build()),
                        Map.entry(TASK.ASSIGNEE_ID, JooqReferenceField.builder().dataStore(usersStore).field(TASK.ASSIGNEE_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(TASK.REPORTER_ID, JooqReferenceField.builder().dataStore(usersStore).field(TASK.REPORTER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(TASK.ESTIMATED_HOURS, JooqDoubleField.builder().build()),
                        Map.entry(TASK.DUE_DATE, JooqDateField.builder().build()),
                        Map.entry(TASK.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(TASK.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(TASK.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                .build();

        var labelConfig = JooqDataStoreConfig.of(LABEL)
                .dataStoreInstance(labelStore)
                .fields(Map.of(
                        LABEL.ID, JooqNumericIdField.builder().build(),
                        LABEL.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build(),
                        LABEL.COLOR, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                        LABEL.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                        LABEL.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var taskCommentConfig = JooqDataStoreConfig.of(TASK_COMMENT)
                .dataStoreInstance(taskCommentStore)
                .fields(Map.of(
                        TASK_COMMENT.ID, JooqNumericIdField.builder().build(),
                        TASK_COMMENT.TASK_ID, JooqReferenceField.builder().dataStore(taskStore).field(TASK_COMMENT.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                        TASK_COMMENT.AUTHOR_ID, JooqReferenceField.builder().dataStore(usersStore).field(TASK_COMMENT.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        TASK_COMMENT.CONTENT, JooqTextAreaField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 2000 characters", 0, 2000))).build(),
                        TASK_COMMENT.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var taskLabelConfig = JooqDataStoreConfig.of(TASK_LABEL)
                .dataStoreInstance(taskLabelStore)
                .fields(Map.of(
                        TASK_LABEL.ID, JooqNumericIdField.builder().build(),
                        TASK_LABEL.TASK_ID, JooqReferenceField.builder().dataStore(taskStore).field(TASK_LABEL.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                        TASK_LABEL.LABEL_ID, JooqReferenceField.builder().dataStore(labelStore).field(TASK_LABEL.LABEL_ID).filterField(LABEL.NAME).children(List.of(LABEL.NAME)).build()))
                .build();

        var timeEntryConfig = JooqDataStoreConfig.of(TIME_ENTRY)
                .dataStoreInstance(timeEntryStore)
                .fields(Map.of(
                        TIME_ENTRY.ID, JooqNumericIdField.builder().build(),
                        TIME_ENTRY.TASK_ID, JooqReferenceField.builder().dataStore(taskStore).field(TIME_ENTRY.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                        TIME_ENTRY.USER_ID, JooqReferenceField.builder().dataStore(usersStore).field(TIME_ENTRY.USER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        TIME_ENTRY.HOURS_SPENT, JooqDoubleField.builder().required(true).build(),
                        TIME_ENTRY.DESCRIPTION, JooqTextAreaField.builder().build(),
                        TIME_ENTRY.ENTRY_DATE, JooqDateField.builder().required(true).build(),
                        TIME_ENTRY.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var attachmentConfig = JooqDataStoreConfig.of(ATTACHMENT)
                .dataStoreInstance(attachmentStore)
                .fields(Map.of(
                        ATTACHMENT.ID, JooqNumericIdField.builder().build(),
                        ATTACHMENT.TASK_ID, JooqReferenceField.builder().dataStore(taskStore).field(ATTACHMENT.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                        ATTACHMENT.UPLOADER_ID, JooqReferenceField.builder().dataStore(usersStore).field(ATTACHMENT.UPLOADER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        ATTACHMENT.NAME, JooqTextField.builder().required(true).build(),
                        ATTACHMENT.PATH, JooqFileField.builder().resourceProvider(new LocalFileResourceProvider("attachments")).required(true).build(),
                        ATTACHMENT.UPLOADED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var notificationConfig = JooqDataStoreConfig.of(NOTIFICATION)
                .dataStoreInstance(notificationStore)
                .fields(Map.of(
                        NOTIFICATION.ID, JooqNumericIdField.builder().build(),
                        NOTIFICATION.TITLE, JooqTextField.builder().build(),
                        NOTIFICATION.MESSAGE, JooqTextAreaField.builder().build(),
                        NOTIFICATION.LINK, JooqTextField.builder().build(),
                        NOTIFICATION.IS_READ, JooqCheckboxField.builder().build(),
                        NOTIFICATION.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        // Project Form Configuration
        var projectForm = JooqFormRoute.builder()
                .titleField(PROJECT.NAME)
                .fields(List.of(
                        JooqFormElement.of(PROJECT.NAME, "route.projects.labels.name").build(),
                        JooqFormElement.of(PROJECT.CODE, "route.projects.labels.code").build(),
                        JooqFormElement.of(PROJECT.DESCRIPTION, "route.projects.labels.description").build(),
                        JooqFormElement.of(PROJECT.STATUS, "route.projects.labels.status").build(),
                        JooqFormElement.of(PROJECT.PRIORITY, "route.projects.labels.priority").build(),
                        JooqFormElement.of(PROJECT.START_DATE, "route.projects.labels.start_date").build(),
                        JooqFormElement.of(PROJECT.END_DATE, "route.projects.labels.end_date").build(),
                        JooqFormElement.of(PROJECT.PROGRESS_PERCENTAGE, "route.projects.labels.progress").build(),
                        JooqFormElement.of(PROJECT.COLOR, "route.projects.labels.color").build(),
                        JooqCollection.builder()
                                .label("route.projects.labels.members")
                                .field(PROJECT_MEMBER.USER_ID)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(projectMemberConfig)
                                .oneToMany(new JooqOneToMany(PROJECT_MEMBER.PROJECT_ID))
                                .children(List.of(PROJECT_MEMBER.USER_ID, PROJECT_MEMBER.ROLE))
                                .form(JooqFormRoute.builder()
                                        .titleField(PROJECT_MEMBER.USER_ID)
                                        .fields(List.of(
                                                JooqFormElement.of(PROJECT_MEMBER.USER_ID, "route.project_members.labels.user").build(),
                                                JooqFormElement.of(PROJECT_MEMBER.ROLE, "route.project_members.labels.role").build()
                                        ))
                                        .build())
                                .build()))
                .build();

        // User Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> userForm = JooqFormRoute.builder()
                .titleField(USERS.USERNAME)
                .fields(List.of(
                        JooqFormElement.of(USERS.USERNAME, "route.users.labels.username").build(),
                        JooqFormElement.of(USERS.PASSWORD_HASH, "route.users.labels.password").build(),
                        JooqCollection.builder()
                                .label("route.users.labels.projects")
                                .field(PROJECT_MEMBER.PROJECT_ID)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(projectMemberConfig)
                                .oneToMany(new JooqOneToMany(PROJECT_MEMBER.USER_ID))
                                .children(List.of(PROJECT_MEMBER.PROJECT_ID, PROJECT_MEMBER.ROLE))
                                .form(JooqFormRoute.builder()
                                        .titleField(PROJECT_MEMBER.PROJECT_ID)
                                        .fields(List.of(
                                                JooqFormElement.of(PROJECT_MEMBER.PROJECT_ID, "route.project_members.labels.project").build(),
                                                JooqFormElement.of(PROJECT_MEMBER.ROLE, "route.project_members.labels.role").build()
                                        ))
                                        .build())
                                .build()))
                .build();

        // Task Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .titleField(TASK.TITLE)
                .routeActions(List.of(
                        SingleEntityRouteAction.<TableField<?, ?>, TableRecord<?>>builder()
                                .componentFactory(() -> {
                                    Button button = new Button("Start Progress");
                                    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                                    return button;
                                })
                                .handler(context -> {
                                    TableRecord<?> record = context.getFirstSelectedEntity();
                                    record.set((TableField) TASK.STATUS, "in_progress");
                                    context.dataStore().updateRecord(record);
                                    context.refreshCallback().run();
                                })
                                .build(),
                        SingleEntityRouteAction.<TableField<?, ?>, TableRecord<?>>builder()
                                .componentFactory(() -> new Button("Review"))
                                .handler(context -> {
                                    TableRecord<?> record = context.getFirstSelectedEntity();
                                    record.set((TableField) TASK.STATUS, "in_review");
                                    context.dataStore().updateRecord(record);
                                    context.refreshCallback().run();
                                })
                                .build(),
                        SingleEntityRouteAction.<TableField<?, ?>, TableRecord<?>>builder()
                                .componentFactory(() -> {
                                    Button button = new Button("Done");
                                    button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
                                    return button;
                                })
                                .handler(context -> {
                                    TableRecord<?> record = context.getFirstSelectedEntity();
                                    record.set((TableField) TASK.STATUS, "done");
                                    context.dataStore().updateRecord(record);
                                    context.refreshCallback().run();
                                })
                                .build()
                ))
                .fields(List.of(
                        JooqFormElement.of(TASK.TITLE, "route.tasks.labels.title").build(),
                        JooqFormElement.of(TASK.DESCRIPTION, "route.tasks.labels.description").build(),
                        JooqFormElement.of(TASK.TASK_TYPE, "route.tasks.labels.task_type").build(),
                        JooqFormElement.of(TASK.STATUS, "route.tasks.labels.status").build(),
                        JooqFormElement.of(TASK.PRIORITY, "route.tasks.labels.priority").build(),
                        JooqFormElement.of(TASK.ASSIGNEE_ID, "route.tasks.labels.assignee").build(),
                        JooqFormElement.of(TASK.ESTIMATED_HOURS, "route.tasks.labels.estimated_hours").build(),
                        JooqFormElement.of(TASK.DUE_DATE, "route.tasks.labels.due_date").build(),
                        JooqFormElement.of(TASK.PARENT_TASK_ID, "route.tasks.labels.parent_task").build(),
                        JooqFormElement.of(TASK.SPRINT_ID, "route.tasks.labels.sprint").build(),
                        JooqCollection.builder()
                                .label("route.tasks.labels.subtasks")
                                .field(TASK.TITLE)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(taskConfig)
                                .oneToMany(new JooqOneToMany(TASK.PARENT_TASK_ID))
                                .children(List.of(TASK.TASK_NUMBER, TASK.TITLE, TASK.STATUS))
                                .form(JooqFormRoute.builder()
                                        .titleField(TASK.TITLE)
                                        .fields(List.of(
                                                JooqFormElement.of(TASK.TITLE, "route.tasks.labels.title").build(),
                                                JooqFormElement.of(TASK.DESCRIPTION, "route.tasks.labels.description").build(),
                                                JooqFormElement.of(TASK.ASSIGNEE_ID, "route.tasks.labels.assignee").build(),
                                                JooqFormElement.of(TASK.STATUS, "route.tasks.labels.status").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollection.builder()
                                .label("route.tasks.labels.time_entries")
                                .field(TIME_ENTRY.HOURS_SPENT)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(timeEntryConfig)
                                .oneToMany(new JooqOneToMany(TIME_ENTRY.TASK_ID))
                                .children(List.of(TIME_ENTRY.USER_ID, TIME_ENTRY.HOURS_SPENT, TIME_ENTRY.ENTRY_DATE))
                                .form(JooqFormRoute.builder()
                                        .titleField(TIME_ENTRY.HOURS_SPENT)
                                        .fields(List.of(
                                                JooqFormElement.of(TIME_ENTRY.HOURS_SPENT, "route.time_entries.labels.hours").build(),
                                                JooqFormElement.of(TIME_ENTRY.DESCRIPTION, "route.time_entries.labels.description").build(),
                                                JooqFormElement.of(TIME_ENTRY.ENTRY_DATE, "route.time_entries.labels.date").build(),
                                                JooqFormElement.of(TIME_ENTRY.USER_ID, "route.time_entries.labels.user").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollection.builder()
                                .label("route.tasks.labels.attachments")
                                .field(ATTACHMENT.NAME)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(attachmentConfig)
                                .oneToMany(new JooqOneToMany(ATTACHMENT.TASK_ID))
                                .children(List.of(ATTACHMENT.NAME, ATTACHMENT.UPLOADED_AT))
                                .form(JooqFormRoute.builder()
                                        .titleField(ATTACHMENT.NAME)
                                        .fields(List.of(
                                                JooqFormElement.of(ATTACHMENT.NAME, "route.attachments.labels.name").build(),
                                                JooqFormElement.of(ATTACHMENT.PATH, "route.attachments.labels.file").build(),
                                                JooqFormElement.of(ATTACHMENT.UPLOADER_ID, "route.project_members.labels.user").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollection.builder()
                                .label("route.tasks.labels.comments")
                                .field(TASK_COMMENT.CONTENT)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(taskCommentConfig)
                                .oneToMany(new JooqOneToMany(TASK_COMMENT.TASK_ID))
                                .children(List.of(TASK_COMMENT.CONTENT, TASK_COMMENT.CREATED_AT))
                                .emptyMessage("route.tasks.labels.comments-empty-message")
                                .form(JooqFormRoute.builder()
                                        .titleField(TASK_COMMENT.CONTENT)
                                        .fields(List.of(
                                                JooqFormElement.of(TASK_COMMENT.CONTENT, "route.tasks.labels.comment").build()))
                                        .build())
                                .build(),
                        JooqCollection.builder()
                                .label("route.tasks.labels.labels")
                                .field(LABEL.NAME)
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new ConnectDialogFactory<>())
                                .dataStoreConfig(labelConfig)
                                .manyToMany(new JooqManyToMany(
                                        TASK_LABEL.TASK_ID,
                                        TASK_LABEL.LABEL_ID,
                                        LABEL.ID,
                                        TASK_LABEL))
                                .children(List.of(LABEL.NAME, LABEL.COLOR))
                                .emptyMessage("route.tasks.labels.labels-empty-message")
                                .titleField(LABEL.NAME)
                                .build()))
                .build();

        // Milestone Form Configuration
        var milestoneForm = JooqFormRoute.builder()
                .titleField(MILESTONE.TITLE)
                .fields(List.of(
                        JooqFormElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                        JooqFormElement.of(MILESTONE.DESCRIPTION, "route.milestones.labels.description").build(),
                        JooqFormElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build(),
                        JooqFormElement.of(MILESTONE.COMPLETION_PERCENTAGE, "route.milestones.labels.completion").build()))
                .build();

        // Sprint Form Configuration
        var sprintForm = JooqFormRoute.builder()
                .titleField(SPRINT.NAME)
                .fields(List.of(
                        JooqFormElement.of(SPRINT.NAME, "route.sprints.labels.name").build(),
                        JooqFormElement.of(SPRINT.GOAL, "route.sprints.labels.goal").build(),
                        JooqFormElement.of(SPRINT.START_DATE, "route.sprints.labels.start_date").build(),
                        JooqFormElement.of(SPRINT.END_DATE, "route.sprints.labels.end_date").build(),
                        JooqFormElement.of(SPRINT.STATUS, "route.sprints.labels.status").build()))
                .build();

        var projectTasksRoute = JooqKanbanRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("route.tasks.title")
                .titleField(TASK.TITLE)
                .descriptionField(TASK.DESCRIPTION)
                .columnField(TASK.STATUS)
                .filterField(TASK.TITLE)
                .writeRoles(List.of("admin", "manager", "developer"))
                .form(taskForm)
                .build();

        var projectSprintsRoute = JooqListRoute.builder()
                .dataStoreConfig(sprintConfig)
                .title("route.sprints.title")
                .filterField(SPRINT.NAME)
                .columns(List.of(
                        JooqFormElement.of(SPRINT.NAME, "route.sprints.labels.name").build(),
                        JooqFormElement.of(SPRINT.START_DATE, "route.sprints.labels.start_date").build(),
                        JooqFormElement.of(SPRINT.END_DATE, "route.sprints.labels.end_date").build(),
                        JooqFormElement.of(SPRINT.STATUS, "route.sprints.labels.status").build()))
                .form(sprintForm)
                .build();

        var projectMilestonesRoute = JooqListRoute.builder()
                .dataStoreConfig(milestoneConfig)
                .title("route.milestones.title")
                .filterField(MILESTONE.TITLE)
                .columns(List.of(
                        JooqFormElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                        JooqFormElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build(),
                        JooqFormElement.of(MILESTONE.COMPLETION_PERCENTAGE, "route.milestones.labels.completion").build()))
                .form(milestoneForm)
                .build();

        // Routes Configuration
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        routes.put("my-tasks", JooqKanbanRoute.builder()
                .defaultRoute(true)
                .iconFactory(VaadinIcon.CLIPBOARD_USER::create)
                .dataStoreConfig(taskConfig)
                .title("route.my-tasks.title")
                .titleField(TASK.TITLE)
                .descriptionField(TASK.DESCRIPTION)
                .columnField(TASK.STATUS)
                .filterField(TASK.TITLE)
                .routeFilter(DynamicRouteFilter.<TableField<?, ?>>builder()
                        .field(TASK.ASSIGNEE_ID)
                        .valueProvider(() -> {
                            String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
                            var users = usersStore.getRecordsFromTableWhereColumnEquals(USERS.USERNAME, username, 0, 1);
                            return !users.isEmpty() ? users.getFirst().get(USERS.ID) : null;
                        })
                        .build())
                .form(taskForm)
                .build());

        routes.put("search", SearchRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .title("route.search.title")
                .iconFactory(VaadinIcon.SEARCH::create)
                .hiddenInMenu(true)  // Only accessible via search panel
                .build());

        routes.put("my-projects", JooqListRoute.builder()
                .dataStoreConfig(projectConfig)
                .iconFactory(VaadinIcon.FOLDER::create)
                .title("route.my-projects.title")
                .filterField(PROJECT.NAME)
                .columns(List.of(
                        JooqFormElement.of(PROJECT.NAME, "route.projects.labels.name").build(),
                        JooqFormElement.of(PROJECT.STATUS, "route.projects.labels.status").build(),
                        JooqFormElement.of(PROJECT.PRIORITY, "route.projects.labels.priority").build(),
                        JooqFormElement.of(PROJECT.END_DATE, "route.projects.labels.end_date").build()))
                .routeFilter(DynamicRouteFilter.<TableField<?, ?>>builder()
                        .field(PROJECT.OWNER_ID)
                        .valueProvider(() -> {
                            String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
                            var users = usersStore.getRecordsFromTableWhereColumnEquals(USERS.USERNAME, username, 0, 1);
                            return !users.isEmpty() ? users.getFirst().get(USERS.ID) : null;
                        })
                        .build())
                .form(projectForm)
                .build());

        routes.put("projects", JooqGridRoute.builder()
                .dataStoreConfig(projectConfig)
                .iconFactory(VaadinIcon.RECORDS::create)
                .title("route.projects.title")
                .titleField(PROJECT.NAME)
                .descriptionField(PROJECT.DESCRIPTION)
                .writeRoles(List.of("admin", "owner"))
                .form(CustomViewFactoryRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(projectConfig)
                        .viewFactory(record -> new com.vaadin.flow.component.html.Div())
                        .title("route.projects.title")
                        .routes(Map.of(
                                "tasks", projectTasksRoute,
                                "sprints", projectSprintsRoute,
                                "milestones", projectMilestonesRoute,
                                "edit", projectForm))
                        .build())
                .build());

        routes.put("tasks-kanban", JooqKanbanRoute.builder()
                .iconFactory(VaadinIcon.TASKS::create)
                .dataStoreConfig(taskConfig)
                .title("route.tasks.title")
                .titleField(TASK.TITLE)
                .descriptionField(TASK.DESCRIPTION)
                .columnField(TASK.STATUS)
                .titleField(TASK.TITLE)
                .filterField(TASK.TITLE)
                .writeRoles(List.of("admin", "manager", "developer"))
                .form(taskForm)
                .build());

        routes.put("my-milestones", JooqListRoute.builder()
                .dataStoreConfig(milestoneConfig)
                .iconFactory(VaadinIcon.FLAG::create)
                .title("route.my-milestones.title")
                .filterField(MILESTONE.TITLE)
                .columns(List.of(
                        JooqFormElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                        JooqFormElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build(),
                        JooqFormElement.of(MILESTONE.COMPLETION_PERCENTAGE, "route.milestones.labels.completion").build()))
                .form(milestoneForm)
                .build());

        routes.put("milestones", JooqGridRoute.builder()
                .dataStoreConfig(milestoneConfig)
                .iconFactory(VaadinIcon.FLAG::create)
                .title("route.milestones.title")
                .titleField(MILESTONE.TITLE)
                .descriptionField(MILESTONE.DESCRIPTION)
                .writeRoles(List.of("admin", "manager"))
                .form(milestoneForm)
                .build());

        routes.put("users", JooqGridRoute.builder()
                .dataStoreConfig(usersConfig)
                .iconFactory(VaadinIcon.USERS::create)
                .title("route.users.title")
                .titleField(USERS.USERNAME)
                .hiddenInMenu(true)  // Hidden from menu - only accessible by admins for managing user permissions
                .writeRoles(List.of("admin"))
                .form(userForm)
                .build());

        routes.put("profile", JooqSingleFormRoute.builder()
                .iconFactory(VaadinIcon.USER::create)
                .dataStoreConfig(usersConfig)
                .title("route.profile.title")
                .hiddenInMenu(true)  // Only accessible via global action (app bar)
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
                        JooqFormElement.of(USERS.USERNAME, "route.profile.labels.username").readOnly(true).build(),
                        JooqFormElement.of(USERS.PASSWORD_HASH, "route.profile.labels.password").build(),
                        JooqFormElement.of(USERS.CREATED_AT, "route.profile.labels.created_at").readOnly(true).build()))
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

        LinkedHashMap<SprintStatus, String> sprintStatuses = new LinkedHashMap<>();
        sprintStatuses.put(SprintStatus.PLANNED, "selects.sprint-status.planned");
        sprintStatuses.put(SprintStatus.ACTIVE, "selects.sprint-status.active");
        sprintStatuses.put(SprintStatus.COMPLETED, "selects.sprint-status.completed");
        sprintStatuses.put(SprintStatus.CANCELLED, "selects.sprint-status.cancelled");

        LinkedHashMap<String, String> projectRoles = new LinkedHashMap<>();
        projectRoles.put("owner", "selects.project-roles.owner");
        projectRoles.put("admin", "selects.project-roles.admin");
        projectRoles.put("member", "selects.project-roles.member");
        projectRoles.put("viewer", "selects.project-roles.viewer");

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("pm_i18n")
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(usersConfig)
                        .roleResolutionStrategy(new ClassBasedRoleResolutionStrategy<>(
                                Map.of(
                                        PROJECT.getRecordType(), new JoinTableRoleResolutionStrategy<TableField<?, ?>>(
                                                projectMemberStore,
                                                PROJECT_MEMBER.USER_ID,
                                                PROJECT_MEMBER.PROJECT_ID,
                                                PROJECT_MEMBER.ROLE,
                                                USERS.ID,
                                                PROJECT.ID
                                        )
                                ),
                                // Global role strategy
                                new JoinTableRoleResolutionStrategy<TableField<?, ?>>(
                                        userRolesStore,
                                        rolesStore,
                                        USER_ROLES.USER_ID,
                                        USER_ROLES.ROLE_ID,
                                        ROLES.NAME,
                                        USERS.ID
                                )
                        ))
                        .availableRoles(com.github.appreciated.vortex_crud.core.config.model.Roles.builder().roles(List.of("admin", "manager", "developer", "viewer")).build())
                        .defaultReadRoles(List.of("viewer"))
                        .defaultWriteRoles(List.of("admin", "manager"))
                        .signUpEnabled(true)
                        .loginView(LoginView.class)
                        .signUpView(SignUpView.class)
                        .username(JooqFormElement.of(USERS.USERNAME, "route.users.labels.username").build())
                        .password(JooqFormElement.of(USERS.PASSWORD_HASH, "route.users.labels.password").build())
                        .signUpFields(List.of())
                        .build())
                .routes(routes)
                .versioning(JooqVersioning.builder().dataStores(List.of(PROJECT, TASK, MILESTONE, TASK_COMMENT, SPRINT)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(Selects.builder()
                        .configs(Map.of(
                                "project-status", projectStatuses,
                                "task-status", taskStatuses,
                                "task-type", taskTypes,
                                "priority", priorities,
                                "sprint-status", sprintStatuses,
                                "project-roles", projectRoles))
                        .build())
                .notificationPanelConfiguration(NotificationPanelConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(notificationConfig)
                        .timestampField(NOTIFICATION.CREATED_AT)
                        .messageField(NOTIFICATION.MESSAGE)
                        .readStatusField(NOTIFICATION.IS_READ)
                        .readStatusValueForRead(1)
                        .readStatusValueForUnread(0)
                        .build())
                .build();
    }
}
