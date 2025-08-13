package com.github.appreciated.vortex_crud.test.jpa.ui.card;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IdFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import com.github.appreciated.vortex_crud.test.jpa.shared.JpaProjectRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JpaCardVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaProjectRepository projectRepository;

    public JpaCardVortexCrudConfiguration(JpaProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        Map<JpaRepository<?, ?>, DataStoreConfig<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> dataStores = Map.of(
                projectRepository, JpaDataStoreConfig.of(projectRepository)
                        .withFields(Map.of(
                                "id", new JpaField(IdFieldFactory.class, true),
                                "name", new JpaField(TextFieldFactory.class, true, true),
                                "description", new JpaField(TextFieldFactory.class)
                        ))
                        .build()
        );

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("projects-cards", JpaRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(projectRepository)
                .withTitle("route.projects.title-cards")
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .withFilterField("name")
                        .withChildren(
                                new JpaFieldElement("name", "route.projects.labels.name"),
                                new JpaFieldElement("description", "route.projects.labels.description")
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
