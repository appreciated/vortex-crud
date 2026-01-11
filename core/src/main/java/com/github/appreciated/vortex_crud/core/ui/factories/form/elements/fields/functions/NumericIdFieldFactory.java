package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions;

import com.github.appreciated.vortex_crud.core.annotation.NoCoverage;
import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.components.NumericIdTextField;
import com.vaadin.flow.component.Component;

import java.util.Collection;
import java.util.List;

/**
 * Factory for numeric ID fields (Integer, Long, etc.).
 * Creates a read-only NumericIdTextField for displaying numeric ID values.
 */
public class NumericIdFieldFactory<ModelClass, FieldType, RepositoryType> implements VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> {

    @Override
    public Component createComponent(RepositoryType table, FieldType field, Field<? extends ModelClass, FieldType, RepositoryType> dataStoreField, VortexCrudContext<? super ModelClass, FieldType, RepositoryType> context) {
        return new NumericIdTextField();
    }

    @NoCoverage
    @Override
    public Collection<String> getValidDatabaseTypesForExpectedType() {
        return List.of("INTEGER", "INT", "BIGINT", "SERIAL", "BIGSERIAL");
    }
}
