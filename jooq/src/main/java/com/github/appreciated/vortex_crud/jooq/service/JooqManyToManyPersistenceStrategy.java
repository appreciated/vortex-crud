package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.impl.TableImpl;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * jOOQ implementation of the RecordRetrievalStrategy.
 * Uses jOOQ's DSL to create a query with a where clause.
 */
@Component
public class JooqManyToManyPersistenceStrategy implements ManyToManyPersistenceStrategy<Object, TableField<?, ?>, TableImpl<?>> {

    @Override
    public List<Object> getManyToMany(VortexCrudDataStore<TableField<?, ?>, ?> dataStore, ManyToMany<Object, TableField<?, ?>, TableImpl<?>> manyToMany, TableImpl<?> modelClass) {
        return List.of();
    }

    @Override
    public void insert(List entities, Class modelClass) {

    }

    @Override
    public void deleteAll(List entities, Class modelClass) {

    }
}