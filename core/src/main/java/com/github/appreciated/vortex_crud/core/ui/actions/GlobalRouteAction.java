package com.github.appreciated.vortex_crud.core.ui.actions;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * A route action that does not require any entity selection.
 * Global actions are always enabled and can perform operations
 * that don't depend on specific entity selections.
 *
 * <p>Examples: Export All, Generate Report, Bulk Import, Refresh Data</p>
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
 *         List<Task> allTasks = context.getDataStore()
 *             .getRecordsFromTable(0, Integer.MAX_VALUE);
 *         exportTasks(allTasks);
 *         context.showSuccessNotification("Exported " + allTasks.size() + " tasks");
 *     })
 *     .build()
 * }</pre>
 *
 * @param <FieldType> The type used to identify fields in the data store
 * @param <ModelClass> The type of entity
 */
@Accessors(fluent = true)
@Builder
@Getter
public class GlobalRouteAction<FieldType, ModelClass> implements RouteAction<FieldType, ModelClass> {

    /**
     * Factory for creating the component (button, menu item, etc.)
     */
    private final SerializableSupplier<Component> componentFactory;

    /**
     * Handler that executes when the component is clicked
     */
    private final SerializableConsumer<RouteActionContext<FieldType, ModelClass>> handler;

    /**
     * Determines if this action should be visible (defaults to true)
     */
    @Builder.Default
    private final boolean visible = true;

    @Override
    public void handle(RouteActionContext<FieldType, ModelClass> context) {
        handler.accept(context);
    }

    /**
     * Global actions are always enabled as they don't depend on entity selection.
     *
     * @param context The current action context
     * @return Always returns true
     */
    @Override
    public boolean isEnabled(RouteActionContext<FieldType, ModelClass> context) {
        return true;
    }
}
