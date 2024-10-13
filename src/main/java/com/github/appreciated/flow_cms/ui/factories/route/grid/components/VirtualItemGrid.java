package com.github.appreciated.flow_cms.ui.factories.route.grid.components;

import com.github.appreciated.flow_cms.config.model.ItemFactoryConfig;
import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.entity.EntityUtil;
import com.github.appreciated.flow_cms.service.FlowCmsEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemFactory;
import com.github.appreciated.flow_cms.ui.factories.item.FlowCmsItemFactoryRegistry;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * A custom VirtualList implementation for rendering entity items in a responsive grid layout with lazy loading.
 * This renderer dynamically adjusts the number of columns based on the browser window size and supports configuring card widths.
 * It also provides functionality for item click events and loading data from the database using a lazy loading approach.
 */

public class VirtualItemGrid extends VirtualList<EntityItemList> {

    private final String table;
    private final FlowCmsItemFactory itemFactory;
    private final ItemFactoryConfig factoryConfig;
    private final String route;
    private final FlowCmsEntityManagerService entityManagerService;
    private int minWidth = 190;  // Mindestbreite der Karte (in Pixel)
    private int maxWidth = 300;  // Maximalbreite der Karte (in Pixel)
    private int currentNumberOfColumns = -1;

    public VirtualItemGrid(int i,
                           String route,
                           RouteConfig config,
                           FlowCmsEntityManagerService entityManagerService,
                           FlowCmsItemFactoryRegistry itemFactoryRegistry) {
        this.route = route;
        this.entityManagerService = entityManagerService;
        table = config.getTable();
        factoryConfig = config.getFactoryConfiguration().getItemFactory();
        this.itemFactory = itemFactoryRegistry.getFactory(config.getFactoryConfiguration().getItemFactory());
        setSizeFull();
        this.addAttachListener(event -> {
            if (event.isInitialAttach()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    event.getUI().access(this::onBrowserWindowResize);
                }).start();
            }
        });
    }

    private void initRenderer() {
        setRenderer(new ComponentRenderer<>(item -> {
            HorizontalLayout wrapper = new HorizontalLayout();
            HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);
            layout.setWidthFull();
            for (GenericEntity entity : item.getList()) {
                Div div = new Div(itemFactory.renderItem(factoryConfig, entity, maxWidth));
                div.getStyle().set("display", "flex");
                div.addClickListener(event -> onItemClick(entity));
                layout.add(div);
            }
            wrapper.add(layout);
            wrapper.getStyle().set("padding", "10px 10px 0px 10px");
            return wrapper;
        }));
    }

    private void onItemClick(GenericEntity entity) {
        getUI().ifPresent(ui -> ui.navigate("/view/" + route + "/" + EntityUtil.getId(entity)));
    }

    private void initLazyLoadingDataProvider() {
        this.setDataProvider(DataProvider.fromCallbacks(
                        // Fetching the data from the database for a given offset and limit
                        query -> {
                            int offset = query.getOffset() * currentNumberOfColumns;
                            int limit = query.getLimit() * currentNumberOfColumns;
                            List<GenericEntity> items = entityManagerService.getRecordsFromTable(table, offset, limit);

                            List<EntityItemList> wrappers = new ArrayList<>();
                            for (int i = 0; i < items.size() / currentNumberOfColumns; i++) {
                                List<GenericEntity> group = items.subList(i, Math.min(i + currentNumberOfColumns, items.size()));
                                wrappers.add(new EntityItemList(group));
                            }

                            return wrappers.stream();
                        },
                        // Providing the total number of records for correct pagination
                        query -> {
                            int count = entityManagerService.count(table);
                            //System.out.println(count);
                            //System.out.println(currentNumberOfColumns);
                            return count / currentNumberOfColumns;
                        }
                )
        );
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attachEvent.getUI().getPage().addBrowserWindowResizeListener(e -> onBrowserWindowResize());
    }

    /**
     * Setzt die minimale und maximale Breite für die Karten.
     *
     * @param minWidth Mindestbreite in Pixel
     * @param maxWidth Maximalbreite in Pixel
     */
    public void setCardWidth(int minWidth, int maxWidth) {
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
        onBrowserWindowResize();
    }

    /**
     * Methode zur dynamischen Anpassung des Layouts basierend auf der Breite des Containers.
     */
    private void onBrowserWindowResize() {
        PendingJavaScriptResult containerWidthResult = getElement().executeJs("return $0.clientWidth;", getElement());
        containerWidthResult.then(Double.class, containerWidth -> {
            if (containerWidth != null) {
                // Berechne die Anzahl der Spalten basierend auf der Breite des Containers
                int newNumberOfColumns = Math.max(1, (int) Math.floor(containerWidth / minWidth));
                if (newNumberOfColumns != currentNumberOfColumns) {
                    currentNumberOfColumns = newNumberOfColumns;
                    initRenderer();
                    initLazyLoadingDataProvider();
                }
            }
        });
    }
}
