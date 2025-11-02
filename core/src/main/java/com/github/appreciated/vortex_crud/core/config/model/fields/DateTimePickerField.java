package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.DateTimePickerFactory;
import lombok.Builder;

import java.util.List;

/**
 * Thin Field type for DateTimePickerFactory.
 */
@Builder
public record DateTimePickerField<ModelClass, FieldType, RepositoryType>(
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles,
        Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory
) implements Field<ModelClass, FieldType, RepositoryType> {

    public DateTimePickerField(boolean required) {
        this(null, required, null, null, (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) DateTimePickerFactory.class);
    }

}