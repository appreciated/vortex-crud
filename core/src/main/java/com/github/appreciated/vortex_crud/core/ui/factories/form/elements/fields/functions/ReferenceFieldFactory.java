package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.EntityComboBoxWrapper;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class ReferenceFieldFactory<DataStoreId, FieldId> implements VortexCrudFieldFactory<DataStoreId, FieldId> {

    private final VortexCrudDataStoreFieldNameResolver<FieldId> resolver;
    private final VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> managerFactoryRegistry;
    private final ReflectionService reflectionService;

    public ReferenceFieldFactory(VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                                 VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> managerFactoryRegistry,
                                 ReflectionService reflectionService
    ) {
        this.resolver = resolver;
        this.managerFactoryRegistry = managerFactoryRegistry;
        this.reflectionService = reflectionService;
    }

    @Override
    public Component createComponent(DataStoreId table, FieldId field, Field<DataStoreId, FieldId> dataStoreField) {
        return new EntityComboBoxWrapper<>(resolver, managerFactoryRegistry, dataStoreField, reflectionService);
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("UUID", "INTEGER", "INT", "CHAR", "VARCHAR", "SERIAL");
    }
}