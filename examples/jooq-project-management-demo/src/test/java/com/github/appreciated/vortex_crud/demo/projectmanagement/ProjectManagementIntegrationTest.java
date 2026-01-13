package com.github.appreciated.vortex_crud.demo.projectmanagement;

import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.NotificationRecord;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.TaskCommentRecord;
import com.github.appreciated.vortex_crud.core.config.model.KanbanRoute;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.TaskRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.NOTIFICATION;
import static com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.TASK;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProjectManagementIntegrationTest {

    @Autowired
    private ProjectManagementConfiguration configuration;

    @Autowired
    private DSLContext dsl;

    @Test
    void testConfigurationLoads() {
        assertThat(configuration).isNotNull();
        assertThat(configuration.get()).isNotNull();
    }

    @Test
    void testTaskAssignmentNotification() {
        dsl.deleteFrom(TASK).where(TASK.TASK_NUMBER.eq(999)).execute();

        // Create a task
        TaskRecord task = dsl.newRecord(TASK);
        task.setProjectId(1);
        task.setTaskNumber(999);
        task.setTitle("Test Task for Notification");
        task.setReporterId(1);
        task.setAssigneeId(null);
        task.setCreatedAt(LocalDateTime.now());
        task.store();

        Integer taskId = task.getId();

        int initialNotifications = dsl.fetchCount(NOTIFICATION);

        // Get store from route to ensure we are using the one with hooks/wrapper
        var route = configuration.get().routes().get("tasks-kanban");
        var taskStore = (JooqDataStore<TaskRecord>) route.dataStoreInstance();

        TaskRecord recordToUpdate = taskStore.getRecordById(taskId);
        recordToUpdate.setAssigneeId(1);

        taskStore.updateRecord(recordToUpdate);

        int finalNotifications = dsl.fetchCount(NOTIFICATION);
        assertThat(finalNotifications).isEqualTo(initialNotifications + 1);

        NotificationRecord notification = dsl.selectFrom(NOTIFICATION)
            .orderBy(NOTIFICATION.ID.desc())
            .limit(1)
            .fetchOne();

        assertThat(notification.getUserId()).isEqualTo(1);
        assertThat(notification.getTitle()).isEqualTo("Task Assigned");

        taskStore.deleteRecordById(taskId);
        if(notification != null) notification.delete();
    }

    @Test
    void testCommentNotification() {
        dsl.deleteFrom(TASK).where(TASK.TASK_NUMBER.eq(888)).execute();

        // Create a task assigned to user 1
        TaskRecord task = dsl.newRecord(TASK);
        task.setProjectId(1);
        task.setTaskNumber(888);
        task.setTitle("Task for Comment Test");
        task.setReporterId(1);
        task.setAssigneeId(1); // Assigned to User 1
        task.setCreatedAt(LocalDateTime.now());
        task.store();

        // Get Comment DataStore from route
        // Route path for comments? It is a collection in task form.
        // We can't easily get the DataStore instance from the route structure programmatically without deep traversal.
        // But we know `ProjectManagementConfiguration` puts it there.
        // And we know `ProjectManagementConfiguration` uses `CommentNotificationDataStore`.
        // Let's rely on retrieving the store manually if traversing is hard,
        // OR better: Assume the configuration is correct and we just need A DataStore that behaves like it.
        // But we want to test integration.

        // Let's traverse.
        var route = configuration.get().routes().get("tasks-kanban");
        var kanbanRoute = (KanbanRoute) route;
        var taskForm = kanbanRoute.form();
        // find collection
        var commentsCollection = (com.github.appreciated.vortex_crud.core.config.model.Collection) taskForm.fields().stream()
                .filter(f -> f instanceof com.github.appreciated.vortex_crud.core.config.model.Collection)
                .filter(c -> "route.tasks.labels.comments".equals(((com.github.appreciated.vortex_crud.core.config.model.Collection)c).label()))
                .findFirst().orElseThrow();

        var taskCommentStore = (JooqDataStore<TaskCommentRecord>) commentsCollection.dataStoreConfig().dataStoreInstance();

        int initialNotifications = dsl.fetchCount(NOTIFICATION);

        // Add comment by User 2
        TaskCommentRecord comment = dsl.newRecord(com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.TASK_COMMENT);
        comment.setTaskId(task.getId());
        comment.setAuthorId(2);
        comment.setContent("This is a test comment");
        comment.setCreatedAt(LocalDateTime.now());

        taskCommentStore.insertRecord(comment);

        int finalNotifications = dsl.fetchCount(NOTIFICATION);
        assertThat(finalNotifications).isEqualTo(initialNotifications + 1);

        NotificationRecord notification = dsl.selectFrom(NOTIFICATION)
            .orderBy(NOTIFICATION.ID.desc())
            .limit(1)
            .fetchOne();

        assertThat(notification).isNotNull();
        assertThat(notification.getUserId()).isEqualTo(1); // Assignee of the task
        assertThat(notification.getTitle()).isEqualTo("New Comment");
        assertThat(notification.getMessage()).contains("Task for Comment Test");

        // Clean up
        task.delete();
        if(notification != null) notification.delete();
    }

    @Test
    void testPermissionsConfiguration() {
        var app = configuration.get();
        var taskRoute = app.routes().get("tasks-kanban");

        // Viewer should not have write access
        assertThat(taskRoute.writeRoles()).doesNotContain("viewer");
        assertThat(taskRoute.writeRoles()).contains("admin", "manager", "developer");

        // Project configuration check
        var projectRoute = app.routes().get("projects");
        assertThat(projectRoute.writeRoles()).contains("admin", "owner");
        assertThat(projectRoute.writeRoles()).doesNotContain("viewer", "member");
    }
}
