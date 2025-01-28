package com.github.appreciated.vortex_crud.core.file_provider;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudFileProviderRegistry {

    VortexCrudResourceProvider getFactory(Class<? extends VortexCrudResourceProvider> type);

    void addFactory(Class<? extends VortexCrudResourceProvider> key, VortexCrudResourceProvider factory);

}