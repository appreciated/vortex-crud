package com.github.appreciated.vortex_crud.core.ui.actions;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableSupplier;

import java.io.Serializable;

/**
 * Base interface for custom route actions.
 * Route actions allow developers to add custom buttons or components to routes
 * with full access to the data store and selected entities.
 *
 * <p>Implementations should separate component creation (factory) from action logic (handler).
 * The framework automatically registers click listeners and manages component state.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * GlobalRouteAction.<Task>builder()
 *     .componentFactory(() -> {
 *         Button btn = new Button("Export All", VaadinIcon.DOWNLOAD.create());
 *         btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
 *         return btn;
 *     })
 *     .handler(context -> {
 *         List<Task> tasks = context.getDataStore()
 *             .getRecordsFromTable(0, Integer.MAX_VALUE);
 *         // Export logic here
 *     })
 *     .build()
 * }</pre>
 *
 * @param <FieldType>  The type used to identify fields in the data store
 * @param <ModelClass> The type of entity
 */
public interface RouteAction<FieldType, ModelClass> extends Serializable {

    /**
     * Factory for creating the component (button, menu item, etc.).
     * This is called once when the route is rendered.
     * The framework will register click listeners on the returned component.
     *
     * @return A supplier that creates the component
     */
    SerializableSupplier<Component> componentFactory();

    /**
     * Handler that executes when the component is clicked.
     * This is called with the current context, including selected entities.
     *
     * @param context The action context with data store access and selected entities
     */
    void handle(RouteActionContext<FieldType, ModelClass> context);

    /**
     * Determines if this action should be visible.
     * Defaults to true.
     *
     * @return true if the action should be visible, false otherwise
     */
    default boolean visible() {
        return true;
    }

    /**
     * Determines if the component should be enabled given the current context.
     * This is called whenever the selection state changes.
     * Subclasses can override this to implement custom enablement logic.
     *
     * @param context The current action context with selection state
     * @return true if the component should be enabled, false otherwise
     */
    boolean isEnabled(RouteActionContext<FieldType, ModelClass> context);
}
