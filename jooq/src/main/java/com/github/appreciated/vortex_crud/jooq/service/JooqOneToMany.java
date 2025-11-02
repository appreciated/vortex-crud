package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.OneToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;

import java.util.List;

public class JooqOneToMany implements OneToMany<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final TableField<?, ?> referenceField;

    public JooqOneToMany(TableField<?, ?> referenceField) {
        this.referenceField = referenceField;
    }

    @Override
    public List<TableRecord<?>> getData(Object foreignKeyValue, VortexCrudDataStore<TableField<?, ?>, ?> dataStore, CollectionConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> collectionConfiguration) {
        return foreignKeyValue == null ? List.of() :
                (List<TableRecord<?>>) dataStore.getRecordsFromTableWhereColumnEquals(referenceField, foreignKeyValue, 0, Integer.MAX_VALUE);
    }

    @Override
    public TableField<?, ?> getReferenceField(CollectionConfiguration<TableRecord<?>, TableField<?, ?>, TableImpl<?>> collectionConfiguration) {
        return referenceField;
    }

}
