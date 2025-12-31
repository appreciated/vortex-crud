package com.github.appreciated.vortex_crud.test.jooq.ui.notification_panel;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqApplication;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqDataStoreConfig;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqGridRoute;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.NOTIFICATION_PANEL_TEST;

@Configuration
public class JooqNotificationPanelVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    public JooqNotificationPanelVortexCrudConfiguration() {
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> config = JooqDataStoreConfig.of(NOTIFICATION_PANEL_TEST).build();

        // Add a simple grid route so the router layout renders
        GridRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> gridRoute = JooqGridRoute.builder()
                .dataStoreConfig(config)
                .title("Home")
                .defaultRoute(true)
                .build();

        return JooqApplication.builder()
                .applicationName("Notification Test App")
                .i18nBundlePrefix("ui_test_i18n")
                .notificationPanelConfiguration(NotificationPanelConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                        .dataStoreConfig(config)
                        .messageField(NOTIFICATION_PANEL_TEST.MESSAGE)
                        .timestampField(NOTIFICATION_PANEL_TEST.TIMESTAMP)
                        .readStatusField(NOTIFICATION_PANEL_TEST.READ)
                        .build())
                .routes(Map.of("home", gridRoute))
                .build();
    }
}
