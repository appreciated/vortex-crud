package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqNotificationPanelConfiguration;
import lombok.Builder;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.UpdatableRecord;
import org.jooq.impl.TableImpl;

import java.util.function.Supplier;

@Builder
public class JooqNotificationService<R extends UpdatableRecord<R>, T> {

    private final DataStoreConfig<? extends TableRecord<?>, ? extends TableField<?, ?>, ? extends TableImpl<?>> dataStoreConfig;
    private final TableField<R, String> messageField;
    private final TableField<R, T> timestampField;
    private final TableField<R, ?> readStatusField;

    private final TableField<R, ?> userIdField;
    private final TableField<R, String> titleField;
    private final TableField<R, String> linkField;

    @Builder.Default
    private final Object readStatusValueForRead = 1;
    @Builder.Default
    private final Object readStatusValueForUnread = 0;

    @Builder.Default
    private final Supplier<T> timestampSupplier = () -> null;

    @SuppressWarnings("unchecked")
    public void notify(Object userId, String title, String message, String link) {
        JooqDataStore<R> store = (JooqDataStore<R>) dataStoreConfig.dataStoreInstance();
        R record = store.newInstance();

        if (messageField != null) record.set(messageField, message);
        if (titleField != null) record.set(titleField, title);
        if (linkField != null) record.set(linkField, link);
        if (userIdField != null) {
             record.set((TableField) userIdField, userId);
        }

        if (readStatusField != null) {
            record.set((TableField) readStatusField, readStatusValueForUnread);
        }

        if (timestampField != null && timestampSupplier != null) {
             record.set(timestampField, timestampSupplier.get());
        }

        store.insertRecord(record);
    }

    public void notify(Object userId, String message) {
        notify(userId, null, message, null);
    }

    public void notify(Object userId, String title, String message) {
        notify(userId, title, message, null);
    }

    @SuppressWarnings("unchecked")
    public JooqNotificationPanelConfiguration.NotificationPanelConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configurePanel() {
        return JooqNotificationPanelConfiguration.builder()
                .dataStoreConfig((DataStoreConfig) dataStoreConfig)
                .messageField(messageField)
                .timestampField(timestampField)
                .readStatusField(readStatusField)
                .readStatusValueForRead(readStatusValueForRead)
                .readStatusValueForUnread(readStatusValueForUnread)
                .filterField((TableField) userIdField);
    }
}
