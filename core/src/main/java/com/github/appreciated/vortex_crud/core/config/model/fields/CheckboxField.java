package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.CheckboxFieldFactory;
import lombok.Builder;

import java.util.List;

/**
 * Thin Field type for CheckboxFieldFactory.
 */
@Builder
public record CheckboxField<ModelClass, FieldType, RepositoryType>(
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles,
        Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory
) implements Field<ModelClass, FieldType, RepositoryType> {
    @SuppressWarnings("unchecked")
    public static class CheckboxFieldBuilder<ModelClass, FieldType, RepositoryType> {
        CheckboxFieldBuilder() {
            factory = (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) CheckboxFieldFactory.class;
        }
    }

}