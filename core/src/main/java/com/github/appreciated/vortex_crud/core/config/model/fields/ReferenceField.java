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
        List<String> readOnlyRoles,
        Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory
) implements Field<ModelClass, FieldType, RepositoryType> {
    @SuppressWarnings("unchecked")
    public ReferenceField(RepositoryType dataStore,
                          FieldType field,
                          FieldType filterField,
                          boolean required) {
        this(dataStore, field, filterField, null, required, null, null, (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>)  (Class<?>) ReferenceFieldFactory.class);
    }

    @Override
    public Validation validation() {
        return null;
    }
}