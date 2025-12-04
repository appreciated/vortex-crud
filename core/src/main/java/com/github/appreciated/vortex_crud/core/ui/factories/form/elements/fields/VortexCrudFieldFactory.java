package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.context.VortexCrudContext;
import com.vaadin.flow.component.Component;

import java.util.Collection;

public interface VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {
    Component createComponent(RepositoryType table, FieldType field, Field<ModelClass, FieldType, RepositoryType> dataStoreField, VortexCrudContext<ModelClass, FieldType, RepositoryType> context);

    Collection<String> getValidDatabaseTypesForExpectedType();
}
