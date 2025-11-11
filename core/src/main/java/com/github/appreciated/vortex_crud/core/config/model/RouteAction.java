package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;

import java.io.Serializable;

/**
 * Base interface for all route actions.
 * Route actions are custom interactive elements (buttons, menu items, etc.) that can be added to routes.
 *
 * <p>The framework handles the click event registration and passes the context to the handler.
 * This provides a clean separation between component creation and action logic.</p>
 *
 * @param <ModelClass> The type of entity
 */
public interface RouteAction<ModelClass> extends Serializable {

    /**
     * Factory that creates the component for this action.
     * Should return a component (Button, MenuItem, etc.) without click listeners.
     * The framework will register the click listener and invoke the handler.
     *
     * <p>Example:</p>
     * <pre>{@code
     * () -> {
     *     Button button = new Button("Export", VaadinIcon.DOWNLOAD.create());
     *     button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
     *     return button;
     * }
     * }</pre>
     *
     * @return The component factory
     */
    SerializableSupplier<Component> componentFactory();

    /**
     * Handler that executes when the component is clicked.
     * Receives the action context with access to the data store, selected entities,
     * and UI utilities.
     *
     * <p>Example:</p>
     * <pre>{@code
     * context -> {
     *     int count = context.getDataStore().count();
     *     context.showSuccessNotification("Exported " + count + " items");
     * }
     * }</pre>
     *
     * @param context The action context
     */
    void handle(CustomRouteActionContext<ModelClass> context);

    /**
     * Whether this action should be visible.
     * Can be used for conditional visibility based on permissions or other criteria.
     *
     * @return true if the action should be visible
     */
    default boolean visible() {
        return true;
    }

    /**
     * Whether this action should be enabled based on the current context.
     * This is checked before rendering and determines if the component is enabled.
     * Default implementation always returns true (always enabled).
     *
     * <p>Action implementations should override this to provide selection-based enablement.</p>
     *
     * @param context The action context with selected entities
     * @return true if the action should be enabled
     */
    default boolean isEnabled(CustomRouteActionContext<ModelClass> context) {
        return true;
    }
}
