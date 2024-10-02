package com.github.appreciated.flow_cms.ui.route_renderer.cards;

import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.typesafe.config.ConfigObject;
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

public class CardRenderer extends VirtualList<CardRendererWrapper> {

    private final String table;
    private int minWidth = 100;  // Mindestbreite der Karte (in Pixel)
    private int maxWidth = 300;  // Maximalbreite der Karte (in Pixel)
    private final DynamicEntityManagerService entityManagerService;

    public CardRenderer(int i, ConfigObject config, DynamicEntityManagerService entityManagerService) {
        this.entityManagerService = entityManagerService;
        table = config.toConfig().getString("table");
        setSizeFull();
        initRenderer();
        initLazyLoadingDataProvider();
    }

    private void initRenderer() {
        setRenderer(new ComponentRenderer<>(item -> {
            HorizontalLayout wrapper = new HorizontalLayout();
            HorizontalLayout layout = new HorizontalLayout();
            layout.setSpacing(true);
            layout.setWidthFull();
            for (GenericEntity entity : item.getList()) {  // Erstelle eine "Karte" für jedes Element
                VerticalLayout card = new VerticalLayout();
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
        DataProvider<CardRendererWrapper, ?> dataProvider = DataProvider.fromCallbacks(
                // Fetching the data from the database for a given offset and limit
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit() * getCurrentNumberOfColumns();
                    List<GenericEntity> items = entityManagerService.getRecordsFromTable(table, offset, limit);
                    // Gruppiere jeweils 3 GenericEntity-Objekte in einen CardRendererWrapper
                    List<CardRendererWrapper> wrappers = new ArrayList<>();
                    for (int i = 0; i < items.size() / getCurrentNumberOfColumns(); i ++) {
                        // Hole drei Elemente oder die verbleibenden, falls weniger als drei übrig sind
                        List<GenericEntity> group = items.subList(i, Math.min(i + getCurrentNumberOfColumns(), items.size()));
                        wrappers.add(new CardRendererWrapper(group));
                    }

                    return wrappers.stream();
                },
                // Providing the total number of records for correct pagination
                query -> entityManagerService.count(table) / getCurrentNumberOfColumns()
        );

        // Assigning the data provider to the virtual list
        this.setDataProvider(dataProvider);
    }

    private static int getCurrentNumberOfColumns() {
        return 3;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attachEvent.getUI().getPage().addBrowserWindowResizeListener(e -> {
            updateLayout();
        });
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
        updateLayout();
    }

    /**
     * Methode zur dynamischen Anpassung des Layouts basierend auf der Breite des Containers.
     */
    private void updateLayout() {
        PendingJavaScriptResult containerWidth = getElement().executeJs("$0.$server.displaySize($1.clientWidth, $1.clientHeight)", getElement(), getElement());
        containerWidth.then(jsonValue -> {
            // NEED the current Element WIdth
            System.out.println();
        });
    }
}
