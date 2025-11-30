package com.github.appreciated.vortex_crud.jooq.service;

import com.github.appreciated.vortex_crud.core.config.model.*;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.ManyToManyPersistenceStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudForeignKeyResolutionStrategy;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.ListCollectionFactory;
import com.github.appreciated.vortex_crud.jooq.service.JooqDataStore;
import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.TableRecord;
import org.jooq.impl.TableImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JooqVortexCrudConfigService implements VortexCrudConfigService<TableRecord<?>, TableField<?, ?>, TableImpl<?>> {

    private final Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configuration;
    private final ListCollectionFactory<TableRecord<?>, TableField<?, ?>, TableImpl<?>> listCollectionFactory;

    public JooqVortexCrudConfigService(
            VortexCrudConfigurationProvider<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configurationProvider,
            DSLContext dslContext
    ) {
        // 1. Build initial configuration
        this.configuration = configurationProvider.get();
        Selects selects = this.configuration.selects();

        // 2. Create DataStores
        for (Map.Entry<TableImpl<?>, DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>>> entry : configuration.dataStores().entrySet()) {
            TableImpl<?> table = entry.getKey();
            DataStoreConfig<TableRecord<?>, TableField<?, ?>, TableImpl<?>> config = entry.getValue();
            Class<?> recordType = table.getRecordType();

            VortexCrudDataStore<TableField<?, ?>, TableRecord<?>> dataStore = new JooqDataStore(recordType, dslContext, config.hooks());

            // Set dataStoreInstance
            setField(config, "dataStoreInstance", dataStore);
        }

        // Collection Factory
        this.listCollectionFactory = new ListCollectionFactory<>();
    }

    @Override
    public Application<TableRecord<?>, TableField<?, ?>, TableImpl<?>> configuration() {
        return configuration;
    }

    @Override
    public String applicationName() {
        return configuration.applicationName();
    }

    private void setField(Object target, String fieldName, Object value) {
        if (target == null) return;
        Class<?> clazz = target.getClass();
        while (clazz != null) {
            try {
                java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
