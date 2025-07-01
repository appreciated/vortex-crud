package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JPA implementation of the RecordRetrievalStrategy.
 */
@Component
public class JpaManyToManyPersistenceStrategy implements ManyToManyPersistenceStrategy<String, String> {

    @Override
    public List<GenericEntity> getManyToMany(VortexCrudDataStore<String> dataStore, ManyToMany<String, String> manyToMany) {
        return List.of();
    }

    @Override
    public void insert(List<GenericEntity> genericEntities) {

    }

    @Override
    public void deleteAll(List<GenericEntity> list) {

    }
}