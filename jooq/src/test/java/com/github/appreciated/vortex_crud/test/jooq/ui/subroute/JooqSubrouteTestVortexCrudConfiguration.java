package com.github.appreciated.vortex_crud.test.jooq.ui.subroute;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
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

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                SUBROUTE_TASKS, JooqDataStoreConfig.of(SUBROUTE_TASKS)
                        .fields(Map.of(
                                SUBROUTE_TASKS.ID, IdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                SUBROUTE_TASKS.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                SUBROUTE_TASKS.STATUS, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStoreKey(SUBROUTE_TASKS)
                .configuration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .titleField(SUBROUTE_TASKS.TITLE)
                        .children(List.of(JooqFieldElement.of(SUBROUTE_TASKS.TITLE, "route.tasks.labels.title").build()))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JooqSubmenuRoute.builder()
                .dataStoreKey(SUBROUTE_TASKS)
                .title("route.tasks.title")
                .childrenMap(Map.of(
                        "open", JooqMasterDetailRoute.builder()
                                .dataStoreKey(SUBROUTE_TASKS)
                                .title("route.open-tasks.title")
                                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                                        .titleField(SUBROUTE_TASKS.TITLE)
                                        .build())
                                .child(taskForm)
                                .build()
                ))
                .build());

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
