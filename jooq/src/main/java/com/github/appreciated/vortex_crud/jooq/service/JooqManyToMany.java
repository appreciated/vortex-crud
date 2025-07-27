package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.List;

public class JooqManyToMany<RecordType extends TableRecord<?>, DataStoreId extends TableImpl<?>> implements ManyToMany<RecordType, TableField<?, ?>, DataStoreId> {

    private final TableField<?, ?> associativeSourceIdField;
    private final TableField<?, ?> associativeTargetIdField;
    private final TableField<?, ?> dataStoreField;
    private final DataStoreId dataStoreId;

    public JooqManyToMany(TableField<?, ?> associativeSourceIdField,
                          TableField<?, ?> associativeTargetIdField,
                          TableField<?, ?> dataStoreField,
                          DataStoreId dataStoreId) {
        this.associativeSourceIdField = associativeSourceIdField;
        this.associativeTargetIdField = associativeTargetIdField;
        this.dataStoreField = dataStoreField;
        this.dataStoreId = dataStoreId;
    }

    @Override
    public <ModelClass> List<ModelClass> getData(ReflectionService<TableField<?, ?>> reflectionService, VortexCrudDataStoreFactoryRegistry<RecordType, TableField<?, ?>, DataStoreId> dataStoreFactoryRegistry, String foreignKeyValue, VortexCrudDataStore<TableField<?, ?>, ModelClass> dataStore, CollectionConfiguration<RecordType, TableField<?, ?>, DataStoreId> collectionConfiguration) {
        // If we need to resolve a many-to-many relation, it is necessary to do two selects one over the associative
        // datastore and one over the target datastore and one with the actual entries.
        // This could be improved upon, if it was allowed to provide a custom datastore / interface for the sake
        // of resolving the following data.
        VortexCrudDataStore<TableField<?, ?>, RecordType> associativeDataStore = dataStoreFactoryRegistry.getDataStore(dataStoreId);
        List<RecordType> associativeRecords = associativeDataStore.getRecordsFromTableWhereColumnEquals(associativeSourceIdField, foreignKeyValue, 0, Integer.MAX_VALUE);
        List<String> associativeRecordIds = associativeRecords.stream().map(genericEntity -> genericEntity.get(associativeTargetIdField.getName())).map(Object::toString).toList();
        return dataStore.getRecordsFromTableWhereColumnIn(dataStoreField, associativeRecordIds, 0, Integer.MAX_VALUE);
    }

    @Override
    public TableField<?, ?> getReferenceField(CollectionConfiguration<RecordType, TableField<?, ?>, DataStoreId> collectionConfiguration) {
        return dataStoreField;
    }

    @Override
    public RecordType getAssociativeDataStoreKey() {
        return null;
    }

    @Override
    public TableField<?, ?> getAssociativeTargetIdField() {
        return associativeTargetIdField;
    }

    @Override
    public TableField<?, ?> getAssociativeSourceIdField() {
        return associativeSourceIdField;
    }

}