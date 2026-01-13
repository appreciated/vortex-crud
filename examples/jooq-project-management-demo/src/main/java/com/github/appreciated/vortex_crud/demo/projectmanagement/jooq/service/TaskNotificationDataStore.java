package com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.demo.projectmanagement.service.NotificationService;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.TaskRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;

public class TaskNotificationDataStore extends JooqDataStore<TaskRecord> {

    private final NotificationService notificationService;
    private final DSLContext dslContext;

    public TaskNotificationDataStore(Class<TaskRecord> record, DSLContext dslContext, NotificationService notificationService) {
        super(record, dslContext);
        this.dslContext = dslContext;
        this.notificationService = notificationService;
    }

    @Override
    public void updateRecordById(TaskRecord entity) {
        // Fetch old record to compare assignee
        TaskRecord oldRecord = getRecordById(entity.getId());
        super.updateRecordById(entity);

        if (oldRecord != null) {
            Integer oldAssignee = oldRecord.getAssigneeId();
            Integer newAssignee = entity.getAssigneeId();

            if (newAssignee != null && !newAssignee.equals(oldAssignee)) {
                // Task assigned to new user
                notificationService.createNotification(
                    newAssignee,
                    "Task Assigned",
                    "You have been assigned to task: " + entity.getTitle(),
                    "tasks-kanban" // Assuming this is the link or route
                );
            }
        }
    }
}
