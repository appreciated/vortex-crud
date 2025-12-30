package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.config.visitor.ConfigurationVisitor;
import com.github.appreciated.vortex_crud.core.config.visitor.Visitable;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;
import com.vaadin.flow.data.binder.Validator;

import java.util.List;

public interface Field<ModelClass, FieldType, RepositoryType> extends AccessControlled, Visitable {
    VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory();

    /**
     * Get the validators to apply to this field.
     * Users can now directly pass Vaadin Flow binder validators.
     *
     * @return list of validators, or null if no validation needed
     */
    List<Validator<?>> validators();

    boolean required();

    @Override
    default void accept(ConfigurationVisitor visitor) {
        visitor.visit(this);
    }
}
