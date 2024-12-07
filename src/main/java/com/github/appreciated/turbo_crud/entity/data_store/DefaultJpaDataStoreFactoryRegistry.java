package com.github.appreciated.turbo_crud.entity.data_store;

import com.github.appreciated.turbo_crud.config.model.DataStore;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistry;
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
public class DefaultJpaDataStoreFactoryRegistry implements TurboCrudDataStoreFactoryRegistry {

    private final HashMap<String, TurboCrudDataStore> factories = new HashMap<>();

    public DefaultJpaDataStoreFactoryRegistry(TurboCrudConfigService turboCrudConfigService, EntityManager entityManager, TransactionTemplate transactionTemplate) {
        for (Map.Entry<String, DataStore> entry : turboCrudConfigService.getConfiguration().getRepositories().entrySet()) {
            String table = entry.getKey();
            factories.put(table, new JpaDataStore(table, entityManager, transactionTemplate));
        }
        factories.put("users", new JpaDataStore("users", entityManager, transactionTemplate));
        factories.put("roles", new JpaDataStore("roles", entityManager, transactionTemplate));
        factories.put("user_roles", new JpaDataStore("user_roles", entityManager, transactionTemplate));
        factories.put("audit_log", new JpaDataStore("audit_log", entityManager, transactionTemplate));
    }

    public TurboCrudDataStore getFactory(String table) {
        return Optional.ofNullable(factories.get(table)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistry.class.getName(), table)));
    }

    @Override
    public void addFactory(String key, TurboCrudDataStore factory) {
        factories.put(key, factory);
    }
}