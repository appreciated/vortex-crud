package com.github.appreciated.flow_cms.ui.factories.collection.item;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@CssImport("card-styles.css")
public class DefaultCollectionItemImpl extends HorizontalLayout {
    public DefaultCollectionItemImpl() {
        setWidthFull();
        addClassName("card");
    }
}

