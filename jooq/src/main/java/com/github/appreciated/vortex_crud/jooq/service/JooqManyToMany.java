package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.jooq.TableField;
import org.jooq.TableRecord;

import java.util.List;

public class JooqManyToMany implements ManyToMany<Class<? extends TableRecord<?>>, TableField<?, ?>> {

    private final Class<? extends TableRecord<?>> sourceDataStore;
    private final TableField<?, ?> associativeSourceIdField;
    private final TableField<?, ?> associativeTargetIdField;
    private final TableField<?, ?> dataStoreField;

    public JooqManyToMany(Class<? extends TableRecord<?>> sourceDataStore, TableField<?, ?> associativeSourceIdField, TableField<?, ?> associativeTargetIdField, TableField<?, ?> dataStoreField) {
        this.sourceDataStore = sourceDataStore;
        this.associativeSourceIdField = associativeSourceIdField;
        this.associativeTargetIdField = associativeTargetIdField;
        this.dataStoreField = dataStoreField;
    }

    @Override
    public List<GenericEntity> getData(VortexCrudDataStoreFactoryRegistry<Class<? extends TableRecord<?>>, TableField<?, ?>> dataStoreFactoryRegistry, String foreignKeyValue, VortexCrudDataStore<TableField<?, ?>> targetDataStore, CollectionConfiguration<Class<? extends TableRecord<?>>, TableField<?, ?>> collectionConfiguration) {
        // If we need to resolve a many-to-many relation, it is necessary to do two selects one over the associative
        // datastore and one over the target datastore and one with the actual entries.
        // This could be improved upon, if it was allowed to provide a custom datastore / interface for the sake
        // of resolving the following data.
        VortexCrudDataStore<TableField<?, ?>> associativeDataStore = dataStoreFactoryRegistry.getDataStore(sourceDataStore);
        List<GenericEntity> associativeRecords = associativeDataStore.getRecordsFromTableWhereColumnEquals(associativeSourceIdField, foreignKeyValue, 0, Integer.MAX_VALUE);
        List<String> associativeRecordIds = associativeRecords.stream().map(genericEntity -> genericEntity.get(associativeTargetIdField.getName())).map(Object::toString).toList();
        return foreignKeyValue == null ? List.of() :
                targetDataStore.getRecordsFromTableWhereColumnIn(dataStoreField, associativeRecordIds, 0, Integer.MAX_VALUE);
    }

    @Override
    public TableField<?, ?> getReferenceField(CollectionConfiguration<Class<? extends TableRecord<?>>, TableField<?, ?>> collectionConfiguration) {
        return dataStoreField;
    }

    @Override
    public Class<? extends TableRecord<?>> getAssociativeDataStore() {
        return sourceDataStore;
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
