package com.github.appreciated.vortex_crud.test.jooq.ui.master_detail;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IdFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
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

import static com.github.appreciated.vortex_crud.jooq.models.tables.Tasks.TASKS;
import static com.vaadin.flow.component.icon.VaadinIcon.CHECK_CIRCLE;

@Service
public class JooqMasterDetailTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                TASKS, JooqDataStoreConfig.of(TASKS)
                        .withFields(Map.of(
                                TASKS.ID, new JooqField(IdFieldFactory.class, true),
                                TASKS.TITLE, new JooqField(TextFieldFactory.class, true, true),
                                TASKS.STATUS, new JooqField(TextFieldFactory.class, true)
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(TASKS)
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(TASKS.TITLE)
                        .withChildren(new JooqFieldElement(TASKS.TITLE, "route.tasks.labels.title"))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JooqRouteRenderer.of(MasterDetailRouteFactory.class)
                .withIconFactory(CHECK_CIRCLE::create)
                .withDataStore(TASKS)
                .withTitle("route.done-tasks.title")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField(TASKS.TITLE)
                        .withDescriptionField(TASKS.DESCRIPTION)
                        .build())
                .withChild(taskForm)
                .build());

        return JooqApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .build();
    }
}
