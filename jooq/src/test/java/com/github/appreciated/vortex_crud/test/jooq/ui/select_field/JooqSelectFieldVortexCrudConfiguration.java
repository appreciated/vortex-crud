package com.github.appreciated.vortex_crud.test.jooq.ui.select_field;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqSelectField;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqTextField;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.FIELD_TYPES_TEST;

@Service
public class JooqSelectFieldVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqSelectFieldVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        LinkedHashMap<String, String> options = new LinkedHashMap<>();
        options.put("Option 1", "Option 1 Label");
        options.put("Option 2", "Option 2 Label");

        Map<String, LinkedHashMap<?, String>> selectsConfig = new HashMap<>();
        selectsConfig.put("name-options", options);

        Selects selects = Selects.builder()
            .configs(selectsConfig)
            .build();

        JooqDataStore store = new JooqDataStore(FIELD_TYPES_TEST.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(FIELD_TYPES_TEST)
                .dataStoreInstance(store)
                .fields(Map.of(
                        FIELD_TYPES_TEST.ID, JooqNumericIdField.builder().build(),
                        FIELD_TYPES_TEST.NAME, JooqSelectField.builder().values("name-options").build(),
                        FIELD_TYPES_TEST.NOTES, JooqTextField.builder().build() // Just to fill required fields if any
                ))
                .build();

        // Form Route
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> form = JooqFormRoute.builder()
            .dataStoreConfig(config)
            .title("Select Field Test")
            .titleField(FIELD_TYPES_TEST.NAME)
            .children(List.of(
                JooqFieldElement.of(FIELD_TYPES_TEST.NAME, "Name Select").build()
            ))
            .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        // List Route
        routes.put("select-field-test", JooqListRoute.builder()
            .dataStoreConfig(config)
            .title("Select Field List")
            .filterField(FIELD_TYPES_TEST.NAME)
            .children(List.of(
                  JooqFieldElement.of(FIELD_TYPES_TEST.NAME, "Name").build()
            ))
            .child(form)
            .build());

        return JooqApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .selects(selects)
            .build();
    }
}
