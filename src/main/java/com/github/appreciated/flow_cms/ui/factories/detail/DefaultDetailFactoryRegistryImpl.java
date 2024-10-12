package com.github.appreciated.flow_cms.ui.factories.detail;

import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.form.FormCreator;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Factory implementation for creating entity detail renderers.
 * It initializes and provides the appropriate renderer based on the DetailRenderer configuration.
 */

@Service
public class DefaultDetailFactoryRegistryImpl implements FlowCmsDetailFactoryRegistry {

    HashMap<String, FlowCmsDetailFactory> factories = new HashMap<>();

    public DefaultDetailFactoryRegistryImpl(FlowCmsEntityManagerService entityManagerService, FlowCmsConfigService configService, FormCreator formCreator) {
        factories.put("form", new DefaultFormDetailFactoryImpl(entityManagerService, configService, formCreator));
    }

    public FlowCmsDetailFactory getFactory(String type) {
        return factories.get(type);
    }
}
