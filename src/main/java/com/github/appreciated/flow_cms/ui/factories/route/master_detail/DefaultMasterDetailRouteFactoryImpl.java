package com.github.appreciated.flow_cms.ui.factories.route.master_detail;

import com.github.appreciated.flow_cms.config.model.DetailFactory;
import com.github.appreciated.flow_cms.config.model.FactoryConfig;
import com.github.appreciated.flow_cms.config.model.ItemFactoryConfig;
import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.components.RouteHeader;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactory;
import com.github.appreciated.flow_cms.ui.factories.detail.FlowCmsDetailFactoryRegistry;
import com.github.appreciated.flow_cms.ui.factories.icon.FlowCmsIconFactory;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemFactory;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemFactoryRegistry;
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

    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final VerticalLayout detailLayout = new VerticalLayout();
    private final FlowCmsItemFactory itemFactory;
    private final ItemFactoryConfig factoryConfig;

    private final RouteConfig config;
    private final FlowCmsEntityManagerService entityManagerService;
    private final FlowCmsDetailFactoryRegistry detailFactoryRegistry;
    private final String table;
    private final FlowCmsDetailFactory detailFactory;
    private Component active;

    public DefaultMasterDetailRouteFactoryImpl(int currentEntityId,
                                               RouteConfig config,
                                               FlowCmsEntityManagerService entityManagerService,
                                               FlowCmsItemFactoryRegistry itemFactoryRegistry,
                                               FlowCmsDetailFactoryRegistry detailFactoryRegistry,
                                               FlowCmsIconFactory iconFactory) {
        this.config = config;

        this.entityManagerService = entityManagerService;
        this.detailFactoryRegistry = detailFactoryRegistry;
        FactoryConfig factoryConfiguration = config.getFactoryConfiguration();
        this.factoryConfig = factoryConfiguration.getItemFactory();
        this.itemFactory = itemFactoryRegistry.getFactory(factoryConfig);
        this.table = config.getTable();

        DetailFactory detailFactoryConfig = factoryConfiguration.getDetailFactory();
        this.detailFactory = detailFactoryRegistry.getFactory(detailFactoryConfig.getType());

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
                config.getFactoryConfiguration().getDetailFactory(),
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
