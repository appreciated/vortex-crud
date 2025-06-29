package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.RecordRetrievalStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.jpa.service.datastore.JpaRepositoryDataStore;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * JPA implementation of the RecordRetrievalStrategy.
 * Works by using/modifying fields to create a query.
 */
@Component
public class JpaRecordRetrievalStrategy implements RecordRetrievalStrategy<String> {

    @Override
    public List<GenericEntity> getRecordsWhereColumnEquals(
            VortexCrudDataStore<String> dataStore,
            String filterField,
            Object filterValue,
            int offset,
            int limit
    ) {
        if (!(dataStore instanceof JpaRepositoryDataStore<?> jpaDataStore)) {
            throw new IllegalArgumentException("DataStore must be a JpaRepositoryDataStore");
        }
        
        if (filterValue == null) {
            return Collections.emptyList();
        }
        
        // Delegate to the existing implementation in JpaRepositoryDataStore
        return jpaDataStore.getRecordsFromTableWhereColumnEquals(filterField, filterValue, offset, limit);
    }
}