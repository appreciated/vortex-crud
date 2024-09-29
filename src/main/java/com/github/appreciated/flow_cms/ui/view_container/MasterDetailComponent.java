package com.github.appreciated.flow_cms.ui.view_container;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.details.Details;
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
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MasterDetailComponent<GenericEntity> extends HorizontalLayout {

    private final VirtualList<GenericEntity> virtualList = new VirtualList<>();
    private final VerticalLayout formLayout = new VerticalLayout();
    private final Binder<GenericEntity> binder = new Binder<>();

    public MasterDetailComponent(int initialIndex,
                                 Supplier<List<GenericEntity>> lazyLoadableRepository,
                                 BiConsumer<Binder<GenericEntity>, VerticalLayout> formRenderer) {

        // Virtual List mit Lazy Loading einrichten
        DataProvider<GenericEntity, Void> dataProvider = new CallbackDataProvider<>(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    List<GenericEntity> items = lazyLoadableRepository.get().subList(offset, Math.min(offset + limit, lazyLoadableRepository.get().size()));
                    return items.stream();
                },
                query -> lazyLoadableRepository.get().size()
        );

        virtualList.setDataProvider(dataProvider);

        // Ereignis für Listenelementauswahl
        virtualList.setRenderer(new ComponentRenderer<>(
                person -> {
                    HorizontalLayout cardLayout = new HorizontalLayout();
                    cardLayout.setMargin(true);
/*
                    VerticalLayout infoLayout = new VerticalLayout();
                    infoLayout.setSpacing(false);
                    infoLayout.setPadding(false);
                    infoLayout.getElement().appendChild(
                            ElementFactory.createStrong(person.getFullName()));
                    infoLayout.add(new Div(new Text(person.getProfession())));

                    VerticalLayout contactLayout = new VerticalLayout();
                    contactLayout.setSpacing(false);
                    contactLayout.setPadding(false);
                    contactLayout.add(new Div(new Text(person.getEmail())));
                    contactLayout.add(new Div(new Text(person.getAddress().getPhone())));
                    infoLayout.add(new Details("Contact information", contactLayout));
                    cardLayout.add(avatar, infoLayout);
                    return cardLayout;*/

                    return cardLayout;
                }));

        // Formular renderer, um dynamisch das Formular zu generieren
        formRenderer.accept(binder, formLayout);

        // Layout konfigurieren
        add(virtualList, formLayout);
        setFlexGrow(1, virtualList);
        setFlexGrow(2, formLayout);

        // Optional: Setze den initialen Index (falls vorhanden)
        if (initialIndex >= 0 && initialIndex < lazyLoadableRepository.get().size()) {
            Optional<GenericEntity> initialItem = Optional.ofNullable(lazyLoadableRepository.get().get(initialIndex));
            initialItem.ifPresent(item -> binder.setBean(item));
        }
    }
}
