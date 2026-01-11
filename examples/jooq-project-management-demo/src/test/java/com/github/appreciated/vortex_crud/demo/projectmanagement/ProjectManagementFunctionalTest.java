package com.github.appreciated.vortex_crud.demo.projectmanagement;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.ProjectRecord;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.TaskRecord;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.UsersRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProjectManagementFunctionalTest {

    @Autowired
    private ProjectManagementConfiguration configuration;

    @Autowired
    private DSLContext dsl;

    @Test
    void dashboardRouteIsPresent() {
        Application<?, ?, ?> app = configuration.get();
        assertThat(app.routes().get("dashboard")).isNotNull();
        assertThat(app.routes().get("dashboard").defaultRoute()).isTrue();
    }

    @Test
    void notificationIsCreatedOnTaskAssignment() {
        // Setup
        String username = "testuser@" + System.currentTimeMillis() + ".com";
        Integer assigneeId = dsl.insertInto(USERS)
                .set(USERS.USERNAME, username)
                .set(USERS.PASSWORD_HASH, "hash")
                .set(USERS.CREATED_AT, LocalDateTime.now())
                .returningResult(USERS.ID)
                .fetchOne()
                .value1();

        Integer projectId = dsl.insertInto(PROJECT)
                .set(PROJECT.NAME, "Test Project")
                .set(PROJECT.CODE, "TP" + System.currentTimeMillis())
                .set(PROJECT.OWNER_ID, assigneeId)
                .returningResult(PROJECT.ID)
                .fetchOne()
                .value1();

        // Execution: Create Task
        TaskRecord task = dsl.newRecord(TASK);
        task.setTitle("Test Task");
        task.setProjectId(projectId);
        task.setAssigneeId(assigneeId);
        task.setReporterId(assigneeId);
        task.setTaskNumber(1);
        task.setCreatedAt(LocalDateTime.now());

        // Get the DataStore from configuration to ensure hooks are active
        // Note: In a real app we'd use the Service or DataStore instance directly if exposed.
        // Here we rely on the fact that JooqDataStore is instantiated in Configuration,
        // but wait, we need to use THAT instance or one configured identically.
        // ProjectManagementConfiguration.get() returns Application which has routes.
        // But the DataStores are created inside `get()`.
        // We can re-instantiate JooqDataStore with the same logic or use the one from config if possible.
        // However, `get()` creates NEW instances of DataStore every time it is called.
        // This is a flaw in `ProjectManagementConfiguration` if we want to test hooks on the "singleton" store.
        // BUT, `JooqDataStore` just uses `dsl`. Hooks are attached to the `JooqDataStore` instance.
        // If we use `dsl.insert(task).execute()`, hooks are bypassed!
        // We MUST use `JooqDataStore.insertRecord(task)`.

        // Since we can't easily get the *exact* instance used by the UI without refactoring configuration to be a Bean provider,
        // we will manually verify the logic by instantiating the Configuration's `get()` once and using the store from the route config.

        var app = configuration.get();
        var taskRoute = app.routes().get("tasks-kanban");
        // We need to cast to access dataStoreInstance
        var dataStoreConfig = taskRoute.dataStoreConfig();
        @SuppressWarnings("unchecked")
        JooqDataStore<TaskRecord> taskStore = (JooqDataStore<TaskRecord>) dataStoreConfig.dataStoreInstance();

        taskStore.insertRecord(task);

        // Verification
        var notifications = dsl.selectFrom(NOTIFICATION)
                .where(NOTIFICATION.USER_ID.eq(assigneeId))
                .and(NOTIFICATION.TITLE.like("Assigned to Task: Test Task"))
                .fetch();

        assertThat(notifications).isNotEmpty();
    }
}
