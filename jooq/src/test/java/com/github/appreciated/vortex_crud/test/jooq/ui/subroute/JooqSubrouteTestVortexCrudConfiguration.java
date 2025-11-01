package com.github.appreciated.vortex_crud.test.jooq.ui.subroute;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.tables.SubrouteTasks.SUBROUTE_TASKS;

@Service
public class JooqSubrouteTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                SUBROUTE_TASKS, JooqDataStoreConfig.of(SUBROUTE_TASKS)
                        .withFields(Map.of(
                                SUBROUTE_TASKS.ID, new IdField<>(),
                                SUBROUTE_TASKS.TITLE, new TextField<>(),
                                SUBROUTE_TASKS.STATUS, new TextField<>()
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(SUBROUTE_TASKS)
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(SUBROUTE_TASKS.TITLE)
                        .withChildren(new JooqFieldElement(SUBROUTE_TASKS.TITLE, "route.tasks.labels.title"))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JooqRouteRenderer.of(SubmenuRouteFactory.class)
                .withDataStore(SUBROUTE_TASKS)
                .withTitle("route.tasks.title")
                .withChildrenMap(Map.of(
                        "open", JooqRouteRenderer.of(MasterDetailRouteFactory.class)
                                .withDataStore(SUBROUTE_TASKS)
                                .withTitle("route.open-tasks.title")
                                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                                        .withTitleField(SUBROUTE_TASKS.TITLE)
                                        .build())
                                .withChild(taskForm)
                                .build()
                ))
                .build());

        return JooqApplication.builder()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .build();
    }
}
