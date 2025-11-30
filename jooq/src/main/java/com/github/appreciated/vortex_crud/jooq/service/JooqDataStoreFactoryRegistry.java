package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JooqDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final HashMap<TableImpl<?>, VortexCrudDataStore<TableField<?, ?>, TableRecord<?>>> factories = new HashMap<>();

    public JooqDataStoreFactoryRegistry() {
    }

    @Override
    public VortexCrudDataStore<TableField<?, ?>, TableRecord<?>> getDataStore(TableImpl<?> table) {
        return Optional.ofNullable(factories.get(table))
                .orElseThrow(() -> new IllegalStateException("Cannot provide factory for key " + table));
    }

    @Override
    public void addFactory(TableImpl<?> key, VortexCrudDataStore<TableField<?, ?>, TableRecord<?>> factory) {
        factories.put(key, factory);
    }
}
