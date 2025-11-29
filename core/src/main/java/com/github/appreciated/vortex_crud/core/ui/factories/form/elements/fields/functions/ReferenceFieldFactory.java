package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.EntityComboBoxWrapper;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

public class ReferenceFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudDataStoreFieldNameResolver<FieldType> resolver;
    private final ReflectionService<FieldType> reflectionService;

    public ReferenceFieldFactory(VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                 ReflectionService<FieldType> reflectionService
    ) {
        this.resolver = resolver;
        this.reflectionService = reflectionService;
    }

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField) {
        return new EntityComboBoxWrapper<>(resolver, dataStoreField, reflectionService);
    }

    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("UUID", "INTEGER", "INT", "CHAR", "VARCHAR", "SERIAL");
    }
}