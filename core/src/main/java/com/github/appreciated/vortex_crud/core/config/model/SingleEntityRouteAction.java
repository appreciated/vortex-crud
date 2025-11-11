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
 * A route action that operates on a single selected entity.
 * The action button will be disabled if no entity or multiple entities are selected.
 *
 * <p>Examples: Edit, Approve, Archive, Send Email, Duplicate</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * SingleEntityRouteAction.<Task>builder()
 *     .componentFactory(context -> {
 *         Button approveBtn = new Button("Approve", VaadinIcon.CHECK.create());
 *         approveBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
 *
 *         // Button is automatically enabled/disabled based on selection
 *         approveBtn.setEnabled(context.getSelectedEntities().size() == 1);
 *
 *         approveBtn.addClickListener(e -> {
 *             Task task = context.getFirstSelectedEntity();
 *             task.setStatus("APPROVED");
 *             context.getDataStore().updateRecordById(task);
 *             context.showSuccessNotification("Task approved!");
 *             context.getRefreshCallback().run();
 *         });
 *         return approveBtn;
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
     * The factory receives the action context and should return a component with click listeners registered.
     * The component should check context.getSelectedEntities().size() == 1 to enable/disable itself.
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
