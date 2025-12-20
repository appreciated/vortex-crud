package com.github.appreciated.vortex_crud.test.jooq.ui.subroute;

import com.github.appreciated.vortex_crud.core.config.model.*;

import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.tables.SubrouteTasks.SUBROUTE_TASKS;

@Service
public class JooqSubrouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqSubrouteTestVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore<>(SUBROUTE_TASKS.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(SUBROUTE_TASKS)
                        .dataStoreInstance(store)
                        .fields(Map.of(
                                SUBROUTE_TASKS.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                SUBROUTE_TASKS.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                SUBROUTE_TASKS.STATUS, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                        ))
                        .build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(SUBROUTE_TASKS.TITLE)
                        .children(List.of(JooqFieldElement.of(SUBROUTE_TASKS.TITLE, "route.tasks.labels.title").build()))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        GridItemRendererConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> build = JooqGridItemRendererConfiguration.builder()
                .titleField(SUBROUTE_TASKS.TITLE)
                .build();
        routes.put("tasks", JooqSubmenuRoute.builder()
                .dataStoreConfig(config)
                .title("route.tasks.title")
                .childrenMap(Map.of(
                        "open", JooqMasterDetailRoute.builder()
                                .dataStoreConfig(config)
                                .title("route.open-tasks.title")
                                .configuration(build)
                                .child(taskForm)
                                .build()
                ))
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
