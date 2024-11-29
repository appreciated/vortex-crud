package com.github.appreciated.turbo_crud.entity.manager;

import com.github.appreciated.turbo_crud.config.model.Repository;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.form.elements.fields.DefaultFieldFactoryRegistryImpl;
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
public class DefaultEntityManagerFactoryRegistryImpl implements TurboCrudEntityManagerFactoryRegistry {

    private final HashMap<String, TurboCrudEntityManager> factories = new HashMap<>();

    public DefaultEntityManagerFactoryRegistryImpl(TurboCrudConfigService turboCrudConfigService, EntityManager entityManager, TransactionTemplate transactionTemplate) {
        for (Map.Entry<String, Repository> entry : turboCrudConfigService.getConfiguration().getRepositories().entrySet()) {
            String table = entry.getKey();
            factories.put(table, new DefaultJpaEntityManager(table, entityManager, transactionTemplate));
        }
        factories.put("users", new DefaultJpaEntityManager("users", entityManager, transactionTemplate));
        factories.put("roles", new DefaultJpaEntityManager("roles", entityManager, transactionTemplate));
        factories.put("user_roles", new DefaultJpaEntityManager("user_roles", entityManager, transactionTemplate));
        factories.put("audit_log", new DefaultJpaEntityManager("audit_log", entityManager, transactionTemplate));
    }

    public TurboCrudEntityManager getFactory(String table) {
        return Optional.ofNullable(factories.get(table)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory for key '%s'".formatted(DefaultFieldFactoryRegistryImpl.class.getName(), table)));
    }

    @Override
    public void addFactory(String key, TurboCrudEntityManager factory) {
        factories.put(key, factory);
    }
}