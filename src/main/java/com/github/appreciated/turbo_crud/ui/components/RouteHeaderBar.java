package com.github.appreciated.turbo_crud.ui.components;

import com.github.appreciated.turbo_crud.config.model.Route;
import com.github.appreciated.turbo_crud.ui.factories.icon.TurboCrudIconFactory;
import com.github.appreciated.turbo_crud.ui.factories.route.DetailRouteSetting;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment.CENTER;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.BETWEEN;

public class RouteHeaderBar extends HorizontalLayout {
    public RouteHeaderBar(Route route,
                          DetailRouteSetting detailRouteSetting,
                          TurboCrudIconFactory iconFactory,
                          ComponentEventListener<ClickEvent<Button>> listener) {
        // HEADER
        HorizontalLayout header = new RouteHeader(route, iconFactory);

        Button addButton = new Button(VaadinIcon.PLUS.create());
        addButton.addClickListener(listener);
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(header, addButton);
        setPadding(false);
        setAlignItems(CENTER);
        setWidthFull();
        setJustifyContentMode(BETWEEN);

        // Back button
        Button back = new Button(VaadinIcon.ANGLE_LEFT.create(), event -> UI.getCurrent().getPage().getHistory().back());
        back.getStyle().set("font-size", "1.6em")
                .set("padding", "calc(var(--lumo-button-size) / 8)")
                .set("border-radius", "100%")
                .set("box-sizing", "content-box");

        assert detailRouteSetting != null;
        if (!detailRouteSetting.isWrapped()) {
            add(back);
        }

        if (!detailRouteSetting.isWrapped()) {
            add(back);
        }
        setAlignItems(Alignment.CENTER);
    }
}
