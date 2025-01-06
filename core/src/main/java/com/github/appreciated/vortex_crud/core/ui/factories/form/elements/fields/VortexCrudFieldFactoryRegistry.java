package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudFieldFactoryRegistry<DataStoreId, FieldId> {

    VortexCrudFieldFactory<DataStoreId, FieldId> getFactory(Class<? extends VortexCrudFieldFactory> type);

    void addFactory(Class<? extends VortexCrudFieldFactory> key, VortexCrudFieldFactory<DataStoreId, FieldId> factory);
}