package com.github.appreciated.vortex_crud.demo.devplatform.view;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.TranslationService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

@Component
@UIScope
public class DashboardView extends VerticalLayout {

    public DashboardView(VortexCrudConfigService<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configService, TranslationService translationService) {
        Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> app = configService.configuration();

        add(new H2(translationService.getTranslation("route.dashboard.title", UI.getCurrent().getLocale())));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username != null) {
            // Users store is in "users" route
            RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> usersRoute = app.routes().get("users");
            if (usersRoute != null) {
                VortexCrudDataStore usersStore = usersRoute.dataStoreConfig().dataStoreInstance();

                // Need to cast to TableField because VortexCrudDataStore interface uses generic FieldType
                // but JooqDataStore implementation uses TableField.
                // However, usersStore is typed as VortexCrudDataStore<TableField<?, ?>, ...> inside the config?
                // No, config is typed.
                // The issue is USERS.USERNAME is TableField<Rec, String>.
                // getRecordsFromTableWhereColumnEquals expects FieldType.
                // FieldType is TableField<?, ?>.
                // So (TableField) cast might be needed or generic wildcard match.

                List<TableRecord<?>> users = usersStore.getRecordsFromTableWhereColumnEquals((TableField)USERS.USERNAME, username, 0, 1);

                if (!users.isEmpty()) {
                     TableRecord<?> user = users.get(0);
                     Integer userId = (Integer) user.get(USERS.ID);

                     // Assigned Issues
                     add(new H3(translationService.getTranslation("dashboard.assigned-to-me", UI.getCurrent().getLocale())));
                     RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> issuesRoute = app.routes().get("issues");
                     if (issuesRoute != null) {
                         VortexCrudDataStore issueStore = issuesRoute.dataStoreConfig().dataStoreInstance();
                         List<TableRecord<?>> assignedIssues = issueStore.getRecordsFromTableWhereColumnEquals((TableField)ISSUE.ASSIGNEE_ID, userId, 0, 10);

                         Grid<TableRecord<?>> issueGrid = new Grid<>();
                         issueGrid.addColumn(rec -> rec.get(ISSUE.TITLE)).setHeader("Title");
                         issueGrid.addColumn(rec -> rec.get(ISSUE.STATE)).setHeader("State");
                         issueGrid.setItems(assignedIssues);
                         add(issueGrid);
                     }

                     // My Pull Requests
                     add(new H3(translationService.getTranslation("dashboard.my-pull-requests", UI.getCurrent().getLocale())));
                     RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> prRoute = app.routes().get("pull-requests");
                     if (prRoute != null) {
                         VortexCrudDataStore prStore = prRoute.dataStoreConfig().dataStoreInstance();
                         List<TableRecord<?>> myPrs = prStore.getRecordsFromTableWhereColumnEquals((TableField)PULL_REQUEST.AUTHOR_ID, userId, 0, 10);

                         Grid<TableRecord<?>> prGrid = new Grid<>();
                         prGrid.addColumn(rec -> rec.get(PULL_REQUEST.TITLE)).setHeader("Title");
                         prGrid.addColumn(rec -> rec.get(PULL_REQUEST.STATE)).setHeader("State");
                         prGrid.setItems(myPrs);
                         add(prGrid);
                     }
                }
            }
        }
    }
}
