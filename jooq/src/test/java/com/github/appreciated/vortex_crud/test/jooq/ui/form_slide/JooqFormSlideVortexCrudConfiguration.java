package com.github.appreciated.vortex_crud.test.jooq.ui.form_slide;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.ImageResourceProvider;
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
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.FROM_SLIDE_IMAGES;

@Service
public class JooqFormSlideVortexCrudConfiguration implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                FROM_SLIDE_IMAGES, JooqDataStoreConfig.of(FROM_SLIDE_IMAGES)
                        .withFields(Map.of(
                                FROM_SLIDE_IMAGES.ID, new IdField<>(),
                                FROM_SLIDE_IMAGES.TITLE, new TextField<>(true),
                                FROM_SLIDE_IMAGES.URL, new ImageField<>(new ImageFieldRendererConfiguration<>(ImageResourceProvider.class))
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> formSlideDialog = JooqRouteRenderer.of(FormSlideRouteFactory.class)
                .withDataStore(FROM_SLIDE_IMAGES)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(FROM_SLIDE_IMAGES.TITLE)
                        .withChildren(
                                new JooqFieldElement(FROM_SLIDE_IMAGES.TITLE, "route.image.labels.title"),
                                new JooqFieldElement(FROM_SLIDE_IMAGES.URL, "route.image.labels.image")
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("images", JooqRouteRenderer.of(GridRouteFactory.class)
                .withDataStore(FROM_SLIDE_IMAGES)
                .withTitle("route.image-cards")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField(FROM_SLIDE_IMAGES.TITLE)
                        .withImageField(FROM_SLIDE_IMAGES.URL)
                        .withImageFactory(ImageResourceProvider.class)
                        .build())
                .withChild(formSlideDialog)
                .build());

        return JooqApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .build();
    }
}
