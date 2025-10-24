package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudFieldFactoryRegistry<ModelClass, FieldType, RepositoryType> {

    VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> getFactory(Class<? extends VortexCrudFieldFactory> type);

    void addFactory(Class<? extends VortexCrudFieldFactory> key, VortexCrudFieldFactory<ModelClass, FieldType, RepositoryType> factory);
}