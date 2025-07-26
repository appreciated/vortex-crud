package com.github.appreciated.vortex_crud.core.ui.factories.route.submenu;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
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
public class Submenu<DataStoreId, FieldId, KeyType> extends SplitLayout {

    private final VerticalLayout routeListLayout = new VerticalLayout();
    private final VerticalLayout detailLayout = new VerticalLayout();
    private final RouteRenderer<DataStoreId, FieldId, KeyType> routeRenderer;
    private VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> pathVariables;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory;
    private final Integer currentPathIndex;
    private final VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private Component active;

    public Submenu(Integer currentPathIndex,
                   VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver,
                   VortexCrudRouteFactoryRegistry<DataStoreId, FieldId, KeyType> routeFactory,
                   VortexCrudConfigService<DataStoreId, FieldId, KeyType> configService,
                   VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.currentPathIndex = currentPathIndex;
        this.configService = configService;
        this.dataStoreUtil = dataStoreUtil;
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

    private static <DataStoreId, FieldId, KeyType> boolean hasActiveSubroute(Integer currentPathIndex, VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver) {
        return routeResolver.hasPathForIndex(currentPathIndex + 1);
    }

    private RouteRenderer<DataStoreId, FieldId, KeyType> getActiveSubroute(Integer currentPathIndex, VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver) {
        return routeRenderer.getChildrenMap().get(routeResolver.getPathForIndex(currentPathIndex + 1));
    }

    private void initializeRouteList(Map<String, ? extends RouteRenderer<DataStoreId, FieldId, KeyType>> childRoutes, Integer currentPathIndex, VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver) {
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

            routeButton.getElement().addEventListener("click", event -> {
                getUI().ifPresent(ui -> {
                    String pathForEntity = pathVariables.buildPathUpToIndex(this.currentPathIndex, key);
                    pathVariables = new VortexCrudPathToRouteResolver<>(routeFactory, pathForEntity, configService.getConfiguration().getRouteRenderers(), dataStoreUtil);
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

    private void showRouteDetail(RouteRenderer<DataStoreId, FieldId, KeyType> subRouteRenderer, VortexCrudPathToRouteResolver<DataStoreId, FieldId, KeyType> routeResolver) {
        if (!routeResolver.isLastIndex(currentPathIndex)) {

            detailLayout.removeAll();
            VortexCrudRouteFactory<DataStoreId, FieldId, KeyType> factory = routeFactory.getFactory(subRouteRenderer.getFactory());
            Component component = factory.renderRoute(this.currentPathIndex + 1, pathVariables, new DetailRouteSetting(true, false, false));
            detailLayout.add(component);
        }
    }
}