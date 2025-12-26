package com.github.appreciated.vortex_crud.test.jooq.ui.submenu;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.SingleFormRoute;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.SubrouteTasksRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqTextField;
import com.github.appreciated.vortex_crud.test.jooq.ui.subroute.JooqSubrouteTestApplication;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.SUBROUTE_TASKS;

@Service
public class JooqSubmenuRouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqSubmenuRouteTestVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
         DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskConfig = (DataStoreConfig) JooqDataStoreConfig.builder(SUBROUTE_TASKS, dsl)
                .fields(new LinkedHashMap<>() {{
                    put(SUBROUTE_TASKS.ID, (Field) JooqNumericIdField.builder().build());
                    put(SUBROUTE_TASKS.TITLE, (Field) JooqTextField.builder().build());
                    put(SUBROUTE_TASKS.STATUS, (Field) JooqTextField.builder().build());
                }})
                .build();

        // 1. General Settings (Single Form)
        SingleFormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> generalSettings = JooqSingleFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("General Settings")
                .titleField(SUBROUTE_TASKS.TITLE)
                .entityFilterField(SUBROUTE_TASKS.ID)
                .entityFilterValueProvider(() -> 1) // Assuming ID 1 exists
                .children(List.of(
                        JooqFieldElement.of(SUBROUTE_TASKS.TITLE, "Setting Value").build()
                ))
                .build();

        // 2. Nested Submenu Routes
        // 2.1 Security (Single Form)
        SingleFormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> securitySettings = JooqSingleFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("Security")
                .titleField(SUBROUTE_TASKS.TITLE)
                .entityFilterField(SUBROUTE_TASKS.ID)
                .entityFilterValueProvider(() -> 1)
                .children(List.of(
                        JooqFieldElement.of(SUBROUTE_TASKS.TITLE, "Security Level").build()
                ))
                .build();

        // 2.2 Profile (Single Form)
        SingleFormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> profileSettings = JooqSingleFormRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("Profile")
                .titleField(SUBROUTE_TASKS.TITLE)
                .entityFilterField(SUBROUTE_TASKS.ID)
                .entityFilterValueProvider(() -> 1)
                .children(List.of(
                        JooqFieldElement.of(SUBROUTE_TASKS.TITLE, "Username").build()
                ))
                .build();

        // 3. Advanced Settings (Submenu containing Security and Profile)
        var advancedSettings = JooqSubmenuRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("Advanced Settings")
                .childrenMap(Map.of(
                        "security", securitySettings,
                        "profile", profileSettings
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("submenu-test", JooqSubmenuRoute.builder()
                .dataStoreConfig(taskConfig)
                .title("Submenu Test")
                .childrenMap(Map.of(
                        "general", generalSettings,
                        "advanced", advancedSettings
                ))
                .build());

        return JooqApplication.builder()
                .applicationName("Submenu Test App")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
