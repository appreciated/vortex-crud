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
public class Submenu<ModelClass, FieldType, RepositoryType> extends SplitLayout {

    private final VerticalLayout routeListLayout = new VerticalLayout();
    private final VerticalLayout detailLayout = new VerticalLayout();
    private final RouteRenderer<ModelClass, FieldType, RepositoryType> routeRenderer;
    private VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> pathVariables;
    private final VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory;
    private final Integer currentPathIndex;
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private Component active;

    public Submenu(Integer currentPathIndex,
                   VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                   VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory,
                   VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
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

    private static <ModelClass, FieldType, RepositoryType> boolean hasActiveSubroute(Integer currentPathIndex, VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver) {
        return routeResolver.hasPathForIndex(currentPathIndex + 1);
    }

    private RouteRenderer<ModelClass, FieldType, RepositoryType> getActiveSubroute(Integer currentPathIndex, VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver) {
        return routeRenderer.getChildrenMap().get(routeResolver.getPathForIndex(currentPathIndex + 1));
    }

    private void initializeRouteList(Map<String, ? extends RouteRenderer<ModelClass, FieldType, RepositoryType>> childRoutes, Integer currentPathIndex, VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver) {
        childRoutes.forEach((key, value) -> {
            Card routeButton = new Card();
            routeButton.addClassNames("master");

            if (value.getIconFactory() != null) {
                Component icon = value.getIconFactory().get();
                icon.getStyle()
                        .set("color", "var(--lumo-primary-text-color)")
                        .set("opacity", "0.5");
                routeButton.setHeaderPrefix(icon);
            }
            routeButton.setTitle(new H4(routeButton.getTranslation(value.getTitle())));
            routeButton.setWidthFull();

            if (hasActiveSubroute(currentPathIndex, routeResolver) && value == getActiveSubroute(currentPathIndex, routeResolver)) {
                routeButton.addClassName("active");
                active = routeButton;
            }

            routeButton.getElement().addEventListener("click", event -> getUI().ifPresent(ui -> {
                String pathForEntity = pathVariables.buildPathUpToIndex(this.currentPathIndex, key);
                pathVariables = new VortexCrudPathToRouteResolver<>(routeFactory, pathForEntity, configService.getConfiguration().getRouteRenderers(), dataStoreUtil);
                ui.getPage().getHistory().pushState(null, pathForEntity);
                if (active != null) {
                    active.removeClassName("active");
                }
                showRouteDetail(routeRenderer.getChildrenMap().get(key), pathVariables);
                routeButton.addClassName("active");
                active = routeButton;
            }));
            routeListLayout.add(routeButton);
        });
    }

    private void showRouteDetail(RouteRenderer<ModelClass, FieldType, RepositoryType> subRouteRenderer, VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver) {
        if (!routeResolver.isLastIndex(currentPathIndex)) {

            detailLayout.removeAll();
            VortexCrudRouteFactory<ModelClass, FieldType, RepositoryType> factory = routeFactory.getFactory(subRouteRenderer.getFactory());
            Component component = factory.renderRoute(this.currentPathIndex + 1, pathVariables, new DetailRouteSetting(true, false, false));
            detailLayout.add(component);
        }
    }
}