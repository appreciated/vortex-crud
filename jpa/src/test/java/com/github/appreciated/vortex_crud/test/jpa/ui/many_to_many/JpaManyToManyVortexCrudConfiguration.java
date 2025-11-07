package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.VortexCrudCollectionFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaManyToMany;
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
        CollectionConfiguration.CollectionConfigurationBuilder<JpaRepository<?, ?>, String, JpaRepository<?, ?>> relatedItems = JpaCollectionConfiguration.builder(itemRepository)
                .manyToMany(new JpaManyToMany<>(itemRepository, "relatedItems"));
        CollectionConfiguration<JpaRepository<?, ?>, String, JpaRepository<?, ?>> build = relatedItems
                .children(List.of("name"))
                .build();
        RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>> itemForm = JpaFormRoute.builder()
                .dataStoreKey(itemRepository)
                .configuration(JpaFormRendererConfiguration.builder()
                        .titleField("name")
                        .children(List.of(
                                JpaFieldElement.builder("name", "relations.labels.name").build(),
                                JpaCollectionElement.builder("relations.labels.related").factory((Class<? extends VortexCrudCollectionFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) (Class) ListCollectionFactory.class)
                                        .configuration(JpaCollection.builder((Class<? extends VortexCrudDialogFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) (Class) ConnectDialogFactory.class)
                                                .data(build)
                                                .emptyMessage("relations.related.empty")
                                                .configuration(new com.github.appreciated.vortex_crud.core.config.model.CollectionConfig("name"))
                                                .build())
                                        .build()
                        ))
                        .build())
                .build();

        LinkedHashMap<String, RouteRenderer<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> routes = new LinkedHashMap<>();
        routes.put("many-to-many-test", JpaListRoute.builder()
                .dataStoreKey(itemRepository)
                .iconFactory(FACTORY::create)
                .title("relations.tests.many-to-many.title")
                .configuration(JpaListItemRendererConfiguration.builder()
                        .filterField("name")
                        .children(List.of(
                                JpaFieldElement.builder("name", "relations.labels.name").build()
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
