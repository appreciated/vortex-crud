package com.github.appreciated.vortex_crud.test.jpa.ui.images;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.test.jpa.shared.JpaImageRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JpaImagesVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaImageRepository imageRepository;

    public JpaImagesVortexCrudConfiguration(JpaImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = Map.of(
                imageRepository, JpaDataStoreConfig.of(imageRepository).build()
        );

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("images-grid", JpaRouteRenderer.of(GridRouteFactory.class)
                .withDataStore(imageRepository)
                .withTitle("route.images-cards")
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withImageField("url")
                        .build())
                .build());
        routes.put("images-list", JpaRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(imageRepository)
                .withTitle("route.images-list")
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .withFilterField("title")
                        .withChildren(
                                new JpaFieldElement("url", "route.projects.labels.description"),
                                new JpaFieldElement("title", "route.projects.labels.name")
                        )
                        .build())
                .build());

        return JpaApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .build();
    }
}
