package com.github.appreciated.turbo_crud.ui.factories.route.submenu;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import java.util.Map;

/**
 * Factory für ein Master-Detail-Layout, das mehrere Routen als Children rendert.
 * Links wird eine vertikale Liste der Routen angezeigt, rechts die Detailansicht der ausgewählten Route.
 */
public class Submenu extends SplitLayout {

    private final VerticalLayout routeListLayout = new VerticalLayout();
    private final VerticalLayout detailLayout = new VerticalLayout();
    private final Route route;
    private TurboCrudPathToRouteResolver pathVariables;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final Integer currentPathIndex;
    private final TurboCrudConfigService configService;

    public Submenu(Integer currentPathIndex,
                   TurboCrudPathToRouteResolver routeResolver,
                   TurboCrudRouteFactoryRegistry routeFactory,
                   TurboCrudConfigService configService
    ) {
        this.currentPathIndex = currentPathIndex;
        this.configService = configService;
        route = routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.routeFactory = routeFactory;
        // Master
        routeListLayout.setPadding(false);
        routeListLayout.setSpacing(true);
        routeListLayout.setWidth("200px");
        routeListLayout.setHeightFull();

        // Detail
        detailLayout.setPadding(false);
        detailLayout.setHeightFull();
        detailLayout.setWidth("auto");
        detailLayout.getStyle().set("flex", "4 1 400px");

        addToPrimary(routeListLayout);
        addToSecondary(detailLayout);

        setSizeFull();
        initializeRouteList(route.getChildrenMap());

        showRouteDetail(route.getChild(), routeResolver);
    }

    private void initializeRouteList(Map<String, Route> childRoutes) {
        childRoutes.forEach((key, value) -> {
            Button routeButton = new Button();
            routeButton.setText(routeButton.getTranslation(value.getTitle()));
            routeButton.setWidthFull();

            routeButton.addClickListener(event -> {
                getUI().ifPresent(ui -> {
                    String pathForEntity = pathVariables.generateSubRoute(currentPathIndex, key);
                    pathVariables = new TurboCrudPathToRouteResolver(routeFactory, pathForEntity, configService.getConfiguration().getRoutesConfig());
                    showRouteDetail(route.getChild(), pathVariables);
                    ui.getPage().getHistory().pushState(null, "/view/" + pathForEntity);
                });

            });
            routeListLayout.add(routeButton);
        });
    }

    private void showRouteDetail(Route route, TurboCrudPathToRouteResolver routeResolver) {
        if (!routeResolver.isLastIndex(currentPathIndex)) {
            detailLayout.removeAll();
            TurboCrudRouteFactory factory = routeFactory.getFactory(route.getFactory());
            Component component = factory.renderRoute(this.currentPathIndex + 1, pathVariables, true, false);
            detailLayout.add(component);
        }
    }
}