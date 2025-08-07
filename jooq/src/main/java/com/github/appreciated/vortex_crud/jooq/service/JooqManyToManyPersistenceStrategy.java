package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<TableRecord<?>> resolveManyToMany(VortexCrudDataStore<TableField<?, ?>, ?> targetDataStore, ManyToMany<TableRecord<?>, TableField<?, ?>, TableImpl<?>> manyToMany, Object sourceId) {
        TableField sourceIdField = manyToMany.getAssociativeSourceIdField();
        TableField targetIdField = manyToMany.getAssociativeTargetIdField();
        TableField dataStoreField = manyToMany.getReferenceField(null);

        TableImpl junctionTable = (TableImpl<?>) sourceIdField.getTable();
        TableImpl targetTable = (TableImpl<?>) dataStoreField.getTable();

        Class recordType = targetTable.getRecordType();
        SelectConditionStep<Record> where = dslContext.select()
                .from(junctionTable)
                .join(targetTable)
                .on(targetIdField.eq(dataStoreField))
                .where(sourceIdField.eq(sourceId));
        String sql = where.getSQL();
        @NotNull Map<String, Param<?>> sql1 = where.getParams();
        List list = where
                .fetchInto(recordType);
        return list;
    }

    @Override
    public <E> void insert(List<E> entities, Class<E> modelClass) {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        // Get the junction table and ID fields
        // In a real implementation, we would need to determine these based on the modelClass
        // For now, we'll use placeholder values

        // Use bulk insert instead of looping through each entity
        // Extract source and target IDs using VortexCrudDataStoreUtilStrategy
        List<Object[]> batchValues = entities.stream()
                .map(entity -> {
                    // Get source and target IDs using dataStoreUtil
                    String sourceId = dataStoreUtil.getId(entity);
                    // For target ID, we need to use reflection or other means
                    // This is a placeholder - in a real implementation, we would extract the target ID properly
                    String targetId = "placeholder"; // This should be replaced with actual target ID extraction

                    return new Object[]{sourceId, targetId};
                })
                .filter(values -> values[0] != null && values[1] != null)
                .toList();

        // If we have values to insert, do a bulk insert
        if (!batchValues.isEmpty()) {
            // In a real implementation, we would use the actual junction table and field names
            // Use multi-row insert for better performance
            org.jooq.InsertValuesStep2 query = dslContext.insertInto(DSL.table("junction_table"),
                    DSL.field("source_id"), DSL.field("target_id"));

            for (Object[] values : batchValues) {
                query = query.values(values[0], values[1]);
            }

            query.execute();
        }
    }

    @Override
    public <E> void deleteAll(List<E> entities, Class<E> modelClass) {
        if (entities == null || entities.isEmpty()) {
            return;
        }

        // Get the junction table and ID fields
        // In a real implementation, we would need to determine these based on the modelClass
        // For now, we'll use placeholder values

        // Extract IDs using VortexCrudDataStoreUtilStrategy
        List<String> entityIds = entities.stream()
                .map(dataStoreUtil::getId)
                .filter(id -> id != null)
                .collect(Collectors.toList());

        if (!entityIds.isEmpty()) {
            // Use "where in" clause for better performance
            // In a real implementation, we would use the actual junction table and field names
            dslContext.deleteFrom(DSL.table("junction_table"))
                    .where(DSL.field("source_id").in(entityIds))
                    .execute();
        }
    }
}