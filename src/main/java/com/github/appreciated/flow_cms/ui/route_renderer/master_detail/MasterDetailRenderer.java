package com.github.appreciated.flow_cms.ui.route_renderer.master_detail;

import com.github.appreciated.flow_cms.config.model.ItemRendererConfig;
import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.entity_item_renderer.card.EntityItemRenderer;
import com.github.appreciated.flow_cms.ui.entity_item_renderer.card.FlowCmsEntityItemRendererFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class MasterDetailRenderer extends HorizontalLayout {

    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final VerticalLayout detailLayout = new VerticalLayout();
    private final Binder<GenericEntity> binder = new Binder<>();
    private final EntityItemRenderer entityItemRenderer;
    private final ItemRendererConfig itemRenderer;
    private final DynamicEntityManagerService entityManagerService;
    private final String table;

    public MasterDetailRenderer(int initialIndex, RouteConfig config, DynamicEntityManagerService entityManagerService, FlowCmsEntityItemRendererFactory entityCardRendererFactory) {
        this.entityManagerService = entityManagerService;
        this.itemRenderer = config.getRender_configuration().getItem_renderer();
        this.entityItemRenderer = entityCardRendererFactory.getRenderer(itemRenderer);
        this.table = config.getTable();

        detailLayout.setHeightFull();
        detailLayout.setWidth("unset");
        setFlexGrow(3, detailLayout);

        virtualList.setHeightFull();
        setFlexGrow(1, virtualList);

        // Layout konfigurieren
        add(virtualList, detailLayout);
        setSizeFull();

        initVirtualList();
    }

    private void onItemClick(GenericEntity entity) {
        System.out.println(entity.get("id"));
    }

    public void initVirtualList() {
        this.virtualList.setRenderer(new ComponentRenderer<>(item -> {
            Component component = entityItemRenderer.renderItem(itemRenderer, item, null);
            Div div = new Div(component);
            div.getStyle().set("padding","5px 5px 0px 5px");
            div.addClickListener(event -> onItemClick(item));
            return div;
        }));
        this.virtualList.setDataProvider(DataProvider.fromCallbacks(
                query -> entityManagerService.getRecordsFromTable(table, query.getOffset(), query.getLimit()).stream(),
                query -> entityManagerService.count(table)
        ));
    }
}
