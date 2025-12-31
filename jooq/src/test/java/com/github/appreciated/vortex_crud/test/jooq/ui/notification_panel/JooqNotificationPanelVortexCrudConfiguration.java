package com.github.appreciated.vortex_crud.test.jooq.ui.notification_panel;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.NotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqApplication;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqDataStoreConfig;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.NOTIFICATION_PANEL_TEST;

@Configuration
public class JooqNotificationPanelVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        DataStoreConfig config = JooqDataStoreConfig.builder(NOTIFICATION_PANEL_TEST, null).build();
        
        return JooqApplication.builder()
                .applicationName("Notification Test App")
                .notificationPanelConfiguration(NotificationPanelConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(config)
                        .messageField(NOTIFICATION_PANEL_TEST.MESSAGE)
                        .timestampField(NOTIFICATION_PANEL_TEST.TIMESTAMP)
                        .readStatusField(NOTIFICATION_PANEL_TEST.READ)
                        .build())
                .routes(Collections.emptyMap())
                .build();
    }
}
