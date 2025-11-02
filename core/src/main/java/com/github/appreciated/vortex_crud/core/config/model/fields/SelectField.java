package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.SelectFieldFactory;
import lombok.Builder;

import java.util.List;

@Builder
public record SelectField<ModelClass, FieldType, RepositoryType>(
        String values,
        Validation validation,
        boolean required,
        List<String> writeRoles,
        List<String> readOnlyRoles
) implements Field<ModelClass, FieldType, RepositoryType> {

    public SelectField(String values, boolean required, Validation validation) {
        this(values, validation, required, null, null);
    }

    public String getValues() {
        return values;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        Class<? extends VortexCrudFieldFactory> f = SelectFieldFactory.class;
        return (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) f;
    }

    @Override
    public Validation getValidation() {
        return validation;
    }

    @Override
    public boolean isRequired() {
        return required;
    }


    @Override
    public List<String> getWriteRoles() {
        return writeRoles;
    }


    @Override
    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }
}