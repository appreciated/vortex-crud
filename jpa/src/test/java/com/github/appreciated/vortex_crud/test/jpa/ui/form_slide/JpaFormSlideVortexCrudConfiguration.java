package com.github.appreciated.vortex_crud.test.jpa.ui.form_slide;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.NumericIdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormSlideRoute;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaGridRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaFormSlideVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaImageRepository imageRepository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaFormSlideVortexCrudConfiguration(JpaImageRepository imageRepository, JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.imageRepository = imageRepository;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var imageStore = new JpaRepositoryDataStore<>(imageRepository, annotationRegistryService, new DataStoreHooks<>());
        var imageConfig = JpaDataStoreConfig.builder(imageRepository, imageStore)
                        .fields(Map.of(
                                "id", NumericIdField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "title", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "url", ImageField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().resourceProvider(new LocalImageResourceProvider()).build()
                        ))
                        .build();

        RouteRendererSingleChild<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaFormSlideRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.projects.title-cards")
                .itemFactory(new CardFactory<>())
                .titleField("title")
                .children(List.of(
                        JpaFieldElement.builder("title", "route.images.labels.title").build(),
                        JpaFieldElement.builder("url", "route.images.labels.image").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("images", JpaGridRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.images-cards")
                .titleField("title")
                .imageField("url")
                .resourceProvider(new LocalImageResourceProvider())
                .form(imageForm)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
