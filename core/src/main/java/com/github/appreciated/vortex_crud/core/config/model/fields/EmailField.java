package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.component.EmailFieldFactory;
import lombok.Builder;

import java.util.List;

/**
 * Thin Field type for TextFieldFactory.
 */
@Builder
public record EmailField<ModelClass, FieldType, RepositoryType>(
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles
) implements Field<ModelClass, FieldType, RepositoryType> {

    public EmailField(boolean required) {
        this(null, required, null, null);
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        Class<? extends VortexCrudFieldFactory> f = EmailFieldFactory.class;
        return (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) f;
    }

}