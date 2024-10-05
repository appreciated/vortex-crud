package com.github.appreciated.flow_cms.ui.route_renderer.cards;

import com.github.appreciated.flow_cms.config.model.ColumnConfig;
import com.github.appreciated.flow_cms.config.model.RouteConfig;
import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.ArrayList;
import java.util.List;

public class VirtualCardGridRenderer extends VirtualList<CardEntityList> {

    private final String table;
    private final List<ColumnConfig> renderColmuns;
    private int minWidth = 190;  // Mindestbreite der Karte (in Pixel)
    private int maxWidth = 300;  // Maximalbreite der Karte (in Pixel)
    private final DynamicEntityManagerService entityManagerService;

    private int currentNumberOfColumns = 3; // Standard-Anzahl der Spalten

    public VirtualCardGridRenderer(int i, RouteConfig config, DynamicEntityManagerService entityManagerService) {
        this.entityManagerService = entityManagerService;
        table = config.getTable();
        renderColmuns = config.getRender_configuration().getColumns();
        renderColmuns.forEach(configValue -> {

        });
        setSizeFull();
        initRenderer();
        initLazyLoadingDataProvider();
        this.addAttachListener(event -> new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            event.getUI().access(this::onBrowserWindowResize);
        }).start());
    }

    private void initRenderer() {
        setRenderer(new ComponentRenderer<>(item -> {
            HorizontalLayout wrapper = new HorizontalLayout();
            HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);
            layout.setWidthFull();
            for (GenericEntity entity : item.getList()) {  // Erstelle eine "Karte" für jedes Element
                VerticalLayout card = new VerticalLayout();
                card.setMaxWidth(maxWidth + "px");
                card.getStyle().set("border-radius", "8px");
                card.getStyle().set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.1)");
                card.getStyle().set("padding", "10px");
                card.getStyle().set("background-image", "linear-gradient(var(--lumo-contrast-5pct), var(--lumo-contrast-5pct))");

                Image image = new Image("https://via.placeholder.com/150", "Placeholder Image");
                Text label = new Text("Some Text");

                card.add(image, label);
                layout.add(card);
            }
            wrapper.add(layout);
            wrapper.getStyle().set("padding", "10px 10px 0px 10px");
            return wrapper;
        }));
    }

    private void initLazyLoadingDataProvider() {
        this.setDataProvider(DataProvider.fromCallbacks(
                        // Fetching the data from the database for a given offset and limit
                        query -> {
                            int offset = query.getOffset() * currentNumberOfColumns;
                            int limit = query.getLimit() * currentNumberOfColumns;
                            List<GenericEntity> items = entityManagerService.getRecordsFromTable(table, offset, limit);

                            List<CardEntityList> wrappers = new ArrayList<>();
                            for (int i = 0; i < items.size() / currentNumberOfColumns; i++) {
                                List<GenericEntity> group = items.subList(i, Math.min(i + currentNumberOfColumns, items.size()));
                                wrappers.add(new CardEntityList(group));
                            }

                            return wrappers.stream();
                        },
                        // Providing the total number of records for correct pagination
                        query -> {
                            int count = entityManagerService.count(table);
                            System.out.println(count);
                            System.out.println(currentNumberOfColumns);
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
            System.out.println(containerWidth);
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
