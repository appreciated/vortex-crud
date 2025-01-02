package com.github.appreciated.turbo_crud.core.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.core.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.core.config.model.GridOrListConfiguration;
import com.github.appreciated.turbo_crud.core.config.model.Route;
import com.github.appreciated.turbo_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.turbo_crud.core.entity.DataStoreUtil;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStore;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFactoryRegistry;
import com.github.appreciated.turbo_crud.core.entity.data_store.TurboCrudDataStoreFieldNameResolver;
import com.github.appreciated.turbo_crud.core.file_provider.TurboCrudFileProviderRegistry;
import com.github.appreciated.turbo_crud.core.model.GenericEntity;
import com.github.appreciated.turbo_crud.core.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.core.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.core.ui.components.SearchField;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.core.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.turbo_crud.core.ui.factories.route.TurboCrudRouteFactoryRegistry;
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

public class MasterDetail<DataStoreId, FieldId>  extends SplitLayout {

    private final GridOrListConfiguration<DataStoreId, FieldId>  gridOrListConfiguration;
    private TurboCrudPathToRouteResolver<DataStoreId, FieldId>  pathVariables;
    private final TurboCrudDataStore<FieldId>  dataStore;
    private final TurboCrudItemFactory<FieldId>  itemFactory;
    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final Integer currentPathIndex;
    private final TurboCrudRouteFactoryRegistry<DataStoreId, FieldId>  routeFactory;
    private final TurboCrudConfigService<DataStoreId, FieldId>  configService;
    private final TurboCrudFileProviderRegistry fileProviderRegistry;
    private final TurboCrudDataStoreFieldNameResolver<FieldId> resolver;
    private final Route<DataStoreId, FieldId>  route;
    private final VerticalLayout detailContainer;
    private ConfigurableFilterDataProvider<GenericEntity, Void, String> dataProvider; // Hinzugefügter DataProvider
    private Component active;

    public MasterDetail(Integer currentPathIndex,
                        TurboCrudPathToRouteResolver<DataStoreId, FieldId>  routeResolver,
                        TurboCrudDataStoreFactoryRegistry<DataStoreId, FieldId>  dataStoreFactoryRegistry,
                        TurboCrudItemFactoryRegistry<FieldId>  itemFactoryRegistry,
                        TurboCrudRouteFactoryRegistry<DataStoreId, FieldId>  routeFactory,
                        TurboCrudConfigService<DataStoreId, FieldId>  configService,
                        TurboCrudFileProviderRegistry fileProviderRegistry,
                        TurboCrudDataStoreFieldNameResolver<FieldId> resolver
    ) {
        this.currentPathIndex = currentPathIndex;
        this.routeFactory = routeFactory;
        this.configService = configService;
        this.fileProviderRegistry = fileProviderRegistry;
        this.resolver = resolver;

        route = routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.dataStore = dataStoreFactoryRegistry.getFactory(route.getDataStore());
        this.gridOrListConfiguration = (GridOrListConfiguration<DataStoreId, FieldId> ) route.getConfiguration();
        this.itemFactory = itemFactoryRegistry.getFactory(gridOrListConfiguration.getFactory());
        assert route.getChild() != null;

        detailContainer = new VerticalLayout();
        detailContainer.setPadding(false);
        detailContainer.setHeightFull();
        detailContainer.setWidth("unset");

        HorizontalLayout header = new RouteHeader<>(route);

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

    private void setDetail(TurboCrudPathToRouteResolver<DataStoreId, FieldId>  routeResolver, boolean creation) {
        detailContainer.removeAll();
        if (!routeResolver.isLastIndex(currentPathIndex)) {
            Route<DataStoreId, FieldId>  child = route.getChild();
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
            pathVariables = new TurboCrudPathToRouteResolver<>(routeFactory, pathForEntity, configService.getConfiguration().getRoutes());
            setDetail(pathVariables, false);
            ui.getPage().getHistory().pushState(null, pathForEntity);
        });
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = itemFactory.renderItem(gridOrListConfiguration, item, null, fileProviderRegistry, resolver);
            component.addClassNames("master", "no-padding");
            Div div = new Div(component);
            if (DataStoreUtil.equals(item, pathVariables.getLastSegment())) {
                component.addClassName("active");
                setNewActive(component);
            }
            div.addClickListener(event -> {
                setNewActive(component);
                onItemClick(item);
            });
            return div;
        }));

        dataProvider = new GenericFilterableDataProvider<>(dataStore, gridOrListConfiguration.getTitleField()).withConfigurableFilter();
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
