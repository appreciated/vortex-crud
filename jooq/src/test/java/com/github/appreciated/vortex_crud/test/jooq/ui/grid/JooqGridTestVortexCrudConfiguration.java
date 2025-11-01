package com.github.appreciated.vortex_crud.test.jooq.ui.grid;

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
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jooq.service.syntactic_sugar.*;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.appreciated.vortex_crud.jooq.models.Tables.GRID_IMAGES;

@Service
public class JooqGridTestVortexCrudConfiguration
        implements VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> get() {
        Map<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> dataStores = Map.of(
                GRID_IMAGES, JooqDataStoreConfig.of(GRID_IMAGES)
                        .withFields(Map.of(
                                GRID_IMAGES.ID, new IdField<>(),
                                GRID_IMAGES.TITLE, new TextField<>(),
                                GRID_IMAGES.URL, new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class))
                        ))
                        .build()
        );

        RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>> imageForm = JooqRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(GRID_IMAGES)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JooqRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField(GRID_IMAGES.TITLE)
                        .withChildren(
                                new JooqFieldElement(GRID_IMAGES.TITLE, "route.images.labels.title"),
                                new JooqFieldElement(GRID_IMAGES.URL, "route.images.labels.image")
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> routes = new LinkedHashMap<>();
        routes.put("images-list", JooqRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(GRID_IMAGES)
                .withTitle("route.images-list")
                .withConfiguration(JooqGridOrListRendererConfiguration.of(CardFactory.class)
                        .withInlineEdit(true)
                        .withFilterField(GRID_IMAGES.TITLE)
                        .withChildren(
                                new JooqFieldElement(GRID_IMAGES.URL, "route.projects.labels.description"),
                                new JooqFieldElement(GRID_IMAGES.TITLE, "route.projects.labels.name")
                        )
                        .build())
                .withChild(imageForm)
                .build());

        return JooqApplication.builder()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .build();
    }
}