package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class JooqDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final HashMap<TableImpl<?>, VortexCrudDataStore<TableField<?, ?>, ?>> factories = new HashMap<>();

    public JooqDataStoreFactoryRegistry(VortexCrudConfigService<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configService, DSLContext dslContext) {
        for (Map.Entry<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> entry : configService.getConfiguration().getDataStores().entrySet()) {
            TableImpl<?> table = entry.getKey();
            Class<?> recordType = table.getRecordType();
            factories.put(table, new JooqDataStore(recordType, dslContext));
        }
    }

    @Override
    public VortexCrudDataStore<TableField<?, ?>, ?> getDataStore(TableImpl<?> table) {
        return Optional.ofNullable(factories.get(table))
                .orElseThrow(
                        () -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table))
                );
    }

    @Override
    public void addFactory(TableImpl<?> key, VortexCrudDataStore<TableField<?, ?>, ?> factory) {
        factories.put(key, factory);
    }
}
