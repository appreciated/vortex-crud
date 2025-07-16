package com.github.appreciated.vortex_crud.core.ui.factories.route;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.MultiFormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanDetailFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.VortexCrudListColumnCallbackRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the VortexCrudRouteRendererFactory interface.
 * This factory provides different route renderers such as Master-Detail, Grid, and Item views
 * based on the RouteConfig configuration.
 */

@Service
public class DefaultRouteFactoryRegistry<DataStoreId, FieldId, KeyType> implements VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> {

    final HashMap<Class<? extends VortexCrudRouteFactory>, VortexCrudRouteFactory<DataStoreId, FieldId, KeyType>> factories = new HashMap<>();

    public DefaultRouteFactoryRegistry(VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                                       VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                                       VortexCrudListColumnCallbackRegistry<DataStoreId, FieldId, KeyType> listColumnCallbackRegistry,
                                       VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId, KeyType> dataStoreFactoryRegistry,
                                       VortexCrudDialogFactoryRegistry<DataStoreId, FieldId, KeyType> dialogFactoryRegistry,
                                       VortexCrudFileProviderRegistry fileProviderRegistry,
                                       VortexCrudDataStoreFieldNameResolver<FieldId> resolver,
                                       FormCreator<DataStoreId, FieldId, KeyType> formCreatorService,
                                       ReflectionService<FieldId> reflectionService
    ) {
        factories.put(MasterDetailRouteFactory.class, new MasterDetailRouteFactory<>(dataStoreFactoryRegistry, itemFactoryRegistry, this, configService, fileProviderRegistry, resolver, reflectionService));
        factories.put(ListRouteFactory.class, new ListRouteFactory<>(dataStoreFactoryRegistry, configService, listColumnCallbackRegistry, formCreatorService, dialogFactoryRegistry, this, resolver));
        factories.put(GridRouteFactory.class, new GridRouteFactory<>(dataStoreFactoryRegistry, formCreatorService, dialogFactoryRegistry, this, itemFactoryRegistry, fileProviderRegistry, resolver, reflectionService));
        factories.put(FormRouteFactory.class, new FormRouteFactory<>(dataStoreFactoryRegistry, configService, formCreatorService, this, resolver, reflectionService));
        factories.put(MultiFormRouteFactory.class, new MultiFormRouteFactory<>(dataStoreFactoryRegistry, configService, formCreatorService, this, resolver, reflectionService));
        factories.put(KanbanDetailFactory.class, new KanbanDetailFactory<>(dataStoreFactoryRegistry, configService, itemFactoryRegistry, this, formCreatorService, dialogFactoryRegistry, fileProviderRegistry, resolver, reflectionService));
        factories.put(SubmenuRouteFactory.class, new SubmenuRouteFactory<>(this, configService));
    }

    public VortexCrudRouteFactory<DataStoreId, FieldId, KeyType> getFactory(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, KeyType>> factory) {
        return factories.get(factory);
    }

    @Override
    public void addFactory(Class<? extends VortexCrudRouteFactory<DataStoreId, FieldId, KeyType>> key, VortexCrudRouteFactory<DataStoreId, FieldId, KeyType> factory) {
        factories.put(key, factory);
    }

    @Override
    public boolean isContainerRoute(RouteRenderer<DataStoreId, FieldId, KeyType> currentRouteRenderer) {
        return factories.get(currentRouteRenderer.getFactory()).isContainerRoute();
    }
}

