package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
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
    
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final ReflectionService<String> reflectionService;
    
    public JpaManyToManyPersistenceStrategy(VortexCrudDataStoreUtilStrategy dataStoreUtil, ReflectionService<String> reflectionService) {
        this.dataStoreUtil = dataStoreUtil;
        this.reflectionService = reflectionService;
    }
    
    @Override
    public List<DataStoreId> resolveManyToMany(VortexCrudDataStore<String, ?> targetDataStore, ManyToMany<DataStoreId, String, JpaRepository<?, ?>> manyToMany, Object sourceId) {
        // Implementation would use JPA and reflection to resolve many-to-many relationships
        return List.of();
    }

    @Override
    public void insert(Object sourceId, List<Object> targetObjects, ManyToMany<DataStoreId, String, JpaRepository<?, ?>> manyToMany) {
        // Implementation would use JPA and reflection to insert many-to-many relationships
    }

    @Override
    public void deleteAll(Object sourceId, List<Object> targetObjects, ManyToMany<DataStoreId, String, JpaRepository<?, ?>> manyToMany) {
        // Implementation would use JPA and reflection to delete many-to-many relationships
    }
    
    @Override
    public String getObjectId(Object object) {
        return dataStoreUtil.getId(object);
    }
}
