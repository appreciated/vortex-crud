package com.github.appreciated.turbo_crud.ui.factories.route;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.form.DefaultFormRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.form.DefaultMultiFormRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.grid.DefaultGridRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.kanban.DefaultKanbanDetailFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.list.DefaultListRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.list.TurboCrudListColumnCallbackRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.master_detail.DefaultMasterDetailRouteFactoryImpl;
import com.github.appreciated.turbo_crud.ui.factories.route.submenu.DefaultSubmenuRouteFactoryImpl;
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
                                           TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                                           TurboCrudDialogFactoryRegistry dialogFactoryRegistry,
                                           TurboCrudFileProviderRegistry fileProviderRegistry,
                                           FormCreator formCreatorService
    ) {
        factories.put("master-detail", new DefaultMasterDetailRouteFactoryImpl(entityManagerFactoryRegistry, itemFactoryRegistry, this, iconFactory, configService, fileProviderRegistry));
        factories.put("list", new DefaultListRouteFactoryImpl(entityManagerFactoryRegistry, configService, listColumnCallbackRegistry, iconFactory, formCreatorService, dialogFactoryRegistry, this));
        factories.put("grid", new DefaultGridRouteFactoryImpl(entityManagerFactoryRegistry, formCreatorService, dialogFactoryRegistry, this, itemFactoryRegistry, iconFactory, fileProviderRegistry));
        factories.put("form", new DefaultFormRouteFactoryImpl(entityManagerFactoryRegistry, configService, formCreatorService, this));
        factories.put("multi-form", new DefaultMultiFormRouteFactoryImpl(entityManagerFactoryRegistry, configService, formCreatorService, this));
        factories.put("kanban", new DefaultKanbanDetailFactoryImpl(entityManagerFactoryRegistry, configService, itemFactoryRegistry, this, formCreatorService, dialogFactoryRegistry, iconFactory, fileProviderRegistry));
        factories.put("submenu", new DefaultSubmenuRouteFactoryImpl(this, configService, iconFactory));
    }

    public TurboCrudRouteFactory getFactory(String factory) {
        return factories.get(factory);
    }

    @Override
    public void addFactory(String key, TurboCrudRouteFactory factory) {
        factories.put(key, factory);
    }

    @Override
    public boolean isContainerRoute(Route currentRoute) {
        return factories.get(currentRoute.getFactory()).isContainerRoute();
    }
}

