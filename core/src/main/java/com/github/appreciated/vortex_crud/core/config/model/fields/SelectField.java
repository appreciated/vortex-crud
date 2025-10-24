package com.github.appreciated.vortex_crud.core.config.model.fields;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.functions.SelectFieldFactory;

import java.util.List;

public class SelectField<ModelClass, FieldType, RepositoryType> implements Field<ModelClass, FieldType, RepositoryType> {
    private final String values;
    private Class<? extends VortexCrudFieldFactory> factory;
    private Validation validation;
    private boolean required = false;
    private List<String> writeRoles;
    private List<String> readOnlyRoles;

    public SelectField(String values) {
        this.values = values;
        this.factory = SelectFieldFactory.class;
    }

    public SelectField(String values, boolean required, Validation validation) {
        this(values);
        this.validation = validation;
        this.required = required;
    }

    public String getValues() {
        return values;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory() {
        return (Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>>) factory;
    }

    @Override
    public void setFactory(Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory) {
        this.factory = factory;
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
    public void setWriteRoles(List<String> writeRoles) {
        this.writeRoles = writeRoles;
    }

    @Override
    public List<String> getWriteRoles() {
        return writeRoles;
    }

    @Override
    public void setReadOnlyRoles(List<String> readOnlyRoles) {
        this.readOnlyRoles = readOnlyRoles;
    }

    @Override
    public List<String> getReadOnlyRoles() {
        return readOnlyRoles;
    }
}