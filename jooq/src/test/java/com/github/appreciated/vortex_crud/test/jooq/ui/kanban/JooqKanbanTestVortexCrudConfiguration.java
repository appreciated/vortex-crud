package com.github.appreciated.vortex_crud.test.jooq.ui.kanban;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanFactory;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.tables.KanbanTasks.KANBAN_TASKS;

@Service
public class JooqKanbanTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                KANBAN_TASKS, JooqDataStoreConfig.of(KANBAN_TASKS)
                        .fields(Map.of(
                                KANBAN_TASKS.ID, new IdField<>(),
                                KANBAN_TASKS.TITLE, new TextField<>(),
                                KANBAN_TASKS.STATUS, new SelectField<>("enum-options")
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .dataStoreKey(KANBAN_TASKS)
                .configuration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .titleField(KANBAN_TASKS.TITLE)
                        .children(List.of(JooqFieldElement.of(KANBAN_TASKS.TITLE, "route.tasks.labels.title").build()))
                        .build())
                .build();

        LinkedHashMap<String, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put("a", "enums.option1");
        enumOptions.put("b", "enums.option2");
        enumOptions.put("c", "enums.option3");

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JooqRouteRenderer.of(KanbanFactory.class)
                .iconFactory(VaadinIcon.TASKS::create)
                .dataStoreKey(KANBAN_TASKS)
                .title("route.open-tasks.title")
                .configuration(JooqKanbanConfiguration.of(CardFactory.class)
                        .titleField(KANBAN_TASKS.TITLE)
                        .descriptionField(KANBAN_TASKS.DESCRIPTION)
                        .columnField(KANBAN_TASKS.STATUS)
                        .rowIndexField(KANBAN_TASKS.ROW_INDEX)
                        .filterField(KANBAN_TASKS.TITLE)
                        .build())
                .childrenMap(Map.of("form", taskForm))
                .build()
        );

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .selects(Selects.builder().configs(Map.of("enum-options", enumOptions)).build())
                .build();
    }
}
