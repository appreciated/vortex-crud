package com.github.appreciated.vortex_crud.core.ui.factories.route.list;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRendererSingleChild;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteActionContext;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;

import java.util.Collections;

public class List<ModelClass, FieldType, RepositoryType> extends VerticalLayout {
    private final GenericEntityGrid<ModelClass, FieldType, RepositoryType> entityGrid;

    public List(Integer currentPathIndex,
                 VortexCrudPathToRouteResolver routeResolver,
                VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
         VortexCrudPathToRouteResolver typedRouteResolver =
                routeResolver;
        RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer =
                (RouteRendererSingleChild<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);

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
            VortexCrudDataStore<FieldType, ModelClass> vortexDataStore = routeRenderer.dataStoreInstance();

            headerBar.renderActions(routeRenderer.routeActions(), contextConsumer -> {
                RouteActionContext<FieldType, ModelClass> actionContext = RouteActionContext.<FieldType, ModelClass>builder()
                    .dataStore(vortexDataStore)
                    .selectedEntities(Collections.emptyList())  // No selection support yet
                    .refreshCallback(() -> UI.getCurrent().getPage().reload())
                    .viewComponent(this)
                    .build();
                contextConsumer.accept(actionContext);
            });
        }

        SearchField textField = new SearchField(event -> applyFilter(event.getValue()));
        entityGrid = new GenericEntityGrid<>(typedRouteResolver, routeRenderer, context);
        add(headerBar);
        if (routeRenderer.filterField() != null) {
            add(textField);
        }
        add(entityGrid);
        setSizeFull();
        setPadding(true);
        setSpacing(true);
    }

    private void applyFilter(String filterText) {
        DataProvider<Object, ?> dataProvider = entityGrid.getDataProvider();
        if (dataProvider != null) {
            if (dataProvider instanceof ConfigurableFilterDataProvider) {
                ((ConfigurableFilterDataProvider<?, Void, String>) dataProvider).setFilter(filterText);
            }
        }
    }

    private void onAdd(VortexCrudContext<ModelClass, FieldType, RepositoryType> context, RouteRendererSingleChild<ModelClass, FieldType, RepositoryType> routeRenderer, VortexCrudDataStore<FieldType, ModelClass> dataStore) {
        if (routeRenderer.form() != null && routeRenderer.form().dialogFactory() != null) {
             Dialog dialog = routeRenderer.form().dialogFactory().create(
                null,
                null,
                null,
                routeRenderer.form(),
                null,
                dataStore,
                context,
                routeRenderer.dataStoreConfig(),
                () -> UI.getCurrent().getPage().reload(),
                () -> {
                }
            );
            dialog.open();
        }
    }
}
