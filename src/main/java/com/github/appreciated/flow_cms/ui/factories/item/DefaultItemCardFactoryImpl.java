package com.github.appreciated.flow_cms.ui.factories.item;

import com.github.appreciated.flow_cms.config.model.ItemFactoryConfig;
import com.github.appreciated.flow_cms.service.GenericEntity;
import com.vaadin.flow.component.Component;

/**
 * Default implementation of the {@link TurboCrudItemFactory} interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class DefaultItemCardFactoryImpl implements TurboCrudItemFactory {

    @Override
    public Component renderItem(ItemFactoryConfig itemFactoryConfig, GenericEntity entity, Integer maxWidth) {
        return new DefaultItem(itemFactoryConfig, entity, maxWidth);
    }
}