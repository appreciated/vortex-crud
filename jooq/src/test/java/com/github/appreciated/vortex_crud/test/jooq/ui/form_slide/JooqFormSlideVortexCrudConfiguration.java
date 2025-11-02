package com.github.appreciated.vortex_crud.test.jooq.ui.form_slide;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormSlideRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
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

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                FROM_SLIDE_IMAGES, JooqDataStoreConfig.of(FROM_SLIDE_IMAGES)
                        .fields(Map.of(
                                FROM_SLIDE_IMAGES.ID, new IdField<>(),
                                FROM_SLIDE_IMAGES.TITLE, new TextField<>(true),
                                FROM_SLIDE_IMAGES.URL, new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class))
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> formSlideDialog = JooqRouteRenderer.of(FormSlideRouteFactory.class)
                .dataStoreKey(FROM_SLIDE_IMAGES)
                .title("route.projects.title-cards")
                .configuration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .titleField(FROM_SLIDE_IMAGES.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(FROM_SLIDE_IMAGES.TITLE, "route.image.labels.title").build(),
                                JooqFieldElement.of(FROM_SLIDE_IMAGES.URL, "route.image.labels.image").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("images", JooqRouteRenderer.of(GridRouteFactory.class)
                .dataStoreKey(FROM_SLIDE_IMAGES)
                .title("route.image-cards")
                .configuration(JooqGridItemRendererConfiguration.of(CardFactory.class)
                        .titleField(FROM_SLIDE_IMAGES.TITLE)
                        .imageField(FROM_SLIDE_IMAGES.URL)
                        .imageFactory(LocalImageResourceProvider.class)
                        .build())
                .child(formSlideDialog)
                .build());

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
