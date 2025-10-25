package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class JpaField implements Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private Class<? extends VortexCrudFieldFactory> factory;
    private Validation validation;
    private boolean required = false;
    private List<String> writeRoles;
    private List<String> readOnlyRoles;

    public JpaField(Class<? extends VortexCrudFieldFactory> factory) {
        this.factory = factory;
    }

    public JpaField(Class<? extends VortexCrudFieldFactory> factory, boolean required, Validation validation) {
        this(factory);
        this.validation = validation;
        this.required = required;
    }

    @Override
    public Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> getFactory() {
        return (Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>>) factory;
    }

    @Override
    public void setFactory(Class<? extends VortexCrudFieldFactory<JpaRepository<?, ?>, String, JpaRepository<?, ?>>> factory) {
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
