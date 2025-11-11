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
 * A route action that operates on one or more selected entities.
 * The action component will be disabled if no entities are selected.
 *
 * <p>Examples: Bulk Delete, Export Selected, Bulk Update, Assign To, Move To Category</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * MultiEntityRouteAction.<Task>builder()
 *     .componentFactory(() -> {
 *         Button btn = new Button("Delete Selected", VaadinIcon.TRASH.create());
 *         btn.addThemeVariants(ButtonVariant.LUMO_ERROR);
 *         return btn;
 *     })
 *     .handler(context -> {
 *         context.showConfirmationDialog(
 *             "Delete Tasks",
 *             "Delete " + context.getSelectedEntities().size() + " tasks?",
 *             () -> {
 *                 for (Task task : context.getSelectedEntities()) {
 *                     context.getDataStore().deleteRecordById(task.getId());
 *                 }
 *                 context.showSuccessNotification("Deleted tasks");
 *                 context.getRefreshCallback().run();
 *             }
 *         );
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
public class MultiEntityRouteAction<ModelClass> implements RouteAction<ModelClass> {

    /**
     * Factory that creates the component for this action.
     * Should return a component without click listeners.
     * The framework will automatically enable/disable based on selection.
     */
    private SerializableSupplier<Component> componentFactory;

    /**
     * Handler that executes when the component is clicked.
     * Only called when one or more entities are selected.
     * Receives the action context with access to all selected entities.
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
}
