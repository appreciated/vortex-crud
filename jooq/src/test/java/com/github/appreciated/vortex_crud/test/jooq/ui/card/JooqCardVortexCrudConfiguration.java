package com.github.appreciated.vortex_crud.test.jooq.ui.card;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
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

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                CARD_IMAGES, JooqDataStoreConfig.of(CARD_IMAGES)
                        .fields(Map.of(
                                CARD_IMAGES.ID, IdField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                CARD_IMAGES.TITLE, TextField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder().build(),
                                CARD_IMAGES.URL, ImageField.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                        .configuration(ImageFieldRendererConfiguration.<TableRecord<?>, TableField<?, ?>, TableImpl<?>>builder()
                                                .resourceProvider(LocalImageResourceProvider.class)
                                                .build())
                                        .build()
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqFormRoute.builder()
                .dataStoreKey(CARD_IMAGES)
                .title("route.projects.title-cards")
                .formConfiguration(JooqFormRendererConfiguration.builder()
                        .titleField(CARD_IMAGES.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(CARD_IMAGES.TITLE, "route.images.labels.title").build(),
                                JooqFieldElement.of(CARD_IMAGES.URL, "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("images-grid", JooqGridRoute.builder()
                .dataStoreKey(CARD_IMAGES)
                .title("route.images-cards")
                .configuration(JooqGridItemRendererConfiguration.builder()
                        .titleField(CARD_IMAGES.TITLE)
                        .imageField(CARD_IMAGES.URL)
                        .resourceProvider(LocalImageResourceProvider.class)
                        .build())
                .child(imageForm)
                .build());

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
