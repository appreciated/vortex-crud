package com.github.appreciated.turbo_crud.core.ui.factories.route.submenu;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

import java.util.Map;

/**
 * Factory for a submenu meaning it can render multiple routes as its children, one for each button.
 * A vertical list of routes is displayed on the left and the detailed view of the selected route on the right.
 */
public class Submenu<DataStoreId, FieldId> extends SplitLayout {

    private final VerticalLayout routeListLayout = new VerticalLayout();
    private final VerticalLayout detailLayout = new VerticalLayout();
    private final Route<DataStoreId, FieldId> route;
    private TurboCrudPathToRouteResolver<DataStoreId, FieldId> pathVariables;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final Integer currentPathIndex;
    private final TurboCrudConfigService<DataStoreId, FieldId> configService;
    private Component active;

    public Submenu(Integer currentPathIndex,
                   TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                   TurboCrudRouteFactoryRegistry routeFactory,
                   TurboCrudConfigService<DataStoreId, FieldId> configService
    ) {
        this.currentPathIndex = currentPathIndex;
        this.configService = configService;
        route = routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.routeFactory = routeFactory;
        // Master
        VerticalLayout masterLayout = new VerticalLayout();
        masterLayout.setPadding(true);
        masterLayout.setSizeFull();

        HorizontalLayout header = new RouteHeader<>(route);
        masterLayout.add(header);

        routeListLayout.setPadding(false);
        routeListLayout.setSpacing(false);
        routeListLayout.setWidthFull();
        routeListLayout.setHeightFull();

        masterLayout.add(routeListLayout);

        // Detail
        detailLayout.setPadding(false);
        detailLayout.setHeightFull();
        detailLayout.setWidth("auto");

        addToPrimary(masterLayout);
        addToSecondary(detailLayout);

        setSizeFull();
        initializeRouteList(route.getChildrenMap(), currentPathIndex, routeResolver);

        if (hasActiveSubroute(currentPathIndex, routeResolver)) {
            showRouteDetail(getActiveSubroute(currentPathIndex, routeResolver), routeResolver);
        }

        setPrimaryStyle("flex", "1 0 250px");
        setSecondaryStyle("flex", "1 1 100%");
        addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
    }

    private static <DataStoreId, FieldId> boolean hasActiveSubroute(Integer currentPathIndex, TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver) {
        return routeResolver.hasPathForIndex(currentPathIndex + 1);
    }

    private Route<DataStoreId, FieldId> getActiveSubroute(Integer currentPathIndex, TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver) {
        return route.getChildrenMap().get(routeResolver.getPathForIndex(currentPathIndex + 1));
    }

    private void initializeRouteList(Map<String, ? extends Route<DataStoreId, FieldId>> childRoutes, Integer currentPathIndex, TurboCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver) {
        childRoutes.forEach((key, value) -> {
            HorizontalLayout routeButton = new HorizontalLayout();
            routeButton.addClassNames("card", "master");
            Component icon = value.getIconFactory().get();
            routeButton.add(icon);
            routeButton.add(new H4(routeButton.getTranslation(value.getTitle())));
            routeButton.setWidthFull();

            if (hasActiveSubroute(currentPathIndex, routeResolver) && value == getActiveSubroute(currentPathIndex, routeResolver)) {
                routeButton.addClassName("active");
                active = routeButton;
            }

            routeButton.addClickListener(event -> {
                getUI().ifPresent(ui -> {
                    String pathForEntity = pathVariables.generateSubRoute(this.currentPathIndex, key);
                    pathVariables = new TurboCrudPathToRouteResolver(routeFactory, pathForEntity, configService.getConfiguration().getRoutes());
                    ui.getPage().getHistory().pushState(null, pathForEntity);
                    if (active != null) {
                        active.removeClassName("active");
                    }
                    showRouteDetail(route.getChildrenMap().get(key), pathVariables);
                    routeButton.addClassName("active");
                    active = routeButton;
                });
            });
            routeListLayout.add(routeButton);
        });
    }

    private void showRouteDetail(Route<DataStoreId, FieldId> subRoute, TurboCrudPathToRouteResolver routeResolver) {
        if (!routeResolver.isLastIndex(currentPathIndex)) {

            detailLayout.removeAll();
            TurboCrudRouteFactory<DataStoreId, FieldId> factory = routeFactory.getFactory(subRoute.getFactory());
            Component component = factory.renderRoute(this.currentPathIndex + 1, pathVariables, new DetailRouteSetting(true, false, false));
            detailLayout.add(component);
        }
    }
}