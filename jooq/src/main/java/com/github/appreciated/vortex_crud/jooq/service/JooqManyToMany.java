package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionData;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.jooq.Table;
import org.jooq.TableField;

import java.util.List;

public class JooqManyToMany implements ManyToMany<Table<?>, TableField<?, ?>> {

    private final Table<?> sourceDataStore;
    private final TableField<?, ?> associativeSourceIdField;
    private final TableField<?, ?> associativeTargetIdField;
    private final TableField<?, ?> dataStoreField;

    public JooqManyToMany(Table<?> sourceDataStore, TableField<?, ?> associativeSourceIdField, TableField<?, ?> associativeTargetIdField, TableField<?, ?> dataStoreField) {
        this.sourceDataStore = sourceDataStore;
        this.associativeSourceIdField = associativeSourceIdField;
        this.associativeTargetIdField = associativeTargetIdField;
        this.dataStoreField = dataStoreField;
    }

    @Override
    public List<GenericEntity> getData(VortexCrudDataStoreFactoryRegistry<Table<?>, TableField<?, ?>> dataStoreFactoryRegistry, String foreignKeyValue, VortexCrudDataStore<TableField<?, ?>> targetDataStore, CollectionData<Table<?>, TableField<?, ?>> collectionData) {
        // If we need to resolve a many-to-many relation, it is necessary to do two selects one over the associative
        // datastore and one over the target datastore and one with the actual entries.
        // This could be improved upon, if it was allowed to provide a custom datastore / interface for the sake
        // of resolving the following data.
        VortexCrudDataStore<TableField<?, ?>> associativeDataStore = dataStoreFactoryRegistry.getFactory(sourceDataStore);
        List<GenericEntity> associativeRecords = associativeDataStore.getRecordsFromTableWhereColumnEquals(associativeSourceIdField, foreignKeyValue, 0, Integer.MAX_VALUE);
        List<String> associativeRecordIds = associativeRecords.stream().map(genericEntity -> genericEntity.get(associativeTargetIdField.getName())).map(Object::toString).toList();
        return foreignKeyValue == null ? List.of() :
                targetDataStore.getRecordsFromTableWhereColumnIn(dataStoreField, associativeRecordIds, 0, Integer.MAX_VALUE);
    }

    @Override
    public TableField<?, ?> getReferenceField(CollectionData<Table<?>, TableField<?, ?>> collectionData) {
        return dataStoreField;
    }

    @Override
    public Table<?> getAssociativeDataStore() {
        return sourceDataStore;
    }

    @Override
    public TableField<?, ?> getAssociativeTargetIdField() {
        return associativeTargetIdField;
    }

}
