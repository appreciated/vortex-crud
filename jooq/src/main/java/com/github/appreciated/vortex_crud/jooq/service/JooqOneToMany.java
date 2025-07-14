package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.OneToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import org.jooq.TableField;
import org.jooq.TableRecord;

import java.util.List;

public class JooqOneToMany implements OneToMany<TableRecord<?>, TableField<?, ?>> {

    private final TableField<?, ?> referenceField;

    public JooqOneToMany(TableField<?, ?> referenceField) {
        this.referenceField = referenceField;
    }

    @Override
    public List<TableRecord<?>> getData(String foreignKeyValue, VortexCrudDataStore<TableField<?, ?>, ?> dataStore, CollectionConfiguration<TableRecord<?>, TableField<?, ?>> collectionConfiguration) {
        return foreignKeyValue == null ? List.of() :
                (List<TableRecord<?>>) dataStore.getRecordsFromTableWhereColumnEquals(referenceField, foreignKeyValue, 0, Integer.MAX_VALUE);
    }

    @Override
    public TableField<?, ?> getReferenceField(CollectionConfiguration<TableRecord<?>, TableField<?, ?>> collectionConfiguration) {
        return referenceField;
    }

}
