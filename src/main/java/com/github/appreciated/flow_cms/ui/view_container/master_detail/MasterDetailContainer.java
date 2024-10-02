package com.github.appreciated.flow_cms.ui.view_container.master_detail;

import com.github.appreciated.flow_cms.service.DynamicEntityManagerService;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.typesafe.config.ConfigObject;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;

import java.util.List;

public class MasterDetailContainer extends HorizontalLayout {

    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final VerticalLayout formLayout = new VerticalLayout();
    private final Binder<GenericEntity> binder = new Binder<>();

    public MasterDetailContainer(int initialIndex, ConfigObject config, DynamicEntityManagerService entityManagerService) {
        String table = config.get("table").render();

        // Virtual List mit Lazy Loading einrichten
        DataProvider<GenericEntity, Void> dataProvider = new CallbackDataProvider<>(
                query -> {
                    List<GenericEntity> items = entityManagerService.getRecordsFromTable(table, query.getOffset(), query.getLimit());
                    return items.stream();
                },
                query -> entityManagerService.count(table)
        );

        virtualList.setDataProvider(dataProvider);

        // Ereignis für Listenelementauswahl
        virtualList.setRenderer(new ComponentRenderer<>(entity -> {
            HorizontalLayout cardLayout = new HorizontalLayout();
            cardLayout.setMargin(true);

            VerticalLayout infoLayout = new VerticalLayout();
            infoLayout.setSpacing(false);
            infoLayout.setPadding(false);
            infoLayout.getElement().appendChild(ElementFactory.createStrong(entity.getFirstProperty()));
            infoLayout.add(new Div(new Text(entity.getSecondProperty())));

            return cardLayout;
        }));

        // Formular renderer, um dynamisch das Formular zu generieren
        //formRenderer.accept(binder, formLayout);

        // Layout konfigurieren
        add(virtualList, formLayout);
        setFlexGrow(1, virtualList);
        setFlexGrow(2, formLayout);
    }
}
