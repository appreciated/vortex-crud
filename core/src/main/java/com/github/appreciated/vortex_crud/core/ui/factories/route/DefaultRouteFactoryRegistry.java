package com.github.appreciated.vortex_crud.core.ui.factories.route;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.security.VortexCrudRbacPermissionChecker;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.factories.dialog.VortexCrudDialogFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.form.FormCreator;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.calendar.CalendarFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormSlideRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.MultiFormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.VortexCrudListColumnCallbackRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail.MasterDetailRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.submenu.SubmenuRouteFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the VortexCrudRouteRendererFactory interface.
 * This factory provides different route renderers such as Master-Detail, Grid, and Item views
 * based on the RouteConfig configuration.
 */

@Service
public class DefaultRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> {

    final HashMap<Class<? extends VortexCrudRouteFactory>, VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factories = new HashMap<>();

    public DefaultRouteFactoryRegistry(VortexCrudItemFactoryRegistry<FieldType> itemFactoryRegistry,
                                       VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                                       VortexCrudListColumnCallbackRegistry<ModelClass, FieldType, RepositoryType> listColumnCallbackRegistry,
                                       VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
                                       VortexCrudDialogFactoryRegistry<ModelClass, FieldType, RepositoryType> dialogFactoryRegistry,
                                       VortexCrudFileProviderRegistry fileProviderRegistry,
                                       VortexCrudDataStoreFieldNameResolver<FieldType> resolver,
                                       FormCreator<ModelClass, FieldType, RepositoryType> formCreatorService,
                                       ReflectionService<FieldType> reflectionService,
                                       VortexCrudDataStoreUtilStrategy dataStoreUtil,
                                       @Autowired(required = false) VortexCrudRbacPermissionChecker<ModelClass, FieldType, RepositoryType> permissionChecker
    ) {
        factories.put(MasterDetailRouteFactory.class, new MasterDetailRouteFactory<>(dataStoreFactoryRegistry, itemFactoryRegistry, this, configService, fileProviderRegistry, resolver, reflectionService, dataStoreUtil));
        factories.put(ListRouteFactory.class, new ListRouteFactory<>(dataStoreFactoryRegistry, configService, listColumnCallbackRegistry, formCreatorService, dialogFactoryRegistry, this, resolver, dataStoreUtil));
        factories.put(GridRouteFactory.class, new GridRouteFactory<>(dataStoreFactoryRegistry, formCreatorService, dialogFactoryRegistry, this, itemFactoryRegistry, fileProviderRegistry, resolver, reflectionService, dataStoreUtil));
        factories.put(FormRouteFactory.class, new FormRouteFactory<>(dataStoreFactoryRegistry, configService, formCreatorService, this, reflectionService, permissionChecker));
        factories.put(FormSlideRouteFactory.class, new FormSlideRouteFactory<>(dataStoreFactoryRegistry, configService, formCreatorService, this, resolver, reflectionService, permissionChecker));
        factories.put(MultiFormRouteFactory.class, new MultiFormRouteFactory<>(dataStoreFactoryRegistry, configService, formCreatorService, this, reflectionService, permissionChecker));
        factories.put(KanbanFactory.class, new KanbanFactory<>(dataStoreFactoryRegistry, configService, itemFactoryRegistry, this, formCreatorService, dialogFactoryRegistry, fileProviderRegistry, resolver, reflectionService, dataStoreUtil));
        factories.put(CalendarFactory.class, new CalendarFactory<>(dataStoreFactoryRegistry, configService, itemFactoryRegistry, this, formCreatorService, dialogFactoryRegistry, fileProviderRegistry, resolver, reflectionService, dataStoreUtil));
        factories.put(SubmenuRouteFactory.class, new SubmenuRouteFactory<>(this, configService, dataStoreUtil));
    }

    public VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> getFactory(Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factory) {
        return factories.get(factory);
    }

    @Override
    public void addFactory(Class<? extends VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> key, VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory) {
        factories.put(key, factory);
    }

    @Override
    public boolean isContainerRoute(RouteRenderer<ModelClass, FieldType, RepositoryType> currentRouteRenderer) {
        if (currentRouteRenderer == null) {
            return false;
        }
        return factories.get(currentRouteRenderer.factory()).isContainerRoute();
    }
}

