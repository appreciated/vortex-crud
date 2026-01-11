package com.github.appreciated.vortex_crud.demo.devplatform.view;

import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import org.jooq.DSLContext;
import org.jooq.TableRecord;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

public class RepositoryDetailView extends VerticalLayout {

    private final RepositoryRecord repository;
    private final DSLContext dsl;
    private final VerticalLayout contentArea;
    private final VerticalLayout sidebar;
    private final Span starCount;
    private final Map<Tab, VerticalLayout> tabContents = new HashMap<>();

    public RepositoryDetailView(RepositoryRecord repository, DSLContext dsl) {
        this.repository = repository;
        this.dsl = dsl;

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle()
                .set("background-color", "var(--lumo-contrast-5pct)");

        // Initialize content components
        contentArea = new VerticalLayout();
        sidebar = new VerticalLayout();
        starCount = new Span(String.valueOf(repository.getStarCount() != null ? repository.getStarCount() : 0));

        buildView();
    }

    private void buildView() {
        // Header section
        VerticalLayout header = createHeader();
        add(header);

        // Navigation Tabs
        Tabs tabs = createTabs();
        add(tabs);

        // Main content area with sidebar
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);
        mainContent.setSpacing(true);
        mainContent.getStyle().set("gap", "var(--lumo-space-l)");

        // Setup Main Content Layout
        contentArea.setWidth("70%");
        contentArea.setPadding(false);
        contentArea.setSpacing(true);

        // Setup Sidebar Layout
        sidebar.setWidth("30%");
        sidebar.setPadding(false);
        sidebar.setSpacing(true);
        createSidebarContent();

        mainContent.add(contentArea, sidebar);
        add(mainContent);

