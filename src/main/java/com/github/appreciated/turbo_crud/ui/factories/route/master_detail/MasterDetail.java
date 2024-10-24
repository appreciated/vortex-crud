package com.github.appreciated.turbo_crud.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.dialog.TurboCrudDialogFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.route.TurboCrudRouteFactoryRegistry;
import com.typesafe.config.Config;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.BETWEEN;

/**
 * A layout component implementing a Master-Detail pattern using a VirtualList and a detail view.
 * This renderer dynamically displays a list of entities and their detailed information side by side,
 * supporting click events to update the detail view based on the selected item.
 * It uses lazy loading for efficient data retrieval and rendering.
 */

public class MasterDetail extends SplitLayout {

    private TurboCrudPathToRouteResolver pathVariables;

    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudItemFactory itemFactory;

    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final Config factoryConfig;
    private final Integer currentPathIndex;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudConfigService configService;
    private final Route route;
    private final VerticalLayout detailLayout;

    private Component active;

    public MasterDetail(Integer currentPathIndex,
                        TurboCrudPathToRouteResolver routeResolver,
                        TurboCrudEntityManagerFactoryRegistry entityManagerFactoryRegistry,
                        TurboCrudItemFactoryRegistry itemFactoryRegistry,
                        TurboCrudRouteFactoryRegistry routeFactory,
                        TurboCrudConfigService configService,
                        TurboCrudIconFactory iconFactory) {
        this.currentPathIndex = currentPathIndex;
        this.routeFactory = routeFactory;
        this.configService = configService;

        route = routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.entityManagerService = entityManagerFactoryRegistry.getFactory(route.getRepository());
        this.factoryConfig = route.getConfiguration();
        this.itemFactory = itemFactoryRegistry.getFactory(factoryConfig);
        assert route.getChildren() != null;
        assert route.getChildren().size() == 1;

        detailLayout = new VerticalLayout();
        detailLayout.setPadding(false);
        detailLayout.setHeightFull();
        detailLayout.setWidth("unset");
        detailLayout.getStyle().set("flex", "4 1 400px");

        HorizontalLayout header = new RouteHeader(route, iconFactory);

        Button addButton = new Button(VaadinIcon.PLUS.create());
        addButton.addClickListener(event -> onAdd());
        addButton.addThemeVariants(LUMO_PRIMARY);

        HorizontalLayout headerContainer = new HorizontalLayout(header, addButton);
        headerContainer.setPadding(true);
        headerContainer.setAlignItems(CENTER);
        headerContainer.setWidthFull();
        headerContainer.setJustifyContentMode(BETWEEN);

        TextField textField = new TextField();
        textField.setWidthFull();
        HorizontalLayout searchContainer = new HorizontalLayout(textField);
        searchContainer.setPadding(false);
        searchContainer.getStyle().set("padding", "0px 5px 0 5px");
        searchContainer.setAlignItems(CENTER);
        searchContainer.setWidthFull();

        virtualList.setHeightFull();

        VerticalLayout listWrapper = new VerticalLayout(headerContainer,searchContainer, virtualList);
        listWrapper.getStyle().set("overflow", "hidden");
        listWrapper.setPadding(false);
        listWrapper.setSpacing(false);
        listWrapper.getStyle().set("flex", "1 1 200px");

        // Layout konfigurieren
        addToPrimary(listWrapper);
        addToSecondary(detailLayout);

        setSizeFull();

        initVirtualList();

        getStyle().set("overflow", "hidden");

        setDetail(pathVariables);
    }

    private void setDetail(TurboCrudPathToRouteResolver routeResolver) {
        detailLayout.removeAll();
        if (!routeResolver.isLastIndex(currentPathIndex)) {
            Route child = route.getChild();
            Component component = routeFactory.getFactory(child.getFactory()).renderRoute(
                    currentPathIndex + 1,
                    routeResolver,
                    true,
                    false
            );
            detailLayout.add(component);
        }
    }

    private void onAdd() {
        entityManagerService.insertRecord(new GenericEntity());
    }

    private void onItemClick(GenericEntity entity) {
        getUI().ifPresent(ui -> {
            String pathForEntity = pathVariables.getPathForEntity(currentPathIndex, entity);
            pathVariables = new TurboCrudPathToRouteResolver(routeFactory, pathForEntity, configService.getConfiguration().getRoutesConfig());
            setDetail(pathVariables);
            ui.getPage().getHistory().pushState(null, "/view/" + pathForEntity);
        });
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = itemFactory.renderItem(factoryConfig, item, null);
            component.addClassName("master");
            Div div = new Div(component);
            if (EntityUtil.getId(item).equals(pathVariables.getLastSegment())) {
                component.addClassName("active");
                setNewActive(component);
            }
            div.getStyle().set("padding", "5px 5px 0px 5px");
            div.addClickListener(event -> {
                setNewActive(component);
                onItemClick(item);
            });
            return div;
        }));
        this.virtualList.setDataProvider(DataProvider.fromCallbacks(
                query -> entityManagerService.getRecordsFromTable(query.getOffset(), query.getLimit()).stream(),
                query -> entityManagerService.count()
        ));
    }

    private void setNewActive(Component component) {
        if (active != null) {
            active.removeClassName("active");
        }
        component.addClassName("active");
        active = component;
    }
}
