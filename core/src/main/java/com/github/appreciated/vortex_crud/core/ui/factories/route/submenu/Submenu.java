package com.github.appreciated.vortex_crud.core.ui.factories.route.submenu;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.card.Card;
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
    private final RouteRenderer<DataStoreId, FieldId> routeRenderer;
    private VortexCrudPathToRouteResolver<DataStoreId, FieldId> pathVariables;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory;
    private final Integer currentPathIndex;
    private final VortexCrudConfigService<DataStoreId, FieldId> configService;
    private Component active;

    public Submenu(Integer currentPathIndex,
                   VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                   VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                   VortexCrudConfigService<DataStoreId, FieldId> configService
    ) {
        this.currentPathIndex = currentPathIndex;
        this.configService = configService;
        routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.routeFactory = routeFactory;
        // Master
        VerticalLayout masterLayout = new VerticalLayout();
        masterLayout.setPadding(true);
        masterLayout.setSizeFull();

        HorizontalLayout header = new RouteHeader(routeRenderer);
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
        initializeRouteList(routeRenderer.getChildrenMap(), currentPathIndex, routeResolver);

        if (hasActiveSubroute(currentPathIndex, routeResolver)) {
            showRouteDetail(getActiveSubroute(currentPathIndex, routeResolver), routeResolver);
        }

        setPrimaryStyle("flex", "1 0 250px");
        setSecondaryStyle("flex", "1 1 100%");
        addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
    }

    private static <DataStoreId, FieldId> boolean hasActiveSubroute(Integer currentPathIndex, VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver) {
        return routeResolver.hasPathForIndex(currentPathIndex + 1);
    }

    private RouteRenderer<DataStoreId, FieldId> getActiveSubroute(Integer currentPathIndex, VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver) {
        return routeRenderer.getChildrenMap().get(routeResolver.getPathForIndex(currentPathIndex + 1));
    }

    private void initializeRouteList(Map<String, ? extends RouteRenderer<DataStoreId, FieldId>> childRoutes, Integer currentPathIndex, VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver) {
        childRoutes.forEach((key, value) -> {
            Card routeButton = new Card();
            routeButton.addClassNames("master");

            Component icon = value.getIconFactory().get();
            icon.getStyle()
                    .set("color", "var(--lumo-primary-text-color)")
                    .set("opacity", "0.5");
            routeButton.setHeaderPrefix(icon);

            routeButton.setTitle(new H4(routeButton.getTranslation(value.getTitle())));
            routeButton.setWidthFull();

            if (hasActiveSubroute(currentPathIndex, routeResolver) && value == getActiveSubroute(currentPathIndex, routeResolver)) {
                routeButton.addClassName("active");
                active = routeButton;
            }

            routeButton.getElement().addEventListener("click",event -> {
                getUI().ifPresent(ui -> {
                    String pathForEntity = pathVariables.generateSubRoute(this.currentPathIndex, key);
                    pathVariables = new VortexCrudPathToRouteResolver<>(routeFactory, pathForEntity, configService.getConfiguration().getRouteRenderers());
                    ui.getPage().getHistory().pushState(null, pathForEntity);
                    if (active != null) {
                        active.removeClassName("active");
                    }
                    showRouteDetail(routeRenderer.getChildrenMap().get(key), pathVariables);
                    routeButton.addClassName("active");
                    active = routeButton;
                });
            });
            routeListLayout.add(routeButton);
        });
    }

    private void showRouteDetail(RouteRenderer<DataStoreId, FieldId> subRouteRenderer, VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver) {
        if (!routeResolver.isLastIndex(currentPathIndex)) {

            detailLayout.removeAll();
            VortexCrudRouteFactory<DataStoreId, FieldId> factory = routeFactory.getFactory(subRouteRenderer.getFactory());
            Component component = factory.renderRoute(this.currentPathIndex + 1, pathVariables, new DetailRouteSetting(true, false, false));
            detailLayout.add(component);
        }
    }
}