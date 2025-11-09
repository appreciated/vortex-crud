package com.github.appreciated.vortex_crud.test.jpa.ui.card;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.config.model.fields.IdField;
import com.github.appreciated.vortex_crud.core.config.model.fields.ImageField;
import com.github.appreciated.vortex_crud.core.config.model.fields.TextField;
import com.github.appreciated.vortex_crud.core.file_provider.LocalImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JpaCardVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaImageRepository imageRepository;

    public JpaCardVortexCrudConfiguration(JpaImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = Map.of(
                imageRepository, JpaDataStoreConfig.builder(imageRepository)
                        .fields(Map.of(
                                "id", IdField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "title", TextField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().build(),
                                "url", ImageField.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder().configuration(JpaImageFieldRendererConfiguration.builder().resourceProvider(LocalImageResourceProvider.class).build()).build()
                        ))
                        .build()
        );

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = FormRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
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
        routes.put("images-grid", GridRoute.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                .dataStoreKey(imageRepository)
                .title("route.images-cards")
                .configuration(GridItemRendererConfiguration.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .titleField("title")
                        .imageField("url")
                        .resourceProvider(LocalImageResourceProvider.class)
                        .build())
                .child(imageForm)
                .build());

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .dataStores(dataStores)
                .build();
    }
}
