package com.github.appreciated.vortex_crud.demo.devplatform.view;

import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.RepositoryRecord;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.IssueRecord;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.PullRequestRecord;
import com.github.appreciated.vortex_crud.demo.devplatform.service.GitService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import org.jooq.DSLContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Objects;
import java.util.List;

import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

public class RepositoryDetailView extends VerticalLayout {

    private final RepositoryRecord repository;
    private final DSLContext dsl;
    private final GitService gitService;
    private Span starCount;
    private Button starButton;
    private String currentRef = "HEAD";
    private boolean showBlame = false;

    public RepositoryDetailView(RepositoryRecord repository, DSLContext dsl, GitService gitService) {
        this.repository = repository;
        this.dsl = dsl;
        this.gitService = gitService;

        if (repository.getDefaultBranch() != null && !repository.getDefaultBranch().isEmpty()) {
            this.currentRef = repository.getDefaultBranch();
        }

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle()
                .set("background-color", "var(--lumo-contrast-5pct)");

        buildView();
    }

    private void buildView() {
        // Header section
        VerticalLayout header = createHeader();
        add(header);

        // Main content area with sidebar
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);
        mainContent.setSpacing(true);
        mainContent.getStyle().set("gap", "var(--lumo-space-l)");

        // Main content (left side)
        VerticalLayout contentArea = createContentArea();
        contentArea.setWidth("70%");

        // Sidebar (right side)
        VerticalLayout sidebar = createSidebar();
        sidebar.setWidth("30%");

        mainContent.add(contentArea, sidebar);
        add(mainContent);
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

        starButton = new Button("Star", new Icon(VaadinIcon.STAR));
        starButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        updateStarButtonState();
        starButton.addClickListener(e -> toggleStar());

        Button editButton = new Button(getTranslation("button.edit"), new Icon(VaadinIcon.EDIT));
        editButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        editButton.addClickListener(e -> UI.getCurrent().navigate("repositories/" + repository.getId() + "/edit"));

        actions.add(starButton, editButton);
        statsAndActions.add(stats, actions);
        header.add(statsAndActions);

