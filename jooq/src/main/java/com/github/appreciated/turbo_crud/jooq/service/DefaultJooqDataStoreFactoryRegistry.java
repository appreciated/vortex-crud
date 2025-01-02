package com.github.appreciated.turbo_crud.jooq.service;

import com.github.appreciated.turbo_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
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
public class DefaultJooqDataStoreFactoryRegistry implements TurboCrudDataStoreFactoryRegistry<Table<?>, TableField<?,?>> {

    private final HashMap<Table<?>, TurboCrudDataStore<TableField<?,?>>> factories = new HashMap<>();

    public DefaultJooqDataStoreFactoryRegistry(TurboCrudConfigService<Table<?>, TableField<?,?>> turboCrudConfigService, DSLContext dslContext) {
        for (Map.Entry<Table<?>, DataStoreConfig<Table<?>, TableField<?,?>>> entry : turboCrudConfigService.getConfiguration().getDataStores().entrySet()) {
            Table<?> table = entry.getKey();
            factories.put(table, new JooqDataStore(table, dslContext));
        }
    }

    public TurboCrudDataStore<TableField<?,?>> getFactory(Table<?> table) {
        return Optional.ofNullable(factories.get(table)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table)));
    }

    @Override
    public void addFactory(Table<?> key, TurboCrudDataStore<TableField<?,?>> factory) {
        factories.put(key, factory);
    }
}