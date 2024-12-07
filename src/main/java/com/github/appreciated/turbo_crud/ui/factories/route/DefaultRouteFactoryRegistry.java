package com.github.appreciated.turbo_crud.ui.factories.route;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.form.TCFormRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.form.TCMultiFormRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.TCGridRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.TCKanbanDetailFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.list.TCListRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.list.TurboCrudListColumnCallbackRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.master_detail.TCMasterDetailRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.submenu.TCSubmenuRouteFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the TurboCrudRouteRendererFactory interface.
 * This factory provides different route renderers such as Master-Detail, Grid, and Item views
 * based on the RouteConfig configuration.
 */

@Service
public class DefaultRouteFactoryRegistry implements TurboCrudRouteFactoryRegistry {

    HashMap<Class<?extends TurboCrudRouteFactory>, TurboCrudRouteFactory> factories = new HashMap<>();

    public DefaultRouteFactoryRegistry(TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                       TurboCrudConfigService configService,
                                       TurboCrudListColumnCallbackRegistry listColumnCallbackRegistry,
                                       TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                       TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                                       TurboCrudFileProviderRegistry fileProviderRegistry,
                                       FormCreator formCreatorService
    ) {
        factories.put(TCMasterDetailRouteFactory.class, new TCMasterDetailRouteFactory(entityManagerFactoryRegistry, itemFactoryRegistry, this, configService, fileProviderRegistry));
        factories.put(TCListRouteFactory.class, new TCListRouteFactory(entityManagerFactoryRegistry, configService, listColumnCallbackRegistry, formCreatorService, dialogFactoryRegistry, this));
        factories.put(TCGridRouteFactory.class, new TCGridRouteFactory(entityManagerFactoryRegistry, formCreatorService, dialogFactoryRegistry, this, itemFactoryRegistry, fileProviderRegistry));
        factories.put(TCFormRouteFactory.class, new TCFormRouteFactory(entityManagerFactoryRegistry, configService, formCreatorService, this));
        factories.put(TCMultiFormRouteFactory.class, new TCMultiFormRouteFactory(entityManagerFactoryRegistry, configService, formCreatorService, this));
        factories.put(TCKanbanDetailFactory.class, new TCKanbanDetailFactory(entityManagerFactoryRegistry, configService, itemFactoryRegistry, this, formCreatorService, dialogFactoryRegistry, fileProviderRegistry));
        factories.put(TCSubmenuRouteFactory.class, new TCSubmenuRouteFactory(this, configService));
    }

    public TurboCrudRouteFactory getFactory(Class<?extends TurboCrudRouteFactory> factory) {
        return factories.get(factory);
    }

    @Override
    public void addFactory(Class<?extends TurboCrudRouteFactory> key, TurboCrudRouteFactory factory) {
        factories.put(key, factory);
    }

    @Override
    public boolean isContainerRoute(Route currentRoute) {
        return factories.get(currentRoute.getFactory()).isContainerRoute();
    }
}

