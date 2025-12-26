package com.github.appreciated.vortex_crud.demo.devplatform.view;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.PostConstruct;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

public class DashboardView extends VerticalLayout {

    private final VortexCrudConfigService<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configService;

    public DashboardView(VortexCrudConfigService<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configService) {
        this.configService = configService;
        setPadding(true);
        setSpacing(true);
    }

    @PostConstruct
    public void init() {
        Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> application = configService.configuration();

        // Fetch user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null || "anonymousUser".equals(username)) {
            // Handle anonymous
            add(new H2("Welcome Guest"));
            return;
        }

        VortexCrudDataStore<TableField<?, ?>, TableRecord<?>> userStore = (VortexCrudDataStore) application.identityAndAccessManagement().dataStoreConfig().dataStoreInstance();
        List<TableRecord<?>> users = userStore.getRecordsFromTableWhereColumnEquals(USERS.USERNAME, username, 0, 1);

        if (users.isEmpty()) {
             add(new H2("User not found: " + username));
             return;
        }
        TableRecord<?> user = users.get(0);
        Object userId = user.get(USERS.ID);

        add(new H2("Dashboard: " + username));

        // Assigned Issues
        var issueRoute = application.routes().get("issues");
        if (issueRoute != null) {
            VortexCrudDataStore<TableField<?, ?>, TableRecord<?>> issueStore = (VortexCrudDataStore) issueRoute.dataStoreConfig().dataStoreInstance();
            List<TableRecord<?>> assignedIssues = issueStore.getRecordsFromTableWhereColumnEquals(ISSUE.ASSIGNEE_ID, userId, 0, 50);

            add(new H3("Assigned Issues"));
            Grid<TableRecord<?>> issueGrid = new Grid<>();
            issueGrid.addColumn(rec -> rec.get(ISSUE.TITLE)).setHeader("Title");
            issueGrid.addColumn(rec -> rec.get(ISSUE.STATE)).setHeader("State");
            issueGrid.setItems(assignedIssues);
            add(issueGrid);
        }

        // My Pull Requests
        var prRoute = application.routes().get("pull-requests");
        if (prRoute != null) {
            VortexCrudDataStore<TableField<?, ?>, TableRecord<?>> prStore = (VortexCrudDataStore) prRoute.dataStoreConfig().dataStoreInstance();
            List<TableRecord<?>> myPrs = prStore.getRecordsFromTableWhereColumnEquals(PULL_REQUEST.AUTHOR_ID, userId, 0, 50);

            add(new H3("My Pull Requests"));
            Grid<TableRecord<?>> prGrid = new Grid<>();
            prGrid.addColumn(rec -> rec.get(PULL_REQUEST.TITLE)).setHeader("Title");
            prGrid.addColumn(rec -> rec.get(PULL_REQUEST.STATE)).setHeader("State");
            prGrid.setItems(myPrs);
            add(prGrid);
        }
    }
}
