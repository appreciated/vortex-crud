package com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.service;

import com.github.appreciated.vortex_crud.demo.projectmanagement.service.NotificationService;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.TaskCommentRecord;
import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.TaskRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;

import static com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.TASK;

public class CommentNotificationDataStore extends JooqDataStore<TaskCommentRecord> {

    private final NotificationService notificationService;
    private final DSLContext dslContext;

    public CommentNotificationDataStore(Class<TaskCommentRecord> record, DSLContext dslContext, NotificationService notificationService) {
        super(record, dslContext);
        this.dslContext = dslContext;
        this.notificationService = notificationService;
    }

    @Override
    public Object insertRecord(TaskCommentRecord entity) {
        Object id = super.insertRecord(entity);

        // Notify task assignee
        if (entity.getTaskId() != null) {
            TaskRecord task = dslContext.selectFrom(TASK)
                    .where(TASK.ID.eq(entity.getTaskId()))
                    .fetchOne();

            if (task != null && task.getAssigneeId() != null) {
                // Don't notify if the author is the assignee
                if (entity.getAuthorId() != null && entity.getAuthorId().equals(task.getAssigneeId())) {
                    return id;
                }

                notificationService.createNotification(
                        task.getAssigneeId(),
                        "New Comment",
                        "New comment on task: " + task.getTitle(),
                        "tasks-kanban"
                );
            }
        }

        return id;
    }
}
