package com.github.appreciated.vortex_crud.test.jooq.ui.grid;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
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
        JooqDataStore store = new JooqDataStore(GRID_IMAGES.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(GRID_IMAGES)
                        .dataStoreInstance((VortexCrudDataStore) store)
                        .fields(Map.of(
                                GRID_IMAGES.ID, IdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                GRID_IMAGES.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                GRID_IMAGES.URL, ImageField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                        .configuration(ImageFieldRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                                .resourceProvider(LocalImageResourceProvider.class)
                                                .build())
                                        .build()
                        ))
                        .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.projects.title-cards")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(GRID_IMAGES.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(GRID_IMAGES.TITLE, "route.images.labels.title").build(),
                                JooqFieldElement.of(GRID_IMAGES.URL, "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("images-grid", JooqGridRoute.builder()
                .dataStoreConfig(config)
                .title("route.images-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(GRID_IMAGES.TITLE)
                        .imageField(GRID_IMAGES.URL)
                        .resourceProvider(LocalImageResourceProvider.class)
                        .build())
                .child(imageForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
