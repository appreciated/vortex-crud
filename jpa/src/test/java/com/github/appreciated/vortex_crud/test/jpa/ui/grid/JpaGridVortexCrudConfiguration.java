package com.github.appreciated.vortex_crud.test.jpa.ui.grid;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class JpaGridVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaImageRepository imageRepository;

    public JpaGridVortexCrudConfiguration(JpaImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .dataStoreKey(imageRepository)
                .title("route.projects.title-cards")
                .configuration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.of("title", "route.images.labels.title").build(),
                                JpaFieldElement.of("url", "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("images-list", JpaRouteRenderer.of(ListRouteFactory.class)
                .dataStoreKey(imageRepository)
                .title("route.images-list")
                .configuration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .inlineEdit(true)
                        .filterField("title")
                        .children(List.of(
                                JpaFieldElement.of("url", "route.projects.labels.description").build(),
                                JpaFieldElement.of("title", "route.projects.labels.name").build()
                        ))
                        .build())
                .build());

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
