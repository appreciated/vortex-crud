package com.github.appreciated.turbo_crud.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.typesafe.config.Config;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

/**
 * A layout component implementing a Master-Detail pattern using a VirtualList and a detail view.
 * This renderer dynamically displays a list of entities and their detailed information side by side,
 * supporting click events to update the detail view based on the selected item.
 * It uses lazy loading for efficient data retrieval and rendering.
 */

public class MasterDetail extends SplitLayout {

    private final TurboCrudPathToRouteResolver pathVariables;

    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudItemFactory itemFactory;

    private final String table;

    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final VerticalLayout detailLayout = new VerticalLayout();
    private final Config factoryConfig;
    private final Integer currentPathIndex;

    private Component active;

    public MasterDetail(Integer currentPathIndex,
                        TurboCrudPathToRouteResolver routeResolver,
                        TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                        TurboCrudItemFactoryRegistry itemFactoryRegistry,
                        TurboCrudRouteFactoryRegistry routeFactory,
                        TurboCrudIconFactory iconFactory) {
        this.currentPathIndex = currentPathIndex;

        Route route = routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.entityManagerService = entityManagerFactoryRegistry.getFactory(route.getTable());
        this.factoryConfig = route.getConfiguration();
        this.itemFactory = itemFactoryRegistry.getFactory(factoryConfig);
        this.table = route.getTable();
        assert route.getChildren() != null;
        assert route.getChildren().size() == 1;

        detailLayout.setPadding(false);
        detailLayout.setHeightFull();
        detailLayout.setWidth("unset");
        detailLayout.getStyle().set("flex", "4 1 400px");

        HorizontalLayout header = new RouteHeader(route, iconFactory);
        header.setPadding(true);

        virtualList.setHeightFull();

        VerticalLayout listWrapper = new VerticalLayout(header, virtualList);
        listWrapper.getStyle().set("overflow", "hidden");
        listWrapper.setPadding(false);
        listWrapper.setSpacing(false);
        listWrapper.getStyle().set("flex", "1 1 200px");

        // Layout konfigurieren
        addToPrimary(listWrapper);
        addToSecondary(detailLayout);

        setSizeFull();

        initVirtualList();

        getStyle().set("overflow", "hidden");

        this.detailLayout.removeAll();
        if (!routeResolver.isLastIndex(currentPathIndex)) {
            Route child = route.getChild();
            Component component = routeFactory.getFactory(child.getFactory()).renderRoute(
                    currentPathIndex + 1,
                    routeResolver,
                    true,
                    false
            );
            this.detailLayout.add(component);
        }
    }

    private void onItemClick(GenericEntity entity) {
        getUI().ifPresent(ui -> ui.navigate("/view/" + pathVariables.getPathForEntity(currentPathIndex, entity)));
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = itemFactory.renderItem(factoryConfig, item, null);
            component.addClassName("master");
            Div div = new Div(component);
            if (EntityUtil.getId(item).equals(pathVariables.getLastSegment())) {
                component.addClassName("active");
            }
            div.getStyle().set("padding", "5px 5px 0px 5px");
            div.addClickListener(event -> {
                if (active != null) {
                    active.removeClassName("active");
                }
                component.addClassName("active");
                active = component;
                onItemClick(item);
            });
            return div;
        }));
        this.virtualList.setDataProvider(DataProvider.fromCallbacks(
                query -> entityManagerService.getRecordsFromTable(query.getOffset(), query.getLimit()).stream(),
                query -> entityManagerService.count()
        ));
    }
}
