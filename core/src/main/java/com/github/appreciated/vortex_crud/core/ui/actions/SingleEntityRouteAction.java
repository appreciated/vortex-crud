package com.github.appreciated.vortex_crud.core.ui.actions;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * A route action that requires exactly one entity to be selected.
 * The action is automatically enabled only when exactly one entity is selected.
 *
 * <p>Examples: Edit, Approve, Archive, View Details</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * SingleEntityRouteAction.<Task>builder()
 *     .componentFactory(() -> {
 *         Button btn = new Button("Approve", VaadinIcon.CHECK.create());
 *         btn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
 *         return btn;
 *     })
 *     .handler(context -> {
 *         Task task = context.getFirstSelectedEntity();
 *         task.setStatus("APPROVED");
 *         context.getDataStore().updateRecord(task);
 *         context.showSuccessNotification("Task approved!");
 *         context.getRefreshCallback().run();
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
public class SingleEntityRouteAction<FieldType, ModelClass> implements RouteAction<FieldType, ModelClass> {

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
     * Single entity actions are enabled only when exactly one entity is selected.
     *
     * @param context The current action context
     * @return true if exactly one entity is selected, false otherwise
     */
    @Override
    public boolean isEnabled(RouteActionContext<FieldType, ModelClass> context) {
        return context.getSelectionCount() == 1;
    }
}
