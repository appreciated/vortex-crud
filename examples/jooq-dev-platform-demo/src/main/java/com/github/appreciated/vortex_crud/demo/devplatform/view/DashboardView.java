package com.github.appreciated.vortex_crud.demo.devplatform.view;

import com.github.appreciated.vortex_crud.core.service.TranslationService;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.IssueRecord;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.PullRequestRecord;
import com.github.appreciated.vortex_crud.demo.devplatform.jooq.tables.records.UsersRecord;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.jooq.DSLContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.github.appreciated.vortex_crud.demo.devplatform.jooq.Tables.*;

@Component
@UIScope
public class DashboardView extends VerticalLayout {

    public DashboardView(DSLContext dsl, TranslationService translationService) {
        add(new H2(translationService.getTranslation("route.dashboard.title", UI.getCurrent().getLocale())));

        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        if (username != null) {
            UsersRecord user = dsl.selectFrom(USERS).where(USERS.USERNAME.eq(username)).fetchOne();

            if (user != null) {
                Integer userId = user.getId();

                // Assigned Issues
                add(new H3(translationService.getTranslation("dashboard.assigned-to-me", UI.getCurrent().getLocale())));
                List<IssueRecord> assignedIssues = dsl.selectFrom(ISSUE)
                        .where(ISSUE.ASSIGNEE_ID.eq(userId))
                        .limit(10)
                        .fetch();

                Grid<IssueRecord> issueGrid = new Grid<>();
                issueGrid.addColumn(IssueRecord::getTitle).setHeader("Title");
                issueGrid.addColumn(IssueRecord::getState).setHeader("State");
                issueGrid.setItems(assignedIssues);
                add(issueGrid);

                // My Pull Requests
                add(new H3(translationService.getTranslation("dashboard.my-pull-requests", UI.getCurrent().getLocale())));
                List<PullRequestRecord> myPrs = dsl.selectFrom(PULL_REQUEST)
                        .where(PULL_REQUEST.AUTHOR_ID.eq(userId))
                        .limit(10)
                        .fetch();

                Grid<PullRequestRecord> prGrid = new Grid<>();
                prGrid.addColumn(PullRequestRecord::getTitle).setHeader("Title");
                prGrid.addColumn(PullRequestRecord::getState).setHeader("State");
                prGrid.setItems(myPrs);
                add(prGrid);
            }
        }
    }
}
