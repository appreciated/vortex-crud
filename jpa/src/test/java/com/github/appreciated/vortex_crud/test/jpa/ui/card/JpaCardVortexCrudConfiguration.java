package com.github.appreciated.vortex_crud.test.jpa.ui.card;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.ImageFieldRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.file_provider.ImageResourceProvider;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IdFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ImageFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
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
                imageRepository, JpaDataStoreConfig.of(imageRepository)
                        .withFields(Map.of(
                                "id", new JpaField(IdFieldFactory.class, true),
                                "title", new JpaField(TextFieldFactory.class, true, true),
                                "url", JpaField.of(ImageFieldFactory.class)
                                        .withConfiguration(new ImageFieldRendererConfiguration<>(ImageResourceProvider.class))
                                        .build()
                        ))
                        .build()
        );

        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> imageForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(imageRepository)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withChildren(
                                new JpaFieldElement("title", "route.images.labels.title"),
                                new JpaFieldElement("url", "route.images.labels.image")
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("images-grid", JpaRouteRenderer.of(GridRouteFactory.class)
                .withDataStore(imageRepository)
                .withTitle("route.images-cards")
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .withTitleField("title")
                        .withImageField("url")
                        .withImageFactory(ImageResourceProvider.class)
                        .build())
                .withChild(imageForm)
                .build());

        return JpaApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .withDataStores(dataStores)
                .build();
    }
}
