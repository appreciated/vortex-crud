package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;

public interface Field<ModelClass, FieldType, RepositoryType> extends AccessControlled {
    Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory();

    Validation validation();

    boolean required();
}
