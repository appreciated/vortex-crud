package com.github.appreciated.vortex_crud.test.jpa.ui.form_slide;

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
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaFormSlideVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaImageRepository imageRepository;

    public JpaFormSlideVortexCrudConfiguration(JpaImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = Map.of(
                imageRepository, JpaDataStoreConfig.of(imageRepository)
                        .fields(Map.of(
                                "id", new IdField<>(),
                                "title", new TextField<>(),
                                "url", new ImageField<>(new ImageFieldRendererConfiguration<>(LocalImageResourceProvider.class))
                        ))
                        .build()
        );

        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaRouteRenderer.of(FormSlideRouteFactory.class)
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
        routes.put("images", JpaRouteRenderer.of(GridRouteFactory.class)
                .dataStoreKey(imageRepository)
                .title("route.images-cards")
                .configuration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .titleField("title")
                        .imageField("url")
                        .imageFactory(LocalImageResourceProvider.class)
                        .build())
                .build());

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
