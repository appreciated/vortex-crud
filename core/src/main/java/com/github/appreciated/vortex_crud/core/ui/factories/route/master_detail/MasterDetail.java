package com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.GridOrListRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.DataStoreUtil;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.model.GenericEntity;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
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

public class MasterDetail<DataStoreId, FieldId> extends SplitLayout {

    private final GridOrListRendererConfiguration<DataStoreId, FieldId> gridOrListConfiguration;
    private VortexCrudPathToRouteResolver<DataStoreId, FieldId> pathVariables;
    private final VortexCrudDataStore<FieldId> dataStore;
    private final VortexCrudItemFactory<FieldId> itemFactory;
    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final Integer currentPathIndex;
    private final VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory;
    private final VortexCrudConfigService<DataStoreId, FieldId> configService;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver;
    private final RouteRenderer<DataStoreId, FieldId> routeRenderer;
    private final VerticalLayout detailContainer;
    private ConfigurableFilterDataProvider<GenericEntity, Void, String> dataProvider; // Hinzugefügter DataProvider
    private Component active;

    public MasterDetail(Integer currentPathIndex,
                        VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver,
                        VortexCrudDataStoreFactoryRegistry<DataStoreId, FieldId> dataStoreFactoryRegistry,
                        VortexCrudItemFactoryRegistry<FieldId> itemFactoryRegistry,
                        VortexCrudRouteFactoryRegistry<DataStoreId, FieldId> routeFactory,
                        VortexCrudConfigService<DataStoreId, FieldId> configService,
                        VortexCrudFileProviderRegistry fileProviderRegistry,
                        VortexCrudDataStoreFieldNameResolver<FieldId> fieldNameResolver
    ) {
        this.currentPathIndex = currentPathIndex;
        this.routeFactory = routeFactory;
        this.configService = configService;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;

        routeRenderer = routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.dataStore = dataStoreFactoryRegistry.getDataStore(routeRenderer.getDataStore());
        this.gridOrListConfiguration = (GridOrListRendererConfiguration<DataStoreId, FieldId>) routeRenderer.getConfiguration();
        this.itemFactory = itemFactoryRegistry.getFactory(gridOrListConfiguration.getFactory());
        assert routeRenderer.getChild() != null;

        detailContainer = new VerticalLayout();
        detailContainer.setPadding(false);
        detailContainer.setHeightFull();
        detailContainer.setWidth("unset");

        HorizontalLayout header = new RouteHeader(routeRenderer);

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

    private void setDetail(VortexCrudPathToRouteResolver<DataStoreId, FieldId> routeResolver, boolean creation) {
        detailContainer.removeAll();
        if (!routeResolver.isLastIndex(currentPathIndex)) {
            RouteRenderer<DataStoreId, FieldId> child = routeRenderer.getChild();
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
            pathVariables = new VortexCrudPathToRouteResolver<>(routeFactory, pathForEntity, configService.getConfiguration().getRouteRenderers());
            setDetail(pathVariables, false);
            ui.getPage().getHistory().pushState(null, pathForEntity);
        });
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = itemFactory.renderItem(gridOrListConfiguration, item, null, fileProviderRegistry, fieldNameResolver);
            component.addClassNames("master");
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
