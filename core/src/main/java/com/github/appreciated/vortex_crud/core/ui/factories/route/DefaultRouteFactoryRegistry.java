package com.github.appreciated.vortex_crud.core.ui.factories.route;

import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.ui.factories.route.calendar.CalendarFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.custom.CustomRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.FormSlideRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.form.MultiFormRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.GridRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.kanban.KanbanFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.list.ListRouteFactory;
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
public class DefaultRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> implements VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> {

    final HashMap<Class<? extends VortexCrudRouteFactory>, VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType>> factories = new HashMap<>();

    public DefaultRouteFactoryRegistry() {
        factories.put(MasterDetailRouteFactory.class, new MasterDetailRouteFactory<>());
        factories.put(ListRouteFactory.class, new ListRouteFactory<>());
        factories.put(GridRouteFactory.class, new GridRouteFactory<>());
        factories.put(FormRouteFactory.class, new FormRouteFactory<>());
        factories.put(FormSlideRouteFactory.class, new FormSlideRouteFactory<>());
        factories.put(MultiFormRouteFactory.class, new MultiFormRouteFactory<>());
        factories.put(KanbanFactory.class, new KanbanFactory<>());
        factories.put(CalendarFactory.class, new CalendarFactory<>());
        factories.put(SubmenuRouteFactory.class, new SubmenuRouteFactory<>());
        factories.put(CustomRouteFactory.class, new CustomRouteFactory<>());
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
