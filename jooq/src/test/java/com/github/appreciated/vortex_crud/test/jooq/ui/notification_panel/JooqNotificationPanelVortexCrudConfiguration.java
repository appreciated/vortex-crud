package com.github.appreciated.vortex_crud.test.jooq.ui.notification_panel;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqApplication;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqDataStoreConfig;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqGridRoute;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.JooqNotificationPanelConfiguration;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqCheckboxField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqDateTimePickerField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqTextField;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.NOTIFICATION_PANEL_TEST;

@Configuration
public class JooqNotificationPanelVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqNotificationPanelVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        var dataStore = new JooqDataStore<>(NOTIFICATION_PANEL_TEST.getRecordType(), dsl);

        DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> config = JooqDataStoreConfig.of(NOTIFICATION_PANEL_TEST)
                .dataStoreInstance(dataStore)
                .fields(Map.of(
                        NOTIFICATION_PANEL_TEST.ID, JooqNumericIdField.builder().build(),
                        NOTIFICATION_PANEL_TEST.MESSAGE, JooqTextField.builder().build(),
                        NOTIFICATION_PANEL_TEST.TIMESTAMP, JooqDateTimePickerField.builder().build(),
                        NOTIFICATION_PANEL_TEST.READ, JooqCheckboxField.builder().build()
                ))
                .build();

        // Add a simple grid route so the router layout renders
        GridRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> gridRoute = JooqGridRoute.builder()
                .dataStoreConfig(config)
                .title("Home")
                .titleField(NOTIFICATION_PANEL_TEST.MESSAGE)
                .defaultRoute(true)
                .build();

        return JooqApplication.builder()
                .applicationName("application.name.notification-test")
                .i18nBundlePrefix("ui_test_i18n")
                .notificationPanelConfiguration(JooqNotificationPanelConfiguration.builder()
                        .dataStoreConfig(config)
                        .messageField(NOTIFICATION_PANEL_TEST.MESSAGE)
                        .timestampField(NOTIFICATION_PANEL_TEST.TIMESTAMP)
                        .readStatusField(NOTIFICATION_PANEL_TEST.READ)
                        .build())
                .routes(Map.of("home", gridRoute))
                .build();
    }
}
