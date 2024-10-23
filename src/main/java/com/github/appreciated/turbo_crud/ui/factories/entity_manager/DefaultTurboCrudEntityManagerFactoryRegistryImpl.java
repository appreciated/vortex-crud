package com.github.appreciated.turbo_crud.ui.factories.entity_manager;

import com.github.appreciated.turbo_crud.config.model.RepositoryConfig;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory implementation for creating entity item renderers.
 * It initializes and provides the appropriate renderer based on the ItemRendererConfig configuration.
 */

@Service
public class DefaultTurboCrudEntityManagerFactoryRegistryImpl implements TurboCrudEntityManagerFactoryRegistry {

    private final TurboCrudConfigService turboCrudConfigService;
    HashMap<String, TurboCrudEntityManagerService> factories = new HashMap<>();

    public DefaultTurboCrudEntityManagerFactoryRegistryImpl(TurboCrudConfigService turboCrudConfigService, EntityManager entityManager, TransactionTemplate transactionTemplate) {
        this.turboCrudConfigService = turboCrudConfigService;
        for (Map.Entry<String, RepositoryConfig> entry : this.turboCrudConfigService.getConfiguration().getRepositoriesConfig().entrySet()) {
            String table = entry.getKey();
            factories.put(table, new DefaultJpaEntityManagerService(table, entityManager, transactionTemplate));
        }
        factories.put("users", new DefaultJpaEntityManagerService("users", entityManager, transactionTemplate));
        factories.put("roles", new DefaultJpaEntityManagerService("roles", entityManager, transactionTemplate));
        factories.put("user_roles", new DefaultJpaEntityManagerService("user_roles", entityManager, transactionTemplate));
        factories.put("audit_log", new DefaultJpaEntityManagerService("audit_log", entityManager, transactionTemplate));
    }

    public TurboCrudEntityManagerService getFactory(String table) {
        return factories.get(table);
    }

    @Override
    public void addFactory(String key, TurboCrudEntityManagerService factory) {
        factories.put(key, factory);
    }
}