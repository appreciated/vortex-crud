package com.github.appreciated.flow_cms.ui.factories.route;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsConfigService;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIconFactory;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.route.grid.DefaultGridRouteFactoryImpl;
import com.github.appreciated.flow_cms.ui.factories.route.list.DefaultListRouteFactoryImpl;
import com.github.appreciated.flow_cms.ui.factories.route.list.FlowCmsListColumnCallbackRegistry;
import com.github.appreciated.flow_cms.ui.factories.route.master_detail.DefaultMasterDetailRouteFactoryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the FlowCmsRouteRendererFactory interface.
 * This factory provides different route renderers such as Master-Detail, Grid, and Item views
 * based on the RouteConfig configuration.
 */

@Service
public class DefaultRouteFactoryRegistryImpl implements FlowCmsRouteFactoryRegistry {

    HashMap<String, FlowCmsRouteFactory> factories = new HashMap<>();

    public DefaultRouteFactoryRegistryImpl(FlowCmsEntityManagerService dynamicEntityManager,
                                           FlowCmsItemFactoryRegistry itemFactoryRegistry,
                                           FlowCmsDetailFactoryRegistry detailFactoryRegistry,
                                           FlowCmsConfigService configService,
                                           FlowCmsListColumnCallbackRegistry listColumnCallbackRegistry,
                                           FlowCmsIconFactory iconFactory
    ) {
        factories.put("master-detail", (i, route, config, entityManagerService) -> new DefaultMasterDetailRouteFactoryImpl(i, config, dynamicEntityManager, itemFactoryRegistry, detailFactoryRegistry, iconFactory));
        factories.put("list", (i, route, config, entityManagerService) -> new DefaultListRouteFactoryImpl(i, config, route, dynamicEntityManager, configService, listColumnCallbackRegistry, iconFactory));
        factories.put("grid", (i, route, config, entityManagerService) -> new DefaultGridRouteFactoryImpl(i, config, route, dynamicEntityManager, itemFactoryRegistry, iconFactory));
    }

    public FlowCmsRouteFactory getFactory(RouteConfig routeConfig) {
        return factories.get(routeConfig.getFactory());
    }

    @Override
    public void addFactory(String key, FlowCmsRouteFactory factory) {
        factories.put(key, factory);
    }
}
