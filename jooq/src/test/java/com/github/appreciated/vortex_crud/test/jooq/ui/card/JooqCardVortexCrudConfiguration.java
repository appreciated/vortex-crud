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
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
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
                                CARD_IMAGES.ID, new IdField<>(),
                                CARD_IMAGES.TITLE, new TextField<>(),
                                CARD_IMAGES.URL, new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class))
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .dataStoreKey(CARD_IMAGES)
                .title("route.projects.title-cards")
                .configuration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .titleField(CARD_IMAGES.TITLE)
                        .children(List.of(
                                JooqFieldElement.of(CARD_IMAGES.TITLE, "route.images.labels.title").build(),
                                JooqFieldElement.of(CARD_IMAGES.URL, "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("images-grid", JooqRouteRenderer.of(GridRouteFactory.class)
                .dataStoreKey(CARD_IMAGES)
                .title("route.images-cards")
                .configuration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .titleField(CARD_IMAGES.TITLE)
                        .imageField(CARD_IMAGES.URL)
                        .imageFactory(LocalImageResourceProvider.class)
                        .build())
                .childrenMap(Map.of("form", imageForm))
                .build());

        return JooqApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
