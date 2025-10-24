package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;

public interface Field<ModelClass, FieldType, RepositoryType> extends AccessControlled {
    Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> getFactory();

    void setFactory(Class<? extends VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType>> factory);

    Validation getValidation();

    boolean isRequired();

    /**
     * Minimal legacy-compatible builder to support existing code paths.
     */
    class Builder<ModelClass, FieldType, RepositoryType> {
        private final Field<ModelClass, FieldType, RepositoryType> field;

        public Builder(Field<ModelClass, FieldType, RepositoryType> field) {
            this.field = field;
        }

        public Builder<ModelClass, FieldType, RepositoryType> withConfiguration(Object configuration) {
            // No-op configuration passthrough for compatibility
            return this;
        }

        public Field<ModelClass, FieldType, RepositoryType> build() {
            return field;
        }
    }
}
