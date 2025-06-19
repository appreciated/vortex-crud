package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class JooqDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<Class<? extends TableRecord<?>>, TableField<?,?>> {

    private final HashMap<Class<? extends TableRecord<?>>, VortexCrudDataStore<TableField<?,?>>> factories = new HashMap<>();

    public JooqDataStoreFactoryRegistry(VortexCrudConfigService<Class<? extends TableRecord<?>>, TableField<?,?>> configService, DSLContext dslContext) {
        for (Map.Entry<Class<? extends TableRecord<?>>, DataStoreConfig<Class<? extends TableRecord<?>>, TableField<?,?>>> entry : configService.getConfiguration().getDataStores().entrySet()) {
            Class<? extends TableRecord<?>> table = entry.getKey();
            factories.put(table, new JooqDataStore(table, dslContext));
        }
    }

    public VortexCrudDataStore<TableField<?,?>> getDataStore(Class<? extends TableRecord<?>> table) {
        return Optional.ofNullable(factories.get(table)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table)));
    }

    @Override
    public void addFactory(Class<? extends TableRecord<?>> key, VortexCrudDataStore<TableField<?,?>> factory) {
        factories.put(key, factory);
    }
}