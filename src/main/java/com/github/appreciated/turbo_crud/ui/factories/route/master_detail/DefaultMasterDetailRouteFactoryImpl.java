package com.github.appreciated.turbo_crud.ui.factories.route.master_detail;

import com.github.appreciated.turbo_crud.config.model.DetailFactory;
import com.github.appreciated.turbo_crud.config.model.ItemFactoryConfig;
import com.github.appreciated.turbo_crud.config.model.RouteConfig;
import com.github.appreciated.turbo_crud.service.GenericEntity;
import com.github.appreciated.turbo_crud.service.TurboCrudEntityManagerService;
import com.github.appreciated.turbo_crud.ui.components.RouteHeader;
import com.github.appreciated.turbo_crud.ui.factories.detail.TurboCrudDetailFactory;
import com.github.appreciated.turbo_crud.ui.factories.detail.TurboCrudDetailFactoryRegistry;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactory;
import com.github.appreciated.turbo_crud.ui.factories.item.TurboCrudItemFactoryRegistry;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

/**
 * A layout component implementing a Master-Detail pattern using a VirtualList and a detail view.
 * This renderer dynamically displays a list of entities and their detailed information side by side,
 * supporting click events to update the detail view based on the selected item.
 * It uses lazy loading for efficient data retrieval and rendering.
 */

public class DefaultMasterDetailRouteFactoryImpl extends SplitLayout {

    private final RouteConfig config;
    private final TurboCrudEntityManagerService entityManagerService;
    private final TurboCrudDetailFactoryRegistry detailFactoryRegistry;
    private final TurboCrudItemFactory itemFactory;
    private final ItemFactoryConfig factoryConfig;
    private final TurboCrudDetailFactory detailFactory;

    private final String table;
    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final VerticalLayout detailLayout = new VerticalLayout();

    private Component active;

    public DefaultMasterDetailRouteFactoryImpl(int currentEntityId,
                                               RouteConfig config,
                                               TurboCrudEntityManagerService entityManagerService,
                                               TurboCrudItemFactoryRegistry itemFactoryRegistry,
                                               TurboCrudDetailFactoryRegistry detailFactoryRegistry,
                                               TurboCrudIconFactory iconFactory) {
        this.config = config;
        this.entityManagerService = entityManagerService;
        this.detailFactoryRegistry = detailFactoryRegistry;
        this.factoryConfig = config.getItems();
        this.itemFactory = itemFactoryRegistry.getFactory(factoryConfig);
        this.table = config.getTable();
        DetailFactory detailFactoryConfig = config.getDetail();
        this.detailFactory = detailFactoryRegistry.getFactory(detailFactoryConfig.getFactory());

        detailLayout.setPadding(false);
        detailLayout.setHeightFull();
        detailLayout.setWidth("unset");
        detailLayout.getStyle().set("flex", "4 1 400px");

        HorizontalLayout header = new RouteHeader(config, iconFactory);
        header.setPadding(true);

        virtualList.setHeightFull();

        VerticalLayout listWrapper = new VerticalLayout(header, virtualList);
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
    }

    private void onItemClick(GenericEntity entity) {
        this.detailLayout.removeAll();
        Component component = detailFactory.renderDetail(config.getTable(),
                config.getTitle(),
                config.getDetail(),
                entity,
                true,
                false,
                detailFactoryRegistry
        );
        this.detailLayout.add(component);
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = itemFactory.renderItem(factoryConfig, item, null);
            component.addClassName("master");
            Div div = new Div(component);
            div.getStyle().set("padding", "5px 5px 0px 5px");
            div.addClickListener(event -> {
                if (active != null) {
                    active.removeClassName("active");
                }
                component.addClassName("active");
                active = component;
                onItemClick(item);
            });
            return div;
        }));
        this.virtualList.setDataProvider(DataProvider.fromCallbacks(
                query -> entityManagerService.getRecordsFromTable(table, query.getOffset(), query.getLimit()).stream(),
                query -> entityManagerService.count(table)
        ));
    }
}
