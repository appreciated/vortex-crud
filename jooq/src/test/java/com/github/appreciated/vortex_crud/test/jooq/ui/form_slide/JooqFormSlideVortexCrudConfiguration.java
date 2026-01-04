package com.github.appreciated.vortex_crud.test.jooq.ui.form_slide;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormSlideFactory;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.fields.JooqNumericIdField;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.FROM_SLIDE_IMAGES;

@Service
public class JooqFormSlideVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dsl;

    public JooqFormSlideVortexCrudConfiguration(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        JooqDataStore store = new JooqDataStore(FROM_SLIDE_IMAGES.getRecordType(), dsl);
        var config = JooqDataStoreConfig.of(FROM_SLIDE_IMAGES)
                        .dataStoreInstance((VortexCrudDataStore) store)
                        .fields(Map.of(
                                FROM_SLIDE_IMAGES.ID, JooqNumericIdField.builder().build(),
                                FROM_SLIDE_IMAGES.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().required(true).build(),
                                FROM_SLIDE_IMAGES.URL, ImageField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                        .resourceProvider(new LocalImageResourceProvider())
                                        .build()
                        ))
                        .build();

        FormRoute<TableRecord<?>, TableField<?, ?>, TableImpl<?>> formSlideDialog = JooqFormRoute.builder()
                .dialogFactory(new FormSlideFactory<>())
                .titleField(FROM_SLIDE_IMAGES.TITLE)
                .fields(List.of(
                        JooqFormElement.of(FROM_SLIDE_IMAGES.TITLE, "route.image.labels.title").build(),
                        JooqFormElement.of(FROM_SLIDE_IMAGES.URL, "route.image.labels.image").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("images", JooqGridRoute.builder()
                .dataStoreConfig(config)
                .title("route.image-cards")
                .titleField(FROM_SLIDE_IMAGES.TITLE)
                .imageField(FROM_SLIDE_IMAGES.URL)
                .resourceProvider(new LocalImageResourceProvider())
                .form(formSlideDialog)
                .build());

        return JooqApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
