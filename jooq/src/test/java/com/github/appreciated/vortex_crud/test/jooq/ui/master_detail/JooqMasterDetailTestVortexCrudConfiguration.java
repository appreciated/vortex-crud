package com.github.appreciated.vortex_crud.test.jooq.ui.master_detail;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.MASTER_DETAIL_TASKS;
import static com.vaadin.flow.component.icon.VaadinIcon.CHECK_CIRCLE;

@Service
public class JooqMasterDetailTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                MASTER_DETAIL_TASKS, JooqDataStoreConfig.of(MASTER_DETAIL_TASKS)
                        .withFields(Map.of(
                                MASTER_DETAIL_TASKS.ID, new IdField<>(),
                                MASTER_DETAIL_TASKS.TITLE, new TextField<>(),
                                MASTER_DETAIL_TASKS.STATUS, new TextField<>()
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(MASTER_DETAIL_TASKS)
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(MASTER_DETAIL_TASKS.TITLE)
                        .withChildren(new JooqFieldElement(MASTER_DETAIL_TASKS.TITLE, "route.tasks.labels.title"))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JooqRouteRenderer.of(MasterDetailRouteFactory.class)
                .withIconFactory(CHECK_CIRCLE::create)
                .withDataStore(MASTER_DETAIL_TASKS)
                .withTitle("route.done-tasks.title")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField(MASTER_DETAIL_TASKS.TITLE)
                        .withDescriptionField(MASTER_DETAIL_TASKS.DESCRIPTION)
                        .build())
                .withChild(taskForm)
                .build());

        return JooqApplication.builder()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .build();
    }
}
