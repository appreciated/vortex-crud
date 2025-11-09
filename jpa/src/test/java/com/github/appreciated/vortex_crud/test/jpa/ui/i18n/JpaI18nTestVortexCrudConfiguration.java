package com.github.appreciated.vortex_crud.test.jpa.ui.i18n;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class JpaI18nTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaImageRepository imageRepository;

    public JpaI18nTestVortexCrudConfiguration(JpaImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaFormRoute.builder()
                .dataStoreKey(imageRepository)
                .title("route.projects.title-cards")
                .formConfiguration(JpaFormRendererConfiguration.builder()
                        .titleField("title")
                        .children(List.of(
                                JpaFieldElement.builder("title", "route.images.labels.title").build(),
                                JpaFieldElement.builder("url", "route.images.labels.image").build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("images-list", JpaListRoute.builder()
                .dataStoreKey(imageRepository)
                .title("route.images-list")
                .configuration(JpaListItemRendererConfiguration.builder()
                        .inlineEdit(true)
                        .filterField("title")
                        .children(List.of(
                                JpaFieldElement.builder("url", "route.projects.labels.description").build(),
                                JpaFieldElement.builder("title", "route.projects.labels.name").build()
                        ))
                        .build())
                .child(imageForm)
                .build());

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
