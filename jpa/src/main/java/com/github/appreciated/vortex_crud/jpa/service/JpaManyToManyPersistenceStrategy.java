package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudQueryDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudQueryDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
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

    private final VortexCrudQueryDataStoreUtilStrategy dataStoreUtil;
    private final ReflectionService<String> reflectionService;

    public JpaManyToManyPersistenceStrategy(VortexCrudQueryDataStoreUtilStrategy dataStoreUtil, ReflectionService<String> reflectionService) {
        this.dataStoreUtil = dataStoreUtil;
        this.reflectionService = reflectionService;
    }

    @Override
    public java.util.Collection<DataStoreId> resolveManyToMany(VortexCrudQueryDataStore<String, ?> targetDataStore, ManyToMany<DataStoreId, String, JpaRepository<?, ?>> manyToMany, Object sourceId) {
        if (sourceId == null) {
            return List.of();
        }

        // Get the entity by ID using reflection
        Object entity = targetDataStore.getRecordById(sourceId);
        if (entity == null) {
            return List.of();
        }

        // Get the collection field value
        java.util.Collection<DataStoreId> collection = (java.util.Collection<DataStoreId>) reflectionService.getValue(entity, manyToMany.referenceField(null));

        // Return empty list if collection is null (can happen with JPA lazy loading or uninitialized fields)
        if (collection == null) {
            return List.of();
        }

        return collection.stream().toList();
    }

    @Override
    public void insert(Object sourceId, List<Object> targetObjects, ManyToMany<DataStoreId, String, JpaRepository<?, ?>> manyToMany) {
        if (sourceId == null || targetObjects == null || targetObjects.isEmpty()) {
            return;
        }

        JpaRepository<Object, Object> repository = (JpaRepository<Object, Object>) manyToMany.datastore();
        Object sourceEntity = repository.findById(sourceId).orElse(null);
        if (sourceEntity == null) {
            throw new RuntimeException("Source entity not found");
        }
        reflectionService.addAll(sourceEntity, manyToMany.referenceField(null), targetObjects);
        repository.save(sourceEntity);
    }

    @Override
    public void deleteAll(Object sourceId, List<Object> targetObjects, ManyToMany<DataStoreId, String, JpaRepository<?, ?>> manyToMany) {
        if (sourceId == null || targetObjects == null || targetObjects.isEmpty()) {
            return;
        }

        JpaRepository<Object, Object> repository = (JpaRepository<Object, Object>) manyToMany.datastore();
        Object sourceEntity = repository.findById(sourceId).orElse(null);
        if (sourceEntity == null) {
            throw new RuntimeException("Source entity not found");
        }
        reflectionService.removeAll(sourceEntity, manyToMany.referenceField(null), targetObjects);
        repository.save(sourceEntity);
    }

    @Override
    public String getObjectId(Object object) {
        return dataStoreUtil.getId(object);
    }
}
