package com.github.appreciated.turbo_crud.ui.factories.item;

import com.github.appreciated.turbo_crud.model.GenericEntity;
import com.typesafe.config.Config;
import com.vaadin.flow.component.Component;

/**
 * Default implementation of the {@link TurboCrudItemFactory} interface for rendering entity items as cards.
 * This renderer supports displaying images, titles, and descriptions in a card layout with customizable styling.
 */

public class DefaultItemCardFactoryImpl implements TurboCrudItemFactory {

    @Override
    public Component renderItem(Config itemFactoryConfig, GenericEntity entity, Integer maxWidth){
        return new DefaultItem(itemFactoryConfig, entity, maxWidth);
    }
}