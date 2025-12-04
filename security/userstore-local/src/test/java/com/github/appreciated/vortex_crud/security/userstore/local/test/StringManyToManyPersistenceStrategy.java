package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StringManyToManyPersistenceStrategy implements ManyToManyPersistenceStrategy<Object, String, String> {

    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final ReflectionService<String> reflectionService;

    public StringManyToManyPersistenceStrategy(VortexCrudDataStoreUtilStrategy dataStoreUtil, ReflectionService<String> reflectionService) {
        this.dataStoreUtil = dataStoreUtil;
        this.reflectionService = reflectionService;
    }

    @Override
    public java.util.Collection<Object> resolveManyToMany(VortexCrudDataStore<String, ?> targetDataStore, ManyToMany<Object, String, String> manyToMany, Object sourceId) {
        return List.of();
    }

    @Override
    public void insert(Object sourceId, List<Object> targetObjects, ManyToMany<Object, String, String> manyToMany) {
        // Stub implementation for tests
    }

    @Override
    public void deleteAll(Object sourceId, List<Object> targetObjects, ManyToMany<Object, String, String> manyToMany) {
        // Stub implementation for tests
    }

    @Override
    public String getObjectId(Object object) {
        return object != null ? object.toString() : null;
    }
}
