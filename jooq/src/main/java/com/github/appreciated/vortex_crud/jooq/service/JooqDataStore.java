package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreHooks;
import com.github.appreciated.vortex_crud.core.config.model.RouteFilter;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import jakarta.validation.constraints.NotNull;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.Collections;
import java.util.List;

/**
 * Service for managing dynamic entities using an EntityManager.
 * Provides methods for CRUD operations and lazy loading data from the database.
 */
public class JooqDataStore<ModelClass extends UpdatableRecord<?>> implements VortexCrudDataStore<TableField<?, ?>, ModelClass> {

    private final DSLContext dslContext;
    private final Class<ModelClass> record;
    private final JooqVortexCrudDataStoreUtilStrategy utilStrategy;
    private final DataStoreHooks<ModelClass> hooks;

    public JooqDataStore(Class<ModelClass> record, DSLContext dslContext, DataStoreHooks<ModelClass> hooks) {
        this.dslContext = dslContext;
        if (record == null) {
            throw new IllegalArgumentException("Table name cannot be null");
        }
        this.record = record;
        assert hooks != null;
        this.hooks = hooks;
        utilStrategy = new JooqVortexCrudDataStoreUtilStrategy();
    }

    public Class<ModelClass> getModelClass() {
        return record;
    }

    @Override
    public void updateRecord(ModelClass entity) {
        updateRecordById(entity);
    }

    @Override
    public void deleteRecord(ModelClass entity) {
        hooks.beforeDeletes().forEach(hook -> hook.execute(entity));
        deleteRecordById(utilStrategy.getId(entity));
        hooks.afterDeletes().forEach(hook -> hook.execute(entity));
    }

    @Override
    public ModelClass newInstance() {
        try {
            return record.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new instance of " + record.getName(), e);
        }
    }

    @Override
    public Object insertRecord(ModelClass entity) {
        // Execute before hooks
        hooks.beforeCreates().forEach(hook -> hook.execute(entity));
        ModelClass dst = dslContext.newRecord((Table<ModelClass>) getTable());
        dst.from(entity);
        dst.store();

        // Execute after hooks
        hooks.afterCreates().forEach(hook -> hook.execute(entity));

        return utilStrategy.getId(dst);
    }

    @Override
    public List<ModelClass> getRecordsFromTable(int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ModelClass> getRecordsFromTableWhereColumnEquals(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        if (filterValue == null) {
            return Collections.emptyList();
        }
        return dslContext.select()
                .from(getTable())
                .where(((Field<Object>) filterField).eq(filterValue))
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ModelClass> getRecordsFromTableWhereColumnEqualsOrdered(TableField<?, ?> filterField,
                                                                        Object filterValue,
                                                                        TableField<?, ?> orderField,
                                                                        int offset,
                                                                        int limit) {
        if (filterValue == null) {
            return Collections.emptyList();
        }
        return dslContext.select()
                .from(getTable())
                .where(((Field<Object>) filterField).eq(filterValue))
                .orderBy(orderField.asc())
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnIn(TableField<?, ?> filterField, List<String> filterValues, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.in(filterValues))
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    public List<ModelClass> getRecordsFromTableWhereColumnLike(TableField<?, ?> filterField, Object filterValue, int offset, int limit) {
        return dslContext.select()
                .from(getTable())
                .where(filterField.like("%" + filterValue + "%"))
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ModelClass getRecordById(Object id) {
        return dslContext.select()
                .from(getTable())
                .where(((Field<Object>) getPrimaryKeyField()).eq(id))
                .fetchOptionalInto(record)
                .orElse(null);
    }

    @Override
    public void updateRecordById(ModelClass entity) {
        hooks.beforeUpdates().forEach(hook -> hook.execute(entity));
        dslContext.executeUpdate(entity);
        hooks.afterUpdates().forEach(hook -> hook.execute(entity));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deleteRecordById(Object id) {
        // Fetch the entity before deletion for hooks
        ModelClass entity = getRecordById(id);
        if (entity != null) {
            // Execute before delete hooks
            hooks.beforeDeletes().forEach(hook -> hook.execute(entity));
            dslContext.deleteFrom(getTable())
                    .where(((Field<Object>) getPrimaryKeyField()).eq(id))
                    .execute();

            // Execute after delete hooks
            hooks.afterDeletes().forEach(hook -> hook.execute(entity));
        }
    }

    @Override
    public int count() {
        return dslContext.fetchCount(getTable());
    }

    @Override
    public int countWhereColumnLike(TableField<?, ?> filterField, String filterValue) {
        return dslContext.fetchCount(
                getTable(),
                DSL.field(filterField).like("%" + filterValue + "%")
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public int countWhereFiltersEqual(java.util.List<RouteFilter<TableField<?, ?>>> filters) {
        org.jooq.Condition condition = DSL.trueCondition();
        if (filters != null) {
            for (RouteFilter<TableField<?, ?>> filter : filters) {
                condition = condition.and(((Field<Object>) filter.field()).eq(filter.value()));
            }
        }
        return dslContext.fetchCount(getTable(), condition);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ModelClass> getRecordsFromTableWhereFiltersEqual(java.util.List<RouteFilter<TableField<?, ?>>> filters, int offset, int limit) {
        org.jooq.Condition condition = DSL.trueCondition();
        if (filters != null) {
            for (RouteFilter<TableField<?, ?>> filter : filters) {
                condition = condition.and(((Field<Object>) filter.field()).eq(filter.value()));
            }
        }
        return dslContext.select()
                .from(getTable())
                .where(condition)
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ModelClass> getRecordsFromTableWhereColumnLikeAndFiltersEqual(TableField<?, ?> searchField, Object searchValue, java.util.List<RouteFilter<TableField<?, ?>>> filters, int offset, int limit) {
        org.jooq.Condition condition = DSL.field(searchField).like("%" + searchValue + "%");
        if (filters != null) {
            for (RouteFilter<TableField<?, ?>> filter : filters) {
                condition = condition.and(((Field<Object>) filter.field()).eq(filter.value()));
            }
        }
        return dslContext.select()
                .from(getTable())
                .where(condition)
                .limit(limit)
                .offset(offset)
                .fetchInto(record)
                .stream()
                .toList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int countWhereColumnLikeAndFiltersEqual(TableField<?, ?> searchField, String searchValue, java.util.List<RouteFilter<TableField<?, ?>>> filters) {
        org.jooq.Condition condition = DSL.field(searchField).like("%" + searchValue + "%");
        if (filters != null) {
            for (RouteFilter<TableField<?, ?>> filter : filters) {
                condition = condition.and(((Field<Object>) filter.field()).eq(filter.value()));
            }
        }
        return dslContext.fetchCount(
                getTable(),
                condition
        );
    }

    @NotNull
    private Table<?> getTable() {
        ModelClass modelInstance = newInstance();
        return modelInstance.getTable();
    }

    private TableField<?, ?> getPrimaryKeyField() {
        Table<?> table = getTable();
        UniqueKey<?> pk = table.getPrimaryKey();
        if (pk == null || pk.getFields().isEmpty()) {
            throw new IllegalStateException("Table " + table.getName() + " does not have a primary key defined.");
        }
        return pk.getFields().get(0);
    }

}
