package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.CardFactory;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JpaManyToManyVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaManyToManyEntityRepository itemRepository;

    public JpaManyToManyVortexCrudConfiguration(JpaManyToManyEntityRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        RouteRendererSingleChild<JpaRepository<?, ?>, String, JpaRepository<?, ?>> itemForm = FormRoute.builder()
                .dataStoreKey(itemRepository)
                .configuration(JpaRouteRendererConfiguration.of(CardFactory.class)
                        .titleField("name")
                        .children(List.of(
                                JpaFieldElement.of("name", "relations.labels.name").build(),
                                JpaCollectionElement.of("relations.labels.related")
                                        .factory((Class) ListCollectionFactory.class)
                                        .configuration(JpaCollection.of(ConnectDialogFactory.class)
                                                .data(JpaCollectionConfiguration.of(itemRepository)
                                                        .manyToMany(new com.github.appreciated.vortex_crud.jpa.service.JpaManyToMany(itemRepository, "relatedItems"))
                                                        .children(List.of("name"))
                                                        .build())
                                                .emptyMessage("relations.related.empty")
                                                .config(new com.github.appreciated.vortex_crud.core.config.model.CollectionConfig("name"))
                                                .build())
                                        .build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("many-to-many-test", ListRoute.builder()
                .dataStoreKey(itemRepository)
                .iconFactory(FACTORY::create)
                .title("relations.tests.many-to-many.title")
                .configuration(ListItemRendererConfiguration.builder()
                        .filterField("name")
                        .children(List.of(
                                JpaFieldElement.of("name", "relations.labels.name").build()
                        ))
                        .build())
                .child(itemForm)
                .build());

        return JpaApplication.builder()
                .name("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
