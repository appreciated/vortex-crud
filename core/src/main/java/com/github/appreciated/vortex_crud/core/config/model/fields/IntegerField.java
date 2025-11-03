package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.IntegerNumberFieldFactory;
import lombok.Builder;

import java.util.List;

/**
 * Thin Field type for IntegerNumberFieldFactory.
 */
@Builder
public record IntegerField<ModelClass, FieldType, RepositoryType>(
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles,
        Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory
) implements Field<ModelClass, FieldType, RepositoryType> {
    @SuppressWarnings("unchecked")
    public static class IntegerFieldBuilder<ModelClass, FieldType, RepositoryType> {
        IntegerFieldBuilder() {
            factory = (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) IntegerNumberFieldFactory.class;
        }
    }

}