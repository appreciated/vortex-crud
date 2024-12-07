package com.github.appreciated.turbo_crud.ui.factories.route;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.form.TCMultiFormRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.list.TurboCrudListColumnCallbackRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.submenu.SubmenuRouteFactory;
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
        factories.put(MasterDetailRouteFactory.class, new MasterDetailRouteFactory(entityManagerFactoryRegistry, itemFactoryRegistry, this, configService, fileProviderRegistry));
        factories.put(ListRouteFactory.class, new ListRouteFactory(entityManagerFactoryRegistry, configService, listColumnCallbackRegistry, formCreatorService, dialogFactoryRegistry, this));
        factories.put(GridRouteFactory.class, new GridRouteFactory(entityManagerFactoryRegistry, formCreatorService, dialogFactoryRegistry, this, itemFactoryRegistry, fileProviderRegistry));
        factories.put(FormRouteFactory.class, new FormRouteFactory(entityManagerFactoryRegistry, configService, formCreatorService, this));
        factories.put(TCMultiFormRouteFactory.class, new TCMultiFormRouteFactory(entityManagerFactoryRegistry, configService, formCreatorService, this));
        factories.put(KanbanDetailFactory.class, new KanbanDetailFactory(entityManagerFactoryRegistry, configService, itemFactoryRegistry, this, formCreatorService, dialogFactoryRegistry, fileProviderRegistry));
        factories.put(SubmenuRouteFactory.class, new SubmenuRouteFactory(this, configService));
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

