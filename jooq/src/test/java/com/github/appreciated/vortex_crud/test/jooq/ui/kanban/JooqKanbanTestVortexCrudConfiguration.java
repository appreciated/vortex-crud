package com.github.appreciated.vortex_crud.test.jooq.ui.kanban;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IdFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.SelectFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.tables.KanbanTasks.KANBAN_TASKS;


@Service
public class JooqKanbanTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                KANBAN_TASKS, JooqDataStoreConfig.of(KANBAN_TASKS)
                        .withFields(Map.of(
                                KANBAN_TASKS.ID, new JooqField(IdFieldFactory.class, true),
                                KANBAN_TASKS.TITLE, new JooqField(TextFieldFactory.class, true, true),
                                KANBAN_TASKS.STATUS, new JooqField(SelectFieldFactory.class, "enum-options")
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(KANBAN_TASKS)
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(KANBAN_TASKS.TITLE)
                        .withChildren(new JooqFieldElement(KANBAN_TASKS.TITLE, "route.tasks.labels.title"))
                        .build())
                .build();

        LinkedHashMap<String, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put("a", "enums.option1");
        enumOptions.put("b", "enums.option2");
        enumOptions.put("c", "enums.option3");

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JooqRouteRenderer.of(KanbanDetailFactory.class)
                .withIconFactory(VaadinIcon.TASKS::create)
                .withDataStore(KANBAN_TASKS)
                .withTitle("route.open-tasks.title")
                .withConfiguration(JooqKanban.of(CardFactory.class)
                        .withTitleField(KANBAN_TASKS.TITLE)
                        .withDescriptionField(KANBAN_TASKS.DESCRIPTION)
                        .withColumnField(KANBAN_TASKS.STATUS)
                        .withRowIndexField(KANBAN_TASKS.ROW_INDEX)
                        .withFilterField(KANBAN_TASKS.TITLE)
                        .build())
                .withChild(taskForm)
                .build()
        );

        return JooqApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .withSelects(Selects.of().withConfigs(Map.of("enum-options", enumOptions)).build())
                .build();
    }
}
