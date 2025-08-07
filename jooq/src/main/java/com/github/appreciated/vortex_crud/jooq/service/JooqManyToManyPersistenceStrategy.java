package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * jOOQ implementation of the ManyToManyPersistenceStrategy.
 * Uses jOOQ's DSL to create and execute queries for many-to-many relationships.
 */
@Component
public class JooqManyToManyPersistenceStrategy implements ManyToManyPersistenceStrategy<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final DSLContext dslContext;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;

    public JooqManyToManyPersistenceStrategy(DSLContext dslContext, VortexCrudDataStoreUtilStrategy dataStoreUtil) {
        this.dslContext = dslContext;
        this.dataStoreUtil = dataStoreUtil;
    }

    @Override
    public List<TableRecord<?>> resolveManyToMany(VortexCrudDataStore<TableField<?, ?>, ?> targetDataStore,
                                                  ManyToMany<TableRecord<?>, TableField<?, ?>, TableImpl<?>> manyToMany,
                                                  Object sourceId) {
        TableField sourceIdField = manyToMany.getAssociativeSourceIdField();
        TableField targetIdField = manyToMany.getAssociativeTargetIdField();
        TableField dataStoreField = manyToMany.getReferenceField(null);

        TableImpl junctionTable = (TableImpl<?>) sourceIdField.getTable();
        TableImpl targetTable = (TableImpl<?>) dataStoreField.getTable();

        Class recordType = targetTable.getRecordType();
        return dslContext.select()
                .from(junctionTable)
                .join(targetTable)
                .on(targetIdField.eq(dataStoreField))
                .where(sourceIdField.eq(sourceId))
                .fetchInto(recordType);
    }

    @Override
    public void insert(Object sourceId, List<Object> targetObjects, ManyToMany<TableRecord<?>, TableField<?, ?>, TableImpl<?>> manyToMany) {
        if (targetObjects == null || targetObjects.isEmpty() || sourceId == null) {
            return;
        }

        // Extract necessary fields from the ManyToMany configuration
        TableField<?, ?> sourceIdField = manyToMany.getAssociativeSourceIdField();
        TableField<?, ?> targetIdField = manyToMany.getAssociativeTargetIdField();
        TableImpl<?> junctionTable = (TableImpl<?>) sourceIdField.getTable();

        // Insert records one by one instead of using batch
        for (Object targetObject : targetObjects) {
            Object targetId = getObjectId(targetObject);

            if (targetId != null) {
                @NotNull Row2 row = DSL.row(sourceId, targetId);
                dslContext.insertInto(junctionTable)
                        .columns(sourceIdField, targetIdField)
                        .values(row)
                        .execute();
            }
        }
    }

    @Override
    public void deleteAll(Object sourceId, List<Object> targetObjects, ManyToMany<TableRecord<?>, TableField<?, ?>, TableImpl<?>> manyToMany) {
        if (targetObjects == null || targetObjects.isEmpty() || sourceId == null) {
            return;
        }

        // Extract necessary fields from the ManyToMany configuration
        TableField sourceIdField = manyToMany.getAssociativeSourceIdField();
        TableField targetIdField = manyToMany.getAssociativeTargetIdField();
        TableImpl<?> junctionTable = (TableImpl<?>) sourceIdField.getTable();

        // Create a batch delete statement
        BatchBindStep batch = dslContext.batch(
                dslContext.delete(junctionTable)
                        .where(sourceIdField.eq(DSL.param("sourceId"))
                                .and(targetIdField.eq(DSL.param("targetId"))))
        );

        // Add each entity to the batch
        for (Object targetObject : targetObjects) {
            Object targetId = getObjectId(targetObject);

            if (targetId != null) {
                batch.bind(sourceId, targetId);
            }
        }

        batch.execute();
    }
    
    @Override
    public String getObjectId(Object object) {
        return dataStoreUtil.getId(object);
    }
}