package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.TextAreaFieldFactory;
import lombok.Builder;

import java.util.List;

/**
 * Thin Field type for TextAreaFieldFactory.
 */
@Builder
public record TextAreaField<ModelClass, FieldType, RepositoryType>(
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles,
        Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory
) implements Field<ModelClass, FieldType, RepositoryType> {
    @SuppressWarnings("unchecked")
    public static class TextAreaFieldBuilder<ModelClass, FieldType, RepositoryType> {
        TextAreaFieldBuilder() {
            factory = (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) TextAreaFieldFactory.class;
        }
    }

}