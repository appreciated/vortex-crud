package com.github.appreciated.vortex_crud.demo.devplatform;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.actions.SingleEntityRouteAction;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.IssueState;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.Priority;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.PullRequestState;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.RepositoryVisibility;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.*;
import com.github.appreciated.vortex_crud.demo.devplatform.service.GitRepositoryService;
import com.github.appreciated.vortex_crud.demo.devplatform.view.DashboardView;
import com.github.appreciated.vortex_crud.demo.devplatform.view.RepositoryDetailViewFactory;
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
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinServletRequest;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.github.appreciated.vortex_crud.core.config.model.AuditingAction.*;
import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

@Service
public class DevPlatformConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;
    private final GitRepositoryService gitService;

    public DevPlatformConfiguration(DSLContext dsl, GitRepositoryService gitService) {
        this.dsl = dsl;
        this.gitService = gitService;
    }

    @Override
    public com.github.appreciated.vortex_crud.core.config.model.Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Data Stores
        JooqDataStore<UsersRecord> usersStore = new JooqDataStore<>(USERS.getRecordType(), dsl);
        JooqDataStore<RolesRecord> rolesStore = new JooqDataStore<>(ROLES.getRecordType(), dsl);
        JooqDataStore<UserRolesRecord> userRolesStore = new JooqDataStore<>(USER_ROLES.getRecordType(), dsl);
        JooqDataStore<NotificationRecord> notificationStore = new JooqDataStore<>(NOTIFICATION.getRecordType(), dsl);
        JooqDataStore<RepositoryRecord> repositoryStore = new JooqDataStore<>(REPOSITORY.getRecordType(), dsl);
        JooqDataStore<RepositoryStarRecord> repositoryStarStore = new JooqDataStore<>(REPOSITORY_STAR.getRecordType(), dsl);
        JooqDataStore<WikiPageRecord> wikiPageStore = new JooqDataStore<>(WIKI_PAGE.getRecordType(), dsl);
        JooqDataStore<GitCommitRecord> gitCommitStore = new JooqDataStore<>(GIT_COMMIT.getRecordType(), dsl);
        JooqDataStore<GitBranchRecord> gitBranchStore = new JooqDataStore<>(GIT_BRANCH.getRecordType(), dsl);

        // Notification Hooks
        DataStoreHooks<IssueRecord> issueHooks = DataStoreHooks.<IssueRecord>builder()
                .afterCreate(record -> {
                    try {
                        String title = record.get(ISSUE.TITLE);
                        Integer repoId = record.get(ISSUE.REPOSITORY_ID);
                        Integer assigneeId = record.get(ISSUE.ASSIGNEE_ID);
                        var repo = dsl.selectFrom(REPOSITORY).where(REPOSITORY.ID.eq(repoId)).fetchOne();
                        if (repo != null) {
                            if (assigneeId != null) {
                                var notif = dsl.newRecord(NOTIFICATION);
                                notif.setUserId(assigneeId);
                                notif.setTitle("Assigned to Issue: " + title);
                                notif.setMessage("In repository " + repo.getName());
                                notif.setIsRead(0);
                                notif.setCreatedAt(LocalDateTime.now().toString());
                                notif.store();
                            }
                            Integer ownerId = repo.getOwnerId();
                            if (ownerId != null && !ownerId.equals(assigneeId)) {
                                var notif = dsl.newRecord(NOTIFICATION);
                                notif.setUserId(ownerId);
                                notif.setTitle("New Issue: " + title);
                                notif.setMessage("In repository " + repo.getName());
                                notif.setIsRead(0);
                                notif.setCreatedAt(LocalDateTime.now().toString());
                                notif.store();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .build();

        DataStoreHooks<PullRequestRecord> prHooks = DataStoreHooks.<PullRequestRecord>builder()
                .afterCreate(record -> {
                    try {
                        String title = record.get(PULL_REQUEST.TITLE);
                        Integer repoId = record.get(PULL_REQUEST.REPOSITORY_ID);
                        Integer assigneeId = record.get(PULL_REQUEST.ASSIGNEE_ID);
                        var repo = dsl.selectFrom(REPOSITORY).where(REPOSITORY.ID.eq(repoId)).fetchOne();
                        if (repo != null) {
                            if (assigneeId != null) {
                                var notif = dsl.newRecord(NOTIFICATION);
                                notif.setUserId(assigneeId);
                                notif.setTitle("Assigned to PR: " + title);
                                notif.setMessage("In repository " + repo.getName());
                                notif.setIsRead(0);
                                notif.setCreatedAt(LocalDateTime.now().toString());
                                notif.store();
                            }
                            Integer ownerId = repo.getOwnerId();
                            if (ownerId != null && !ownerId.equals(assigneeId)) {
                                var notif = dsl.newRecord(NOTIFICATION);
                                notif.setUserId(ownerId);
                                notif.setTitle("New PR: " + title);
                                notif.setMessage("In repository " + repo.getName());
                                notif.setIsRead(0);
                                notif.setCreatedAt(LocalDateTime.now().toString());
                                notif.store();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .build();

        JooqDataStore<IssueRecord> issueStore = new JooqDataStore<>(ISSUE.getRecordType(), dsl, issueHooks);
        JooqDataStore<PullRequestRecord> pullRequestStore = new JooqDataStore<>(PULL_REQUEST.getRecordType(), dsl, prHooks);
        JooqDataStore<OrganizationRecord> organizationStore = new JooqDataStore<>(ORGANIZATION.getRecordType(), dsl);
        JooqDataStore<MilestoneRecord> milestoneStore = new JooqDataStore<>(MILESTONE.getRecordType(), dsl);
        JooqDataStore<LabelRecord> labelStore = new JooqDataStore<>(LABEL.getRecordType(), dsl);
        JooqDataStore<CommentRecord> commentStore = new JooqDataStore<>(COMMENT.getRecordType(), dsl);
        JooqDataStore<IssueLabelRecord> issueLabelStore = new JooqDataStore<>(ISSUE_LABEL.getRecordType(), dsl);
        JooqDataStore<PullRequestLabelRecord> pullRequestLabelStore = new JooqDataStore<>(PULL_REQUEST_LABEL.getRecordType(), dsl);
        JooqDataStore<OrganizationMemberRecord> organizationMemberStore = new JooqDataStore<>(ORGANIZATION_MEMBER.getRecordType(), dsl);
        JooqDataStore<RepositoryCollaboratorRecord> repositoryCollaboratorStore = new JooqDataStore<>(REPOSITORY_COLLABORATOR.getRecordType(), dsl);

        // Configs
        var usersConfig = JooqDataStoreConfig.of(USERS)
                .dataStoreInstance(usersStore)
                .fields(Map.of(
                        USERS.ID, JooqNumericIdField.builder().build(),
                        USERS.USERNAME, JooqEmailField.builder().required(true).build(),
                        USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                        USERS.CREATED_AT, JooqDateTimePickerField.builder().build()))
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

        var wikiPageConfig = JooqDataStoreConfig.of(WIKI_PAGE)
                .dataStoreInstance(wikiPageStore)
                .fields(Map.of(
                        WIKI_PAGE.ID, JooqNumericIdField.builder().build(),
                        WIKI_PAGE.REPOSITORY_ID, JooqReferenceField.builder().dataStore(repositoryStore).field(WIKI_PAGE.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                        WIKI_PAGE.TITLE, JooqTextField.builder().required(true).build(),
                        WIKI_PAGE.CONTENT, JooqMarkDownField.builder().build(),
                        WIKI_PAGE.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var gitCommitConfig = JooqDataStoreConfig.of(GIT_COMMIT)
                .dataStoreInstance(gitCommitStore)
                .fields(Map.of(
                        GIT_COMMIT.ID, JooqNumericIdField.builder().build(),
                        GIT_COMMIT.REPOSITORY_ID, JooqReferenceField.builder().dataStore(repositoryStore).field(GIT_COMMIT.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                        GIT_COMMIT.HASH, JooqTextField.builder().build(),
                        GIT_COMMIT.MESSAGE, JooqTextAreaField.builder().build(),
                        GIT_COMMIT.AUTHOR_NAME, JooqTextField.builder().build(),
                        GIT_COMMIT.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var gitBranchConfig = JooqDataStoreConfig.of(GIT_BRANCH)
                .dataStoreInstance(gitBranchStore)
                .fields(Map.of(
                        GIT_BRANCH.ID, JooqNumericIdField.builder().build(),
                        GIT_BRANCH.REPOSITORY_ID, JooqReferenceField.builder().dataStore(repositoryStore).field(GIT_BRANCH.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                        GIT_BRANCH.NAME, JooqTextField.builder().build(),
                        GIT_BRANCH.HEAD_COMMIT_ID, JooqReferenceField.builder().dataStore(gitCommitStore).field(GIT_BRANCH.HEAD_COMMIT_ID).filterField(GIT_COMMIT.HASH).children(List.of(GIT_COMMIT.HASH)).build()))
                .build();

        var organizationMemberConfig = JooqDataStoreConfig.of(ORGANIZATION_MEMBER)
                .dataStoreInstance(organizationMemberStore)
                .fields(Map.of(
                        ORGANIZATION_MEMBER.ID, JooqNumericIdField.builder().build(),
                        ORGANIZATION_MEMBER.ORGANIZATION_ID, JooqReferenceField.builder().dataStore(organizationStore).field(ORGANIZATION_MEMBER.ORGANIZATION_ID).filterField(ORGANIZATION.NAME).children(List.of(ORGANIZATION.NAME)).build(),
                        ORGANIZATION_MEMBER.USER_ID, JooqReferenceField.builder().dataStore(usersStore).field(ORGANIZATION_MEMBER.USER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        ORGANIZATION_MEMBER.ROLE, JooqSelectField.builder().values("organization-roles").build(),
                        ORGANIZATION_MEMBER.JOINED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var repositoryCollaboratorConfig = JooqDataStoreConfig.of(REPOSITORY_COLLABORATOR)
                .dataStoreInstance(repositoryCollaboratorStore)
                .fields(Map.of(
                        REPOSITORY_COLLABORATOR.ID, JooqNumericIdField.builder().build(),
                        REPOSITORY_COLLABORATOR.REPOSITORY_ID, JooqReferenceField.builder().dataStore(repositoryStore).field(REPOSITORY_COLLABORATOR.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                        REPOSITORY_COLLABORATOR.USER_ID, JooqReferenceField.builder().dataStore(usersStore).field(REPOSITORY_COLLABORATOR.USER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        REPOSITORY_COLLABORATOR.PERMISSION, JooqSelectField.builder().values("repository-permissions").build(),
                        REPOSITORY_COLLABORATOR.INVITED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var organizationConfig = JooqDataStoreConfig.of(ORGANIZATION)
                .dataStoreInstance(organizationStore)
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
                .dataStoreInstance(repositoryStore)
                .fields(Map.ofEntries(
                        Map.entry(REPOSITORY.ID, JooqNumericIdField.builder().build()),
                        Map.entry(REPOSITORY.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                        Map.entry(REPOSITORY.SLUG, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                        Map.entry(REPOSITORY.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 2000 characters", 0, 2000))).build()),
                        Map.entry(REPOSITORY.OWNER_ID, JooqReferenceField.builder().dataStore(usersStore).field(REPOSITORY.OWNER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(REPOSITORY.ORGANIZATION_ID, JooqReferenceField.builder().dataStore(organizationStore).field(REPOSITORY.ORGANIZATION_ID).filterField(ORGANIZATION.NAME).children(List.of(ORGANIZATION.NAME)).build()),
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
                .dataStoreInstance(milestoneStore)
                .fields(Map.of(
                        MILESTONE.ID, JooqNumericIdField.builder().build(),
                        MILESTONE.REPOSITORY_ID, JooqReferenceField.builder().dataStore(repositoryStore).field(MILESTONE.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                        MILESTONE.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build(),
                        MILESTONE.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                        MILESTONE.STATE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                        MILESTONE.DUE_DATE, JooqDateField.builder().build(),
                        MILESTONE.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        MILESTONE.CLOSED_AT, JooqDateTimePickerField.builder().build(),
                        MILESTONE.CUSTOM_FIELDS, JooqTextAreaField.builder().build()))
                .build();

        var issueConfig = JooqDataStoreConfig.of(ISSUE)
                .dataStoreInstance(issueStore)
                .fields(Map.ofEntries(
                        Map.entry(ISSUE.ID, JooqNumericIdField.builder().build()),
                        Map.entry(ISSUE.REPOSITORY_ID, JooqReferenceField.builder().dataStore(repositoryStore).field(ISSUE.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build()),
                        Map.entry(ISSUE.ISSUE_NUMBER, JooqIntegerField.builder().required(true).build()),
                        Map.entry(ISSUE.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 300 characters", 0, 300))).build()),
                        Map.entry(ISSUE.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 5000 characters", 0, 5000))).build()),
                        Map.entry(ISSUE.STATE, JooqSelectField.builder().values("issue-state").build()),
                        Map.entry(ISSUE.ISSUE_TYPE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build()),
                        Map.entry(ISSUE.PRIORITY, JooqSelectField.builder().values("priority").build()),
                        Map.entry(ISSUE.AUTHOR_ID, JooqReferenceField.builder().dataStore(usersStore).field(ISSUE.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(ISSUE.ASSIGNEE_ID, JooqReferenceField.builder().dataStore(usersStore).field(ISSUE.ASSIGNEE_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(ISSUE.MILESTONE_ID, JooqReferenceField.builder().dataStore(milestoneStore).field(ISSUE.MILESTONE_ID).filterField(MILESTONE.TITLE).children(List.of(MILESTONE.TITLE)).build()),
                        Map.entry(ISSUE.CLOSED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(ISSUE.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(ISSUE.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(ISSUE.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                .build();

        var pullRequestConfig = JooqDataStoreConfig.of(PULL_REQUEST)
                .dataStoreInstance(pullRequestStore)
                .fields(Map.ofEntries(
                        Map.entry(PULL_REQUEST.ID, JooqNumericIdField.builder().build()),
                        Map.entry(PULL_REQUEST.REPOSITORY_ID, JooqReferenceField.builder().dataStore(repositoryStore).field(PULL_REQUEST.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build()),
                        Map.entry(PULL_REQUEST.PR_NUMBER, JooqIntegerField.builder().required(true).build()),
                        Map.entry(PULL_REQUEST.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 300 characters", 0, 300))).build()),
                        Map.entry(PULL_REQUEST.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 5000 characters", 0, 5000))).build()),
                        Map.entry(PULL_REQUEST.STATE, JooqSelectField.builder().values("pull-request-state").build()),
                        Map.entry(PULL_REQUEST.SOURCE_BRANCH, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build()),
                        Map.entry(PULL_REQUEST.TARGET_BRANCH, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build()),
                        Map.entry(PULL_REQUEST.AUTHOR_ID, JooqReferenceField.builder().dataStore(usersStore).field(PULL_REQUEST.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(PULL_REQUEST.ASSIGNEE_ID, JooqReferenceField.builder().dataStore(usersStore).field(PULL_REQUEST.ASSIGNEE_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                        Map.entry(PULL_REQUEST.MILESTONE_ID, JooqReferenceField.builder().dataStore(milestoneStore).field(PULL_REQUEST.MILESTONE_ID).filterField(MILESTONE.TITLE).children(List.of(MILESTONE.TITLE)).build()),
                        Map.entry(PULL_REQUEST.IS_DRAFT, JooqCheckboxField.builder().build()),
                        Map.entry(PULL_REQUEST.MERGED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PULL_REQUEST.CLOSED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PULL_REQUEST.CREATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PULL_REQUEST.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                        Map.entry(PULL_REQUEST.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                .build();

        var labelConfig = JooqDataStoreConfig.of(LABEL)
                .dataStoreInstance(labelStore)
                .fields(Map.of(
                        LABEL.ID, JooqNumericIdField.builder().build(),
                        LABEL.REPOSITORY_ID, JooqReferenceField.builder().dataStore(repositoryStore).field(LABEL.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                        LABEL.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build(),
                        LABEL.COLOR, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                        LABEL.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                        LABEL.CREATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var commentConfig = JooqDataStoreConfig.of(COMMENT)
                .dataStoreInstance(commentStore)
                .fields(Map.of(
                        COMMENT.ID, JooqNumericIdField.builder().build(),
                        COMMENT.ENTITY_TYPE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                        COMMENT.ENTITY_ID, JooqIntegerField.builder().required(true).build(),
                        COMMENT.AUTHOR_ID, JooqReferenceField.builder().dataStore(usersStore).field(COMMENT.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                        COMMENT.CONTENT, JooqTextAreaField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 5000 characters", 0, 5000))).build(),
                        COMMENT.CREATED_AT, JooqDateTimePickerField.builder().build(),
                        COMMENT.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                .build();

        var issueLabelConfig = JooqDataStoreConfig.of(ISSUE_LABEL)
                .dataStoreInstance(issueLabelStore)
                .fields(Map.of(
                        ISSUE_LABEL.ID, JooqNumericIdField.builder().build(),
                        ISSUE_LABEL.ISSUE_ID, JooqReferenceField.builder().dataStore(issueStore).field(ISSUE_LABEL.ISSUE_ID).filterField(ISSUE.TITLE).children(List.of(ISSUE.TITLE)).build(),
                        ISSUE_LABEL.LABEL_ID, JooqReferenceField.builder().dataStore(labelStore).field(ISSUE_LABEL.LABEL_ID).filterField(LABEL.NAME).children(List.of(LABEL.NAME)).build()))
                .build();

        var pullRequestLabelConfig = JooqDataStoreConfig.of(PULL_REQUEST_LABEL)
                .dataStoreInstance(pullRequestLabelStore)
                .fields(Map.of(
                        PULL_REQUEST_LABEL.ID, JooqNumericIdField.builder().build(),
                        PULL_REQUEST_LABEL.PULL_REQUEST_ID, JooqReferenceField.builder().dataStore(pullRequestStore).field(PULL_REQUEST_LABEL.PULL_REQUEST_ID).filterField(PULL_REQUEST.TITLE).children(List.of(PULL_REQUEST.TITLE)).build(),
                        PULL_REQUEST_LABEL.LABEL_ID, JooqReferenceField.builder().dataStore(labelStore).field(PULL_REQUEST_LABEL.LABEL_ID).filterField(LABEL.NAME).children(List.of(LABEL.NAME)).build()))
                .build();

        // Label Form Configuration
        var labelForm = JooqFormRoute.builder()
                .titleField(LABEL.NAME)
                .fields(List.of(
                        JooqFormElement.of(LABEL.NAME, "route.labels.labels.name").build(),
                        JooqFormElement.of(LABEL.COLOR, "route.labels.labels.color").build(),
                        JooqFormElement.of(LABEL.DESCRIPTION, "route.labels.labels.description").build()))
                .build();

        // Issue Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> issueForm = JooqFormRoute.builder()
                .titleField(ISSUE.TITLE)
                .fields(List.of(
                        JooqFormElement.of(ISSUE.TITLE, "route.issues.labels.title").build(),
                        JooqFormElement.of(ISSUE.DESCRIPTION, "route.issues.labels.description").build(),
                        JooqFormElement.of(ISSUE.STATE, "route.issues.labels.state").build(),
                        JooqFormElement.of(ISSUE.ISSUE_TYPE, "route.issues.labels.type").build(),
                        JooqFormElement.of(ISSUE.PRIORITY, "route.issues.labels.priority").build(),
                        JooqFormElement.of(ISSUE.ASSIGNEE_ID, "route.issues.labels.assignee").build(),
                        JooqFormElement.of(ISSUE.MILESTONE_ID, "route.issues.labels.milestone").build(),
                        JooqCollection.builder()
                                .field(LABEL.NAME)
                                .label("route.issues.labels.labels")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new ConnectDialogFactory<>())
                                .dataStoreConfig(labelConfig)
                                .manyToMany(new JooqManyToMany(
                                        ISSUE_LABEL.ISSUE_ID,
                                        ISSUE_LABEL.LABEL_ID,
                                        LABEL.ID,
                                        ISSUE_LABEL))
                                .children(List.of(LABEL.NAME, LABEL.COLOR))
                                .emptyMessage("route.issues.labels.labels-empty-message")
                                .titleField(LABEL.NAME)
                                .build()))
                .build();

        // Pull Request Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> pullRequestForm = JooqFormRoute.builder()
                .titleField(PULL_REQUEST.TITLE)
                .fields(List.of(
                        JooqFormElement.of(PULL_REQUEST.TITLE, "route.pull-requests.labels.title").build(),
                        JooqFormElement.of(PULL_REQUEST.DESCRIPTION, "route.pull-requests.labels.description").build(),
                        JooqFormElement.of(PULL_REQUEST.STATE, "route.pull-requests.labels.state").build(),
                        JooqFormElement.of(PULL_REQUEST.SOURCE_BRANCH, "route.pull-requests.labels.source_branch").build(),
                        JooqFormElement.of(PULL_REQUEST.TARGET_BRANCH, "route.pull-requests.labels.target_branch").build(),
                        JooqFormElement.of(PULL_REQUEST.ASSIGNEE_ID, "route.pull-requests.labels.assignee").build(),
                        JooqFormElement.of(PULL_REQUEST.IS_DRAFT, "route.pull-requests.labels.is_draft").build(),
                        JooqCollection.builder()
                                .field(LABEL.NAME)
                                .label("route.pull-requests.labels.labels")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new ConnectDialogFactory<>())
                                .dataStoreConfig(labelConfig)
                                .manyToMany(new JooqManyToMany(
                                        PULL_REQUEST_LABEL.PULL_REQUEST_ID,
                                        PULL_REQUEST_LABEL.LABEL_ID,
                                        LABEL.ID,
                                        PULL_REQUEST_LABEL))
                                .children(List.of(LABEL.NAME, LABEL.COLOR))
                                .emptyMessage("route.pull-requests.labels.labels-empty-message")
                                .titleField(LABEL.NAME)
                                .build()))
                .build();

        // Repository Form Configuration
        var repositoryForm = JooqFormRoute.builder()
                .titleField(REPOSITORY.NAME)
                .fields(List.of(
                        JooqFormElement.of(REPOSITORY.NAME, "route.repositories.labels.name").build(),
                        JooqFormElement.of(REPOSITORY.SLUG, "route.repositories.labels.slug").build(),
                        JooqFormElement.of(REPOSITORY.DESCRIPTION, "route.repositories.labels.description").build(),
                        JooqFormElement.of(REPOSITORY.VISIBILITY, "route.repositories.labels.visibility").build(),
                        JooqFormElement.of(REPOSITORY.DEFAULT_BRANCH, "route.repositories.labels.default_branch").build(),
                        JooqFormElement.of(REPOSITORY.LANGUAGE, "route.repositories.labels.language").build(),
                        JooqFormElement.of(REPOSITORY.TOPICS, "route.repositories.labels.topics").build(),
                        JooqFormElement.of(REPOSITORY.README_CONTENT, "route.repositories.labels.readme").build(),
                        JooqCollection.builder()
                                .field(REPOSITORY_COLLABORATOR.USER_ID)
                                .label("route.repositories.labels.collaborators")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(repositoryCollaboratorConfig)
                                .oneToMany(new JooqOneToMany(REPOSITORY_COLLABORATOR.REPOSITORY_ID))
                                .children(List.of(REPOSITORY_COLLABORATOR.USER_ID, REPOSITORY_COLLABORATOR.PERMISSION))
                                .form(JooqFormRoute.builder()
                                        .titleField(REPOSITORY_COLLABORATOR.USER_ID)
                                        .fields(List.of(
                                                JooqFormElement.of(REPOSITORY_COLLABORATOR.USER_ID, "route.repository_collaborators.labels.user").build(),
                                                JooqFormElement.of(REPOSITORY_COLLABORATOR.PERMISSION, "route.repository_collaborators.labels.permission").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollection.builder()
                                .field(WIKI_PAGE.TITLE)
                                .label("route.wiki.title")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(wikiPageConfig)
                                .oneToMany(new JooqOneToMany(WIKI_PAGE.REPOSITORY_ID))
                                .children(List.of(WIKI_PAGE.TITLE, WIKI_PAGE.CREATED_AT))
                                .form(JooqFormRoute.builder()
                                        .titleField(WIKI_PAGE.TITLE)
                                        .fields(List.of(
                                                JooqFormElement.of(WIKI_PAGE.TITLE, "route.wiki.labels.title").build(),
                                                JooqFormElement.of(WIKI_PAGE.CONTENT, "route.wiki.labels.content").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollection.builder()
                                .field(GIT_COMMIT.HASH)
                                .label("route.commits.title")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(gitCommitConfig)
                                .oneToMany(new JooqOneToMany(GIT_COMMIT.REPOSITORY_ID))
                                .children(List.of(GIT_COMMIT.HASH, GIT_COMMIT.MESSAGE, GIT_COMMIT.AUTHOR_NAME, GIT_COMMIT.CREATED_AT))
                                .form(JooqFormRoute.builder()
                                        .titleField(GIT_COMMIT.HASH)
                                        .fields(List.of(
                                                JooqFormElement.of(GIT_COMMIT.HASH, "route.commits.labels.hash").build(),
                                                JooqFormElement.of(GIT_COMMIT.MESSAGE, "route.commits.labels.message").build(),
                                                JooqFormElement.of(GIT_COMMIT.AUTHOR_NAME, "route.commits.labels.author").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollection.builder()
                                .field(GIT_BRANCH.NAME)
                                .label("route.branches.title")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(gitBranchConfig)
                                .oneToMany(new JooqOneToMany(GIT_BRANCH.REPOSITORY_ID))
                                .children(List.of(GIT_BRANCH.NAME))
                                .form(JooqFormRoute.builder()
                                        .titleField(GIT_BRANCH.NAME)
                                        .fields(List.of(
                                                JooqFormElement.of(GIT_BRANCH.NAME, "route.branches.labels.name").build(),
                                                JooqFormElement.of(GIT_BRANCH.HEAD_COMMIT_ID, "route.branches.labels.head_commit").build()
                                        ))
                                        .build())
                                .build()))
                .build();

        // Organization Form Configuration
        var organizationForm = JooqFormRoute.builder()
                .titleField(ORGANIZATION.NAME)
                .fields(List.of(
                        JooqFormElement.of(ORGANIZATION.NAME, "route.organizations.labels.name").build(),
                        JooqFormElement.of(ORGANIZATION.DISPLAY_NAME, "route.organizations.labels.display_name").build(),
                        JooqFormElement.of(ORGANIZATION.DESCRIPTION, "route.organizations.labels.description").build(),
                        JooqFormElement.of(ORGANIZATION.WEBSITE, "route.organizations.labels.website").build(),
                        JooqCollection.builder()
                                .field(ORGANIZATION_MEMBER.USER_ID)
                                .label("route.organizations.labels.members")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(organizationMemberConfig)
                                .oneToMany(new JooqOneToMany(ORGANIZATION_MEMBER.ORGANIZATION_ID))
                                .children(List.of(ORGANIZATION_MEMBER.USER_ID, ORGANIZATION_MEMBER.ROLE))
                                .form(JooqFormRoute.builder()
                                        .titleField(ORGANIZATION_MEMBER.USER_ID)
                                        .fields(List.of(
                                                JooqFormElement.of(ORGANIZATION_MEMBER.USER_ID, "route.organization_members.labels.user").build(),
                                                JooqFormElement.of(ORGANIZATION_MEMBER.ROLE, "route.organization_members.labels.role").build()
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
                                .field(REPOSITORY_COLLABORATOR.REPOSITORY_ID)
                                .label("route.users.labels.repositories")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(repositoryCollaboratorConfig)
                                .oneToMany(new JooqOneToMany(REPOSITORY_COLLABORATOR.USER_ID))
                                .children(List.of(REPOSITORY_COLLABORATOR.REPOSITORY_ID, REPOSITORY_COLLABORATOR.PERMISSION))
                                .form(JooqFormRoute.builder()
                                        .titleField(REPOSITORY_COLLABORATOR.REPOSITORY_ID)
                                        .fields(List.of(
                                                JooqFormElement.of(REPOSITORY_COLLABORATOR.REPOSITORY_ID, "route.repository_collaborators.labels.repository").build(),
                                                JooqFormElement.of(REPOSITORY_COLLABORATOR.PERMISSION, "route.repository_collaborators.labels.permission").build()
                                        ))
                                        .build())
                                .build(),
                        JooqCollection.builder()
                                .field(ORGANIZATION_MEMBER.ORGANIZATION_ID)
                                .label("route.users.labels.organizations")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(organizationMemberConfig)
                                .oneToMany(new JooqOneToMany(ORGANIZATION_MEMBER.USER_ID))
                                .children(List.of(ORGANIZATION_MEMBER.ORGANIZATION_ID, ORGANIZATION_MEMBER.ROLE))
                                .form(JooqFormRoute.builder()
                                        .titleField(ORGANIZATION_MEMBER.ORGANIZATION_ID)
                                        .fields(List.of(
                                                JooqFormElement.of(ORGANIZATION_MEMBER.ORGANIZATION_ID, "route.organization_members.labels.organization").build(),
                                                JooqFormElement.of(ORGANIZATION_MEMBER.ROLE, "route.organization_members.labels.role").build()
                                        ))
                                        .build())
                                .build()))
                .build();

        // Milestone Form Configuration
        var milestoneForm = JooqFormRoute.builder()
                .titleField(MILESTONE.TITLE)
                .fields(List.of(
                        JooqFormElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                        JooqFormElement.of(MILESTONE.DESCRIPTION, "route.milestones.labels.description").build(),
                        JooqFormElement.of(MILESTONE.STATE, "route.milestones.labels.state").build(),
                        JooqFormElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build()))
                .build();

        // Repository Child Routes (project-scoped routes)
        var repositoryIssuesRoute = JooqKanbanRoute.builder()
                .dataStoreConfig(issueConfig)
                .title("route.issues.title")
                .titleField(ISSUE.TITLE)
                .descriptionField(ISSUE.DESCRIPTION)
                .columnField(ISSUE.STATE)
                .filterField(ISSUE.TITLE)
                .writeRoles(List.of("admin", "developer", "contributor"))
                .form(issueForm)
                .build();

        var repositoryPullRequestsRoute = JooqListRoute.builder()
                .dataStoreConfig(pullRequestConfig)
                .title("route.pull-requests.title")
                .filterField(PULL_REQUEST.TITLE)
                .columns(List.of(
                        JooqFormElement.of(PULL_REQUEST.TITLE, "route.pull-requests.labels.title").build(),
                        JooqFormElement.of(PULL_REQUEST.STATE, "route.pull-requests.labels.state").build(),
                        JooqFormElement.of(PULL_REQUEST.SOURCE_BRANCH, "route.pull-requests.labels.source_branch").build(),
                        JooqFormElement.of(PULL_REQUEST.TARGET_BRANCH, "route.pull-requests.labels.target_branch").build()))
                .writeRoles(List.of("admin", "developer", "contributor"))
                .form(pullRequestForm)
                .build();

        var repositoryMilestonesRoute = JooqListRoute.builder()
                .dataStoreConfig(milestoneConfig)
                .title("route.milestones.title")
                .filterField(MILESTONE.TITLE)
                .columns(List.of(
                        JooqFormElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                        JooqFormElement.of(MILESTONE.STATE, "route.milestones.labels.state").build(),
                        JooqFormElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build()))
                .writeRoles(List.of("admin", "developer"))
                .form(milestoneForm)
                .build();

        var repositoryLabelsRoute = JooqGridRoute.builder()
                .dataStoreConfig(labelConfig)
                .title("route.labels.title")
                .titleField(LABEL.NAME)
                .filterField(LABEL.NAME)
                .descriptionField(LABEL.DESCRIPTION)
                .writeRoles(List.of("admin", "developer"))
                .form(labelForm)
                .build();

        // Routes Configuration
        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        routes.put("dashboard", CustomRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .title("route.dashboard.title")
                        .iconFactory(VaadinIcon.DASHBOARD::create)
                .defaultRoute(true)
                .componentClass(DashboardView.class)
                .build());

        routes.put("search", SearchRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .title("route.search.title")
                .iconFactory(VaadinIcon.SEARCH::create)
                .hiddenInMenu(true)  // Only accessible via search panel
                .build());

        routes.put("repositories", JooqGridRoute.builder()
                .defaultRoute(false)
                .dataStoreConfig(repositoryConfig)
                .iconFactory(VaadinIcon.STORAGE::create)
                .title("route.repositories.title")
                .titleField(REPOSITORY.NAME)
                .filterField(REPOSITORY.NAME)
                .descriptionField(REPOSITORY.DESCRIPTION)
                .writeRoles(List.of("admin", "write")) // Repo permission values
                .form(CustomViewFactoryRoute.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(repositoryConfig)
                        .viewFactory(new RepositoryDetailViewFactory(dsl, gitService))
                        .title("route.repositories.title")
                        .routes(Map.of(
                                "issues", repositoryIssuesRoute,
                                "pull-requests", repositoryPullRequestsRoute,
                                "milestones", repositoryMilestonesRoute,
                                "labels", repositoryLabelsRoute,
                                "edit", repositoryForm))
                        .build())
                .routeActions(Collections.singletonList(
                        SingleEntityRouteAction.<TableField<?, ?>, TableRecord<?>>builder()
                                .componentFactory(() -> new Button(VaadinIcon.STAR.create()))
                                .handler(context -> {
                                    TableRecord<?> repo = context.getFirstSelectedEntity();
                                    String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
                                    var users = usersStore.getRecordsFromTableWhereColumnEquals(USERS.USERNAME, username, 0, 1);
                                    if (!users.isEmpty()) {
                                        var user = (TableRecord<?>) users.get(0);
                                        Integer userId = user.get(USERS.ID);
                                        Integer repoId = repo.get(REPOSITORY.ID);

                                        var existingStar = dsl.selectFrom(REPOSITORY_STAR)
                                                .where(REPOSITORY_STAR.USER_ID.eq(userId))
                                                .and(REPOSITORY_STAR.REPOSITORY_ID.eq(repoId))
                                                .fetchOne();

                                        if (existingStar != null) {
                                            existingStar.delete();
                                            Integer count = repo.get(REPOSITORY.STAR_COUNT);
                                            repo.set(REPOSITORY.STAR_COUNT, count != null ? count - 1 : 0);
                                        } else {
                                            var newStar = dsl.newRecord(REPOSITORY_STAR);
                                            newStar.set(REPOSITORY_STAR.USER_ID, userId);
                                            newStar.set(REPOSITORY_STAR.REPOSITORY_ID, repoId);
                                            newStar.store();
                                            Integer count = repo.get(REPOSITORY.STAR_COUNT);
                                            repo.set(REPOSITORY.STAR_COUNT, count != null ? count + 1 : 1);
                                        }
                                        repositoryStore.updateRecordById((RepositoryRecord) repo);
                                    }
                                })
                                .build()
                ))
                .build());

        routes.put("my-issues", JooqKanbanRoute.builder()
                .dataStoreConfig(issueConfig)
                .iconFactory(VaadinIcon.BUG::create)
                .title("route.my-issues.title")
                .titleField(ISSUE.TITLE)
                .descriptionField(ISSUE.DESCRIPTION)
                .columnField(ISSUE.STATE)
                .filterField(ISSUE.TITLE)
                .routeFilter(DynamicRouteFilter.<TableField<?, ?>>builder()
                        .field(ISSUE.ASSIGNEE_ID)
                        .valueProvider(() -> {
                            String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
                            var users = usersStore.getRecordsFromTableWhereColumnEquals(USERS.USERNAME, username, 0, 1);
                            return !users.isEmpty() ? users.getFirst().get(USERS.ID) : null;
                        })
                        .build())
                .form(issueForm)
                .build());

        routes.put("my-pull-requests", JooqListRoute.builder()
                .dataStoreConfig(pullRequestConfig)
                .iconFactory(VaadinIcon.COMPILE::create)
                .title("route.my-pull-requests.title")
                .filterField(PULL_REQUEST.TITLE)
                .columns(List.of(
                        JooqFormElement.of(PULL_REQUEST.TITLE, "route.pull-requests.labels.title").build(),
                        JooqFormElement.of(PULL_REQUEST.STATE, "route.pull-requests.labels.state").build(),
                        JooqFormElement.of(PULL_REQUEST.SOURCE_BRANCH, "route.pull-requests.labels.source_branch").build(),
                        JooqFormElement.of(PULL_REQUEST.TARGET_BRANCH, "route.pull-requests.labels.target_branch").build()))
                .routeFilter(DynamicRouteFilter.<TableField<?, ?>>builder()
                        .field(PULL_REQUEST.AUTHOR_ID)
                        .valueProvider(() -> {
                            String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
                            var users = usersStore.getRecordsFromTableWhereColumnEquals(USERS.USERNAME, username, 0, 1);
                            return !users.isEmpty() ? users.get(0).get(USERS.ID) : null;
                        })
                        .build())
                .form(pullRequestForm)
                .build());

        routes.put("my-milestones", JooqListRoute.builder()
                .dataStoreConfig(milestoneConfig)
                .iconFactory(VaadinIcon.FLAG::create)
                .title("route.my-milestones.title")
                .filterField(MILESTONE.TITLE)
                .columns(List.of(
                        JooqFormElement.of(MILESTONE.TITLE, "route.milestones.labels.title").build(),
                        JooqFormElement.of(MILESTONE.STATE, "route.milestones.labels.state").build(),
                        JooqFormElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build()))
                .form(milestoneForm)
                .build());

        routes.put("organizations", JooqGridRoute.builder()
                .dataStoreConfig(organizationConfig)
                .iconFactory(VaadinIcon.BUILDING::create)
                .title("route.organizations.title")
                .titleField(ORGANIZATION.NAME)
                .filterField(ORGANIZATION.NAME)
                .descriptionField(ORGANIZATION.DESCRIPTION)
                .writeRoles(List.of("admin", "owner")) // Org role values
                .form(organizationForm)
                .build());

        routes.put("users", JooqGridRoute.builder()
                .dataStoreConfig(usersConfig)
                .iconFactory(VaadinIcon.USERS::create)
                .title("route.users.title")
                .titleField(USERS.USERNAME)
                .filterField(USERS.USERNAME)
                .hiddenInMenu(true)  // Hidden from menu - only accessible by admins for managing user permissions
                .writeRoles(List.of("admin"))  // Only global admins can manage users and assign permissions
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
                        .roleResolutionStrategy(new ClassBasedRoleResolutionStrategy<>(
                                Map.of(
                                        REPOSITORY.getRecordType(), new JoinTableRoleResolutionStrategy<TableField<?, ?>>(
                                                (VortexCrudDataStore) repositoryCollaboratorStore,
                                                REPOSITORY_COLLABORATOR.USER_ID,
                                                REPOSITORY_COLLABORATOR.REPOSITORY_ID,
                                                REPOSITORY_COLLABORATOR.PERMISSION,
                                                USERS.ID,
                                                REPOSITORY.ID
                                        ),
                                        ORGANIZATION.getRecordType(), new JoinTableRoleResolutionStrategy<TableField<?, ?>>(
                                                (VortexCrudDataStore) organizationMemberStore,
                                                ORGANIZATION_MEMBER.USER_ID,
                                                ORGANIZATION_MEMBER.ORGANIZATION_ID,
                                                ORGANIZATION_MEMBER.ROLE,
                                                USERS.ID,
                                                ORGANIZATION.ID
                                        )
                                ),
                                // Global role strategy
                                new JoinTableRoleResolutionStrategy<TableField<?, ?>>(
                                        (VortexCrudDataStore) userRolesStore,
                                        (VortexCrudDataStore) rolesStore,
                                        USER_ROLES.USER_ID,
                                        USER_ROLES.ROLE_ID,
                                        ROLES.NAME,
                                        USERS.ID
                                )
                        ))
                        .availableRoles(Roles.builder().roles(List.of("admin", "developer", "contributor", "viewer")).build())
                        .defaultReadRoles(List.of("viewer"))
                        .defaultWriteRoles(List.of("admin", "developer"))
                        .signUpEnabled(true)
                        .loginView(LoginView.class)
                        .signUpView(SignUpView.class)
                        .username(JooqFormElement.of(USERS.USERNAME, "route.users.labels.username").build())
                        .password(JooqFormElement.of(USERS.PASSWORD_HASH, "route.users.labels.password").build())
                        .signUpFields(List.of())
                        .build())
                .routes(routes)
                .versioning(JooqVersioning.builder().dataStores(List.of(REPOSITORY, ISSUE, PULL_REQUEST, ORGANIZATION, MILESTONE, WIKI_PAGE)).build())
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
