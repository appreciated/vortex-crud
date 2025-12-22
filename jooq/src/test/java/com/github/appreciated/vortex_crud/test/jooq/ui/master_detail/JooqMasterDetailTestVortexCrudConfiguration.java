package com.github.appreciated.vortex_crud.test.jooq.ui.master_detail;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqTextField;
import org.jooq.DSLContext;
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

    private final DSLContext dsl;

    public JooqMasterDetailTestVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(MASTER_DETAIL_TASKS.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(MASTER_DETAIL_TASKS)
                        .dataStoreInstance((VortexCrudDataStore) store)
                        .fields(Map.of(
                                MASTER_DETAIL_TASKS.ID, JooqNumericIdField.builder().build(),
                                MASTER_DETAIL_TASKS.TITLE, JooqTextField.builder().build(),
                                MASTER_DETAIL_TASKS.DESCRIPTION, JooqTextField.builder().build()
                        ))
                        .build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> taskForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.projects.title-cards")
                .titleField(MASTER_DETAIL_TASKS.TITLE)
                .children(List.of(
                        JooqFieldElement.of(MASTER_DETAIL_TASKS.TITLE, "route.tasks.labels.title").build(),
                        JooqFieldElement.of(MASTER_DETAIL_TASKS.DESCRIPTION, "route.tasks.labels.description").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("tasks", JooqMasterDetailRoute.builder()
                .iconFactory(CHECK_CIRCLE::create)
                .dataStoreConfig(config)
                .title("route.done-tasks.title")
                .titleField(MASTER_DETAIL_TASKS.TITLE)
                .descriptionField(MASTER_DETAIL_TASKS.STATUS)
                .form(taskForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