        return header;
    }

    private void updateStarButtonState() {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        var users = dsl.selectFrom(USERS).where(USERS.USERNAME.eq(username)).fetch();
        if (!users.isEmpty()) {
            var user = users.get(0);
            var existingStar = dsl.selectFrom(REPOSITORY_STAR)
                    .where(REPOSITORY_STAR.USER_ID.eq(user.getId()))
                    .and(REPOSITORY_STAR.REPOSITORY_ID.eq(repository.getId()))
                    .fetchOne();

            if (existingStar != null) {
                starButton.setText("Unstar");
                starButton.setIcon(new Icon(VaadinIcon.STAR));
                starButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            } else {
                starButton.setText("Star");
                starButton.setIcon(new Icon(VaadinIcon.STAR_O));
                starButton.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            }
        }
    }

    private void toggleStar() {
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        var users = dsl.selectFrom(USERS).where(USERS.USERNAME.eq(username)).fetch();
        if (!users.isEmpty()) {
            var user = users.get(0);
            Integer userId = user.getId();
            Integer repoId = repository.getId();

            var existingStar = dsl.selectFrom(REPOSITORY_STAR)
                    .where(REPOSITORY_STAR.USER_ID.eq(userId))
                    .and(REPOSITORY_STAR.REPOSITORY_ID.eq(repoId))
                    .fetchOne();

            int count = repository.getStarCount() != null ? repository.getStarCount() : 0;

            if (existingStar != null) {
                existingStar.delete();
                count = Math.max(0, count - 1);
            } else {
                var newStar = dsl.newRecord(REPOSITORY_STAR);
                newStar.set(REPOSITORY_STAR.USER_ID, userId);
                newStar.set(REPOSITORY_STAR.REPOSITORY_ID, repoId);
                newStar.store();
                count++;
            }

            repository.setStarCount(count);
            // We should ideally update the database record too, though it might be handled by the route action logic in real app
            dsl.update(REPOSITORY).set(REPOSITORY.STAR_COUNT, count).where(REPOSITORY.ID.eq(repoId)).execute();

            // Update UI
            if (starCount != null) {
                starCount.setText(String.valueOf(count));
            }
            updateStarButtonState();
        }
    }

    private HorizontalLayout createStatsLayout() {
        HorizontalLayout stats = new HorizontalLayout();
        stats.setSpacing(true);
        stats.setAlignItems(FlexComponent.Alignment.CENTER);

        // Stars
        Span starIcon = new Span(new Icon(VaadinIcon.STAR));
        starIcon.getStyle().set("color", "var(--lumo-secondary-text-color)");
        starCount = new Span(String.valueOf(repository.getStarCount() != null ? repository.getStarCount() : 0));
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

    private VerticalLayout createContentArea() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.getStyle()
                .set("background-color", "var(--lumo-base-color)")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("border", "1px solid var(--lumo-contrast-10pct)");

        Tab overviewTab = new Tab("Overview");
        Tab codeTab = new Tab("Code");
        Tab commitsTab = new Tab("Commits");
        Tab issuesTab = new Tab("Issues");
        Tab prsTab = new Tab("Pull Requests");
        Tab wikiTab = new Tab("Wiki");

        Tabs tabs = new Tabs(overviewTab, codeTab, commitsTab, issuesTab, prsTab, wikiTab);

        VerticalLayout tabContent = new VerticalLayout();
        tabContent.setPadding(false);
        tabContent.setSpacing(true);

        // Initial content
        showOverview(tabContent);

        tabs.addSelectedChangeListener(event -> {
            tabContent.removeAll();
            Tab selectedTab = event.getSelectedTab();
            if (selectedTab.equals(overviewTab)) {
                showOverview(tabContent);
            } else if (selectedTab.equals(codeTab)) {
                showCode(tabContent, "");
            } else if (selectedTab.equals(commitsTab)) {
                showCommits(tabContent);
            } else if (selectedTab.equals(issuesTab)) {
                showIssues(tabContent);
            } else if (selectedTab.equals(prsTab)) {
                showPullRequests(tabContent);
            } else if (selectedTab.equals(wikiTab)) {
                showWiki(tabContent);
            }
        });

        content.add(tabs, tabContent);
        return content;
    }

    private void showCode(VerticalLayout container, String path) {
        // Branch Controls
        HorizontalLayout controls = new HorizontalLayout();
        controls.setAlignItems(FlexComponent.Alignment.CENTER);
        controls.setSpacing(true);

        ComboBox<String> branchSelect = new ComboBox<>();
        branchSelect.setPlaceholder("Branch");
        branchSelect.setItems(gitService.listBranches(repository.getSlug()));
        branchSelect.setValue(currentRef);
        branchSelect.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                currentRef = e.getValue();
                container.removeAll();
                showCode(container, "");
            }
        });

        Button newBranchBtn = new Button("New Branch", new Icon(VaadinIcon.PLUS), e -> openNewBranchDialog(branchSelect));
        newBranchBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);

        Button deleteBranchBtn = new Button("Delete Branch", new Icon(VaadinIcon.TRASH), e -> openDeleteBranchDialog(branchSelect));
        deleteBranchBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

        controls.add(branchSelect, newBranchBtn, deleteBranchBtn);
        container.add(controls);

        refreshCode(container, path);
    }

    private void refreshCode(VerticalLayout container, String path) {
         if (path != null && !path.isEmpty()) {
            Button backButton = new Button("..", e -> {
                String parentPath = new File(path).getParent();
                container.removeAll();
                showCode(container, parentPath == null ? "" : parentPath);
            });
            backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            container.add(backButton);
        }

        List<GitService.FileEntry> files = gitService.listFiles(repository.getSlug(), path, currentRef);
        if (files.isEmpty()) {
            container.add(new Paragraph("No files found."));
            return;
        }

        Grid<GitService.FileEntry> grid = new Grid<>();
        grid.addComponentColumn(entry -> {
            Icon icon = new Icon(entry.isDirectory() ? VaadinIcon.FOLDER : VaadinIcon.FILE_CODE);
            icon.setSize("16px");
            icon.getStyle().set("color", entry.isDirectory() ? "var(--lumo-primary-text-color)" : "var(--lumo-secondary-text-color)");
            Span name = new Span(entry.name());
            HorizontalLayout row = new HorizontalLayout(icon, name);
            row.setAlignItems(FlexComponent.Alignment.CENTER);
            row.setSpacing(true);
            return row;
        }).setHeader("Name");

        grid.addItemClickListener(e -> {
            GitService.FileEntry entry = e.getItem();
            if (entry.isDirectory()) {
                container.removeAll();
                showCode(container, entry.path());
            } else {
                showFileContent(container, entry.path());
            }
        });

        grid.setItems(files);
        container.add(grid);
    }

    private void openNewBranchDialog(ComboBox<String> branchSelect) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("New Branch");

        TextField branchNameField = new TextField("Branch Name");
        Button createBtn = new Button("Create", e -> {
            try {
                gitService.createBranch(repository.getSlug(), branchNameField.getValue(), currentRef);
                branchSelect.setItems(gitService.listBranches(repository.getSlug()));
                branchSelect.setValue(branchNameField.getValue());
                Notification.show("Branch created");
                dialog.close();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });
        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.add(new VerticalLayout(branchNameField, new HorizontalLayout(createBtn, cancelBtn)));
        dialog.open();
    }

    private void openDeleteBranchDialog(ComboBox<String> branchSelect) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Delete Branch");
        dialog.add(new Text("Are you sure you want to delete branch " + currentRef + "?"));

        Button confirmBtn = new Button("Delete", e -> {
             try {
                gitService.deleteBranch(repository.getSlug(), currentRef);
                branchSelect.setItems(gitService.listBranches(repository.getSlug()));
                branchSelect.setValue("HEAD");
                Notification.show("Branch deleted");
                dialog.close();
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });
        confirmBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.getFooter().add(confirmBtn, cancelBtn);
        dialog.open();
    }

    private void showFileContent(VerticalLayout container, String path) {
        container.removeAll();
        Button backButton = new Button("Back to browsing", e -> {
            container.removeAll();
            String parentPath = new File(path).getParent();
            showCode(container, parentPath == null ? "" : parentPath);
        });
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Toolbar for file
        HorizontalLayout toolbar = new HorizontalLayout(backButton);
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        HorizontalLayout actions = new HorizontalLayout();

        Button rawBtn = new Button("Raw", new Icon(VaadinIcon.DOWNLOAD_ALT));
        rawBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        // Create a StreamResource
        String filename = new File(path).getName();
        StreamResource resource = new StreamResource(filename, () -> {
            byte[] content = gitService.getRawFileContent(repository.getSlug(), path, currentRef);
            return new ByteArrayInputStream(content != null ? content : new byte[0]);
        });
        Anchor downloadLink = new Anchor(resource, "");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(rawBtn);

        Button blameBtn = new Button("Blame", new Icon(VaadinIcon.EYE));
        blameBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, showBlame ? ButtonVariant.LUMO_PRIMARY : ButtonVariant.LUMO_TERTIARY);
        blameBtn.addClickListener(e -> {
            showBlame = !showBlame;
            showFileContent(container, path); // Refresh view
        });

        actions.add(downloadLink, blameBtn);
        toolbar.add(actions);
        container.add(toolbar);

        if (showBlame) {
            renderBlameView(container, path);
        } else {
            String content = gitService.getFileContent(repository.getSlug(), path, currentRef);
            Pre codeBlock = new Pre();
            codeBlock.setText(content);
            codeBlock.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
            codeBlock.getStyle().set("padding", "var(--lumo-space-m)");
            codeBlock.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
            codeBlock.getStyle().set("overflow", "auto");
            codeBlock.setWidthFull();
            container.add(codeBlock);
        }
    }

    private void renderBlameView(VerticalLayout container, String path) {
        List<GitService.BlameInfo> blameInfos = gitService.blame(repository.getSlug(), currentRef, path);
        String content = gitService.getFileContent(repository.getSlug(), path, currentRef);
        String[] lines = content.split("\n");

        Grid<BlameLine> grid = new Grid<>();
        grid.setWidthFull();
        grid.addColumn(BlameLine::commitShort).setHeader("Commit").setWidth("100px").setFlexGrow(0);
        grid.addColumn(BlameLine::author).setHeader("Author").setWidth("150px").setFlexGrow(0);
        grid.addColumn(BlameLine::date).setHeader("Date").setWidth("150px").setFlexGrow(0);
        grid.addColumn(BlameLine::content).setHeader("Content").setAutoWidth(true);

        List<BlameLine> blameLines = new java.util.ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            GitService.BlameInfo info = (i < blameInfos.size()) ? blameInfos.get(i) : null;
            blameLines.add(new BlameLine(
                info != null ? info.commitId().substring(0, 7) : "",
                info != null ? info.author() : "",
                info != null ? info.date().toLocalDate().toString() : "",
                lines[i]
            ));
        }
        grid.setItems(blameLines);
        container.add(grid);
    }

    record BlameLine(String commitShort, String author, String date, String content) {}

    private void showCommits(VerticalLayout container) {
        List<GitService.CommitInfo> commits = gitService.getCommitHistory(repository.getSlug(), currentRef);

        Grid<GitService.CommitInfo> grid = new Grid<>();
        grid.addColumn(c -> c.id().substring(0, 7)).setHeader("SHA").setWidth("100px").setFlexGrow(0);
        grid.addColumn(GitService.CommitInfo::shortMessage).setHeader("Message").setAutoWidth(true);
        grid.addColumn(GitService.CommitInfo::author).setHeader("Author").setWidth("150px").setFlexGrow(0);
        grid.addColumn(c -> c.date().toString()).setHeader("Date").setWidth("200px").setFlexGrow(0);

        grid.addItemClickListener(e -> openCommitDialog(e.getItem()));

        grid.setItems(commits);
        container.add(grid);
    }

    private void openCommitDialog(GitService.CommitInfo commit) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Commit Details: " + commit.id().substring(0, 7));
        dialog.setWidth("80vw");
        dialog.setHeight("80vh");

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();

        content.add(new H3(commit.shortMessage()));
        content.add(new Paragraph(commit.fullMessage()));

        // Show Diff
        String diff = gitService.getDiff(repository.getSlug(), null, commit.id());
        // Note: passing null as oldRef relies on getDiff logic to find parent

        Pre diffBlock = new Pre();
        diffBlock.setText(diff);
        diffBlock.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
        diffBlock.getStyle().set("padding", "var(--lumo-space-m)");
        diffBlock.getStyle().set("overflow", "auto");
        diffBlock.setSizeFull();

        content.add(diffBlock);
        dialog.add(content);

        Button closeBtn = new Button("Close", e -> dialog.close());
        dialog.getFooter().add(closeBtn);
        dialog.open();
    }

    private void showOverview(VerticalLayout container) {
        // README section
        if (repository.getReadmeContent() != null && !repository.getReadmeContent().isEmpty()) {
            H2 readmeHeader = new H2("README.md");
            readmeHeader.getStyle()
                    .set("margin-top", "var(--lumo-space-m)")
                    .set("padding-bottom", "var(--lumo-space-m)")
                    .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

            Div readmeContent = new Div();
            readmeContent.getStyle()
                    .set("padding", "var(--lumo-space-m)")
                    .set("white-space", "pre-wrap")
                    .set("font-family", "var(--lumo-font-family)")
                    .set("line-height", "1.6");
            readmeContent.setText(repository.getReadmeContent());

            container.add(readmeHeader, readmeContent);
        } else {
            Paragraph noReadme = new Paragraph("No README available");
            noReadme.getStyle().set("color", "var(--lumo-secondary-text-color)");
            noReadme.getStyle().set("padding", "var(--lumo-space-m)");
            container.add(noReadme);
        }
    }

    private void showIssues(VerticalLayout container) {
        List<IssueRecord> issues = dsl.selectFrom(ISSUE)
                .where(ISSUE.REPOSITORY_ID.eq(repository.getId()))
                .fetch();

        if (issues.isEmpty()) {
             container.add(new Paragraph("No issues found."));
             return;
        }

        Grid<IssueRecord> grid = new Grid<>();
        grid.addColumn(IssueRecord::getTitle).setHeader("Title");
        grid.addColumn(IssueRecord::getState).setHeader("State");
        grid.addColumn(IssueRecord::getPriority).setHeader("Priority");
        grid.setItems(issues);
        grid.addItemClickListener(e -> UI.getCurrent().navigate("repositories/" + repository.getId() + "/issues/" + e.getItem().getId() + "/edit"));

        // Add link to full issues view
        Button viewAllIssues = new Button("View All Issues", new Icon(VaadinIcon.ARROW_RIGHT));
        viewAllIssues.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        viewAllIssues.addClickListener(e -> UI.getCurrent().navigate("repositories/" + repository.getId() + "/issues"));

        container.add(grid, viewAllIssues);
    }

    private void showPullRequests(VerticalLayout container) {
        List<PullRequestRecord> prs = dsl.selectFrom(PULL_REQUEST)
                .where(PULL_REQUEST.REPOSITORY_ID.eq(repository.getId()))
                .fetch();

        if (prs.isEmpty()) {
             container.add(new Paragraph("No pull requests found."));
             return;
        }

        Grid<PullRequestRecord> grid = new Grid<>();
        grid.addColumn(PullRequestRecord::getTitle).setHeader("Title");
        grid.addColumn(PullRequestRecord::getState).setHeader("State");
        grid.addColumn(PullRequestRecord::getSourceBranch).setHeader("Source");
        grid.addColumn(PullRequestRecord::getTargetBranch).setHeader("Target");
        grid.setItems(prs);
        grid.addItemClickListener(e -> UI.getCurrent().navigate("repositories/" + repository.getId() + "/pull-requests/" + e.getItem().getId() + "/edit"));

        // Add link to full pull requests view
        Button viewAllPRs = new Button("View All Pull Requests", new Icon(VaadinIcon.ARROW_RIGHT));
        viewAllPRs.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        viewAllPRs.addClickListener(e -> UI.getCurrent().navigate("repositories/" + repository.getId() + "/pull-requests"));

        container.add(grid, viewAllPRs);
    }

    private void showWiki(VerticalLayout container) {
         var wikiPages = dsl.selectFrom(WIKI_PAGE)
                .where(WIKI_PAGE.REPOSITORY_ID.eq(repository.getId()))
                .fetch();

        if (wikiPages.isEmpty()) {
             container.add(new Paragraph("No wiki pages found."));
             return;
        }

        // Simple list for now
        wikiPages.forEach(page -> {
            VerticalLayout pageLayout = new VerticalLayout();
            pageLayout.add(new H2(page.getTitle()));
            Div content = new Div();
            content.setText(page.getContent());
            pageLayout.add(content);
            container.add(pageLayout);
        });
    }

    private VerticalLayout createSidebar() {
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setPadding(false);
        sidebar.setSpacing(true);

        // About section
        VerticalLayout aboutSection = new VerticalLayout();
        aboutSection.setPadding(true);
        aboutSection.setSpacing(true);
        aboutSection.getStyle()
                .set("background-color", "var(--lumo-base-color)")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("border", "1px solid var(--lumo-contrast-10pct)");

        H2 aboutHeader = new H2("About");
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

        // Clone URL
        String baseUrl = com.vaadin.flow.server.VaadinServlet.getCurrent().getServletContext().getContextPath();
        String host = com.vaadin.flow.server.VaadinRequest.getCurrent().getHeader("host");
        String scheme = "http"; // Default to http if cannot determine
        if (com.vaadin.flow.server.VaadinRequest.getCurrent() instanceof com.vaadin.flow.server.VaadinServletRequest) {
             scheme = ((com.vaadin.flow.server.VaadinServletRequest) com.vaadin.flow.server.VaadinRequest.getCurrent()).getScheme();
        }
        String cloneUrl = scheme + "://" + host + baseUrl + "/git/" + repository.getSlug();

        TextField cloneUrlField = new TextField("Clone URL");
        cloneUrlField.setValue(cloneUrl);
        cloneUrlField.setReadOnly(true);
        cloneUrlField.setWidthFull();
        cloneUrlField.addThemeName("small");
        cloneUrlField.setSuffixComponent(new Button(new Icon(VaadinIcon.COPY), e -> {
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", cloneUrl);
            Notification.show("URL copied to clipboard");
        }));

        aboutSection.add(cloneUrlField);

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

            H2 topicsHeader = new H2(getTranslation("route.repositories.labels.topics"));
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

        return sidebar;
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
