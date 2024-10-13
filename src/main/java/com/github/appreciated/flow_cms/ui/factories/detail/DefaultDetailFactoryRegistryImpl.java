package com.github.appreciated.flow_cms.ui.factories.detail;

import com.github.appreciated.flow_cms.service.TurboCrudConfigService;
import com.github.appreciated.flow_cms.service.TurboCrudEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.form.FormCreator;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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
        return factories.get(type);
    }
}
