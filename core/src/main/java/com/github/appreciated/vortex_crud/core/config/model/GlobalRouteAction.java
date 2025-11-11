package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A global route action that doesn't require any entity selection.
 * Global actions are always enabled and can perform operations that don't depend on selected entities.
 *
 * <p>Examples: Export All, Generate Report, Import Data, Refresh, Settings</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * GlobalRouteAction.<Task>builder()
 *     .componentFactory(() -> {
 *         Button btn = new Button("Export All", VaadinIcon.DOWNLOAD.create());
 *         btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
 *         return btn;
 *     })
 *     .handler(context -> {
 *         List<Task> allTasks = context.getDataStore()
 *             .getRecordsFromTable(0, Integer.MAX_VALUE);
 *         exportToCSV(allTasks);
 *         context.showSuccessNotification("Exported " + allTasks.size() + " tasks");
 *     })
 *     .visible(true)
 *     .build()
 * }</pre>
 *
 * @param <ModelClass> The type of entity
 */
@Accessors(fluent = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class GlobalRouteAction<ModelClass> implements RouteAction<ModelClass> {

    /**
     * Factory that creates the component for this action.
     * Should return a component without click listeners.
     */
    private SerializableSupplier<Component> componentFactory;

    /**
     * Handler that executes when the component is clicked.
     * Receives the action context with access to the data store and UI utilities.
     */
    private SerializableConsumer<CustomRouteActionContext<ModelClass>> handler;

    /**
     * Whether this action should be visible.
     */
    @Builder.Default
    private boolean visible = true;

    @Override
    public void handle(CustomRouteActionContext<ModelClass> context) {
        handler.accept(context);
    }

    @Override
    public boolean isEnabled(CustomRouteActionContext<ModelClass> context) {
        // Global actions are always enabled
        return true;
    }
}
