package com.github.appreciated.vortex_crud.core.ui.factories.menu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;

/**
 * Factory interface for creating menu action components.
 * Menu action components are additional UI elements that can be added to route headers
 * or menu bars to provide custom functionality like dropdowns, filters, or action buttons.
 *
 * @param <ModelClass> The type of entity
 * @param <FieldType> The type used to identify fields
 * @param <RepositoryType> The type of repository key
 */
@FunctionalInterface
public interface MenuActionComponentFactory<ModelClass, FieldType, RepositoryType>
        extends SerializableSupplier<Component> {

    /**
     * Creates and returns a component to be added to the menu.
     *
     * @return The component to add to the menu
     */
    @Override
    Component get();
}
