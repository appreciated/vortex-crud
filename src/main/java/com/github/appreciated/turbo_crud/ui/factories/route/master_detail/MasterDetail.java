package com.github.appreciated.turbo_crud.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.GridOrListConfiguration;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.entity.manager.TurboCrudEntityManager;
import com.github.appreciated.turbo_crud.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.components.SearchField;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.BETWEEN;

public class MasterDetail extends SplitLayout {

    private final GridOrListConfiguration gridOrListConfiguration;
    private TurboCrudPathToRouteResolver pathVariables;
    private final TurboCrudEntityManager entityManager;
    private final TurboCrudItemFactory itemFactory;
    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final Integer currentPathIndex;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudConfigService configService;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;
    private final Route route;
    private final VerticalLayout detailContainer;
    private ConfigurableFilterDataProvider<GenericEntity, Void, String> dataProvider; // Hinzugefügter DataProvider
    private Component active;

    public MasterDetail(Integer currentPathIndex,
                        TurboCrudPathToRouteResolver routeResolver,
                        TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                        TurboCrudItemFactoryRegistry itemFactoryRegistry,
                        TurboCrudRouteFactoryRegistry routeFactory,
                        TurboCrudConfigService configService,
                        TurboCrudIconFactory iconFactory,
                        TurboCrudFileProviderRegistry fileProviderRegistry
    ) {
        this.currentPathIndex = currentPathIndex;
        this.routeFactory = routeFactory;
        this.configService = configService;
        this.fileProviderRegistry = fileProviderRegistry;

        route = routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.entityManager = entityManagerFactoryRegistry.getFactory(route.getRepository());
        Config factoryConfig = route.getConfiguration();
        this.gridOrListConfiguration = ConfigBeanFactory.create(factoryConfig, GridOrListConfiguration.class);
        this.itemFactory = itemFactoryRegistry.getFactory(gridOrListConfiguration.getFactory());
        assert route.getChildren() != null;
        assert route.getChildren().size() == 1;

        detailContainer = new VerticalLayout();
        detailContainer.setPadding(false);
        detailContainer.setHeightFull();
        detailContainer.setWidth("unset");

        HorizontalLayout header = new RouteHeader(route, iconFactory);

        Button addButton = new Button(VaadinIcon.PLUS.create());
        addButton.addClickListener(event -> onAdd());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout headerContainer = new HorizontalLayout(header, addButton);
        headerContainer.setPadding(false);
        headerContainer.setAlignItems(CENTER);
        headerContainer.setWidthFull();
        headerContainer.setJustifyContentMode(BETWEEN);

        SearchField textField = new SearchField(event -> applyFilter(event.getValue()));

        HorizontalLayout searchContainer = new HorizontalLayout(textField);
        searchContainer.setPadding(false);
        searchContainer.setAlignItems(CENTER);
        searchContainer.setWidthFull();

        virtualList.setHeightFull();

        VerticalLayout masterContainer = new VerticalLayout(headerContainer, searchContainer, virtualList);
        masterContainer.getStyle().set("overflow", "hidden");
        masterContainer.setPadding(true);
        masterContainer.setSpacing(true);

        // Layout konfigurieren
        addToPrimary(masterContainer);

        addToSecondary(detailContainer);

        setSizeFull();

        initVirtualList();

        getStyle().set("overflow", "hidden");

        setDetail(pathVariables, false);

        setPrimaryStyle("flex", "1 0 400px");
        setSecondaryStyle("flex", "1 1 100%");
        addThemeVariants(SplitLayoutVariant.LUMO_SMALL);
    }

    private void setDetail(TurboCrudPathToRouteResolver routeResolver, boolean creation) {
        detailContainer.removeAll();
        if (!routeResolver.isLastIndex(currentPathIndex)) {
            Route child = route.getChild();
            Component component = routeFactory.getFactory(child.getFactory()).renderRoute(
                    currentPathIndex + 1,
                    routeResolver,
                    new DetailRouteSetting(true, false, creation)
            );
            detailContainer.add(component);
        }
    }

    private void onAdd() {
        setDetail(pathVariables, true);
    }

    private void onItemClick(GenericEntity entity) {
        getUI().ifPresent(ui -> {
            String pathForEntity = pathVariables.getPathForEntity(currentPathIndex, entity);
            pathVariables = new TurboCrudPathToRouteResolver(routeFactory, pathForEntity, configService.getConfiguration().getRoutesConfig());
            setDetail(pathVariables, false);
            ui.getPage().getHistory().pushState(null, "/view/" + pathForEntity);
        });
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = itemFactory.renderItem(gridOrListConfiguration, item, null, fileProviderRegistry);
            component.addClassName("master");
            Div div = new Div(component);
            if (EntityUtil.equals(item, pathVariables.getLastSegment())) {
                component.addClassName("active");
                setNewActive(component);
            }
            div.addClickListener(event -> {
                setNewActive(component);
                onItemClick(item);
            });
            return div;
        }));

        dataProvider = new GenericFilterableDataProvider(entityManager, gridOrListConfiguration.getTitleField()).withConfigurableFilter();
        this.virtualList.setDataProvider(dataProvider);
    }

    private void setNewActive(Component component) {
        if (active != null) {
            active.removeClassName("active");
        }
        component.addClassName("active");
        active = component;
    }

    private void applyFilter(String filterText) {
        if (virtualList.getDataProvider() != null) {
            dataProvider.setFilter(filterText);
        }
    }
}
