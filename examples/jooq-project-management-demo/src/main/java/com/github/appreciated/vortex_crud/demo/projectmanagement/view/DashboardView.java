package com.github.appreciated.vortex_crud.demo.projectmanagement.view;

import com.github.appreciated.vortex_crud.core.service.TranslationService;
import com.github.appreciated.vortex_crud.demo.projectmanagement.enums.TaskStatus;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.NotificationRecord;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.TaskRecord;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.UsersRecord;
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

import static com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.*;

@Component
@UIScope
public class DashboardView extends VerticalLayout {

    public DashboardView(DSLContext dsl, TranslationService translationService) {
        add(new H2(translationService.getTranslation("route.dashboard.title", UI.getCurrent().getLocale())));

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : null;

        if (username != null) {
            UsersRecord user = dsl.selectFrom(USERS).where(USERS.USERNAME.eq(username)).fetchOne();

            if (user != null) {
                Integer userId = user.getId();

                // Assigned Tasks
                add(new H3(translationService.getTranslation("dashboard.assigned-tasks", UI.getCurrent().getLocale())));
                List<TaskRecord> assignedTasks = dsl.selectFrom(TASK)
                        .where(TASK.ASSIGNEE_ID.eq(userId))
                        .and(TASK.STATUS.ne(TaskStatus.DONE))
                        .limit(10)
                        .fetch();

                Grid<TaskRecord> taskGrid = new Grid<>();
                taskGrid.addColumn(TaskRecord::getTitle).setHeader(translationService.getTranslation("route.tasks.labels.title", UI.getCurrent().getLocale()));
                taskGrid.addColumn(TaskRecord::getStatus).setHeader(translationService.getTranslation("route.tasks.labels.status", UI.getCurrent().getLocale()));
                taskGrid.addColumn(TaskRecord::getPriority).setHeader(translationService.getTranslation("route.tasks.labels.priority", UI.getCurrent().getLocale()));
                taskGrid.setItems(assignedTasks);
                taskGrid.addItemClickListener(event -> UI.getCurrent().navigate("tasks-kanban/" + event.getItem().getId() + "/edit"));
                add(taskGrid);

                // Recent Notifications
                add(new H3(translationService.getTranslation("dashboard.recent-notifications", UI.getCurrent().getLocale())));
                List<NotificationRecord> notifications = dsl.selectFrom(NOTIFICATION)
                        .where(NOTIFICATION.USER_ID.eq(userId))
                        .orderBy(NOTIFICATION.CREATED_AT.desc())
                        .limit(5)
                        .fetch();

                Grid<NotificationRecord> notificationGrid = new Grid<>();
                notificationGrid.addColumn(NotificationRecord::getTitle).setHeader(translationService.getTranslation("route.notifications.labels.title", UI.getCurrent().getLocale()));
                notificationGrid.addColumn(NotificationRecord::getMessage).setHeader(translationService.getTranslation("route.notifications.labels.message", UI.getCurrent().getLocale()));
                notificationGrid.addColumn(NotificationRecord::getCreatedAt).setHeader(translationService.getTranslation("route.notifications.labels.created_at", UI.getCurrent().getLocale()));
                notificationGrid.setItems(notifications);
                add(notificationGrid);
            }
        }
    }
}
