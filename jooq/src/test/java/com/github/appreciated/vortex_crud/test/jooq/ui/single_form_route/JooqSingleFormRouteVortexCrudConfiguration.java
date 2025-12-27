package com.github.appreciated.vortex_crud.test.jooq.ui.single_form_route;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.PdfField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextAreaField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.SINGLE_FORM_ROUTE_MISSING_TEST;

@Service
public class JooqSingleFormRouteVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqSingleFormRouteVortexCrudConfiguration(DSLContext dsl) {
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

        JooqDataStore store = new JooqDataStore(SINGLE_FORM_ROUTE_MISSING_TEST.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(SINGLE_FORM_ROUTE_MISSING_TEST)
                .dataStoreInstance((VortexCrudDataStore) store)
                .fields(Map.of(
                        SINGLE_FORM_ROUTE_MISSING_TEST.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                        SINGLE_FORM_ROUTE_MISSING_TEST.NAME, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                        SINGLE_FORM_ROUTE_MISSING_TEST.PDF_DOC, PdfField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                .resourceProvider(new LocalPdfResourceProvider())
                                .build(),
                        SINGLE_FORM_ROUTE_MISSING_TEST.NOTES, TextAreaField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();

        // Single Form Route
        routes.put("single-form-test", JooqSingleFormRoute.builder()
             .dataStoreConfig(config)
             .title("Single Form")
             .entityFilterField(SINGLE_FORM_ROUTE_MISSING_TEST.ID)
             .entityFilterValueProvider(() -> 1)
             .titleField(SINGLE_FORM_ROUTE_MISSING_TEST.NAME)
             .children(List.of(
                 JooqFieldElement.of(SINGLE_FORM_ROUTE_MISSING_TEST.NAME, "Name").build(),
                 JooqFieldElement.of(SINGLE_FORM_ROUTE_MISSING_TEST.PDF_DOC, "PDF").build()
             ))
             .build());

        return JooqApplication.builder()
            .applicationName("application.name")
            .i18nBundlePrefix("ui_test_i18n")
            .routes(routes)
            .selects(selects)
            .build();
    }
}
