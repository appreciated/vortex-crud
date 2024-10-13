package com.github.appreciated.flow_cms.ui.factories.route;

import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.TurboCrudConfigService;
import com.github.appreciated.flow_cms.service.TurboCrudEntityManagerService;
import com.github.appreciated.flow_cms.ui.factories.detail.TurboCrudDetailFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.flow_cms.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.route.grid.DefaultGridRouteFactoryImpl;
import com.github.appreciated.flow_cms.ui.factories.route.list.DefaultListRouteFactoryImpl;
import com.github.appreciated.flow_cms.ui.factories.route.list.TurboCrudListColumnCallbackRegistry;
import com.github.appreciated.flow_cms.ui.factories.route.master_detail.DefaultMasterDetailRouteFactoryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the TurboCrudRouteRendererFactory interface.
 * This factory provides different route renderers such as Master-Detail, Grid, and Item views
 * based on the RouteConfig configuration.
 */

@Service
public class DefaultRouteFactoryRegistryImpl implements TurboCrudRouteFactoryRegistry {

    HashMap<String, TurboCrudRouteFactory> factories = new HashMap<>();

    public DefaultRouteFactoryRegistryImpl(TurboCrudEntityManagerService dynamicEntityManager,
                                           TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                           TurboCrudDetailFactoryRegistry detailFactoryRegistry,
                                           TurboCrudConfigService configService,
                                           TurboCrudListColumnCallbackRegistry listColumnCallbackRegistry,
                                           TurboCrudIconFactory iconFactory
    ) {
        factories.put("master-detail", (i, route, config, entityManagerService) -> new DefaultMasterDetailRouteFactoryImpl(i, config, dynamicEntityManager, itemFactoryRegistry, detailFactoryRegistry, iconFactory));
        factories.put("list", (i, route, config, entityManagerService) -> new DefaultListRouteFactoryImpl(i, config, route, dynamicEntityManager, configService, listColumnCallbackRegistry, iconFactory));
        factories.put("grid", (i, route, config, entityManagerService) -> new DefaultGridRouteFactoryImpl(i, config, route, dynamicEntityManager, itemFactoryRegistry, iconFactory));
    }

    public TurboCrudRouteFactory getFactory(RouteConfig routeConfig) {
        return factories.get(routeConfig.getFactory());
    }

    @Override
    public void addFactory(String key, TurboCrudRouteFactory factory) {
        factories.put(key, factory);
    }
}
