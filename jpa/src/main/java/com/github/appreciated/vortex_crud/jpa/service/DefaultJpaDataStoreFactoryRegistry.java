package com.github.appreciated.vortex_crud.jpa.service;

import com.github.appreciated.vortex_crud.core.config.model.DataStoreConfig;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultJpaDataStoreFactoryRegistry implements VortexCrudDataStoreFactoryRegistry<String, String> {

    private final HashMap<String, VortexCrudDataStore> factories = new HashMap<>();

    public DefaultJpaDataStoreFactoryRegistry(VortexCrudConfigService<String, String> vortexCrudConfigService, EntityManager entityManager, TransactionTemplate transactionTemplate) {
        for (Map.Entry<String, DataStoreConfig<String, String>> entry : vortexCrudConfigService.getConfiguration().getDataStores().entrySet()) {
            String table = entry.getKey();
            factories.put(table, new JpaDataStore(table, entityManager, transactionTemplate));
        }
        factories.put("users", new JpaDataStore("users", entityManager, transactionTemplate));
        factories.put("roles", new JpaDataStore("roles", entityManager, transactionTemplate));
        factories.put("user_roles", new JpaDataStore("user_roles", entityManager, transactionTemplate));
        factories.put("audit_log", new JpaDataStore("audit_log", entityManager, transactionTemplate));
    }

    public VortexCrudDataStore<String> getFactory(String table) {
        return Optional.ofNullable(factories.get(table)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table)));
    }

    @Override
    public void addFactory(String key, VortexCrudDataStore<String> factory) {
        factories.put(key, factory);
    }
}