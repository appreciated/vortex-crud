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
 * A route action that operates on a single selected entity.
 * The action component will be disabled if no entity or multiple entities are selected.
 *
 * <p>Examples: Edit, Approve, Archive, Send Email, Duplicate</p>
 *
 * <p>Usage example:</p>
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
 *         context.getDataStore().updateRecordById(task);
 *         context.showSuccessNotification("Task approved!");
 *         context.getRefreshCallback().run();
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
public class SingleEntityRouteAction<ModelClass> implements RouteAction<ModelClass> {

    /**
     * Factory that creates the component for this action.
     * Should return a component without click listeners.
     * The framework will automatically enable/disable based on selection.
     */
    private SerializableSupplier<Component> componentFactory;

    /**
     * Handler that executes when the component is clicked.
     * Only called when exactly one entity is selected.
     * Receives the action context with access to the selected entity.
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
