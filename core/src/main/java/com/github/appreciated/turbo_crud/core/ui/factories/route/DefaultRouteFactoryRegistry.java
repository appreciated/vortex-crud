package com.github.appreciated.turbo_crud.core.ui.factories.route;

import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.form.MultiFormRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.list.TurboCrudListColumnCallbackRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the TurboCrudRouteRendererFactory interface.
 * This factory provides different route renderers such as Master-Detail, Grid, and Item views
 * based on the RouteConfig configuration.
 */

@Service
public class DefaultRouteFactoryRegistry<DataStoreId, FieldId> implements TurboCrudRouteFactoryRegistry<DataStoreId, FieldId> {

    HashMap<Class<? extends TurboCrudRouteFactory>, TurboCrudRouteFactory<DataStoreId, FieldId>> factories = new HashMap<>();

    public DefaultRouteFactoryRegistry(TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                       TurboCrudConfigService<DataStoreId, FieldId> configService,
                                       TurboCrudListColumnCallbackRegistry listColumnCallbackRegistry,
                                       TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                                       TurboCrudDialogFactoryRegistry<DataStoreId, FieldId> dialogFactoryRegistry,
                                       TurboCrudFileProviderRegistry fileProviderRegistry,
                                       FormCreator formCreatorService
    ) {
        factories.put(MasterDetailRouteFactory.class, new MasterDetailRouteFactory(dataStoreFactoryRegistry, itemFactoryRegistry, this, configService, fileProviderRegistry));
        factories.put(ListRouteFactory.class, new ListRouteFactory(dataStoreFactoryRegistry, configService, listColumnCallbackRegistry, formCreatorService, dialogFactoryRegistry, this));
        factories.put(GridRouteFactory.class, new GridRouteFactory(dataStoreFactoryRegistry, formCreatorService, dialogFactoryRegistry, this, itemFactoryRegistry, fileProviderRegistry));
        factories.put(FormRouteFactory.class, new FormRouteFactory(dataStoreFactoryRegistry, configService, formCreatorService, this));
        factories.put(MultiFormRouteFactory.class, new MultiFormRouteFactory(dataStoreFactoryRegistry, configService, formCreatorService, this));
        factories.put(KanbanDetailFactory.class, new KanbanDetailFactory(dataStoreFactoryRegistry, configService, itemFactoryRegistry, this, formCreatorService, dialogFactoryRegistry, fileProviderRegistry));
        factories.put(SubmenuRouteFactory.class, new SubmenuRouteFactory(this, configService));
    }

    public TurboCrudRouteFactory<DataStoreId, FieldId> getFactory(Class<? extends TurboCrudRouteFactory<DataStoreId, FieldId>> factory) {
        return factories.get(factory);
    }

    @Override
    public void addFactory(Class<? extends TurboCrudRouteFactory<DataStoreId, FieldId>> key, TurboCrudRouteFactory<DataStoreId, FieldId> factory) {
        factories.put(key, factory);
    }

    @Override
    public boolean isContainerRoute(Route<DataStoreId, FieldId> currentRoute) {
        return factories.get(currentRoute.getFactory()).isContainerRoute();
    }
}

