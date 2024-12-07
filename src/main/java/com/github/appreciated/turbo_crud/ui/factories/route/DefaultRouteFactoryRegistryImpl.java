package com.github.appreciated.turbo_crud.ui.factories.route;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.form.TCFormRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.form.TCMultiFormRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.TCGridRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.TCKanbanDetailFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.list.TCListRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.list.TurboCrudListColumnCallbackRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.master_detail.TCMasterDetailRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.submenu.TCSubmenuRouteFactoryImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the TurboCrudRouteRendererFactory interface.
 * This factory provides different route renderers such as Master-Detail, Grid, and Item views
 * based on the RouteConfig configuration.
 */

@Service
public class DefaultRouteFactoryRegistryImpl implements TurboCrudRouteFactoryRegistry {

    HashMap<Class<?extends TurboCrudRouteFactory>, TurboCrudRouteFactory> factories = new HashMap<>();

    public DefaultRouteFactoryRegistryImpl(TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                           TurboCrudConfigService configService,
                                           TurboCrudListColumnCallbackRegistry listColumnCallbackRegistry,
                                           TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                           TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                                           TurboCrudFileProviderRegistry fileProviderRegistry,
                                           FormCreator formCreatorService
    ) {
        factories.put(TCMasterDetailRouteFactoryImpl.class, new TCMasterDetailRouteFactoryImpl(entityManagerFactoryRegistry, itemFactoryRegistry, this, configService, fileProviderRegistry));
        factories.put(TCListRouteFactoryImpl.class, new TCListRouteFactoryImpl(entityManagerFactoryRegistry, configService, listColumnCallbackRegistry, formCreatorService, dialogFactoryRegistry, this));
        factories.put(TCGridRouteFactoryImpl.class, new TCGridRouteFactoryImpl(entityManagerFactoryRegistry, formCreatorService, dialogFactoryRegistry, this, itemFactoryRegistry, fileProviderRegistry));
        factories.put(TCFormRouteFactoryImpl.class, new TCFormRouteFactoryImpl(entityManagerFactoryRegistry, configService, formCreatorService, this));
        factories.put(TCMultiFormRouteFactoryImpl.class, new TCMultiFormRouteFactoryImpl(entityManagerFactoryRegistry, configService, formCreatorService, this));
        factories.put(TCKanbanDetailFactoryImpl.class, new TCKanbanDetailFactoryImpl(entityManagerFactoryRegistry, configService, itemFactoryRegistry, this, formCreatorService, dialogFactoryRegistry, fileProviderRegistry));
        factories.put(TCSubmenuRouteFactoryImpl.class, new TCSubmenuRouteFactoryImpl(this, configService));
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

