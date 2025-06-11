package com.github.appreciated.vortex_crud.core.ui.factories.form.elements.collection.item;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("vortex-crud-default-card-item-styles.css")
public class DefaultCollectionItem extends Card {

    private final VerticalLayout contentLayout;
    private final VerticalLayout actionsLayout;

    public DefaultCollectionItem() {
        setWidthFull();
        addClassNames("hoverable");
        getStyle().set("--vaadin-card-padding", "var(--lumo-space-s)");

        contentLayout = new VerticalLayout();
        contentLayout.setPadding(false);
        actionsLayout = new VerticalLayout();
        actionsLayout.setPadding(false);
        actionsLayout.getElement().getStyle().set("flex", "0 0 28px");

        contentLayout.addClassName("content");
        actionsLayout.addClassName("actions");

        add(new HorizontalLayout(contentLayout, actionsLayout));
    }

    public void addContent(Component content) {
        contentLayout.add(content);
    }

    public void addActions(Component actions) {
        actionsLayout.add(actions);
    }

    public void clearContent() {
        contentLayout.removeAll();
    }

    public void clearActions() {
        actionsLayout.removeAll();
    }

    public VerticalLayout getContent() {
        return contentLayout;
    }

    public VerticalLayout getActions() {
        return actionsLayout;
    }

}
