package com.github.appreciated.vortex_crud.example.jooq;

import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.jooq.models.tables.Users;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

@Service
public class DefaultJooqDataStoreFactoryRegistryInit {

    public DefaultJooqDataStoreFactoryRegistryInit(VortexCrudDataStoreFactoryRegistry<Table<?>, TableField<?,?>> registry, DSLContext dslContext) {
      registry.addFactory(Users.USERS, new JooqDataStore(Users.USERS, dslContext));
    }
}