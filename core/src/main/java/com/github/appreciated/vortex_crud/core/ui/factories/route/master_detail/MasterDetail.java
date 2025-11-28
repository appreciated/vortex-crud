package com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail;

import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.GridItemRendererConfiguration;
import com.github.appreciated.vortex_crud.core.config.model.MasterDetailRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFieldNameResolver;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.file_provider.VortexCrudFileProviderRegistry;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteActionContext;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactoryRegistry;
import com.github.appreciated.vortex_crud.core.ui.factories.route.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.ui.factories.route.VortexCrudRouteFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

public class MasterDetail<ModelClass, FieldType, RepositoryType> extends SplitLayout {

    private final GridItemRendererConfiguration<ModelClass, FieldType, RepositoryType> itemRendererConfiguration;
    private VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> pathVariables;
    private final VortexCrudDataStore<FieldType, ?> dataStore;
    private final VortexCrudItemFactory<FieldType> itemFactory;
    private final VirtualList<Object> virtualList = new VirtualList<>();
    private final Integer currentPathIndex;
    private final VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory;
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudFileProviderRegistry fileProviderRegistry;
    private final VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver;
    private final ReflectionService<FieldType> reflectionService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final MasterDetailRoute<ModelClass, FieldType, RepositoryType> routeRenderer;
    private final VerticalLayout detailContainer;
    private ConfigurableFilterDataProvider<Object, Void, String> dataProvider; // Hinzugefügter DataProvider
    private Component active;

    @SuppressWarnings("unchecked")
    public MasterDetail(Integer currentPathIndex,
                        VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver,
                        VortexCrudItemFactoryRegistry<FieldType> itemFactoryRegistry,
                        VortexCrudRouteFactoryRegistry<ModelClass, FieldType, RepositoryType> routeFactory,
                        VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
                        VortexCrudFileProviderRegistry fileProviderRegistry,
                        VortexCrudDataStoreFieldNameResolver<FieldType> fieldNameResolver,
                        ReflectionService<FieldType> reflectionService,
                        VortexCrudDataStoreUtilStrategy dataStoreUtil
    ) {
        this.currentPathIndex = currentPathIndex;
        this.routeFactory = routeFactory;
        this.configService = configService;
        this.fileProviderRegistry = fileProviderRegistry;
        this.fieldNameResolver = fieldNameResolver;
        this.reflectionService = reflectionService;
        this.dataStoreUtil = dataStoreUtil;

        routeRenderer = (MasterDetailRoute<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.dataStore = (VortexCrudDataStore<FieldType, ?>) routeRenderer.dataStoreInstance();
        this.itemRendererConfiguration = (GridItemRendererConfiguration<ModelClass, FieldType, RepositoryType>) routeRenderer.configuration();
        this.itemFactory = itemFactoryRegistry.getFactory(itemRendererConfiguration.factory());
        assert routeRenderer.child() != null;

        detailContainer = new VerticalLayout();
        detailContainer.setPadding(false);
        detailContainer.setHeightFull();
        detailContainer.setWidth("unset");

        RouteHeader routeHeader = new RouteHeader(routeRenderer);
        RouteHeaderBarWithSaveDeleteBack headerBar = new RouteHeaderBarWithSaveDeleteBack(false,
                false,
                null,
                event -> onAdd(),
                null,
                null,
                routeHeader);

        // Render custom route actions if configured
        if (routeRenderer.routeActions() != null && !routeRenderer.routeActions().isEmpty()) {
            headerBar.renderActions(routeRenderer.routeActions(), contextConsumer -> {
                RouteActionContext<FieldType, ModelClass> context = RouteActionContext.<FieldType, ModelClass>builder()
                    .dataStore((VortexCrudDataStore<FieldType, ModelClass>) dataStore)
                    .selectedEntities(java.util.Collections.emptyList())  // No selection support yet
                    .refreshCallback(() -> UI.getCurrent().getPage().reload())
                    .viewComponent(this)
                    .build();
                contextConsumer.accept(context);
            });
        }

        SearchField textField = new SearchField(event -> applyFilter(event.getValue()));
        textField.getStyle().set("--lumo-border-radius-m", "var(--vaadin-card-border-radius)");

        HorizontalLayout searchContainer = new HorizontalLayout(textField);
        searchContainer.setPadding(false);
        searchContainer.setAlignItems(CENTER);
        searchContainer.setWidthFull();

        virtualList.setHeightFull();

        VerticalLayout masterContainer = new VerticalLayout(headerBar, searchContainer, virtualList);
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

    private void setDetail(VortexCrudPathToRouteResolver<ModelClass, FieldType, RepositoryType> routeResolver, boolean creation) {
        detailContainer.removeAll();
        if (!routeResolver.isLastIndex(currentPathIndex) || creation) {
            RouteRenderer<ModelClass, FieldType, RepositoryType> child = routeRenderer.child();
            Component component = routeFactory.getFactory(child.factory()).renderRoute(
                    creation ? currentPathIndex : currentPathIndex + 1,
                    routeResolver,
                    new DetailRouteSetting(true, false, creation)
            );
            detailContainer.add(component);
        }
    }

    private void onAdd() {
        UI.getCurrent().getPage().getHistory().pushState(null, "");
        setDetail(pathVariables, true);
    }

    private void onItemClick(Object entity) {
        getUI().ifPresent(ui -> {
            String pathForEntity = pathVariables.getPathForEntity(currentPathIndex, entity);
            pathVariables = new VortexCrudPathToRouteResolver<>(routeFactory,
                    pathForEntity,
                    configService.configuration().routes(),
                    dataStoreUtil);
            setDetail(pathVariables, false);
            ui.getPage().getHistory().pushState(null, pathForEntity);
        });
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = itemFactory.renderItem(itemRendererConfiguration,
                    item,
                    null,
                    fileProviderRegistry,
                    fieldNameResolver,
                    reflectionService);
            component.addClassNames("master");
            Div div = new Div(component);
            if (dataStoreUtil.equals(item, pathVariables.getLastSegment())) {
                component.addClassName("active");
                setNewActive(component);
            }
            div.addClickListener(event -> {
                setNewActive(component);
                onItemClick(item);
            });
            return div;
        }));

        dataProvider = new GenericFilterableDataProvider<>(dataStore, itemRendererConfiguration.titleField()).withConfigurableFilter();
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
