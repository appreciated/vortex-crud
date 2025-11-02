package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.DateFieldFactory;
import lombok.Builder;

import java.util.List;

/**
 * Thin Field type for DateFieldFactory.
 */
@Builder
public record DateField<ModelClass, FieldType, RepositoryType>(
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles,
        Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory
) implements Field<ModelClass, FieldType, RepositoryType> {

    public DateField(boolean required) {
        this(null, required, null, null, (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) DateFieldFactory.class);
    }

}