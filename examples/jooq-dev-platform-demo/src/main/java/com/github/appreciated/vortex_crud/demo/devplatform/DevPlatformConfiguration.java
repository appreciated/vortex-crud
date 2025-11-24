package com.github.appreciated.vortex_crud.demo.devplatform;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.IssueState;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.Priority;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.PullRequestState;
import com.github.appreciated.vortex_crud.demo.devplatform.enums.RepositoryVisibility;
import com.github.appreciated.vortex_crud.jooq.service.JooqManyToMany;
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
import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

@Service
public class DevPlatformConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Data Store Configurations
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.ofEntries(
                Map.entry(ORGANIZATION, JooqDataStoreConfig.of(ORGANIZATION)
                        .fields(Map.of(
                                ORGANIZATION.ID, JooqIdField.builder().build(),
                                ORGANIZATION.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build(),
                                ORGANIZATION.DISPLAY_NAME, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build(),
                                ORGANIZATION.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                                ORGANIZATION.WEBSITE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                                ORGANIZATION.CREATED_AT, JooqDateTimePickerField.builder().build(),
                                ORGANIZATION.IS_ACTIVE, JooqCheckboxField.builder().build(),
                                ORGANIZATION.CUSTOM_FIELDS, JooqTextAreaField.builder().build()))
                        .build()),

                Map.entry(REPOSITORY, JooqDataStoreConfig.of(REPOSITORY)
                        .fields(Map.ofEntries(
                                Map.entry(REPOSITORY.ID, JooqIdField.builder().build()),
                                Map.entry(REPOSITORY.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                                Map.entry(REPOSITORY.SLUG, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                                Map.entry(REPOSITORY.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 2000 characters", 0, 2000))).build()),
                                Map.entry(REPOSITORY.OWNER_ID, JooqReferenceField.builder().dataStore(USERS).field(REPOSITORY.OWNER_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                                Map.entry(REPOSITORY.ORGANIZATION_ID, JooqReferenceField.builder().dataStore(ORGANIZATION).field(REPOSITORY.ORGANIZATION_ID).filterField(ORGANIZATION.NAME).children(List.of(ORGANIZATION.NAME)).build()),
                                Map.entry(REPOSITORY.VISIBILITY, JooqSelectField.builder().values("repository-visibility").build()),
                                Map.entry(REPOSITORY.DEFAULT_BRANCH, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                                Map.entry(REPOSITORY.IS_ARCHIVED, JooqCheckboxField.builder().build()),
                                Map.entry(REPOSITORY.STAR_COUNT, JooqIntegerField.builder().build()),
                                Map.entry(REPOSITORY.FORK_COUNT, JooqIntegerField.builder().build()),
                                Map.entry(REPOSITORY.LANGUAGE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build()),
                                Map.entry(REPOSITORY.TOPICS, JooqTextAreaField.builder().build()),
                                Map.entry(REPOSITORY.README_CONTENT, JooqTextAreaField.builder().build()),
                                Map.entry(REPOSITORY.CREATED_AT, JooqDateTimePickerField.builder().build()),
                                Map.entry(REPOSITORY.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                                Map.entry(REPOSITORY.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                        .build()),

                Map.entry(ISSUE, JooqDataStoreConfig.of(ISSUE)
                        .fields(Map.ofEntries(
                                Map.entry(ISSUE.ID, JooqIdField.builder().build()),
                                Map.entry(ISSUE.REPOSITORY_ID, JooqReferenceField.builder().dataStore(REPOSITORY).field(ISSUE.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build()),
                                Map.entry(ISSUE.ISSUE_NUMBER, JooqIntegerField.builder().required(true).build()),
                                Map.entry(ISSUE.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 300 characters", 0, 300))).build()),
                                Map.entry(ISSUE.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 5000 characters", 0, 5000))).build()),
                                Map.entry(ISSUE.STATE, JooqSelectField.builder().values("issue-state").build()),
                                Map.entry(ISSUE.ISSUE_TYPE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build()),
                                Map.entry(ISSUE.PRIORITY, JooqSelectField.builder().values("priority").build()),
                                Map.entry(ISSUE.AUTHOR_ID, JooqReferenceField.builder().dataStore(USERS).field(ISSUE.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                                Map.entry(ISSUE.ASSIGNEE_ID, JooqReferenceField.builder().dataStore(USERS).field(ISSUE.ASSIGNEE_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                                Map.entry(ISSUE.MILESTONE_ID, JooqReferenceField.builder().dataStore(MILESTONE).field(ISSUE.MILESTONE_ID).filterField(MILESTONE.TITLE).children(List.of(MILESTONE.TITLE)).build()),
                                Map.entry(ISSUE.CLOSED_AT, JooqDateTimePickerField.builder().build()),
                                Map.entry(ISSUE.CREATED_AT, JooqDateTimePickerField.builder().build()),
                                Map.entry(ISSUE.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                                Map.entry(ISSUE.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                        .build()),

                Map.entry(PULL_REQUEST, JooqDataStoreConfig.of(PULL_REQUEST)
                        .fields(Map.ofEntries(
                                Map.entry(PULL_REQUEST.ID, JooqIdField.builder().build()),
                                Map.entry(PULL_REQUEST.REPOSITORY_ID, JooqReferenceField.builder().dataStore(REPOSITORY).field(PULL_REQUEST.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build()),
                                Map.entry(PULL_REQUEST.PR_NUMBER, JooqIntegerField.builder().required(true).build()),
                                Map.entry(PULL_REQUEST.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 300 characters", 0, 300))).build()),
                                Map.entry(PULL_REQUEST.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 5000 characters", 0, 5000))).build()),
                                Map.entry(PULL_REQUEST.STATE, JooqSelectField.builder().values("pull-request-state").build()),
                                Map.entry(PULL_REQUEST.SOURCE_BRANCH, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build()),
                                Map.entry(PULL_REQUEST.TARGET_BRANCH, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build()),
                                Map.entry(PULL_REQUEST.AUTHOR_ID, JooqReferenceField.builder().dataStore(USERS).field(PULL_REQUEST.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                                Map.entry(PULL_REQUEST.ASSIGNEE_ID, JooqReferenceField.builder().dataStore(USERS).field(PULL_REQUEST.ASSIGNEE_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build()),
                                Map.entry(PULL_REQUEST.MILESTONE_ID, JooqReferenceField.builder().dataStore(MILESTONE).field(PULL_REQUEST.MILESTONE_ID).filterField(MILESTONE.TITLE).children(List.of(MILESTONE.TITLE)).build()),
                                Map.entry(PULL_REQUEST.IS_DRAFT, JooqCheckboxField.builder().build()),
                                Map.entry(PULL_REQUEST.MERGED_AT, JooqDateTimePickerField.builder().build()),
                                Map.entry(PULL_REQUEST.CLOSED_AT, JooqDateTimePickerField.builder().build()),
                                Map.entry(PULL_REQUEST.CREATED_AT, JooqDateTimePickerField.builder().build()),
                                Map.entry(PULL_REQUEST.UPDATED_AT, JooqDateTimePickerField.builder().build()),
                                Map.entry(PULL_REQUEST.CUSTOM_FIELDS, JooqTextAreaField.builder().build())))
                        .build()),

                Map.entry(MILESTONE, JooqDataStoreConfig.of(MILESTONE)
                        .fields(Map.of(
                                MILESTONE.ID, JooqIdField.builder().build(),
                                MILESTONE.REPOSITORY_ID, JooqReferenceField.builder().dataStore(REPOSITORY).field(MILESTONE.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                                MILESTONE.TITLE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 200 characters", 0, 200))).build(),
                                MILESTONE.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 1000 characters", 0, 1000))).build(),
                                MILESTONE.STATE, JooqTextField.builder().validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                                MILESTONE.DUE_DATE, JooqDateField.builder().build(),
                                MILESTONE.CREATED_AT, JooqDateTimePickerField.builder().build(),
                                MILESTONE.CLOSED_AT, JooqDateTimePickerField.builder().build(),
                                MILESTONE.CUSTOM_FIELDS, JooqTextAreaField.builder().build()))
                        .build()),

                Map.entry(LABEL, JooqDataStoreConfig.of(LABEL)
                        .fields(Map.of(
                                LABEL.ID, JooqIdField.builder().build(),
                                LABEL.REPOSITORY_ID, JooqReferenceField.builder().dataStore(REPOSITORY).field(LABEL.REPOSITORY_ID).filterField(REPOSITORY.NAME).children(List.of(REPOSITORY.NAME)).build(),
                                LABEL.NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 50 characters", 0, 50))).build(),
                                LABEL.COLOR, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                                LABEL.DESCRIPTION, JooqTextAreaField.builder().validators(List.of(new StringLengthValidator("Maximum 500 characters", 0, 500))).build(),
                                LABEL.CREATED_AT, JooqDateTimePickerField.builder().build()))
                        .build()),

                Map.entry(COMMENT, JooqDataStoreConfig.of(COMMENT)
                        .fields(Map.of(
                                COMMENT.ID, JooqIdField.builder().build(),
                                COMMENT.ENTITY_TYPE, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 20 characters", 0, 20))).build(),
                                COMMENT.ENTITY_ID, JooqIntegerField.builder().required(true).build(),
                                COMMENT.AUTHOR_ID, JooqReferenceField.builder().dataStore(USERS).field(COMMENT.AUTHOR_ID).filterField(USERS.USERNAME).children(List.of(USERS.USERNAME)).build(),
                                COMMENT.CONTENT, JooqTextAreaField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 5000 characters", 0, 5000))).build(),
                                COMMENT.CREATED_AT, JooqDateTimePickerField.builder().build(),
                                COMMENT.UPDATED_AT, JooqDateTimePickerField.builder().build()))
                        .build()),

                Map.entry(ISSUE_LABEL, JooqDataStoreConfig.of(ISSUE_LABEL)
                        .fields(Map.of(
                                ISSUE_LABEL.ID, JooqIdField.builder().build(),
                                ISSUE_LABEL.ISSUE_ID, JooqReferenceField.builder().dataStore(ISSUE).field(ISSUE_LABEL.ISSUE_ID).filterField(ISSUE.TITLE).children(List.of(ISSUE.TITLE)).build(),
                                ISSUE_LABEL.LABEL_ID, JooqReferenceField.builder().dataStore(LABEL).field(ISSUE_LABEL.LABEL_ID).filterField(LABEL.NAME).children(List.of(LABEL.NAME)).build()))
                        .build()),

                Map.entry(PULL_REQUEST_LABEL, JooqDataStoreConfig.of(PULL_REQUEST_LABEL)
                        .fields(Map.of(
                                PULL_REQUEST_LABEL.ID, JooqIdField.builder().build(),
                                PULL_REQUEST_LABEL.PULL_REQUEST_ID, JooqReferenceField.builder().dataStore(PULL_REQUEST).field(PULL_REQUEST_LABEL.PULL_REQUEST_ID).filterField(PULL_REQUEST.TITLE).children(List.of(PULL_REQUEST.TITLE)).build(),
                                PULL_REQUEST_LABEL.LABEL_ID, JooqReferenceField.builder().dataStore(LABEL).field(PULL_REQUEST_LABEL.LABEL_ID).filterField(LABEL.NAME).children(List.of(LABEL.NAME)).build()))
                        .build()),

                Map.entry(USERS, JooqDataStoreConfig.of(USERS)
                        .fields(Map.of(
                                USERS.ID, JooqIdField.builder().build(),
                                USERS.USERNAME, JooqEmailField.builder().required(true).build(),
                                USERS.PASSWORD_HASH, JooqPasswordField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 255 characters", 0, 255))).build(),
                                USERS.CREATED_AT, JooqDateTimePickerField.builder().build()))
                        .build()),

                Map.entry(CUSTOM_FIELD_DEFINITION, JooqDataStoreConfig.of(CUSTOM_FIELD_DEFINITION)
                        .fields(Map.ofEntries(
                                Map.entry(CUSTOM_FIELD_DEFINITION.ID, JooqIdField.builder().build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.ENTITY_TYPE, JooqSelectField.builder().values("entity-type").required(true).build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.FIELD_NAME, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.FIELD_LABEL, JooqTextField.builder().required(true).validators(List.of(new StringLengthValidator("Maximum 100 characters", 0, 100))).build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.FIELD_TYPE, JooqSelectField.builder().values("field-type").required(true).build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.FIELD_ORDER, JooqIntegerField.builder().build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.IS_REQUIRED, JooqCheckboxField.builder().build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.DEFAULT_VALUE, JooqTextField.builder().build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.OPTIONS, JooqTextAreaField.builder().build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.VALIDATION_RULES, JooqTextAreaField.builder().build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.DESCRIPTION, JooqTextAreaField.builder().build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.IS_ACTIVE, JooqCheckboxField.builder().build()),
                                Map.entry(CUSTOM_FIELD_DEFINITION.CREATED_AT, JooqDateTimePickerField.builder().build())
                        ))
                        .build())
        );

        // Custom Field Definition Form
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> customFieldForm = JooqFormRoute.builder()
                .dataStoreKey(CUSTOM_FIELD_DEFINITION)
                .title("route.custom_fields.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(CUSTOM_FIELD_DEFINITION.FIELD_LABEL)
                        .children(List.of(
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.ENTITY_TYPE, "route.custom_fields.labels.entity_type").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.FIELD_NAME, "route.custom_fields.labels.field_name").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.FIELD_LABEL, "route.custom_fields.labels.field_label").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.FIELD_TYPE, "route.custom_fields.labels.field_type").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.FIELD_ORDER, "route.custom_fields.labels.field_order").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.IS_REQUIRED, "route.custom_fields.labels.is_required").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.DEFAULT_VALUE, "route.custom_fields.labels.default_value").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.OPTIONS, "route.custom_fields.labels.options").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.VALIDATION_RULES, "route.custom_fields.labels.validation_rules").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.DESCRIPTION, "route.custom_fields.labels.description").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.IS_ACTIVE, "route.custom_fields.labels.is_active").build()
                        ))
                        .build())
                .build();

        // Issue Form Configuration
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> issueForm = JooqFormRoute.builder()
                .dataStoreKey(ISSUE)
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
                                        .factory((Class<? extends VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class<?>) ListCollectionFactory.class)
                                        .configuration(JooqCollection.builder((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class) ConnectDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(LABEL)
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
                .dataStoreKey(PULL_REQUEST)
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
                                        .factory((Class<? extends VortexCrudCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class<?>) ListCollectionFactory.class)
                                        .configuration(JooqCollection.builder((Class<? extends VortexCrudDialogFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>>>) (Class) ConnectDialogFactory.class)
                                                .data(JooqCollectionConfiguration.of(LABEL)
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
                .dataStoreKey(REPOSITORY)
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
                                JooqFieldElement.of(REPOSITORY.README_CONTENT, "route.repositories.labels.readme").build()))
                        .build())
                .build();

        // Organization Form Configuration
        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> organizationForm = JooqFormRoute.builder()
                .dataStoreKey(ORGANIZATION)
                .title("route.organizations.title")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(ORGANIZATION.NAME)
                        .children(List.of(
                                JooqFieldElement.of(ORGANIZATION.NAME, "route.organizations.labels.name").build(),
                                JooqFieldElement.of(ORGANIZATION.DISPLAY_NAME, "route.organizations.labels.display_name").build(),
                                JooqFieldElement.of(ORGANIZATION.DESCRIPTION, "route.organizations.labels.description").build(),
                                JooqFieldElement.of(ORGANIZATION.WEBSITE, "route.organizations.labels.website").build()))
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
                                JooqFieldElement.of(MILESTONE.STATE, "route.milestones.labels.state").build(),
                                JooqFieldElement.of(MILESTONE.DUE_DATE, "route.milestones.labels.due_date").build()))
                        .build())
                .build();

        // Routes Configuration
        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        routes.put("repositories", JooqGridRoute.builder()
                .isDefaultRoute(true)
                .dataStoreKey(REPOSITORY)
                .iconFactory(VaadinIcon.STORAGE::create)
                .title("route.repositories.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(REPOSITORY.NAME)
                        .descriptionField(REPOSITORY.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin", "developer"))
                .child(repositoryForm)
                .build());

        routes.put("issues", JooqKanbanRoute.builder()
                .iconFactory(VaadinIcon.BUG::create)
                .dataStoreKey(ISSUE)
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
                .dataStoreKey(PULL_REQUEST)
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
                .dataStoreKey(ORGANIZATION)
                .iconFactory(VaadinIcon.BUILDING::create)
                .title("route.organizations.title")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(ORGANIZATION.NAME)
                        .descriptionField(ORGANIZATION.DESCRIPTION)
                        .build())
                .writeRoles(List.of("admin"))
                .child(organizationForm)
                .build());

        routes.put("milestones", JooqListRoute.builder()
                .dataStoreKey(MILESTONE)
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

        routes.put("milestones-calendar", JooqCalendarRoute.builder()
                .dataStoreKey(MILESTONE)
                .iconFactory(VaadinIcon.CALENDAR::create)
                .title("route.milestones.title-calendar")
                .configuration(JooqCalendarConfiguration.builder()
                        .titleField(MILESTONE.TITLE)
                        .descriptionField(MILESTONE.DESCRIPTION)
                        .startDateField(MILESTONE.DUE_DATE)
                        .build())
                .writeRoles(List.of("admin", "developer"))
                .child(milestoneForm)
                .build());

        routes.put("custom-fields", JooqListRoute.builder()
                .dataStoreKey(CUSTOM_FIELD_DEFINITION)
                .iconFactory(VaadinIcon.TOOLS::create)
                .title("route.custom_fields.title")
                .configuration(JooqListItemRendererConfiguration.builder()
                        .filterField(CUSTOM_FIELD_DEFINITION.FIELD_LABEL)
                        .children(List.of(
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.ENTITY_TYPE, "route.custom_fields.labels.entity_type").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.FIELD_LABEL, "route.custom_fields.labels.field_label").build(),
                                JooqFieldElement.of(CUSTOM_FIELD_DEFINITION.FIELD_TYPE, "route.custom_fields.labels.field_type").build()
                        ))
                        .build())
                .writeRoles(List.of("admin"))
                .child(customFieldForm)
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

        LinkedHashMap<String, String> entityTypes = new LinkedHashMap<>();
        entityTypes.put("repository", "selects.entity-type.repository");
        entityTypes.put("issue", "selects.entity-type.issue");
        entityTypes.put("pull_request", "selects.entity-type.pull_request");
        entityTypes.put("organization", "selects.entity-type.organization");
        entityTypes.put("milestone", "selects.entity-type.milestone");

        LinkedHashMap<String, String> fieldTypes = new LinkedHashMap<>();
        fieldTypes.put("text", "selects.field-type.text");
        fieldTypes.put("number", "selects.field-type.number");
        fieldTypes.put("date", "selects.field-type.date");
        fieldTypes.put("select", "selects.field-type.select");
        fieldTypes.put("multiselect", "selects.field-type.multiselect");
        fieldTypes.put("checkbox", "selects.field-type.checkbox");

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("dev_i18n")
                .identityAndAccessManagement(LocalIdentityAndAccessManagement.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .repositoryKey(USERS)
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
                        .configs(Map.ofEntries(
                                Map.entry("repository-visibility", repositoryVisibilities),
                                Map.entry("issue-state", issueStates),
                                Map.entry("pull-request-state", pullRequestStates),
                                Map.entry("priority", priorities),
                                Map.entry("entity-type", entityTypes),
                                Map.entry("field-type", fieldTypes)
                        ))
                        .build())
                .dataStores(dataStores)
                .build();
    }
}
