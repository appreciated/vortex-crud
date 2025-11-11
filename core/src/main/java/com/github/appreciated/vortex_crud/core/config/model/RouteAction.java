package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;

import java.io.Serializable;

/**
 * Base interface for all route actions.
 * Route actions are custom interactive elements (buttons, menu items, etc.) that can be added to routes.
 *
 * <p>Implementations should provide a component factory that creates the UI element,
 * and register appropriate click listeners that receive the action context.</p>
 *
 * @param <ModelClass> The type of entity
 */
public interface RouteAction<ModelClass> extends Serializable {

    /**
     * Factory that creates the component for this action.
     * The factory receives the action context and should return a fully configured component
     * with click listeners registered.
     *
     * <p>Example:</p>
     * <pre>{@code
     * context -> {
     *     Button button = new Button("Export", VaadinIcon.DOWNLOAD.create());
     *     button.addClickListener(e -> {
     *         // Access context here
     *         int count = context.getDataStore().count();
     *         context.showSuccessNotification("Exported " + count + " items");
     *     });
     *     return button;
     * }
     * }</pre>
     *
     * @return The component factory
     */
    SerializableSupplier<Component> componentFactory(CustomRouteActionContext<ModelClass> context);

    /**
     * Whether this action should be visible.
     * Can be used for conditional visibility based on permissions or other criteria.
     *
     * @return true if the action should be visible
     */
    default boolean visible() {
        return true;
    }
}
