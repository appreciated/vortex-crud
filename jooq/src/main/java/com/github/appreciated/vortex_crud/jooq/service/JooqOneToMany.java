package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.CollectionConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.OneToMany;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import org.jooq.TableField;
import org.jooq.TableRecord;

import java.util.List;

public class JooqOneToMany implements OneToMany<Class<? extends TableRecord<?>>, TableField<?, ?>> {

    private final TableField<?, ?> referenceField;

    public JooqOneToMany(TableField<?, ?> referenceField) {
        this.referenceField = referenceField;
    }

    @Override
    public List<GenericEntity> getData(String foreignKeyValue, VortexCrudDataStore<TableField<?, ?>> dataStore, CollectionConfiguration<Class<? extends TableRecord<?>>, TableField<?, ?>> collectionConfiguration) {
        return foreignKeyValue == null ? List.of() :
                dataStore.getRecordsFromTableWhereColumnEquals(referenceField, foreignKeyValue, 0, Integer.MAX_VALUE);
    }

    @Override
    public TableField<?, ?> getReferenceField(CollectionConfiguration<Class<? extends TableRecord<?>>, TableField<?, ?>> collectionConfiguration) {
        return referenceField;
    }

}
