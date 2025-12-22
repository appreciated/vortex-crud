package com.github.appreciated.vortex_crud.test.jooq.ui.field_types;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.Selects;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.PdfField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.LocalPdfResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
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
public class JooqFieldTypesVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqFieldTypesVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        // Selects config
        LinkedHashMap<String, String> tagOptions = new LinkedHashMap<>();
        tagOptions.put("tag1", "Tag 1");
        tagOptions.put("tag2", "Tag 2");

        Map<String, LinkedHashMap<?, String>> selectsConfig = new HashMap<>();
        selectsConfig.put("tags", tagOptions);

        Selects selects = Selects.builder()
            .configs(selectsConfig)
            .build();

        JooqDataStore store = new JooqDataStore(FIELD_TYPES_TEST.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(FIELD_TYPES_TEST)
                .dataStoreInstance(store)
                .fields(Map.of(
                        FIELD_TYPES_TEST.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                        FIELD_TYPES_TEST.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                        FIELD_TYPES_TEST.PDF_DOC, PdfField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                .resourceProvider(new LocalPdfResourceProvider())
                                .build(),
                        FIELD_TYPES_TEST.NOTES, TextAreaField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                ))
                .build();

        // Form Route
        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> form = JooqFormRoute.builder()
            .dataStoreConfig(config)
            .title("route.missing.title")
            .titleField(FIELD_TYPES_TEST.NAME)
            .children(List.of(
                JooqFieldElement.of(FIELD_TYPES_TEST.NAME, "Name").build(),
                JooqFieldElement.of(FIELD_TYPES_TEST.PDF_DOC, "PDF").build(),
                JooqFieldElement.of(FIELD_TYPES_TEST.NOTES, "Notes").build()
            ))
            .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        // List Route
        routes.put("missing-features-test", JooqListRoute.builder()
            .dataStoreConfig(config)
            .title("route.missing.list")
            .filterField(FIELD_TYPES_TEST.NAME)
            .children(List.of(
                  JooqFieldElement.of(FIELD_TYPES_TEST.NAME, "Name").build()
            ))
            .form(form)
            .build());

        return JooqApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .selects(selects)
            .build();
    }
}
