package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.jooq.models.tables.records.UsersRecord;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.springframework.stereotype.Service;

@Service
public class DefaultJooqDataStoreFactoryRegistryInit {

    public DefaultJooqDataStoreFactoryRegistryInit(VortexCrudDataStoreFactoryRegistry<TableRecord<?>, TableField<?, ?>> registry, DSLContext dslContext) {
        registry.addFactory(UsersRecord.class, new JooqDataStore(UsersRecord.class, dslContext));
    }
}