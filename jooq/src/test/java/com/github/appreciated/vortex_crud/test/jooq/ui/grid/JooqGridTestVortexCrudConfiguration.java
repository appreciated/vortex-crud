package com.github.appreciated.vortex_crud.test.jooq.ui.grid;

import com.github.appreciated.vortex_crud.core.config.model.*;
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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.GRID_IMAGES;

@Service
public class JooqGridTestVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqGridTestVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(GRID_IMAGES.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(GRID_IMAGES)
                        .dataStoreInstance((VortexCrudDataStore) store)
                        .fields(Map.of(
                                GRID_IMAGES.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                GRID_IMAGES.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                GRID_IMAGES.URL, ImageField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                        .resourceProvider(new LocalImageResourceProvider())
                                        .build()
                        ))
                        .build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqFormRoute.builder()
                .titleField(GRID_IMAGES.TITLE)
                .fields(List.of(
                        JooqFormElement.of(GRID_IMAGES.TITLE, "route.images.labels.title").build(),
                        JooqFormElement.of(GRID_IMAGES.URL, "route.images.labels.image").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("images-grid", JooqGridRoute.builder()
                .dataStoreConfig(config)
                .title("route.images-cards")
                .titleField(GRID_IMAGES.TITLE)
                .imageField(GRID_IMAGES.URL)
                .resourceProvider(new LocalImageResourceProvider())
                .form(imageForm)
                .build());

        routes.put("filtered-grid", JooqGridRoute.builder()
                .dataStoreConfig(config)
                .title("Filtered Grid")
                .titleField(GRID_IMAGES.TITLE)
                .imageField(GRID_IMAGES.URL)
                .resourceProvider(new LocalImageResourceProvider())
                .filter(RouteFilter.<TableField<?, ?>>builder()
                        .field(GRID_IMAGES.TITLE)
                        .value("ItemOne")
                        .build())
                .filter(RouteFilter.<TableField<?, ?>>builder()
                        .field(GRID_IMAGES.URL)
                        .value("./red.png")
                        .build())
                .form(imageForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
