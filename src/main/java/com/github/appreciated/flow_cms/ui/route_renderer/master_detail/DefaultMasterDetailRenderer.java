package com.github.appreciated.flow_cms.ui.route_renderer.master_detail;

import com.github.appreciated.flow_cms.config.model.DetailRenderer;
import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;
import com.github.appreciated.flow_cms.config.model.RenderConfig;
import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.entity_detail.FlowCmsEntityDetailRenderer;
import com.github.appreciated.flow_cms.ui.entity_detail.FlowCmsEntityDetailRendererFactory;
import com.github.appreciated.flow_cms.ui.entity_item_renderer.card.FlowCmsEntityItemRenderer;
import com.github.appreciated.flow_cms.ui.entity_item_renderer.card.FlowCmsEntityItemRendererFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
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

public class DefaultMasterDetailRenderer extends SplitLayout {

    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final VerticalLayout detailLayout = new VerticalLayout();
    private final FlowCmsEntityItemRenderer entityItemRenderer;
    private final ItemRendererConfig itemRenderer;

    private final RouteConfig config;
    private final DynamicEntityManagerService entityManagerService;
    private final String table;
    private final FlowCmsEntityDetailRenderer detailRenderer;

    public DefaultMasterDetailRenderer(int initialIndex, RouteConfig config, DynamicEntityManagerService entityManagerService, FlowCmsEntityItemRendererFactory entityCardRendererFactory, FlowCmsEntityDetailRendererFactory detailRendererFactory) {
        this.config = config;

        this.entityManagerService = entityManagerService;
        RenderConfig renderConfiguration = config.getRenderConfiguration();
        this.itemRenderer = renderConfiguration.getItemRenderer();
        this.entityItemRenderer = entityCardRendererFactory.getRenderer(itemRenderer);
        this.table = config.getTable();

        DetailRenderer detailRendererConfig = renderConfiguration.getDetailRenderer();
        this.detailRenderer = detailRendererFactory.getRenderer(detailRendererConfig);

        detailLayout.setPadding(false);
        detailLayout.setHeightFull();
        detailLayout.setWidth("unset");
        detailLayout.getStyle().set("flex", "4 1 400px");

        HorizontalLayout header = new HorizontalLayout(new H2(getTranslation(config.getTitle())));
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
        this.detailLayout.add(detailRenderer.renderDetail(config, entity));
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = entityItemRenderer.renderItem(itemRenderer, item, null);
            Div div = new Div(component);
            div.getStyle().set("padding", "5px 5px 0px 5px");
            div.addClickListener(event -> onItemClick(item));
            return div;
        }));
        this.virtualList.setDataProvider(DataProvider.fromCallbacks(
                query -> entityManagerService.getRecordsFromTable(table, query.getOffset(), query.getLimit()).stream(),
                query -> entityManagerService.count(table)
        ));
    }
}
