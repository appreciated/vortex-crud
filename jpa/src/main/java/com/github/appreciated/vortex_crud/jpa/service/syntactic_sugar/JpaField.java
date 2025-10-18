package com.github.appreciated.vortex_crud.jpa.service.syntactic_sugar;

import com.github.appreciated.vortex_crud.core.config.model.Field;
import com.github.appreciated.vortex_crud.core.config.model.Validation;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaField implements Field<JpaRepository<?, ?>, String, JpaRepository<?, ?>> {

    private Class<? extends VortexCrudFieldFactory> factory;
    private Validation validation;
    private boolean required = false;

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
}
