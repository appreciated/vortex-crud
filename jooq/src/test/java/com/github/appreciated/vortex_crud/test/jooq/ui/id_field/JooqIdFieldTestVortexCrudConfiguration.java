package com.github.appreciated.vortex_crud.test.jooq.ui.id_field;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.ID_FIELD_TEST;

@Service
public class JooqIdFieldTestVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqIdFieldTestVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(ID_FIELD_TEST.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(ID_FIELD_TEST)
                .dataStoreInstance(store)
                .fields(Map.of(
                        ID_FIELD_TEST.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                        ID_FIELD_TEST.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                ))
                .build();

        // Form Route with ID field
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> form = JooqFormRoute.builder()
            .titleField(ID_FIELD_TEST.NAME)
            .fields(List.of(
                JooqFormElement.of(ID_FIELD_TEST.ID, "id-field.labels.id").build(),
                JooqFormElement.of(ID_FIELD_TEST.NAME, "id-field.labels.name").build()
            ))
            .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();

        // List Route
        routes.put("id-test-list", JooqListRoute.builder()
            .dataStoreConfig(config)
            .title("route.id-field.title-list")
            .searchField(ID_FIELD_TEST.NAME)
            .columns(List.of(
                  JooqFormElement.of(ID_FIELD_TEST.NAME, "id-field.labels.name").build()
             ))
            .form(form)
            .build());

        return JooqApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .build();
    }
}
