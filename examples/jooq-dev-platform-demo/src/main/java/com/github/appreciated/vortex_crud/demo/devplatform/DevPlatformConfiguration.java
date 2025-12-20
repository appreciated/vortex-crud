package com.github.appreciated.vortex_crud.demo.devplatform;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.IssueState;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.Priority;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.PullRequestState;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.RepositoryVisibility;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
import com.github.appreciated.vortex_crud.jooq.service.JooqOneToMany;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.*;
import com.github.appreciated.vortex_crud.security.core.strategy.ClassBasedRoleResolutionStrategy;
import com.github.appreciated.vortex_crud.security.core.strategy.JoinTableRoleResolutionStrategy;
import com.github.appreciated.vortex_crud.security.core.strategy.RoleResolutionStrategy;
import com.github.appreciated.vortex_crud.security.core.view.LocalIdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.github.appreciated.vortex_crud.security.core.view.SignUpView;
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
import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

@Service
public class DevPlatformConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public DevPlatformConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Data Stores
        JooqDataStore organizationStore = new JooqDataStore(ORGANIZATION.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore repositoryStore = new JooqDataStore(REPOSITORY.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore issueStore = new JooqDataStore(ISSUE.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore pullRequestStore = new JooqDataStore(PULL_REQUEST.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore milestoneStore = new JooqDataStore(MILESTONE.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore labelStore = new JooqDataStore(LABEL.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore commentStore = new JooqDataStore(COMMENT.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore issueLabelStore = new JooqDataStore(ISSUE_LABEL.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore pullRequestLabelStore = new JooqDataStore(PULL_REQUEST_LABEL.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore usersStore = new JooqDataStore(USERS.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore organizationMemberStore = new JooqDataStore(ORGANIZATION_MEMBER.getRecordType(), dsl, new DataStoreHooks<>());
        JooqDataStore repositoryCollaboratorStore = new JooqDataStore(REPOSITORY_COLLABORATOR.getRecordType(), dsl, new DataStoreHooks<>());

        // Configs
        var usersConfig = JooqDataStoreConfig.of(USERS)
                .dataStoreInstance((VortexCrudDataStore) usersStore)
                .fields(Map.of(
                        USERS.ID, JooqNumericIdField.builder().build(),
                        USERS.USERNAME, JooqEmailField.builder().required(true).build(),
                        USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        USERS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var organizationMemberConfig = JooqDataStoreConfig.of(ORGANIZATION_MEMBER)
                .dataStoreInstance((VortexCrudDataStore) organizationMemberStore)
                .fields(Map.of(
                        ORGANIZATION_MEMBER.ID, JooqNumericIdField.builder().build(),
                        ORGANIZATION_MEMBER.ORGANIZATION_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) organizationStore).field(ORGANIZATION_MEMBER.ORGANIZATION_ID).filterField(ORGANIZATION.NAME).children(List.of(ORGANIZATION.NAME)).build(),
                        ORGANIZATION_MEMBER.USER_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(ORGANIZATION_MEMBER.USER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        ORGANIZATION_MEMBER.ROLE, JooqSelectField.builder().values("organization-roles").build(),
                        ORGANIZATION_MEMBER.JOINED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var repositoryCollaboratorConfig = JooqDataStoreConfig.of(REPOSITORY_COLLABORATOR)
                .dataStoreInstance((VortexCrudDataStore) repositoryCollaboratorStore)
                .fields(Map.of(
                        REPOSITORY_COLLABORATOR.ID, JooqNumericIdField.builder().build(),
                        REPOSITORY_COLLABORATOR.REPOSITORY_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) repositoryStore).field(REPOSITORY_COLLABORATOR.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                        REPOSITORY_COLLABORATOR.USER_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(REPOSITORY_COLLABORATOR.USER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        REPOSITORY_COLLABORATOR.PERMISSION, JooqSelectField.builder().values("repository-permissions").build(),
                        REPOSITORY_COLLABORATOR.INVITED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var organizationConfig = JooqDataStoreConfig.of(ORGANIZATION)
                .dataStoreInstance((VortexCrudDataStore) organizationStore)
                .fields(Map.of(
                        ORGANIZATION.ID, JooqNumericIdField.builder().build(),
                        ORGANIZATION.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build(),
                        ORGANIZATION.DISPLAY_NAME, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build(),
                        ORGANIZATION.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                        ORGANIZATION.WEBSITE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        ORGANIZATION.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        ORGANIZATION.IS_ACTIVE, JooqCheckboxField.builder().build(),
                        ORGANIZATION.CUSTOM_FIELDS, JooqTextAreaField.builder().build()))
                .build();

        var repositoryConfig = JooqDataStoreConfig.of(REPOSITORY)
                .dataStoreInstance((VortexCrudDataStore) repositoryStore)
                .fields(Map.ofEntries(
                        Map.entry(REPOSITORY.ID, JooqNumericIdField.builder().build()),
                        Map.entry(REPOSITORY.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                        Map.entry(REPOSITORY.SLUG, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                        Map.entry(REPOSITORY.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 2000 characters", 0, 2000))).build()),
                        Map.entry(REPOSITORY.OWNER_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(REPOSITORY.OWNER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(REPOSITORY.ORGANIZATION_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) organizationStore).field(REPOSITORY.ORGANIZATION_ID).filterField(ORGANIZATION.NAME).children(List.of(ORGANIZATION.NAME)).build()),
                        Map.entry(REPOSITORY.VISIBILITY, JooqSelectField.builder().values("repository-visibility").build()),
                        Map.entry(REPOSITORY.DEFAULT_BRANCH, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                        Map.entry(REPOSITORY.IS_ARCHIVED, JooqCheckboxField.builder().build()),
                        Map.entry(REPOSITORY.STAR_COUNT, JooqIntegerField.builder().build()),
                        Map.entry(REPOSITORY.FORK_COUNT, JooqIntegerField.builder().build()),
                        Map.entry(REPOSITORY.LANGUAGE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build()),
                        Map.entry(REPOSITORY.TOPICS, JooqTextAreaField.builder().build()),
                        Map.entry(REPOSITORY.README_CONTENT, JooqMarkDownField.builder().build()),
                        Map.entry(REPOSITORY.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(REPOSITORY.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(REPOSITORY.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                .build();

        var milestoneConfig = JooqDataStoreConfig.of(MILESTONE)
                .dataStoreInstance((VortexCrudDataStore) milestoneStore)
                .fields(Map.of(
                        MILESTONE.ID, JooqNumericIdField.builder().build(),
                        MILESTONE.REPOSITORY_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) repositoryStore).field(MILESTONE.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                        MILESTONE.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build(),
                        MILESTONE.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                        MILESTONE.STATE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                        MILESTONE.DUE_DATE, JooqDateField.builder().build(),
                        MILESTONE.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        MILESTONE.CLOSED_AT, JooqDateTimePickerField.builder().build(),
                        MILESTONE.CUSTOM_FIELDS, JooqTextAreaField.builder().build()))
                .build();

        var issueConfig = JooqDataStoreConfig.of(ISSUE)
                .dataStoreInstance((VortexCrudDataStore) issueStore)
                .fields(Map.ofEntries(
                        Map.entry(ISSUE.ID, JooqNumericIdField.builder().build()),
                        Map.entry(ISSUE.REPOSITORY_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) repositoryStore).field(ISSUE.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build()),
                        Map.entry(ISSUE.ISSUE_NUMBER, JooqIntegerField.builder().required(true).build()),
                        Map.entry(ISSUE.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 300 characters", 0, 300))).build()),
                        Map.entry(ISSUE.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 5000 characters", 0, 5000))).build()),
                        Map.entry(ISSUE.STATE, JooqSelectField.builder().values("issue-state").build()),
                        Map.entry(ISSUE.ISSUE_TYPE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build()),
                        Map.entry(ISSUE.PRIORITY, JooqSelectField.builder().values("priority").build()),
                        Map.entry(ISSUE.AUTHOR_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(ISSUE.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(ISSUE.ASSIGNEE_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(ISSUE.ASSIGNEE_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(ISSUE.MILESTONE_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) milestoneStore).field(ISSUE.MILESTONE_ID).filterField(MILESTONE.TITLE).children(List.of(MILESTONE.TITLE)).build()),
                        Map.entry(ISSUE.CLOSED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(ISSUE.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(ISSUE.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(ISSUE.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                .build();

        var pullRequestConfig = JooqDataStoreConfig.of(PULL_REQUEST)
                .dataStoreInstance((VortexCrudDataStore) pullRequestStore)
                .fields(Map.ofEntries(
                        Map.entry(PULL_REQUEST.ID, JooqNumericIdField.builder().build()),
                        Map.entry(PULL_REQUEST.REPOSITORY_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) repositoryStore).field(PULL_REQUEST.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build()),
                        Map.entry(PULL_REQUEST.PR_NUMBER, JooqIntegerField.builder().required(true).build()),
                        Map.entry(PULL_REQUEST.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 300 characters", 0, 300))).build()),
                        Map.entry(PULL_REQUEST.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 5000 characters", 0, 5000))).build()),
                        Map.entry(PULL_REQUEST.STATE, JooqSelectField.builder().values("pull-request-state").build()),
                        Map.entry(PULL_REQUEST.SOURCE_BRANCH, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build()),
                        Map.entry(PULL_REQUEST.TARGET_BRANCH, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build()),
                        Map.entry(PULL_REQUEST.AUTHOR_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(PULL_REQUEST.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(PULL_REQUEST.ASSIGNEE_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(PULL_REQUEST.ASSIGNEE_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(PULL_REQUEST.MILESTONE_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) milestoneStore).field(PULL_REQUEST.MILESTONE_ID).filterField(MILESTONE.TITLE).children(List.of(MILESTONE.TITLE)).build()),
                        Map.entry(PULL_REQUEST.IS_DRAFT, JooqCheckboxField.builder().build()),
                        Map.entry(PULL_REQUEST.MERGED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PULL_REQUEST.CLOSED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PULL_REQUEST.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PULL_REQUEST.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PULL_REQUEST.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                .build();

        var labelConfig = JooqDataStoreConfig.of(LABEL)
                .dataStoreInstance((VortexCrudDataStore) labelStore)
                .fields(Map.of(
                        LABEL.ID, JooqNumericIdField.builder().build(),
                        LABEL.REPOSITORY_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) repositoryStore).field(LABEL.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                        LABEL.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build(),
                        LABEL.COLOR, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                        LABEL.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                        LABEL.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var commentConfig = JooqDataStoreConfig.of(COMMENT)
                .dataStoreInstance((VortexCrudDataStore) commentStore)
                .fields(Map.of(
                        COMMENT.ID, JooqNumericIdField.builder().build(),
                        COMMENT.ENTITY_TYPE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                        COMMENT.ENTITY_ID, JooqIntegerField.builder().required(true).build(),
                        COMMENT.AUTHOR_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) usersStore).field(COMMENT.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        COMMENT.CONTENT, JooqTextAreaField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 5000 characters", 0, 5000))).build(),
                        COMMENT.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        COMMENT.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var issueLabelConfig = JooqDataStoreConfig.of(ISSUE_LABEL)
                .dataStoreInstance((VortexCrudDataStore) issueLabelStore)
                .fields(Map.of(
                        ISSUE_LABEL.ID, JooqNumericIdField.builder().build(),
                        ISSUE_LABEL.ISSUE_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) issueStore).field(ISSUE_LABEL.ISSUE_ID).filterField(ISSUE.TITLE).children(List.of(ISSUE.TITLE)).build(),
                        ISSUE_LABEL.LABEL_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) labelStore).field(ISSUE_LABEL.LABEL_ID).filterField(LABEL.NAME).children(List.of(LABEL.NAME)).build()))
                .build();

        var pullRequestLabelConfig = JooqDataStoreConfig.of(PULL_REQUEST_LABEL)
                .dataStoreInstance((VortexCrudDataStore) pullRequestLabelStore)
                .fields(Map.of(
                        PULL_REQUEST_LABEL.ID, JooqNumericIdField.builder().build(),
                        PULL_REQUEST_LABEL.PULL_REQUEST_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) pullRequestStore).field(PULL_REQUEST_LABEL.PULL_REQUEST_ID).filterField(PULL_REQUEST.TITLE).children(List.of(PULL_REQUEST.TITLE)).build(),
                        PULL_REQUEST_LABEL.LABEL_ID, JooqReferenceField.builder().dataStore((VortexCrudDataStore) labelStore).field(PULL_REQUEST_LABEL.LABEL_ID).filterField(LABEL.NAME).children(List.of(LABEL.NAME)).build()))
                .build();

        // Issue Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> issueForm = JooqFormRoute.builder()
                .dataStoreConfig(issueConfig)
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(ISSUE.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(ISSUE.TITLE, "route.issues.labels.title").build(),
                                JooqFieldElement.of(ISSUE.DESCRIPTION, "route.issues.labels.description").build(),
                                JooqFieldElement.of(ISSUE.STATE, "route.issues.labels.state").build(),
                                JooqFieldElement.of(ISSUE.ISSUE_TYPE, "route.issues.labels.type").build(),
                                JooqFieldElement.of(ISSUE.PRIORITY, "route.issues.labels.priority").build(),
                                JooqFieldElement.of(ISSUE.ASSIGNEE_ID, "route.issues.labels.assignee").build(),
                                JooqFieldElement.of(ISSUE.MILESTONE_ID, "route.issues.labels.milestone").build(),
                                JooqCollectionElement.of("route.issues.labels.labels")
                                        .factory(new ListCollectionFactory<>())
                                        .configuration(JooqCollection.builder(new ConnectDialogFactory<>())
                                                .data(JooqCollectionConfiguration.of(labelConfig)
                                                        .manyToMany(new JooqManyToMany(
                                                                ISSUE_LABEL.ISSUE_ID,
                                                                ISSUE_LABEL.LABEL_ID,
                                                                LABEL.ID,
                                                                ISSUE_LABEL))
                                                        .children(List.of(LABEL.NAME, LABEL.COLOR))
                                                        .build())
                                                .emptyMessage("route.issues.labels.labels-empty-message")
                                                .configuration(new CollectionConfig<TableField<?, ?>>(LABEL.NAME))
                                                .build())
                                        .build()))
                        .build())
                .build();

        // Pull Request Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> pullRequestForm = JooqFormRoute.builder()
                .dataStoreConfig(pullRequestConfig)
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(PULL_REQUEST.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(PULL_REQUEST.TITLE, "route.pull_requests.labels.title").build(),
                                JooqFieldElement.of(PULL_REQUEST.DESCRIPTION, "route.pull_requests.labels.description").build(),
                                JooqFieldElement.of(PULL_REQUEST.STATE, "route.pull_requests.labels.state").build(),
                                JooqFieldElement.of(PULL_REQUEST.SOURCE_BRANCH, "route.pull_requests.labels.source_branch").build(),
                                JooqFieldElement.of(PULL_REQUEST.TARGET_BRANCH, "route.pull_requests.labels.target_branch").build(),
                                JooqFieldElement.of(PULL_REQUEST.ASSIGNEE_ID, "route.pull_requests.labels.assignee").build(),
                                JooqFieldElement.of(PULL_REQUEST.IS_DRAFT, "route.pull_requests.labels.is_draft").build(),
                                JooqCollectionElement.of("route.pull_requests.labels.labels")
                                        .factory(new ListCollectionFactory<>())
                                        .configuration(JooqCollection.builder(new ConnectDialogFactory<>())
                                                .data(JooqCollectionConfiguration.of(labelConfig)
                                                        .manyToMany(new JooqManyToMany(
                                                                PULL_REQUEST_LABEL.PULL_REQUEST_ID,
                                                                PULL_REQUEST_LABEL.LABEL_ID,
                                                                LABEL.ID,
                                                                PULL_REQUEST_LABEL))
                                                        .children(List.of(LABEL.NAME, LABEL.COLOR))
                                                        .build())
                                                .emptyMessage("route.pull_requests.labels.labels-empty-message")
                                                .configuration(new CollectionConfig<TableField<?, ?>>(LABEL.NAME))
                                                .build())
                                        .build()))
                        .build())
                .build();

        // Repository Form Configuration
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> repositoryForm = JooqFormRoute.builder()
                .dataStoreConfig(repositoryConfig)
                .title("route.repositories.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(REPOSITORY.NAME)
                        .children(List.of(
                                JooqFieldElement.of(REPOSITORY.NAME, "route.repositories.labels.name").build(),
                                JooqFieldElement.of(REPOSITORY.SLUG, "route.repositories.labels.slug").build(),
                                JooqFieldElement.of(REPOSITORY.DESCRIPTION, "route.repositories.labels.description").build(),
                                JooqFieldElement.of(REPOSITORY.VISIBILITY, "route.repositories.labels.visibility").build(),
                                JooqFieldElement.of(REPOSITORY.DEFAULT_BRANCH, "route.repositories.labels.default_branch").build(),
                                JooqFieldElement.of(REPOSITORY.LANGUAGE, "route.repositories.labels.language").build(),
                                JooqFieldElement.of(REPOSITORY.TOPICS, "route.repositories.labels.topics").build(),
                                JooqFieldElement.of(REPOSITORY.README_CONTENT, "route.repositories.labels.readme").build(),
                                JooqCollectionElement.of("route.repositories.labels.collaborators")
                                        .factory(new ListCollectionFactory<>())
                                        .configuration(JooqCollection.builder(new FormDialogFactory<>())
                                                .data(JooqCollectionConfiguration.of(repositoryCollaboratorConfig)
                                                        .oneToMany(new JooqOneToMany(REPOSITORY_COLLABORATOR.REPOSITORY_ID))
                                                        .children(List.of(REPOSITORY_COLLABORATOR.USER_ID, REPOSITORY_COLLABORATOR.PERMISSION))
                                                        .build())
                                                .child(JooqFormRoute.builder()
                                                     .formConfiguration(JooqFormRendererConfiguration.builder()
                                                         .titleField(REPOSITORY_COLLABORATOR.USER_ID)
                                                         .children(List.of(
                                                             JooqFieldElement.of(REPOSITORY_COLLABORATOR.USER_ID, "route.repository_collaborators.labels.user").build(),
                                                             JooqFieldElement.of(REPOSITORY_COLLABORATOR.PERMISSION, "route.repository_collaborators.labels.permission").build()
                                                         ))
                                                         .build())
                                                     .build())
                                                .build())
                                        .build()))
                        .build())
                .build();

        // Organization Form Configuration
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> organizationForm = JooqFormRoute.builder()
                .dataStoreConfig(organizationConfig)
                .title("route.organizations.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(ORGANIZATION.NAME)
                        .children(List.of(
                                JooqFieldElement.of(ORGANIZATION.NAME, "route.organizations.labels.name").build(),
                                JooqFieldElement.of(ORGANIZATION.DISPLAY_NAME, "route.organizations.labels.display_name").build(),
                                JooqFieldElement.of(ORGANIZATION.DESCRIPTION, "route.organizations.labels.description").build(),
                                JooqFieldElement.of(ORGANIZATION.WEBSITE, "route.organizations.labels.website").build(),
                                JooqCollectionElement.of("route.organizations.labels.members")
                                        .factory(new ListCollectionFactory<>())
                                        .configuration(JooqCollection.builder(new FormDialogFactory<>())
                                                .data(JooqCollectionConfiguration.of(organizationMemberConfig)
                                                        .oneToMany(new JooqOneToMany(ORGANIZATION_MEMBER.ORGANIZATION_ID))
                                                        .children(List.of(ORGANIZATION_MEMBER.USER_ID, ORGANIZATION_MEMBER.ROLE))
                                                        .build())
                                                .child(JooqFormRoute.builder()
                                                     .formConfiguration(JooqFormRendererConfiguration.builder()
                                                         .titleField(ORGANIZATION_MEMBER.USER_ID)
                                                         .children(List.of(
                                                             JooqFieldElement.of(ORGANIZATION_MEMBER.USER_ID, "route.organization_members.labels.user").build(),
                                                             JooqFieldElement.of(ORGANIZATION_MEMBER.ROLE, "route.organization_members.labels.role").build()
                                                         ))
                                                         .build())
                                                     .build())
                                                .build())
                                        .build()))
                        .build())
                .build();

        // User Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> userForm = JooqFormRoute.builder()
                .dataStoreConfig(usersConfig)
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(USERS.USERNAME)
                        .children(List.of(
                                JooqFieldElement.of(USERS.USERNAME, "route.users.labels.username").build(),
                                JooqFieldElement.of(USERS.PASSWORD_HASH, "route.users.labels.password").build(),
                                JooqCollectionElement.of("route.users.labels.repositories")
                                        .factory(new ListCollectionFactory<>())
                                        .configuration(JooqCollection.builder(new FormDialogFactory<>())
                                                .data(JooqCollectionConfiguration.of(repositoryCollaboratorConfig)
                                                        .oneToMany(new JooqOneToMany(REPOSITORY_COLLABORATOR.USER_ID))
                                                        .children(List.of(REPOSITORY_COLLABORATOR.REPOSITORY_ID, REPOSITORY_COLLABORATOR.PERMISSION))
                                                        .build())
                                                .child(JooqFormRoute.builder()
                                                     .formConfiguration(JooqFormRendererConfiguration.builder()
                                                         .titleField(REPOSITORY_COLLABORATOR.REPOSITORY_ID)
                                                         .children(List.of(
                                                             JooqFieldElement.of(REPOSITORY_COLLABORATOR.REPOSITORY_ID, "route.repository_collaborators.labels.repository").build(),
                                                             JooqFieldElement.of(REPOSITORY_COLLABORATOR.PERMISSION, "route.repository_collaborators.labels.permission").build()
                                                         ))
                                                         .build())
                                                     .build())
                                                .build())
                                        .build(),
                                JooqCollectionElement.of("route.users.labels.organizations")
                                        .factory(new ListCollectionFactory<>())
                                        .configuration(JooqCollection.builder(new FormDialogFactory<>())
                                                .data(JooqCollectionConfiguration.of(organizationMemberConfig)
                                                        .oneToMany(new JooqOneToMany(ORGANIZATION_MEMBER.USER_ID))
                                                        .children(List.of(ORGANIZATION_MEMBER.ORGANIZATION_ID, ORGANIZATION_MEMBER.ROLE))
                                                        .build())
                                                .child(JooqFormRoute.builder()
                                                     .formConfiguration(JooqFormRendererConfiguration.builder()
                                                         .titleField(ORGANIZATION_MEMBER.ORGANIZATION_ID)
                                                         .children(List.of(
                                                             JooqFieldElement.of(ORGANIZATION_MEMBER.ORGANIZATION_ID, "route.organization_members.labels.organization").build(),
                                                             JooqFieldElement.of(ORGANIZATION_MEMBER.ROLE, "route.organization_members.labels.role").build()
                                                         ))
                                                         .build())
                                                     .build())
                                                .build())
                                        .build()))
                        .build())
                .build();

        // Milestone Form Configuration
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> milestoneForm = JooqFormRoute.builder()
                .dataStoreConfig(milestoneConfig)
                .title("route.milestones.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(MILESTONE.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                                JooqFieldElement.of(MILESTONE.DESCRIPTION, "route.milestones.labels.description").build(),
                                JooqFieldElement.of(MILESTONE.STATE, "route.milestones.labels.state").build(),
                                JooqFieldElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build()))
                        .build())
                .build();

        // Routes Configuration
        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        routes.put("repositories", JooqGridRoute.builder()
                .isDefaultRoute(true)
                .dataStoreConfig(repositoryConfig)
                .iconFactory(VaadinIcon.STORAGE::create)
                .title("route.repositories.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(REPOSITORY.NAME)
                        .descriptionField(REPOSITORY.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin", "write")) // Repo permission values
                .child(repositoryForm)
                .build());

        routes.put("issues", JooqKanbanRoute.builder()
                .iconFactory(VaadinIcon.BUG::create)
                .dataStoreConfig(issueConfig)
                .title("route.issues.title")
                .configuration(JooqKanbanConfiguration.builder()
                        .titleField(ISSUE.TITLE)
                        .descriptionField(ISSUE.DESCRIPTION)
                        .columnField(ISSUE.STATE)
                        .filterField(ISSUE.TITLE)
                        .build())
                .writeRoles(List.of("admin", "developer", "contributor"))
                .child(issueForm)
                .build());

        routes.put("pull-requests", JooqListRoute.builder()
                .dataStoreConfig(pullRequestConfig)
                .iconFactory(VaadinIcon.COMPILE::create)
                .title("route.pull_requests.title")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField(PULL_REQUEST.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(PULL_REQUEST.TITLE, "route.pull_requests.labels.title").build(),
                                JooqFieldElement.of(PULL_REQUEST.STATE, "route.pull_requests.labels.state").build(),
                                JooqFieldElement.of(PULL_REQUEST.SOURCE_BRANCH, "route.pull_requests.labels.source_branch").build(),
                                JooqFieldElement.of(PULL_REQUEST.TARGET_BRANCH, "route.pull_requests.labels.target_branch").build()))
                        .build())
                .writeRoles(List.of("admin", "developer", "contributor"))
                .child(pullRequestForm)
                .build());

        routes.put("organizations", JooqGridRoute.builder()
                .dataStoreConfig(organizationConfig)
                .iconFactory(VaadinIcon.BUILDING::create)
                .title("route.organizations.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(ORGANIZATION.NAME)
                        .descriptionField(ORGANIZATION.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin", "owner")) // Org role values
                .child(organizationForm)
                .build());

        routes.put("milestones", JooqListRoute.builder()
                .dataStoreConfig(milestoneConfig)
                .iconFactory(VaadinIcon.FLAG::create)
                .title("route.milestones.title")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField(MILESTONE.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                                JooqFieldElement.of(MILESTONE.STATE, "route.milestones.labels.state").build(),
                                JooqFieldElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build()))
                        .build())
                .writeRoles(List.of("admin", "developer"))
                .child(milestoneForm)
                .build());

        routes.put("users", JooqGridRoute.builder()
                .dataStoreConfig(usersConfig)
                .iconFactory(VaadinIcon.USERS::create)
                .title("route.users.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(USERS.USERNAME)
                        .build())
                .writeRoles(List.of("admin"))
                .child(userForm)
                .build());

        // Select Options
        LinkedHashMap<RepositoryVisibility, String> repositoryVisibilities = new LinkedHashMap<>();
        repositoryVisibilities.put(RepositoryVisibility.PUBLIC, "selects.repository-visibility.public");
        repositoryVisibilities.put(RepositoryVisibility.PRIVATE, "selects.repository-visibility.private");
        repositoryVisibilities.put(RepositoryVisibility.INTERNAL, "selects.repository-visibility.internal");

        LinkedHashMap<IssueState, String> issueStates = new LinkedHashMap<>();
        issueStates.put(IssueState.OPEN, "selects.issue-state.open");
        issueStates.put(IssueState.CLOSED, "selects.issue-state.closed");

        LinkedHashMap<PullRequestState, String> pullRequestStates = new LinkedHashMap<>();
        pullRequestStates.put(PullRequestState.OPEN, "selects.pull-request-state.open");
        pullRequestStates.put(PullRequestState.MERGED, "selects.pull-request-state.merged");
        pullRequestStates.put(PullRequestState.CLOSED, "selects.pull-request-state.closed");

        LinkedHashMap<Priority, String> priorities = new LinkedHashMap<>();
        priorities.put(Priority.LOW, "selects.priority.low");
        priorities.put(Priority.MEDIUM, "selects.priority.medium");
        priorities.put(Priority.HIGH, "selects.priority.high");
        priorities.put(Priority.CRITICAL, "selects.priority.critical");

        LinkedHashMap<String, String> repositoryPermissions = new LinkedHashMap<>();
        repositoryPermissions.put("admin", "selects.repository-permissions.admin");
        repositoryPermissions.put("write", "selects.repository-permissions.write");
        repositoryPermissions.put("read", "selects.repository-permissions.read");

        LinkedHashMap<String, String> organizationRoles = new LinkedHashMap<>();
        organizationRoles.put("owner", "selects.organization-roles.owner");
        organizationRoles.put("admin", "selects.organization-roles.admin");
        organizationRoles.put("member", "selects.organization-roles.member");

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("dev_i18n")
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(usersConfig)
                        .roleResolutionStrategy(new ClassBasedRoleResolutionStrategy<>(Map.of(
                                REPOSITORY.getRecordType(), new JoinTableRoleResolutionStrategy<>(
                                        (VortexCrudDataStore) repositoryCollaboratorStore,
                                        REPOSITORY_COLLABORATOR.USER_ID,
                                        REPOSITORY_COLLABORATOR.REPOSITORY_ID,
                                        REPOSITORY_COLLABORATOR.PERMISSION,
                                        USERS.ID,
                                        REPOSITORY.ID
                                ),
                                ORGANIZATION.getRecordType(), new JoinTableRoleResolutionStrategy<>(
                                        (VortexCrudDataStore) organizationMemberStore,
                                        ORGANIZATION_MEMBER.USER_ID,
                                        ORGANIZATION_MEMBER.ORGANIZATION_ID,
                                        ORGANIZATION_MEMBER.ROLE,
                                        USERS.ID,
                                        ORGANIZATION.ID
                                )
                        )))
                        .availableRoles(Roles.builder().roles(List.of("admin", "developer", "contributor", "viewer")).build())
                        .defaultReadRoles(List.of("viewer"))
                        .defaultWriteRoles(List.of("admin", "developer"))
                        .signUpEnabled(true)
                        .loginView(LoginView.class)
                        .signUpView(SignUpView.class)
                        .username(JooqFieldElement.of(USERS.USERNAME, "route.users.labels.username").build())
                        .password(JooqFieldElement.of(USERS.PASSWORD_HASH, "route.users.labels.password").build())
                        .signUpFields(List.of())
                        .rolesField(null)
                        .build())
                .routes(routes)
                .versioning(JooqVersioning.builder().dataStores(List.of(REPOSITORY, ISSUE, PULL_REQUEST, ORGANIZATION, MILESTONE)).build())
                .auditing(Auditing.builder().actions(List.of(CREATE, UPDATE, DELETE, LOGIN, LOGOUT)).build())
                .selects(Selects.builder()
                        .configs(Map.of(
                                "repository-visibility", repositoryVisibilities,
                                "issue-state", issueStates,
                                "pull-request-state", pullRequestStates,
                                "priority", priorities,
                                "repository-permissions", repositoryPermissions,
                                "organization-roles", organizationRoles))
                        .build())
                .build();
    }
}
