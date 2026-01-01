package com.github.appreciated.vortex_crud.test.jpa.ui.one_to_many;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.FormRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.FormDialogFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.JpaOneToMany;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaCollectionElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFieldElement;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaFormRoute;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaListRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.vaadin.flow.component.icon.VaadinIcon.FACTORY;

@Service
public class JpaOneToManyVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaOneToManyParentRepository parentRepository;
    private final JpaOneToManyChildRepository childRepository;
    private final JpaFieldService fieldService;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;

    public JpaOneToManyVortexCrudConfiguration(JpaOneToManyParentRepository parentRepository, JpaOneToManyChildRepository childRepository, JpaFieldService fieldService, JpaFieldAnnotationRegistryService annotationRegistryService) {
        this.parentRepository = parentRepository;
        this.childRepository = childRepository;
        this.fieldService = fieldService;
        this.annotationRegistryService = annotationRegistryService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var parentStore = new JpaRepositoryDataStore<>(parentRepository, annotationRegistryService);
        var childStore = new JpaRepositoryDataStore<>(childRepository, annotationRegistryService);

        Map<Class<?>, VortexCrudDataStore> storeMap = new HashMap<>();
        storeMap.put(parentStore.getModelClass(), parentStore);
        storeMap.put(childStore.getModelClass(), childStore);

        var parentConfig = JpaDataStoreConfig.builder(parentRepository, parentStore)
                .withServices(fieldService, storeMap)
                .build();

        var childConfig = JpaDataStoreConfig.builder(childRepository, childStore)
                .withServices(fieldService, storeMap)
                .build();

        FormRoute<JpaRepository<?, ?>, String, JpaRepository<?, ?>> childForm = JpaFormRoute.builder()
                .dataStoreConfig(childConfig)
                .titleField("name")
                .fields(List.of(JpaFieldElement.builder("name", "relations.labels.name").build()))
                .build();

        FormRoute parentForm = JpaFormRoute.builder()
                .dataStoreConfig(parentConfig)
                .titleField("name")
                .fields(List.of(
                        JpaFieldElement.builder("name", "relations.labels.name").build(),
                        JpaCollectionElement.builder("relations.labels.children")
                                .field("children")
                                .listFactory(new ListCollectionFactory<>())
                                .dialogFactory(new FormDialogFactory<>())
                                .dataStoreConfig(childConfig)
                                .oneToMany(new JpaOneToMany("parent"))
                                .children(List.of("name"))
                                .emptyMessage("relations.children.empty")
                                .form(childForm)
                                .build()
                ))
                .build();

        LinkedHashMap<String, RouteRenderer<?, ?, ?>> routes = new LinkedHashMap<>();
        routes.put("one-to-many-test", JpaListRoute.builder()
                .dataStoreConfig(parentConfig)
                .iconFactory(FACTORY::create)
                .title("relations.tests.one-to-many.title")
                .filterField("name")
                .columns(List.of(
                        JpaFieldElement.builder("name", "relations.labels.name").build()
                ))
                .form(parentForm)
                .build());

        return JpaApplication.builder()
                .applicationName("application.name")
                .i18nBundlePrefix("ui_test_i18n")
                .routes(routes)
                .build();
    }
}
