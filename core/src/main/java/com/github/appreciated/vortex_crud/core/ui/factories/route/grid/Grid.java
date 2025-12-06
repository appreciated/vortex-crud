package com.github.appreciated.vortex_crud.core.ui.factories.route.grid;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteActionContext;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components.EntityItemList;
import com.github.appreciated.vortex_crud.core.ui.factories.route.grid.components.VirtualItemGrid;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.Collections;

public class Grid<ModelClass, FieldType, RepositoryType> extends VerticalLayout {
    private final VirtualItemGrid<ModelClass, FieldType, RepositoryType> virtualGrid;

    public Grid(VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer,
                VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(context, routeRenderer, routeRenderer.dataStoreInstance()),
                null,
                null,
                routeHeader);

        // Render custom route actions if configured
        if (routeRenderer.routeActions() != null && !routeRenderer.routeActions().isEmpty()) {
            VortexCrudDataStore<FieldType, ModelClass> dataStore = routeRenderer.dataStoreInstance();

            headerBar.renderActions(routeRenderer.routeActions(), contextConsumer -> {
                RouteActionContext<FieldType, ModelClass> actionContext = RouteActionContext.<FieldType, ModelClass>builder()
                    .dataStore(dataStore)
                    .selectedEntities(Collections.emptyList())  // No selection support yet
                    .refreshCallback(() -> UI.getCurrent().getPage().reload())
                    .viewComponent(this)
                    .build();
                contextConsumer.accept(actionContext);
            });
        }

        SearchField search = new SearchField(event -> applyFilter(event.getValue()));
        virtualGrid = new VirtualItemGrid<>(routeResolver,
                routeRenderer,
                context
        );
        add(headerBar, search, virtualGrid);
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

    private void applyFilter(String filterText) {
        DataProvider<EntityItemList<ModelClass>, ?> dataProvider = virtualGrid.getDataProvider();
        if (dataProvider != null) {
            if (dataProvider instanceof ConfigurableFilterDataProvider) {
                ((ConfigurableFilterDataProvider<?, Void, String>) dataProvider).setFilter(filterText);
            }
        }
    }

    private void onAdd(VortexCrudContext<ModelClass, FieldType, RepositoryType> context,
                       RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer,
                       VortexCrudDataStore<FieldType, ModelClass> dataStore) {

        if (routeRenderer.child() != null && routeRenderer.child().dialogFactory() != null) {
            Dialog dialog = routeRenderer.child().dialogFactory().create(
                    null,
                    null,
                    null,
                    routeRenderer.child(),
                    null,
                    dataStore,
                    context,
                    () -> UI.getCurrent().getPage().reload(),
                    () -> {

                    });
            dialog.open();
        }
    }
}
