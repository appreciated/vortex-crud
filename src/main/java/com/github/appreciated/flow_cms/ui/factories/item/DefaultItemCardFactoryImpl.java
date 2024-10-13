package com.github.appreciated.flow_cms.ui.factories.item;

import com.github.appreciated.flow_cms.config.model.ItemFactoryConfig;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Default implementation of the EntityItemRenderer interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class DefaultItemCardFactoryImpl implements FlowCmsItemFactory {

    @Override
    public Component renderItem(ItemFactoryConfig itemFactoryConfig, GenericEntity entity, Integer maxWidth) {
        return new DefaultItem(itemFactoryConfig, entity, maxWidth);
    }
}