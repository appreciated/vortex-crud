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

    @Override
    public DataStoreId getModelClass() {
        return dataStoreId;
    }

}