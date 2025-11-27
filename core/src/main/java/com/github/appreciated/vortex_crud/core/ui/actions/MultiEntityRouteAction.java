package com.github.appreciated.vortex_crud.core.ui.actions;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * A route action that requires one or more entities to be selected.
 * The action is automatically enabled only when at least one entity is selected.
 *
 * <p>Examples: Bulk Delete, Export Selected, Assign to User, Change Status</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * MultiEntityRouteAction.<Task>builder()
 *     .componentFactory(() -> {
 *         Button btn = new Button("Export Selected", VaadinIcon.DOWNLOAD.create());
 *         return btn;
 *     })
 *     .handler(context -> {
 *         List<Task> selectedTasks = context.getSelectedEntities();
 *         exportTasks(selectedTasks);
 *     })
 *     .build()
 * }</pre>
 *
 * @param <FieldType>  The type used to identify fields in the data store
 * @param <ModelClass> The type of entity
 */
@Accessors(fluent = true)
@Builder
@Getter
public class MultiEntityRouteAction<FieldType, ModelClass> implements RouteAction<FieldType, ModelClass> {

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

    /**
     * Minimum number of entities required for this action (defaults to 1)
     */
    @Builder.Default
    private final int minSelectionCount = 1;

    /**
     * Maximum number of entities allowed for this action (defaults to unlimited)
     */
    @Builder.Default
    private final int maxSelectionCount = Integer.MAX_VALUE;

    @Override
    public void handle(RouteActionContext<FieldType, ModelClass> context) {
        handler.accept(context);
    }

    /**
     * Multi entity actions are enabled when the selection count is within the specified range.
     *
     * @param context The current action context
     * @return true if the selection count is within the valid range, false otherwise
     */
    @Override
    public boolean isEnabled(RouteActionContext<FieldType, ModelClass> context) {
        int count = context.getSelectionCount();
        return count >= minSelectionCount && count <= maxSelectionCount;
    }
}
