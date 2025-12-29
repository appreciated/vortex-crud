package com.github.appreciated.vortex_crud.demo.projectmanagement;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.LocalFileResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.SingleEntityRouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.Priority;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.ProjectStatus;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.TaskStatus;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.TaskType;
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
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        JooqDataStore projectStore = new JooqDataStore(PROJECT.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore taskStore = new JooqDataStore(TASK.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore milestoneStore = new JooqDataStore(MILESTONE.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore labelStore = new JooqDataStore(LABEL.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore taskCommentStore = new JooqDataStore(TASK_COMMENT.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore taskLabelStore = new JooqDataStore(TASK_LABEL.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore usersStore = new JooqDataStore(USERS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore projectMemberStore = new JooqDataStore(PROJECT_MEMBER.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore timeEntryStore = new JooqDataStore(TIME_ENTRY.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore attachmentStore = new JooqDataStore(ATTACHMENT.getRecordType(), dsl, new DataStoreHooks<>());

        // Configs
        var usersConfig = JooqDataStoreConfig.of(USERS)
                .dataStoreInstance((VortexCrudDataStore) usersStore)
                .fields(Map.of(
                        USERS.ID, JooqNumericIdField.builder().build(),
                        USERS.USERNAME, JooqEmailField.builder().required(true).build(),
                        USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        USERS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var projectMemberConfig = JooqDataStoreConfig.of(PROJECT_MEMBER)
                .dataStoreInstance((VortexCrudDataStore) projectMemberStore)
                .fields(Map.of(
                        PROJECT_MEMBER.ID, JooqNumericIdField.builder().build(),
                        PROJECT_MEMBER.PROJECT_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) projectStore).field(PROJECT_MEMBER.PROJECT_ID).filterField(PROJECT.NAME).children(List.of(PROJECT.NAME)).build(),
                        PROJECT_MEMBER.USER_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(PROJECT_MEMBER.USER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        PROJECT_MEMBER.ROLE, JooqSelectField.builder().values("project-roles").build(),
                        PROJECT_MEMBER.JOINED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var projectConfig = JooqDataStoreConfig.of(PROJECT)
                .dataStoreInstance((VortexCrudDataStore) projectStore)
                .fields(Map.ofEntries(
                        Map.entry(PROJECT.ID, JooqNumericIdField.builder().build()),
                        Map.entry(PROJECT.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build()),
                        Map.entry(PROJECT.CODE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build()),
                        Map.entry(PROJECT.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build()),
                        Map.entry(PROJECT.STATUS, JooqSelectField.builder().values("project-status").build()),
                        Map.entry(PROJECT.PRIORITY, JooqSelectField.builder().values("priority").build()),
                        Map.entry(PROJECT.START_DATE, JooqDateField.builder().build()),
                        Map.entry(PROJECT.END_DATE, JooqDateField.builder().build()),
                        Map.entry(PROJECT.OWNER_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(PROJECT.OWNER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(PROJECT.PROGRESS_PERCENTAGE, JooqIntegerField.builder().build()),
                        Map.entry(PROJECT.COLOR, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build()),
                        Map.entry(PROJECT.IS_ARCHIVED, JooqCheckboxField.builder().build()),
                        Map.entry(PROJECT.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PROJECT.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PROJECT.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                .build();

        var milestoneConfig = JooqDataStoreConfig.of(MILESTONE)
                .dataStoreInstance((VortexCrudDataStore) milestoneStore)
                .fields(Map.of(
                        MILESTONE.ID, JooqNumericIdField.builder().build(),
                        MILESTONE.PROJECT_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) projectStore).field(MILESTONE.PROJECT_ID).filterField(PROJECT.NAME).children(List.of(PROJECT.NAME)).build(),
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
                .dataStoreInstance((VortexCrudDataStore) taskStore)
                .fields(Map.ofEntries(
                        Map.entry(TASK.ID, JooqNumericIdField.builder().build()),
                        Map.entry(TASK.PROJECT_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) projectStore).field(TASK.PROJECT_ID).filterField(PROJECT.NAME).children(List.of(PROJECT.NAME)).build()),
                        Map.entry(TASK.MILESTONE_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) milestoneStore).field(TASK.MILESTONE_ID).filterField(MILESTONE.TITLE).children(List.of(MILESTONE.TITLE)).build()),
                        Map.entry(TASK.PARENT_TASK_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) taskStore).field(TASK.PARENT_TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build()),
                        Map.entry(TASK.TASK_NUMBER, JooqIntegerField.builder().required(true).build()),
                        Map.entry(TASK.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 300 characters", 0, 300))).build()),
                        Map.entry(TASK.DESCRIPTION, JooqMarkDownField.builder().validators(List.of(new StringLengthValidator("Maximum 2000 characters", 0, 2000))).build()),
                        Map.entry(TASK.TASK_TYPE, JooqSelectField.builder().values("task-type").build()),
                        Map.entry(TASK.STATUS, JooqSelectField.builder().values("task-status").build()),
                        Map.entry(TASK.PRIORITY, JooqSelectField.builder().values("priority").build()),
                        Map.entry(TASK.ASSIGNEE_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(TASK.ASSIGNEE_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(TASK.REPORTER_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(TASK.REPORTER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(TASK.ESTIMATED_HOURS, JooqDoubleField.builder().build()),
                        Map.entry(TASK.DUE_DATE, JooqDateField.builder().build()),
                        Map.entry(TASK.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(TASK.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(TASK.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                .build();

        var labelConfig = JooqDataStoreConfig.of(LABEL)
                .dataStoreInstance((VortexCrudDataStore) labelStore)
                .fields(Map.of(
                        LABEL.ID, JooqNumericIdField.builder().build(),
                        LABEL.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build(),
                        LABEL.COLOR, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                        LABEL.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                        LABEL.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var taskCommentConfig = JooqDataStoreConfig.of(TASK_COMMENT)
                .dataStoreInstance((VortexCrudDataStore) taskCommentStore)
                .fields(Map.of(
                        TASK_COMMENT.ID, JooqNumericIdField.builder().build(),
                        TASK_COMMENT.TASK_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) taskStore).field(TASK_COMMENT.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                        TASK_COMMENT.AUTHOR_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(TASK_COMMENT.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        TASK_COMMENT.CONTENT, JooqTextAreaField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 2000 characters", 0, 2000))).build(),
                        TASK_COMMENT.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var taskLabelConfig = JooqDataStoreConfig.of(TASK_LABEL)
                .dataStoreInstance((VortexCrudDataStore) taskLabelStore)
                .fields(Map.of(
                        TASK_LABEL.ID, JooqNumericIdField.builder().build(),
                        TASK_LABEL.TASK_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) taskStore).field(TASK_LABEL.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                        TASK_LABEL.LABEL_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) labelStore).field(TASK_LABEL.LABEL_ID).filterField(LABEL.NAME).children(List.of(LABEL.NAME)).build()))
                .build();

        var timeEntryConfig = JooqDataStoreConfig.of(TIME_ENTRY)
                .dataStoreInstance((VortexCrudDataStore) timeEntryStore)
                .fields(Map.of(
                        TIME_ENTRY.ID, JooqNumericIdField.builder().build(),
                        TIME_ENTRY.TASK_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) taskStore).field(TIME_ENTRY.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                        TIME_ENTRY.USER_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(TIME_ENTRY.USER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        TIME_ENTRY.HOURS_SPENT, JooqDoubleField.builder().required(true).build(),
                        TIME_ENTRY.DESCRIPTION, JooqTextAreaField.builder().build(),
                        TIME_ENTRY.ENTRY_DATE, JooqDateField.builder().required(true).build(),
                        TIME_ENTRY.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var attachmentConfig = JooqDataStoreConfig.of(ATTACHMENT)
                .dataStoreInstance((VortexCrudDataStore) attachmentStore)
                .fields(Map.of(
                        ATTACHMENT.ID, JooqNumericIdField.builder().build(),
                        ATTACHMENT.TASK_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) taskStore).field(ATTACHMENT.TASK_ID).filterField(TASK.TITLE).children(List.of(TASK.TITLE)).build(),
                        ATTACHMENT.UPLOADER_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(ATTACHMENT.UPLOADER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        ATTACHMENT.NAME, JooqTextField.builder().required(true).build(),
                        ATTACHMENT.PATH, JooqFileField.builder().resourceProvider(new LocalFileResourceProvider("attachments")).required(true).build(),
                        ATTACHMENT.UPLOADED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        // Project Form Configuration
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> projectForm = JooqFormRoute.builder()
                .dataStoreConfig(projectConfig)
                .title("route.projects.title")
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
                        JooqFieldElement.of(PROJECT.COLOR, "route.projects.labels.color").build(),
                        JooqCollectionElement.of("route.projects.labels.members")
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(projectMemberConfig)
                                .oneToMany(new JooqOneToMany(PROJECT_MEMBER.PROJECT_ID))
                                .children(List.of(PROJECT_MEMBER.USER_ID, PROJECT_MEMBER.ROLE))
                                .form(JooqFormRoute.builder()
                                        .titleField(PROJECT_MEMBER.USER_ID)
                                        .children(List.of(
                                                JooqFieldElement.of(PROJECT_MEMBER.USER_ID, "route.project_members.labels.user").build(),
                                                JooqFieldElement.of(PROJECT_MEMBER.ROLE, "route.project_members.labels.role").build()
                                        ))
                                        .build())
                                .build()))
                .build();

        // User Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> userForm = JooqFormRoute.builder()
                .dataStoreConfig(usersConfig)
                .titleField(USERS.USERNAME)
                .children(List.of(
                        JooqFieldElement.of(USERS.USERNAME, "route.users.labels.username").build(),
                        JooqFieldElement.of(USERS.PASSWORD_HASH, "route.users.labels.password").build(),
                        JooqCollectionElement.of("route.users.labels.projects")
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(projectMemberConfig)
                                .oneToMany(new JooqOneToMany(PROJECT_MEMBER.USER_ID))
                                .children(List.of(PROJECT_MEMBER.PROJECT_ID, PROJECT_MEMBER.ROLE))
                                .form(JooqFormRoute.builder()
                                        .titleField(PROJECT_MEMBER.PROJECT_ID)
                                        .children(List.of(
                                                JooqFieldElement.of(PROJECT_MEMBER.PROJECT_ID, "route.project_members.labels.project").build(),
                                                JooqFieldElement.of(PROJECT_MEMBER.ROLE, "route.project_members.labels.role").build()
                                        ))
                                        .build())
                                .build()))
                .build();

        // Task Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStoreConfig(taskConfig)
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
                .children(List.of(
                        JooqFieldElement.of(TASK.TITLE, "route.tasks.labels.title").build(),
                        JooqFieldElement.of(TASK.DESCRIPTION, "route.tasks.labels.description").build(),
                        JooqFieldElement.of(TASK.TASK_TYPE, "route.tasks.labels.task_type").build(),
                        JooqFieldElement.of(TASK.STATUS, "route.tasks.labels.status").build(),
                        JooqFieldElement.of(TASK.PRIORITY, "route.tasks.labels.priority").build(),
                        JooqFieldElement.of(TASK.ASSIGNEE_ID, "route.tasks.labels.assignee").build(),
                        JooqFieldElement.of(TASK.ESTIMATED_HOURS, "route.tasks.labels.estimated_hours").build(),
                        JooqFieldElement.of(TASK.DUE_DATE, "route.tasks.labels.due_date").build(),
                        JooqFieldElement.of(TASK.PARENT_TASK_ID, "route.tasks.labels.parent_task").build(),
                        JooqCollectionElement.of("route.tasks.labels.subtasks")
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(taskConfig)
                                .oneToMany(new JooqOneToMany(TASK.PARENT_TASK_ID))
                                .children(List.of(TASK.TASK_NUMBER, TASK.TITLE, TASK.STATUS))
                                .form(JooqFormRoute.builder()
                                        .titleField(TASK.TITLE)
                                        .children(List.of(
                                                JooqFieldElement.of(TASK.TITLE, "route.tasks.labels.title").build(),
                                                JooqFieldElement.of(TASK.DESCRIPTION, "route.tasks.labels.description").build(),
                                                JooqFieldElement.of(TASK.ASSIGNEE_ID, "route.tasks.labels.assignee").build(),
                                                JooqFieldElement.of(TASK.STATUS, "route.tasks.labels.status").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollectionElement.of("route.tasks.labels.time_entries")
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(timeEntryConfig)
                                .oneToMany(new JooqOneToMany(TIME_ENTRY.TASK_ID))
                                .children(List.of(TIME_ENTRY.USER_ID, TIME_ENTRY.HOURS_SPENT, TIME_ENTRY.ENTRY_DATE))
                                .form(JooqFormRoute.builder()
                                        .titleField(TIME_ENTRY.HOURS_SPENT)
                                        .children(List.of(
                                                JooqFieldElement.of(TIME_ENTRY.HOURS_SPENT, "route.time_entries.labels.hours").build(),
                                                JooqFieldElement.of(TIME_ENTRY.DESCRIPTION, "route.time_entries.labels.description").build(),
                                                JooqFieldElement.of(TIME_ENTRY.ENTRY_DATE, "route.time_entries.labels.date").build(),
                                                JooqFieldElement.of(TIME_ENTRY.USER_ID, "route.time_entries.labels.user").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollectionElement.of("route.tasks.labels.attachments")
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(attachmentConfig)
                                .oneToMany(new JooqOneToMany(ATTACHMENT.TASK_ID))
                                .children(List.of(ATTACHMENT.NAME, ATTACHMENT.UPLOADED_AT))
                                .form(JooqFormRoute.builder()
                                        .titleField(ATTACHMENT.NAME)
                                        .children(List.of(
                                                JooqFieldElement.of(ATTACHMENT.NAME, "route.attachments.labels.name").build(),
                                                JooqFieldElement.of(ATTACHMENT.PATH, "route.attachments.labels.file").build(),
                                                JooqFieldElement.of(ATTACHMENT.UPLOADER_ID, "route.project_members.labels.user").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollectionElement.of("route.tasks.labels.comments")
                                .factory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(taskCommentConfig)
                                .oneToMany(new JooqOneToMany(TASK_COMMENT.TASK_ID))
                                .children(List.of(TASK_COMMENT.CONTENT, TASK_COMMENT.CREATED_AT))
                                .emptyMessage("route.tasks.labels.comments-empty-message")
                                .form(JooqFormRoute.builder()
                                        .titleField(TASK_COMMENT.CONTENT)
                                        .children(List.of(
                                                JooqFieldElement.of(TASK_COMMENT.CONTENT, "route.tasks.labels.comment").build()))
                                        .build())
                                .build(),
                        JooqCollectionElement.of("route.tasks.labels.labels")
                                .factory(new ListCollectionFactory<>())
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
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> milestoneForm = JooqFormRoute.builder()
                .dataStoreConfig(milestoneConfig)
                .title("route.milestones.title")
                .titleField(MILESTONE.TITLE)
                .children(List.of(
                        JooqFieldElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                        JooqFieldElement.of(MILESTONE.DESCRIPTION, "route.milestones.labels.description").build(),
                        JooqFieldElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build(),
                        JooqFieldElement.of(MILESTONE.COMPLETION_PERCENTAGE, "route.milestones.labels.completion").build()))
                .build();

        // Routes Configuration
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        routes.put("projects", JooqGridRoute.builder()
                .isDefaultRoute(true)
                .dataStoreConfig(projectConfig)
                .iconFactory(VaadinIcon.RECORDS::create)
                .title("route.projects.title")
                .titleField(PROJECT.NAME)
                .descriptionField(PROJECT.DESCRIPTION)
                .writeRoles(List.of("admin", "owner"))
                .form(projectForm)
                .build());

        routes.put("tasks-kanban", JooqKanbanRoute.builder()
                .iconFactory(VaadinIcon.TASKS::create)
                .dataStoreConfig(taskConfig)
                .title("route.tasks.title")
                .titleField(TASK.TITLE)
                .descriptionField(TASK.DESCRIPTION)
                .columnField(TASK.STATUS)
                .filterField(TASK.TITLE)
                .writeRoles(List.of("admin", "manager", "developer"))
                .form(taskForm)
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
                .writeRoles(List.of("admin"))
                .form(userForm)
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
                        .roleResolutionStrategy(new JoinTableRoleResolutionStrategy<>(
                                (VortexCrudDataStore) projectMemberStore,
                                PROJECT_MEMBER.USER_ID,
                                PROJECT_MEMBER.PROJECT_ID,
                                PROJECT_MEMBER.ROLE,
                                USERS.ID,
                                PROJECT.ID
                        ))
                        .availableRoles(com.github.appreciated.vortex_crud.core.config.model.Roles.builder().roles(List.of("admin", "manager", "developer", "viewer")).build())
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
                                "priority", priorities,
                                "project-roles", projectRoles))
                        .build())
                .build();
    }
}
