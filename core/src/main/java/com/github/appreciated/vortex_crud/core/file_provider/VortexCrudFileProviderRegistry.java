package com.github.appreciated.vortex_crud.core.file_provider;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface VortexCrudFileProviderRegistry {

    VortexCrudFileProvider getFactory(Class<? extends VortexCrudFileProvider> type);

    void addFactory(Class<? extends VortexCrudFileProvider> key, VortexCrudFileProvider factory);

}