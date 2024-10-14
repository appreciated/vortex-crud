package com.github.appreciated.turbo_crud.ui.factories.elements.collection.item;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("card-styles.css")
public class DefaultCollectionItemImpl extends HorizontalLayout {

    private final VerticalLayout contentLayout;
    private final VerticalLayout actionsLayout;

    public DefaultCollectionItemImpl() {
        setWidthFull();
        addClassNames("card", "collection");

        contentLayout = new VerticalLayout();
        contentLayout.setPadding(false);
        contentLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        actionsLayout = new VerticalLayout();
        actionsLayout.setPadding(false);
        actionsLayout.getElement().getStyle().set("flex", "0 0 28px");

        contentLayout.addClassName("content");
        actionsLayout.addClassName("actions");

        add(contentLayout, actionsLayout);
        expand(contentLayout);
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
