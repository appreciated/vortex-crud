package com.github.appreciated.vortex_crud.core.config.model;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializableSupplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A route action that operates on one or more selected entities.
 * The action button will be disabled if no entities are selected.
 *
 * <p>Examples: Bulk Delete, Export Selected, Bulk Update, Assign To, Move To Category</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * MultiEntityRouteAction.<Task>builder()
 *     .componentFactory(context -> {
 *         Button bulkDeleteBtn = new Button("Delete Selected", VaadinIcon.TRASH.create());
 *         bulkDeleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
 *
 *         // Button is automatically enabled/disabled based on selection
 *         bulkDeleteBtn.setEnabled(!context.getSelectedEntities().isEmpty());
 *
 *         bulkDeleteBtn.addClickListener(e -> {
 *             context.showConfirmationDialog(
 *                 "Delete Tasks",
 *                 "Delete " + context.getSelectedEntities().size() + " tasks?",
 *                 () -> {
 *                     for (Task task : context.getSelectedEntities()) {
 *                         context.getDataStore().deleteRecordById(task.getId());
 *                     }
 *                     context.showSuccessNotification("Deleted tasks");
 *                     context.getRefreshCallback().run();
 *                 }
 *             );
 *         });
 *         return bulkDeleteBtn;
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
     * The factory receives the action context and should return a component with click listeners registered.
     * The component should check !context.getSelectedEntities().isEmpty() to enable/disable itself.
     */
    private SerializableFunction<CustomRouteActionContext<ModelClass>, Component> componentFactory;

    /**
     * Whether this action should be visible.
     */
    @Builder.Default
    private boolean visible = true;

    @Override
    public SerializableSupplier<Component> componentFactory(CustomRouteActionContext<ModelClass> context) {
        return () -> componentFactory.apply(context);
    }
}
