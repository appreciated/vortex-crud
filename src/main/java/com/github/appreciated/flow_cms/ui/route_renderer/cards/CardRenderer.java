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
import com.vaadin.flow.data.renderer.ComponentRenderer;

public class CardRenderer extends VirtualList<CardRendererWrapper> {

    private int minWidth = 100;  // Mindestbreite der Karte (in Pixel)
    private int maxWidth = 300;  // Maximalbreite der Karte (in Pixel)

    public CardRenderer(int i, ConfigObject config, DynamicEntityManagerService entityManagerService) {
        // Setze Flexbox-Layout für die virtuelle Liste
        this.getStyle().set("display", "flex");
        this.getStyle().set("flex-wrap", "wrap");
        this.getStyle().set("gap", "10px");  // Optionaler Abstand zwischen den Karten
        this.getStyle().set("justify-content", "center");  // Karten zentrieren

        // Größe der Liste auf 100% setzen
        setWidthFull();
        setHeightFull();

        setRenderer(new ComponentRenderer<>(item -> {
            HorizontalLayout layout = new HorizontalLayout();
            for (GenericEntity entity : item.getList()) {// Erstelle eine "Karte" für jedes Element
                VerticalLayout card = new VerticalLayout();

                card.getStyle().set("width", "var(--card-width)");
                card.getStyle().set("border", "1px solid #ccc");
                card.getStyle().set("border-radius", "8px");
                card.getStyle().set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.1)");
                card.getStyle().set("padding", "10px");
                card.getStyle().set("background-color", "#fff");

                Image image = new Image("https://via.placeholder.com/150", "Placeholder Image");
                Text label = new Text(item.toString());

                card.add(image, label);
                layout.add(card);
            }
            return layout;
        }));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        attachEvent.getUI().getPage().addBrowserWindowResizeListener(e -> updateLayout());
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
        System.out.println();
    }
}
