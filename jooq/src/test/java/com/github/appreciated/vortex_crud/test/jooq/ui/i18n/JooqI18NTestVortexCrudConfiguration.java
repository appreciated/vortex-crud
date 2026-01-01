package com.github.appreciated.vortex_crud.test.jooq.ui.i18n;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.I18N_IMAGES;

@Service
public class JooqI18NTestVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqI18NTestVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(I18N_IMAGES.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(I18N_IMAGES)
                        .dataStoreInstance((VortexCrudDataStore) store)
                        .fields(Map.of(
                                I18N_IMAGES.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                I18N_IMAGES.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                I18N_IMAGES.URL, ImageField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                        .resourceProvider(new LocalImageResourceProvider())
                                        .build()
                        ))
                        .build();

        JooqFormRoute imageForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.projects.title-cards")
                .titleField(I18N_IMAGES.TITLE)
                .fields(List.of(
                        JooqFieldElement.of(I18N_IMAGES.TITLE, "route.images.labels.title").build(),
                        JooqFieldElement.of(I18N_IMAGES.URL, "route.images.labels.image").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("images-list", JooqListRoute.builder()
                .dataStoreConfig(config)
                .title("route.images-list")
                .inlineEdit(true)
                .filterField(I18N_IMAGES.TITLE)
                .columns(List.of(
                        JooqFieldElement.of(I18N_IMAGES.URL, "route.projects.labels.description").build(),
                        JooqFieldElement.of(I18N_IMAGES.TITLE, "route.projects.labels.name").build()
                ))
                .form(imageForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }

}
