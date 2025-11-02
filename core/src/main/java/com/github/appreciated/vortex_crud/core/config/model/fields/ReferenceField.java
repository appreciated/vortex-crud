package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.ReferenceFieldFactory;
import lombok.Builder;

import java.util.List;

@Builder
public record ReferenceField<ModelClass, FieldType, RepositoryType>(
        RepositoryType dataStore,
        FieldType field,
        FieldType filterField,
        List<FieldType> children,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles
) implements Field<ModelClass, FieldType, RepositoryType> {

    public ReferenceField(RepositoryType dataStore,
                          FieldType field,
                          FieldType filterField,
                          boolean required) {
        this(dataStore, field, filterField, null, required, null, null);
    }

    public RepositoryType getDataStore() {
        return dataStore;
    }

    public FieldType getFilterField() {
        return filterField;
    }

    public List<FieldType> getChildren() {
        return children;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        Class<? extends VortexCrudFieldFactory> f = ReferenceFieldFactory.class;
        return (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) f;
    }

    @Override
    public Validation getValidation() {
        return null;
    }

    public FieldType getField() {
        return field;
    }

}