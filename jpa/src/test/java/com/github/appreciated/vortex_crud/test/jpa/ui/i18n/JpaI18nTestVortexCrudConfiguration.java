package com.github.appreciated.vortex_crud.test.jpa.ui.i18n;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRoute;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaListRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaI18nTestVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaImageRepository imageRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaI18nTestVortexCrudConfiguration(JpaImageRepository imageRepository, JpaFieldService fieldService, JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.imageRepository = imageRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var imageStore = new JpaRepositoryDataStore<>(imageRepository, annotationRegistryService);
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(imageStore.getModelClass(), imageStore);

        var imageConfig = JpaDataStoreConfig.builder(imageRepository, imageStore)
                .withServices(fieldService, storeMap)
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaFormRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.projects.title-cards")
                .titleField("title")
                .fields(List.of(
                        JpaFieldElement.builder("title", "route.images.labels.title").build(),
                        JpaFieldElement.builder("url", "route.images.labels.image").build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("images-list", JpaListRoute.builder()
                .dataStoreConfig(imageConfig)
                .title("route.images-list")
                .inlineEdit(true)
                .filterField("title")
                .columns(List.of(
                        JpaFieldElement.builder("url", "route.projects.labels.description").build(),
                        JpaFieldElement.builder("title", "route.projects.labels.name").build()
                ))
                .form(imageForm)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
