package com.github.appreciated.vortex_crud.test.jpa.ui.data_store_dropdown_menu_action;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreDropdownMenuAction;
import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.jpa.service.JpaFieldAnnotationRegistryService;
import com.github.appreciated.vortex_crud.jpa.service.config.JpaRepositoryDataStore;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaFieldService;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaApplication;
import com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar.JpaDataStoreConfig;
import com.github.appreciated.vortex_crud.test.jpa.ui.data_store_dropdown_menu_action.JpaDropdownRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JpaDataStoreDropdownMenuActionVortexCrudConfiguration implements VortexCrudConfigurationProvider<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private final JpaDropdownRepository repository;
    private final JpaFieldAnnotationRegistryService annotationRegistryService;
    private final JpaFieldService fieldService;

    public JpaDataStoreDropdownMenuActionVortexCrudConfiguration(JpaDropdownRepository repository, JpaFieldAnnotationRegistryService annotationRegistryService, JpaFieldService fieldService) {
        this.repository = repository;
        this.annotationRegistryService = annotationRegistryService;
        this.fieldService = fieldService;
    }

    @Override
    public Application<JpaRepository<?, ?>, String, JpaRepository<?, ?>> get() {
        var dropdownStore = new JpaRepositoryDataStore<>(repository, annotationRegistryService, new DataStoreHooks<>());
        Map<Class<?>, VortexCrudDataStore> storeMap = Map.of(dropdownStore.getModelClass(), dropdownStore);

        var dropdownConfig = JpaDataStoreConfig.builder(repository, dropdownStore)
                .withServices(fieldService, storeMap)
                .build();

        DataStoreDropdownMenuAction<JpaRepository<?, ?>, String, JpaRepository<?, ?>> menuAction =
                DataStoreDropdownMenuAction.<JpaRepository<?, ?>, String, JpaRepository<?, ?>>builder()
                        .dataStoreConfig(dropdownConfig)
                        .label("Active Project")
                        .labelField("name")
                        .build();

        return JpaApplication.builder()
                .applicationName("Dropdown Test App")
                .i18nBundlePrefix("ui_test_i18n")
                .defaultMenuActions(List.of(menuAction))
                .build();
    }
}
