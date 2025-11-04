package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.ManyToMany;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

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
    public TableField<?, ?> referenceField(CollectionConfiguration<RecordType, TableField<?, ?>, DataStoreId> collectionConfiguration) {
        return dataStoreField;
    }

    @Override
    public RecordType associativeDataStoreKey() {
        return null;
    }

    @Override
    public TableField<?, ?> associativeTargetIdField() {
        return associativeTargetIdField;
    }

    @Override
    public TableField<?, ?> associativeSourceIdField() {
        return associativeSourceIdField;
    }

    @Override
    public DataStoreId datastore() {
        return dataStoreId;
    }

}