package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaRouteRendererConfiguration;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JpaManyToManyVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaManyToManyEntityRepository itemRepository;

    public JpaManyToManyVortexCrudConfiguration(JpaManyToManyEntityRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> itemForm = JpaRouteRenderer.of(FormRouteFactory.class)
                .withDataStore(itemRepository)
                .withConfiguration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .withTitleField("name")
                        .withChildren(
                                new JpaFieldElement("name", "relations.labels.name"),
                                JpaCollectionElement.of("relations.labels.related")
                                        .withFactory(ListCollectionFactory.class)
                                        .withConfiguration(JpaCollection.of(ConnectDialogFactory.class)
                                                .withData(JpaCollectionConfiguration.of(itemRepository)
                                                        .withManyToMany(new com.github.appreciated.vortex_crud.jpa.service.JpaManyToMany(itemRepository, "relatedItems"))
                                                        .withChildren("name")
                                                        .build())
                                                .withEmptyMessage("relations.related.empty")
                                                .withConfiguration(new com.github.appreciated.vortex_crud.core.config.model.CollectionConfig("name"))
                                                .build())
                                        .build()
                        )
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("many-to-many-test", JpaRouteRenderer.of(ListRouteFactory.class)
                .withDataStore(itemRepository)
                .withIconFactory(FACTORY::create)
                .withTitle("relations.tests.many-to-many.title")
                .withConfiguration(JpaGridOrListRendererConfiguration.of(CardFactory.class)
                        .withFilterField("name")
                        .withChildren(
                                new JpaFieldElement("name", "relations.labels.name")
                        )
                        .build())
                .withChild(itemForm)
                .build());

        return JpaApplication.of()
                .withName("application.name")
                .withI18nBundlePrefix("ui_test_i18n")
                .withRoutes(routes)
                .build();
    }
}
