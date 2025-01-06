package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.TableField;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultJooqDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<Table<?>, TableField<?,?>> {

    private final HashMap<Table<?>, VortexCrudDataStore<TableField<?,?>>> factories = new HashMap<>();

    public DefaultJooqDataStoreFactoryRegistry(VortexCrudConfigService<Table<?>, TableField<?,?>> vortexCrudConfigService, DSLContext dslContext) {
        for (Map.Entry<Table<?>, DataStoreConfig<Table<?>, TableField<?,?>>> entry : vortexCrudConfigService.getConfiguration().getDataStores().entrySet()) {
            Table<?> table = entry.getKey();
            factories.put(table, new JooqDataStore(table, dslContext));
        }
    }

    public VortexCrudDataStore<TableField<?,?>> getFactory(Table<?> table) {
        return Optional.ofNullable(factories.get(table)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table)));
    }

    @Override
    public void addFactory(Table<?> key, VortexCrudDataStore<TableField<?,?>> factory) {
        factories.put(key, factory);
    }
}