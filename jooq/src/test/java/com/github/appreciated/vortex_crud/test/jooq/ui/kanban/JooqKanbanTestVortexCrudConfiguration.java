package com.github.appreciated.vortex_crud.test.jooq.ui.kanban;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.SelectField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.KanbanTasksRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.jooq.DSLContext;
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

    private final DSLContext dsl;

    public JooqKanbanTestVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore<KanbanTasksRecord> store = new JooqDataStore<>(KANBAN_TASKS.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(KANBAN_TASKS)
                        .dataStoreInstance(store)
                        .fields(Map.of(
                                KANBAN_TASKS.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                KANBAN_TASKS.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                KANBAN_TASKS.STATUS, SelectField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().values("enum-options").build()
                        ))
                        .build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .titleField(KANBAN_TASKS.TITLE)
                .fields(List.of(JooqFormElement.of(KANBAN_TASKS.TITLE, "route.tasks.labels.title").build()))
                .build();

        LinkedHashMap<String, String> enumOptions = new LinkedHashMap<>();
        enumOptions.put("a", "enums.option1");
        enumOptions.put("b", "enums.option2");
        enumOptions.put("c", "enums.option3");

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("tasks", JooqKanbanRoute.builder()
                .iconFactory(VaadinIcon.TASKS::create)
                .dataStoreConfig(config)
                .title("route.open-tasks.title")
                .titleField(KANBAN_TASKS.TITLE)
                .descriptionField(KANBAN_TASKS.DESCRIPTION)
                .columnField(KANBAN_TASKS.STATUS)
                .rowIndexField(KANBAN_TASKS.ROW_INDEX)
                .filterField(KANBAN_TASKS.TITLE)
                .form(taskForm)
                .build()
        );

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .selects(Selects.builder().configs(Map.of("enum-options", enumOptions)).build())
                .build();
    }
}
