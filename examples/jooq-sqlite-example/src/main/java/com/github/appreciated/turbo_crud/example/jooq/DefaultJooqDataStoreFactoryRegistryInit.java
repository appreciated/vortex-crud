package com.github.appreciated.turbo_crud.example.jooq;

import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.jooq.models.tables.Users;
import com.github.appreciated.turbo_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

@Service
public class DefaultJooqDataStoreFactoryRegistryInit {

    public DefaultJooqDataStoreFactoryRegistryInit(TurboCrudDataStoreFactoryRegistry<Table<?>, TableField<?,?>> registry, DSLContext dslContext) {
      registry.addFactory(Users.USERS, new JooqDataStore(Users.USERS, dslContext));
    }
}