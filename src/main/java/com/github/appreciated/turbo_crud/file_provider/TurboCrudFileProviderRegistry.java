package com.github.appreciated.turbo_crud.file_provider;

/**
 * Interface for a factory that creates form components based on field configuration.
 * Implementations should provide methods for creating components that comply with
 * the specified FieldConfig.
 */

public interface TurboCrudFileProviderRegistry {

    TurboCrudFileProvider getFactory(Class<? extends TurboCrudFileProvider> type);

    void addFactory(Class<? extends TurboCrudFileProvider> key, TurboCrudFileProvider factory);

}