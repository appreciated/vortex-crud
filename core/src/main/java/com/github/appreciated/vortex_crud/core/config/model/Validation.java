package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;

/**
 * Base interface for field validation configurations.
 * Allows for applying validation rules to Vaadin components.
 */
public interface Validation {

    /**
     * Apply the validation to a Vaadin component.
     *
     * @param component the component to apply validation to
     */
    void applyToComponent(Component component);

    /**
     * Check if this validation is applicable to the given component type.
     *
     * @param componentClass the class of the component
     * @return true if the validation can be applied to this component type
     */
    boolean isApplicableToComponent(Class<? extends Component> componentClass);
}

