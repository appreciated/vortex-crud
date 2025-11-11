package com.github.appreciated.vortex_crud.filesystem.service.datastore;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.filesystem.service.config.FileSystemDataStore;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registry for managing FileSystemDataStore instances.
 * Allows adding custom data stores for different entity types.
 */
public class FileSystemDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<Object, String, Class<?>> {

    private final Map<Class<?>, VortexCrudDataStore<String, Object>> dataStores = new HashMap<>();
    private final Path baseStorageDirectory;

    public FileSystemDataStoreFactoryRegistry(Path baseStorageDirectory) {
        this.baseStorageDirectory = baseStorageDirectory;
    }

    /**
     * Register a new entity type with the registry.
     * This will create a FileSystemDataStore for the given model class.
     *
     * @param modelClass The entity class to register
     * @param <T>        The entity type
     * @return The created FileSystemDataStore
     */
    public <T> FileSystemDataStore<T> registerEntityType(Class<T> modelClass) {
        return registerEntityType(modelClass, new DataStoreHooks<>());
    }

    /**
     * Register a new entity type with the registry with custom hooks.
     *
     * @param modelClass The entity class to register
     * @param hooks      Custom hooks for entity lifecycle events
     * @param <T>        The entity type
     * @return The created FileSystemDataStore
     */
    @SuppressWarnings("unchecked")
    public <T> FileSystemDataStore<T> registerEntityType(Class<T> modelClass, DataStoreHooks<T> hooks) {
        FileSystemDataStore<T> dataStore = new FileSystemDataStore<>(modelClass, baseStorageDirectory, hooks);
        dataStores.put(modelClass, (VortexCrudDataStore<String, Object>) dataStore);
        return dataStore;
    }

    @Override
    @SuppressWarnings("unchecked")
    public VortexCrudDataStore<String, Object> getDataStore(Class<?> modelClass) {
        return Optional.ofNullable(dataStores.get(modelClass))
                .orElseThrow(() -> new IllegalStateException(
                        "No FileSystemDataStore registered for model class: " + modelClass.getName() +
                        ". Please register it using registerEntityType() first."));
    }

    @Override
    public void addFactory(Class<?> modelClass, VortexCrudDataStore<String, Object> factory) {
        dataStores.put(modelClass, factory);
    }

    /**
     * Get the base storage directory for all data stores.
     *
     * @return The base storage directory path
     */
    public Path getBaseStorageDirectory() {
        return baseStorageDirectory;
    }

    /**
     * Get all registered model classes.
     *
     * @return Set of registered model classes
     */
    public java.util.Set<Class<?>> getRegisteredModelClasses() {
        return dataStores.keySet();
    }
}
