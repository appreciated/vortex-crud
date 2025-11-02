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
        List<String> readOnlyRoles
) implements Field<ModelClass, FieldType, RepositoryType> {

    @Override
    public Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        Class<? extends VortexCrudFieldFactory> f = TextAreaFieldFactory.class;
        return (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) f;
    }

}