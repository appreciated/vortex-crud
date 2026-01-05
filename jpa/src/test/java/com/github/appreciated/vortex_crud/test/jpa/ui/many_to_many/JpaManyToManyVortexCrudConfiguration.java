package com.github.appreciated.vortex_crud.test.jpa.ui.many_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.ConnectDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.JpaManyToMany;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JpaManyToManyVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaManyToManyEntityRepository itemRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaManyToManyVortexCrudConfiguration(JpaManyToManyEntityRepository itemRepository, JpaFieldService fieldService, JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.itemRepository = itemRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var itemStore = new JpaRepositoryDataStore<>(itemRepository, annotationRegistryService);
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(itemStore.getModelClass(), itemStore);

        var itemConfig = JpaDataStoreConfig.builder(itemRepository, itemStore)
                .withServices(fieldService, storeMap)
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> itemForm = JpaFormRoute.builder()
                .titleField("name")
                .fields(List.of(
                        JpaFormElement.builder("name", "relations.labels.name").build(),
                        JpaCollectionElement.builder("relations.labels.related")
                                .field("relatedItems")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new ConnectDialogFactory<>())
                                .manyToMany(new JpaManyToMany<>(itemRepository, "relatedItems"))
                                .dataStoreConfig(itemConfig)
                                .emptyMessage("relations.related.empty")
                                .children(List.of("name"))
                                .titleField("name")
                                .build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("many-to-many-test", JpaListRoute.builder()
                .dataStoreConfig(itemConfig)
                .iconFactory(FACTORY::create)
                .title("relations.tests.many-to-many.title")
                .filterField("name")
                .form(itemForm)
                .columns(List.of(JpaFormElement.builder("name", "relations.labels.name").build()))
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
