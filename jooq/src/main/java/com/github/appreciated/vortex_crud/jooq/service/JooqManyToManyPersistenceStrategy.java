package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.jooq.TableField;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * jOOQ implementation of the RecordRetrievalStrategy.
 * Uses jOOQ's DSL to create a query with a where clause.
 */
@Component
public class JooqManyToManyPersistenceStrategy implements ManyToManyPersistenceStrategy<TableField<?, ?>> {

    @Override
    public List<GenericEntity> getManyToMany(
            VortexCrudDataStore<TableField<?, ?>> dataStore,
            TableField<?, ?> filterField,
            Object filterValue,
            int offset,
            int limit
    ) {
        if (!(dataStore instanceof JooqDataStore jooqDataStore)) {
            throw new IllegalArgumentException("DataStore must be a JooqDataStore");
        }
        
        if (filterValue == null) {
            return Collections.emptyList();
        }
        
        // Delegate to the existing implementation in JooqDataStore
        return jooqDataStore.getRecordsFromTableWhereColumnEquals(filterField, filterValue, offset, limit);
    }
}