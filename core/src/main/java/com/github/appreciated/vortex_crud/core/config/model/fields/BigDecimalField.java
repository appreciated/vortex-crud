package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.BigDecimalNumberFieldFactory;
import lombok.Builder;

import java.util.List;

/**
 * Thin Field type for BigDecimalNumberFieldFactory.
 */
@Builder
public record BigDecimalField<ModelClass, FieldType, RepositoryType>(
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles,
        Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory
) implements Field<ModelClass, FieldType, RepositoryType> {
    @SuppressWarnings("unchecked")
    public static class BigDecimalFieldBuilder<ModelClass, FieldType, RepositoryType> {
        BigDecimalFieldBuilder() {
            factory = (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) (Class<?>) BigDecimalNumberFieldFactory.class;
        }
    }
}