package com.github.appreciated.vortex_crud.core.config.model;

import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.VortexCrudFieldFactory;

public interface Field<DataStoreId, FieldId, KeyType> {
    Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> getFactory();

    void setFactory(Class<? extends VortexCrudFieldFactory<DataStoreId, FieldId, KeyType>> factory);

    Validation getValidation();

    boolean isRequired();

    /**
     * Minimal legacy-compatible builder to support existing code paths.
     */
    class Builder<DataStoreId, FieldId, KeyType> {
        private final Field<DataStoreId, FieldId, KeyType> field;

        public Builder(Field<DataStoreId, FieldId, KeyType> field) {
            this.field = field;
        }

        public Builder<DataStoreId, FieldId, KeyType> withConfiguration(Object configuration) {
            // No-op configuration passthrough for compatibility
            return this;
        }

        public Field<DataStoreId, FieldId, KeyType> build() {
            return field;
        }
    }
}
