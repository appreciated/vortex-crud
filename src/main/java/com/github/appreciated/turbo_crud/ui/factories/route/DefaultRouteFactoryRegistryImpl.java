package com.github.appreciated.turbo_crud.ui.factories.route;

import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.form.DefaultFormDetailFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.DefaultGridRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.DefaultKanbanDetailFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.list.DefaultListRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.list.TurboCrudListColumnCallbackRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.master_detail.DefaultMasterDetailRouteFactoryImpl;
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

    public DefaultRouteFactoryRegistryImpl(TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                           TurboCrudConfigService configService,
                                           TurboCrudListColumnCallbackRegistry listColumnCallbackRegistry,
                                           TurboCrudIconFactory iconFactory,
                                           TurboCrudEntityManagerService entityService,
                                           FormCreator formCreatorService
    ) {
        factories.put("master-detail", (pathVariables, table, title, pathElement, isWrapped, hideHeader) -> new DefaultMasterDetailRouteFactoryImpl(pathVariables, pathElement, entityService, itemFactoryRegistry, this, iconFactory));
        factories.put("list", (pathVariables, table, title, pathElement, isWrapped, hideHeader) -> new DefaultListRouteFactoryImpl(pathVariables, pathElement, entityService, configService, listColumnCallbackRegistry, iconFactory));
        factories.put("grid", (pathVariables, table, title, pathElement, isWrapped, hideHeader) -> new DefaultGridRouteFactoryImpl(pathVariables, pathElement, entityService, itemFactoryRegistry, iconFactory));
        factories.put("form", new DefaultFormDetailFactoryImpl(entityService, configService, formCreatorService, this));
        factories.put("kanban", new DefaultKanbanDetailFactoryImpl(entityService, configService, formCreatorService));
        factories.put("multi", new DefaultKanbanDetailFactoryImpl(entityService, configService, formCreatorService));
    }

    public TurboCrudRouteFactory getFactory(String factory) {
        return factories.get(factory);
    }

    @Override
    public void addFactory(String key, TurboCrudRouteFactory factory) {
        factories.put(key, factory);
    }
}