        // Load initial tab content
        updateContentArea(tabs.getSelectedTab());
        tabs.addSelectedChangeListener(event -> updateContentArea(event.getSelectedTab()));
    }

    private VerticalLayout createHeader() {
        VerticalLayout header = new VerticalLayout();
        header.setPadding(true);
        header.setSpacing(true);
        header.setWidthFull();
        header.getStyle()
                .set("background-color", "var(--lumo-base-color)")
                .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

        // Repository name and description
        H1 repoName = new H1(repository.getName());
        repoName.getStyle()
                .set("margin", "0")
                .set("font-size", "var(--lumo-font-size-xxl)")
                .set("font-weight", "600");

        if (repository.getDescription() != null && !repository.getDescription().isEmpty()) {
            Paragraph description = new Paragraph(repository.getDescription());
            description.getStyle()
                    .set("margin-top", "var(--lumo-space-xs)")
                    .set("color", "var(--lumo-secondary-text-color)");
            header.add(repoName, description);
        } else {
            header.add(repoName);
        }

        // Stats and action buttons
        HorizontalLayout statsAndActions = new HorizontalLayout();
        statsAndActions.setWidthFull();
        statsAndActions.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        statsAndActions.setAlignItems(FlexComponent.Alignment.CENTER);
        statsAndActions.getStyle().set("margin-top", "var(--lumo-space-m)");

        // Stats
        HorizontalLayout stats = createStatsLayout();

        // Action buttons
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);

        Button starButton = new Button("Star", new Icon(VaadinIcon.STAR));
        starButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        starButton.addClickListener(e -> toggleStar());

        Button editButton = new Button(getTranslation("button.edit"), new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        editButton.addClickListener(e -> UI.getCurrent().navigate("repositories/" + repository.getId() + "/edit"));

        actions.add(starButton, editButton);
        statsAndActions.add(stats, actions);
        header.add(statsAndActions);

        return header;
    }

    private void toggleStar() {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        var users = dsl.selectFrom(USERS).where(USERS.USERNAME.eq(username)).limit(1).fetch();

        if (!users.isEmpty()) {
            var user = users.get(0);
            Integer userId = user.get(USERS.ID);
            Integer repoId = repository.get(REPOSITORY.ID);

            var existingStar = dsl.selectFrom(REPOSITORY_STAR)
                    .where(REPOSITORY_STAR.USER_ID.eq(userId))
                    .and(REPOSITORY_STAR.REPOSITORY_ID.eq(repoId))
                    .fetchOne();

            if (existingStar != null) {
                existingStar.delete();
                Integer count = repository.get(REPOSITORY.STAR_COUNT);
                repository.set(REPOSITORY.STAR_COUNT, count != null ? count - 1 : 0);
            } else {
                var newStar = dsl.newRecord(REPOSITORY_STAR);
                newStar.set(REPOSITORY_STAR.USER_ID, userId);
                newStar.set(REPOSITORY_STAR.REPOSITORY_ID, repoId);
                newStar.store();
                Integer count = repository.get(REPOSITORY.STAR_COUNT);
                repository.set(REPOSITORY.STAR_COUNT, count != null ? count + 1 : 1);
            }
            // Update UI
            starCount.setText(String.valueOf(repository.getStarCount()));
        }
    }

    private HorizontalLayout createStatsLayout() {
        HorizontalLayout stats = new HorizontalLayout();
        stats.setSpacing(true);
        stats.setAlignItems(FlexComponent.Alignment.CENTER);

        // Stars
        Span starIcon = new Span(new Icon(VaadinIcon.STAR));
        starIcon.getStyle().set("color", "var(--lumo-secondary-text-color)");

        starCount.getStyle().set("font-weight", "600");
        Span starLabel = new Span("stars");
        starLabel.getStyle().set("color", "var(--lumo-secondary-text-color)");

        HorizontalLayout starStat = new HorizontalLayout(starIcon, starCount, starLabel);
        starStat.setSpacing(true);
        starStat.setAlignItems(FlexComponent.Alignment.CENTER);
        starStat.getStyle().set("gap", "var(--lumo-space-xs)");

        // Forks
        Span forkIcon = new Span(new Icon(VaadinIcon.CLUSTER));
        forkIcon.getStyle().set("color", "var(--lumo-secondary-text-color)");
        Span forkCount = new Span(String.valueOf(repository.getForkCount() != null ? repository.getForkCount() : 0));
        forkCount.getStyle().set("font-weight", "600");
        Span forkLabel = new Span("forks");
        forkLabel.getStyle().set("color", "var(--lumo-secondary-text-color)");

        HorizontalLayout forkStat = new HorizontalLayout(forkIcon, forkCount, forkLabel);
        forkStat.setSpacing(true);
        forkStat.setAlignItems(FlexComponent.Alignment.CENTER);
        forkStat.getStyle().set("gap", "var(--lumo-space-xs)");

        stats.add(starStat, forkStat);
        return stats;
    }

    private Tabs createTabs() {
        Tabs tabs = new Tabs();
        tabs.setWidthFull();
        tabs.getStyle().set("background-color", "var(--lumo-base-color)");
        tabs.getStyle().set("padding-left", "var(--lumo-space-m)");

        Tab codeTab = new Tab("Code");
        Tab issuesTab = new Tab("Issues");
        Tab prTab = new Tab("Pull Requests");
        Tab wikiTab = new Tab("Wiki");
        Tab commitsTab = new Tab("Commits");
        Tab branchesTab = new Tab("Branches");

        tabs.add(codeTab, issuesTab, prTab, wikiTab, commitsTab, branchesTab);

        tabContents.put(codeTab, createCodeTab());
        tabContents.put(issuesTab, createIssuesTab());
        tabContents.put(prTab, createPullRequestsTab());
        tabContents.put(wikiTab, createWikiTab());
        tabContents.put(commitsTab, createCommitsTab());
        tabContents.put(branchesTab, createBranchesTab());

        return tabs;
    }

    private void updateContentArea(Tab selectedTab) {
        contentArea.removeAll();
        VerticalLayout content = tabContents.get(selectedTab);
        if (content != null) {
            contentArea.add(content);
        }
    }

    private VerticalLayout createCodeTab() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.getStyle()
                .set("background-color", "var(--lumo-base-color)")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("border", "1px solid var(--lumo-contrast-10pct)");

        // README section
        if (repository.getReadmeContent() != null && !repository.getReadmeContent().isEmpty()) {
            H3 readmeHeader = new H3("README.md");
            readmeHeader.getStyle()
                    .set("margin-top", "0")
                    .set("padding-bottom", "var(--lumo-space-m)")
                    .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

            Div readmeContent = new Div();
            readmeContent.getStyle()
                    .set("padding", "var(--lumo-space-m)")
                    .set("white-space", "pre-wrap")
                    .set("font-family", "var(--lumo-font-family)")
                    .set("line-height", "1.6");
            readmeContent.setText(repository.getReadmeContent());

            content.add(readmeHeader, readmeContent);
        } else {
            Paragraph noReadme = new Paragraph("No README available");
            noReadme.getStyle().set("color", "var(--lumo-secondary-text-color)");
            content.add(noReadme);
        }

        return content;
    }

    private VerticalLayout createIssuesTab() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);

        var issues = dsl.selectFrom(ISSUE)
                .where(ISSUE.REPOSITORY_ID.eq(repository.getId()))
                .fetch();

        Grid<IssueRecord> grid = new Grid<>();
        grid.addColumn(IssueRecord::getTitle).setHeader("Title");
        grid.addColumn(IssueRecord::getState).setHeader("State");
        grid.addColumn(IssueRecord::getPriority).setHeader("Priority");
        grid.addItemClickListener(e -> UI.getCurrent().navigate("issues/" + e.getItem().getId() + "/edit"));

        grid.setItems(issues);
        content.add(grid);
        return content;
    }

    private VerticalLayout createPullRequestsTab() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);

        var prs = dsl.selectFrom(PULL_REQUEST)
                .where(PULL_REQUEST.REPOSITORY_ID.eq(repository.getId()))
                .fetch();

        Grid<PullRequestRecord> grid = new Grid<>();
        grid.addColumn(PullRequestRecord::getTitle).setHeader("Title");
        grid.addColumn(PullRequestRecord::getState).setHeader("State");
        grid.addColumn(PullRequestRecord::getSourceBranch).setHeader("Source");
        grid.addItemClickListener(e -> UI.getCurrent().navigate("pull-requests/" + e.getItem().getId() + "/edit"));

        grid.setItems(prs);
        content.add(grid);
        return content;
    }

    private VerticalLayout createWikiTab() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);

        var wikis = dsl.selectFrom(WIKI_PAGE)
                .where(WIKI_PAGE.REPOSITORY_ID.eq(repository.getId()))
                .fetch();

        Grid<WikiPageRecord> grid = new Grid<>();
        grid.addColumn(WikiPageRecord::getTitle).setHeader("Title");
        grid.addColumn(WikiPageRecord::getCreatedAt).setHeader("Created At");
        // No direct navigation for now as there is no wiki top-level route configured, but could link to repo edit form

        grid.setItems(wikis);
        content.add(grid);
        return content;
    }

    private VerticalLayout createCommitsTab() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);

        var commits = dsl.selectFrom(GIT_COMMIT)
                .where(GIT_COMMIT.REPOSITORY_ID.eq(repository.getId()))
                .orderBy(GIT_COMMIT.CREATED_AT.desc())
                .fetch();

        Grid<GitCommitRecord> grid = new Grid<>();
        grid.addColumn(GitCommitRecord::getHash).setHeader("Hash").setAutoWidth(true);
        grid.addColumn(GitCommitRecord::getMessage).setHeader("Message");
        grid.addColumn(GitCommitRecord::getAuthorName).setHeader("Author");
        grid.addColumn(GitCommitRecord::getCreatedAt).setHeader("Date");

        grid.setItems(commits);
        content.add(grid);
        return content;
    }

    private VerticalLayout createBranchesTab() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);

        var branches = dsl.selectFrom(GIT_BRANCH)
                .where(GIT_BRANCH.REPOSITORY_ID.eq(repository.getId()))
                .fetch();

        Grid<GitBranchRecord> grid = new Grid<>();
        grid.addColumn(GitBranchRecord::getName).setHeader("Name");

        grid.setItems(branches);
        content.add(grid);
        return content;
    }

    private void createSidebarContent() {
        // About section
        VerticalLayout aboutSection = new VerticalLayout();
        aboutSection.setPadding(true);
        aboutSection.setSpacing(true);
        aboutSection.getStyle()
                .set("background-color", "var(--lumo-base-color)")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("border", "1px solid var(--lumo-contrast-10pct)");

        H3 aboutHeader = new H3("About");
        aboutHeader.getStyle().set("margin-top", "0").set("font-size", "var(--lumo-font-size-m)");
        aboutSection.add(aboutHeader);

        // Visibility
        if (repository.getVisibility() != null) {
            HorizontalLayout visibilityRow = createInfoRow(VaadinIcon.EYE,
                getTranslation("route.repositories.labels.visibility"),
                repository.getVisibility().toString());
            aboutSection.add(visibilityRow);
        }

        // Default branch
        if (repository.getDefaultBranch() != null && !repository.getDefaultBranch().isEmpty()) {
            HorizontalLayout branchRow = createInfoRow(VaadinIcon.ROAD_BRANCHES,
                getTranslation("route.repositories.labels.default_branch"),
                repository.getDefaultBranch());
            aboutSection.add(branchRow);
        }

        // Language
        if (repository.getLanguage() != null && !repository.getLanguage().isEmpty()) {
            HorizontalLayout languageRow = createInfoRow(VaadinIcon.CODE,
                getTranslation("route.repositories.labels.language"),
                repository.getLanguage());
            aboutSection.add(languageRow);
        }

        sidebar.add(aboutSection);

        // Topics section
        if (repository.getTopics() != null && !repository.getTopics().isEmpty()) {
            VerticalLayout topicsSection = new VerticalLayout();
            topicsSection.setPadding(true);
            topicsSection.setSpacing(true);
            topicsSection.getStyle()
                    .set("background-color", "var(--lumo-base-color)")
                    .set("border-radius", "var(--lumo-border-radius-m)")
                    .set("border", "1px solid var(--lumo-contrast-10pct)");

            H3 topicsHeader = new H3(getTranslation("route.repositories.labels.topics"));
            topicsHeader.getStyle().set("margin-top", "0").set("font-size", "var(--lumo-font-size-m)");

            HorizontalLayout topicTags = new HorizontalLayout();
            topicTags.setSpacing(true);
            topicTags.getStyle().set("flex-wrap", "wrap");

            String[] topics = repository.getTopics().split(",");
            for (String topic : topics) {
                Span topicTag = new Span(topic.trim());
                topicTag.getStyle()
                        .set("background-color", "var(--lumo-primary-color-10pct)")
                        .set("color", "var(--lumo-primary-text-color)")
                        .set("padding", "var(--lumo-space-xs) var(--lumo-space-s)")
                        .set("border-radius", "var(--lumo-border-radius-m)")
                        .set("font-size", "var(--lumo-font-size-s)");
                topicTags.add(topicTag);
            }

            topicsSection.add(topicsHeader, topicTags);
            sidebar.add(topicsSection);
        }
    }

    private HorizontalLayout createInfoRow(VaadinIcon icon, String label, String value) {
        HorizontalLayout row = new HorizontalLayout();
        row.setSpacing(true);
        row.setAlignItems(FlexComponent.Alignment.CENTER);
        row.setWidthFull();

        Icon iconComponent = new Icon(icon);
        iconComponent.setSize("16px");
        iconComponent.getStyle().set("color", "var(--lumo-secondary-text-color)");

        Span labelSpan = new Span(label + ":");
        labelSpan.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "var(--lumo-font-size-s)");

        Span valueSpan = new Span(value);
        valueSpan.getStyle()
                .set("font-weight", "500")
                .set("font-size", "var(--lumo-font-size-s)");

        row.add(iconComponent, labelSpan, valueSpan);
        return row;
    }
}
