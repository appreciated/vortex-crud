package com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.Collections;

public class JooqNotificationPanelConfiguration extends NotificationPanelConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public static NotificationPanelConfiguration.NotificationPanelConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> builder() {
        return NotificationPanelConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                .readStatusValueForRead(1)
                .readStatusValueForUnread(0);
    }

    public static NotificationPanelConfiguration.NotificationPanelConfigurationBuilder<TableRecord<?>, TableField<?, ?>, TableImpl<?>> of(TableImpl<?> table, DSLContext dsl) {
        // Create DataStore
        // Use raw type to bypass generic inference issues with wildcard types
        @SuppressWarnings({"unchecked", "rawtypes"})
        JooqDataStore<?> store = new JooqDataStore(table.getRecordType(), dsl);

        // Create DataStoreConfig
        @SuppressWarnings("unchecked")
        DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> dsConfig =
                (DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>) (DataStoreConfig<?, ?, ?>) DataStoreConfig.builder()
                .factory(table)
                .dataStoreInstance((VortexCrudDataStore) store)
                .fields(Collections.emptyMap())
                .build();

        return JooqNotificationPanelConfiguration.builder()
                .dataStoreConfig(dsConfig);
    }
}
