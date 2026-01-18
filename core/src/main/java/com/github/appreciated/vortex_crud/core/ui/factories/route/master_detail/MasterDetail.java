package com.github.appreciated.vortex_crud.core.ui.factories.route.master_detail;

import com.github.appreciated.vortex_crud.core.config.DetailRouteSetting;
import com.github.appreciated.vortex_crud.core.config.VortexCrudPathToRouteResolver;
import com.github.appreciated.vortex_crud.core.config.model.MasterDetailRoute;
import com.github.appreciated.vortex_crud.core.config.model.RouteRenderer;
import com.github.appreciated.vortex_crud.core.data_provider.GenericFilterableDataProvider;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudDataStoreUtilStrategy;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudContext;
import com.github.appreciated.vortex_crud.core.ui.actions.RouteActionContext;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeader;
import com.github.appreciated.vortex_crud.core.ui.components.RouteHeaderBarWithSaveDeleteBack;
import com.github.appreciated.vortex_crud.core.ui.components.SearchField;
import com.github.appreciated.vortex_crud.core.ui.factories.item.VortexCrudItemFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;

public class MasterDetail<ModelClass, FieldType, RepositoryType> extends SplitLayout {

    private  VortexCrudPathToRouteResolver pathVariables;
    private final VortexCrudDataStore<FieldType, ?> dataStore;
    private final VortexCrudItemFactory<FieldType> itemFactory;
    private final VirtualList<Object> virtualList = new VirtualList<>();
    private final Integer currentPathIndex;
    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final VortexCrudDataStoreUtilStrategy dataStoreUtil;
    private final MasterDetailRoute<ModelClass, FieldType, RepositoryType> routeRenderer;
    private final VerticalLayout detailContainer;
    private final VortexCrudContext<ModelClass, FieldType, RepositoryType> context;
    private ConfigurableFilterDataProvider<Object, Void, String> dataProvider;
    private Component active;

    @SuppressWarnings("unchecked")
    public MasterDetail(Integer currentPathIndex,
                         VortexCrudPathToRouteResolver routeResolver,
                        VortexCrudContext<ModelClass, FieldType, RepositoryType> context
    ) {
        this.currentPathIndex = currentPathIndex;
        this.context = context;
        this.configService = context.configService();
        this.dataStoreUtil = context.dataStoreUtil();

        routeRenderer = (MasterDetailRoute<ModelClass, FieldType, RepositoryType>) routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.dataStore = routeRenderer.dataStoreInstance();
        this.itemFactory = routeRenderer.itemFactory();

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
        if (routeRenderer.actions() != null && !routeRenderer.actions().isEmpty()) {
            headerBar.renderActions(routeRenderer.actions(), contextConsumer -> {
                RouteActionContext<FieldType, ModelClass> actionContext = RouteActionContext.<FieldType, ModelClass>builder()
                    .dataStore((VortexCrudDataStore<FieldType, ModelClass>) dataStore)
                    .selectedEntities(java.util.Collections.emptyList())  // No selection support yet
                    .refreshCallback(() -> UI.getCurrent().getPage().reload())
                    .viewComponent(this)
                    .build();
                contextConsumer.accept(actionContext);
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

        getElement().getClassList().add("vortex-responsive-master-detail");
        String css = """
                 @media (max-width: 800px) {
                    vaadin-split-layout.vortex-responsive-master-detail[state="master"] > [slot="primary"] {
                        flex: 1 1 100% !important;
                        width: 100% !important;
                        min-width: 100% !important;
                        display: flex !important;
                    }
                    vaadin-split-layout.vortex-responsive-master-detail[state="master"] > [slot="secondary"] {
                        display: none !important;
                    }
                    vaadin-split-layout.vortex-responsive-master-detail[state="detail"] > [slot="primary"] {
                        display: none !important;
                    }
                    vaadin-split-layout.vortex-responsive-master-detail[state="detail"] > [slot="secondary"] {
                        flex: 1 1 100% !important;
                        width: 100% !important;
                        min-width: 100% !important;
                        display: flex !important;
                    }
                    vaadin-split-layout.vortex-responsive-master-detail::part(splitter) {
                        display: none !important;
                    }
                    vaadin-split-layout.vortex-responsive-master-detail[state="detail"] .mobile-back-button {
                        display: inline-flex !important;
                    }
                }""";
        getElement().appendChild(new Element("style").setText(css));
    }

    private void setDetail( VortexCrudPathToRouteResolver routeResolver, boolean creation) {
        detailContainer.removeAll();
        boolean showDetail = !routeResolver.isLastIndex(currentPathIndex) || creation;
        getElement().setAttribute("state", showDetail ? "detail" : "master");
        if (showDetail) {
            RouteRenderer<ModelClass, FieldType, RepositoryType> form = routeRenderer.form();
            Component component = form.factory().renderRoute(
                    context,
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
            pathVariables = new VortexCrudPathToRouteResolver(
                    pathForEntity,
                    configService.configuration().routes(),
                    dataStoreUtil);
            setDetail(pathVariables, false);
            ui.getPage().getHistory().pushState(null, pathForEntity);
        });
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = itemFactory.renderItem(routeRenderer,
                    item,
                    null,
                    context);
            component.addClassNames("master");
            component.getStyle().setMarginBottom("var(--vaadin-padding-s)");
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

        dataProvider = new GenericFilterableDataProvider<>(dataStore, routeRenderer.titleField(), routeRenderer.filters()).withConfigurableFilter();
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
