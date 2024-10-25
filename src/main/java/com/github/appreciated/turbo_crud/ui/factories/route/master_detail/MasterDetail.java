package com.github.appreciated.turbo_crud.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.config.TurboCrudPathToRouteResolver;
import com.github.appreciated.turbo_crud.config.model.ItemConfig;
import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.entity.EntityUtil;
import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudConfigService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.entity_manager.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.BETWEEN;

public class MasterDetail extends SplitLayout {

    private final ItemConfig itemConfiguration;
    private TurboCrudPathToRouteResolver pathVariables;
    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudItemFactory itemFactory;
    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final Config factoryConfig;
    private final Integer currentPathIndex;
    private final TurboCrudRouteFactoryRegistry routeFactory;
    private final TurboCrudConfigService configService;
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
                        TurboCrudIconFactory iconFactory) {
        this.currentPathIndex = currentPathIndex;
        this.routeFactory = routeFactory;
        this.configService = configService;

        route = routeResolver.getRouteForIndex(currentPathIndex);

        this.pathVariables = routeResolver;
        this.entityManagerService = entityManagerFactoryRegistry.getFactory(route.getRepository());
        this.factoryConfig = route.getConfiguration();
        itemConfiguration = ConfigBeanFactory.create(this.factoryConfig, ItemConfig.class);

        this.itemFactory = itemFactoryRegistry.getFactory(factoryConfig);
        assert route.getChildren() != null;
        assert route.getChildren().size() == 1;

        detailContainer = new VerticalLayout();
        detailContainer.setPadding(false);
        detailContainer.setHeightFull();
        detailContainer.setWidth("unset");
        detailContainer.getStyle().set("flex", "4 1 400px");

        HorizontalLayout header = new RouteHeader(route, iconFactory);

        Button addButton = new Button(VaadinIcon.PLUS.create());
        addButton.addClickListener(event -> onAdd());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout headerContainer = new HorizontalLayout(header, addButton);
        headerContainer.setPadding(false);
        headerContainer.setAlignItems(CENTER);
        headerContainer.setWidthFull();
        headerContainer.setJustifyContentMode(BETWEEN);

        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.LAZY);
        textField.setPrefixComponent(VaadinIcon.SEARCH.create());
        textField.setWidthFull();
        textField.setPlaceholder("Suchen...");
        textField.addValueChangeListener(event -> applyFilter(event.getValue())); // Suchfunktion hinzufügen

        HorizontalLayout searchContainer = new HorizontalLayout(textField);
        searchContainer.setPadding(false);
        searchContainer.setAlignItems(CENTER);
        searchContainer.setWidthFull();

        virtualList.setHeightFull();

        VerticalLayout masterContainer = new VerticalLayout(headerContainer, searchContainer, virtualList);
        masterContainer.getStyle().set("overflow", "hidden");
        masterContainer.setPadding(true);
        masterContainer.setSpacing(true);
        masterContainer.getStyle().set("flex", "1 1 200px");

        // Layout konfigurieren
        addToPrimary(masterContainer);
        addToSecondary(detailContainer);

        setSizeFull();

        initVirtualList();

        getStyle().set("overflow", "hidden");

        setDetail(pathVariables);
    }

    private void setDetail(TurboCrudPathToRouteResolver routeResolver) {
        detailContainer.removeAll();
        if (!routeResolver.isLastIndex(currentPathIndex)) {
            Route child = route.getChild();
            Component component = routeFactory.getFactory(child.getFactory()).renderRoute(
                    currentPathIndex + 1,
                    routeResolver,
                    true,
                    false
            );
            detailContainer.add(component);
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
            div.addClickListener(event -> {
                setNewActive(component);
                onItemClick(item);
            });
            return div;
        }));
        CallbackDataProvider<GenericEntity, String> baseDataProvider = DataProvider.fromFilteringCallbacks(
                query -> {
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        return entityManagerService.getRecordsFromTable(query.getOffset(), query.getLimit()).stream();
                    } else {
                        return entityManagerService.getRecordsFromTableWhereColumnLike(itemConfiguration.getTitleField(), filterText, query.getOffset(), query.getLimit()).stream();
                    }
                },
                query -> {
                    String filterText = query.getFilter().orElse("");
                    if (filterText.isEmpty()) {
                        return entityManagerService.count();
                    } else {
                        return entityManagerService.countWhereColumnLike(itemConfiguration.getTitleField(), filterText);
                    }
                }
        );
        dataProvider = baseDataProvider.withConfigurableFilter();
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
