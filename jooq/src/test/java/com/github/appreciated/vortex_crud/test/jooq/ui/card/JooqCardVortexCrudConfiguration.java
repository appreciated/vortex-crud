package com.github.appreciated.vortex_crud.test.jooq.ui.card;

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

import static com.github.appreciated.vortex_crud.jooq.models.Tables.CARD_IMAGES;

@Service
public class JooqCardVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqCardVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(CARD_IMAGES.getRecordType(), dsl, new DataStoreHooks<>());
        var config = JooqDataStoreConfig.of(CARD_IMAGES)
                        .dataStoreInstance((VortexCrudDataStore) store)
                        .fields(Map.of(
                                CARD_IMAGES.ID, NumericIdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                CARD_IMAGES.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                CARD_IMAGES.URL, ImageField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                        .resourceProvider(new LocalImageResourceProvider())
                                        .build()
                        ))
                        .build();

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqFormRoute.builder()
                .dataStoreConfig(config)
                .title("route.projects.title-cards")
                .titleField(CARD_IMAGES.TITLE)
                .children(List.of(
                        JooqFieldElement.of(CARD_IMAGES.TITLE, "route.images.labels.title").build(),
                        JooqFieldElement.of(CARD_IMAGES.URL, "route.images.labels.image").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("images-grid", JooqGridRoute.builder()
                .dataStoreConfig(config)
                .title("route.images-cards")
                .titleField(CARD_IMAGES.TITLE)
                .imageField(CARD_IMAGES.URL)
                .resourceProvider(new LocalImageResourceProvider())
                .form(imageForm)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
