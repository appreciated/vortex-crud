package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation of the ManyToManyPersistenceStrategy.
 * Uses reflection to work with model classes directly instead of GenericEntity.
 *
 * @param <DataStoreId> The model class type
 */
@Component
public class JpaManyToManyPersistenceStrategy<DataStoreId> implements ManyToManyPersistenceStrategy<DataStoreId, String, JpaRepository<?, ?>> {

    @Override
    public List<DataStoreId> getManyToMany(VortexCrudDataStore<String, ?> targetDataStore, ManyToMany<DataStoreId, String, JpaRepository<?, ?>> manyToMany, JpaRepository<?, ?> modelClass, Object sourceId) {
        return List.of();
    }

    @Override
    public <E> void insert(List<E> entities, Class<E> modelClass) {
        // Implementation will use reflection to work with the model class
    }

    @Override
    public <E> void deleteAll(List<E> entities, Class<E> modelClass) {
        // Implementation will use reflection to work with the model class
    }
}
