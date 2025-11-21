package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.EntityMultiSelectComboBoxWrapper;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class MultiSelectFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudDataStoreFieldNameResolver<FieldType> resolver;
    private final VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> managerFactoryRegistry;
    private final ReflectionService<FieldType> reflectionService;

    public MultiSelectFieldFactory(VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                   VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> managerFactoryRegistry,
                                   ReflectionService<FieldType> reflectionService
    ) {
        this.resolver = resolver;
        this.managerFactoryRegistry = managerFactoryRegistry;
        this.reflectionService = reflectionService;
    }

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField) {
        return new EntityMultiSelectComboBoxWrapper<>(resolver, managerFactoryRegistry, dataStoreField, reflectionService);
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("UUID", "INTEGER", "INT", "CHAR", "VARCHAR", "SERIAL");
    }
}
