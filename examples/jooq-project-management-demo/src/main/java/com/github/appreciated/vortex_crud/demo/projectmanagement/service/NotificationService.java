package com.github.appreciated.vortex_crud.demo.projectmanagement.service;

import com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.tables.records.NotificationRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.github.appreciated.vortex_crud.demo.projectmanagement.jooq.Tables.NOTIFICATION;

@Service
public class NotificationService {
    private final DSLContext dsl;

    public NotificationService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void createNotification(Integer userId, String title, String message, String link) {
        if (userId == null) {
            // Cannot create notification without a user
            return;
        }

        NotificationRecord record = dsl.newRecord(NOTIFICATION);
        record.setUserId(userId);
        record.setTitle(title);
        record.setMessage(message);
        record.setLink(link);
        record.setIsRead(0); // 0 for unread
        record.setCreatedAt(LocalDateTime.now());
        record.store();
    }
}
