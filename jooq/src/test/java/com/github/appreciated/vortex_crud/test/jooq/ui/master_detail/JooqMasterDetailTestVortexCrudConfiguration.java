package com.github.appreciated.vortex_crud.test.jooq.ui.master_detail;

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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.MASTER_DETAIL_TASKS;
import static com.vaadin.flow.component.icon.VaadinIcon.CHECK_CIRCLE;

@Service
public class JooqMasterDetailTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                MASTER_DETAIL_TASKS, JooqDataStoreConfig.of(MASTER_DETAIL_TASKS)
                        .fields(Map.of(
                                MASTER_DETAIL_TASKS.ID, IdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                MASTER_DETAIL_TASKS.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                MASTER_DETAIL_TASKS.STATUS, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStoreKey(MASTER_DETAIL_TASKS)
                .configuration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .titleField(MASTER_DETAIL_TASKS.TITLE)
                        .children(List.of(JooqFieldElement.of(MASTER_DETAIL_TASKS.TITLE, "route.tasks.labels.title").build()))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JooqMasterDetailRoute.builder()
                .iconFactory(CHECK_CIRCLE::create)
                .dataStoreKey(MASTER_DETAIL_TASKS)
                .title("route.done-tasks.title")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .titleField(MASTER_DETAIL_TASKS.TITLE)
                        .descriptionField(MASTER_DETAIL_TASKS.DESCRIPTION)
                        .build())
                .child(taskForm)
                .build());

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
