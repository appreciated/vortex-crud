package com.github.appreciated.turbo_crud.ui.factories.detail;

import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.elements.fields.DefaultFieldFactoryRegistryImpl;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

/**
 * Factory implementation of {@link TurboCrudDetailFactoryRegistry} for creating entity detail renderers.
 * It initializes and provides the appropriate renderer based on the DetailRenderer configuration.
 */

@Service
public class DefaultDetailFactoryRegistryImpl implements TurboCrudDetailFactoryRegistry {

    HashMap<String, TurboCrudDetailFactory> factories = new HashMap<>();

    public DefaultDetailFactoryRegistryImpl(TurboCrudEntityManagerService entityManagerService, TurboCrudConfigService configService, FormCreator formCreator) {
        factories.put("form", new DefaultFormDetailFactoryImpl(entityManagerService, configService, formCreator));
    }

    public TurboCrudDetailFactory getFactory(String type) {
        return Optional.ofNullable(factories.get(type)).orElseThrow(() -> new IllegalStateException("%s cannot provide factory with key '%s'".formatted(DefaultFieldFactoryRegistryImpl.class.getName(), type)));
    }
}
